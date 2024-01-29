package com.pwc.pwcesg.frontoffice.common.controller;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.SHA256Util;
import com.pwc.pwcesg.config.util.StringUtil;
import com.pwc.pwcesg.frontoffice.common.service.FoCommonService;
import com.pwc.pwcesg.frontoffice.intro.service.IntroService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(value = "/frontoffice")
public class FoCommonController {

	@Value("${memberInfo.saltKey}")
	private String saltKey; // 암호화된 비밀번호

	private final FoCommonService foCommonService;
	private final IntroService introService;

	/**
	 * 로그인 화면
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/login")
	public ModelAndView login() {
		return new ModelAndView("frontoffice/common/login");
	}

	/**
	 * 회원가입 화면
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/join")
	public ModelAndView join() {
		return new ModelAndView("frontoffice/common/njoin");
	}

	/**
	 * 아이디 중복확인
	 *
	 * @param mbrId
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/checkDuplicate")
	public ModelAndView checkDuplicate(@RequestParam String mbrId) {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("isDuplicate", foCommonService.checkDuplicate(mbrId));

		return mav;
	}

	/**
	 * 이메일 인증 번호 발송
	 *
	 * @param request, email
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/checkEmailSend")
	public ModelAndView checkEmailSend(HttpServletRequest request, @RequestParam String email) {
		ModelAndView mav = new ModelAndView("jsonView");
		boolean isEamil = foCommonService.checkEmail(email);
		mav.addObject("isEamil", isEamil);
		if (!isEamil) {
			return mav;
		}

		String maxTime = null;
		Date date = new Date();

		// 포맷변경 ( 년월일 시분초)
		SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();

		// 5분 더하기
		cal.add(Calendar.MINUTE, 5);
		maxTime = sdformat.format(cal.getTime());
		System.out.println("5분후 : " + maxTime); //05/13/2021 13:28:57

		Random rd = new Random();//랜덤 객체 생성
		String rndStr = "";
		for (int i = 0; i < 6; i++) {
			rndStr += (rd.nextInt(9) + 1);
		}
		//메일 발송
		String url = request.getScheme() + "//" + request.getServerName();
		foCommonService.ckSendEmail(email, rndStr, url, maxTime);

		HttpSession session = request.getSession();
		session.setAttribute("ckEmailNum", rndStr);

		return mav;
	}

	/**
	 * 이메일 인증
	 *
	 * @param request, emailCode
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/checkEmail")
	public ModelAndView checkEmail(HttpServletRequest request, @RequestParam String emailCode) {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("isEamil", true);

		HttpSession session = request.getSession();
		if (!emailCode.equals(session.getAttribute("ckEmailNum"))) {
			mav.addObject("isEamil", false);
			return mav;
		}
		session.setAttribute("ckEmailNum", "");
		return mav;
	}

	/**
	 * 회원가입
	 *
	 * @param request, paramMap, regIntrCd
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/insertJoin")
	public ModelAndView insertJoin(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, @RequestParam(value = "regIntrCd", required = false) List<String> regIntrCd) {
		log.info("[@RequestParam]{}", paramMap);

		ModelAndView mav = new ModelAndView("jsonView");

		boolean isDuplicate = foCommonService.checkDuplicate(paramMap.get("regMbrId").toString());
		if (isDuplicate) {
			mav.addObject("msg", "0001"); //아이디 중복
			return mav;
		}
		if (paramMap.get("regAgreeSms") == null || paramMap.get("regAgreeSms").equals("")) {
			paramMap.put("regAgreeSms", "N");
		}
		if (paramMap.get("regAgreeEmail") == null || paramMap.get("regAgreeEmail").equals("")) {
			paramMap.put("regAgreeEmail", "N");
		}
		if (paramMap.get("regNsltAgreYn") == null || paramMap.get("regNsltAgreYn").equals("")) {
			paramMap.put("regNsltAgreYn", "N");
		}
		if (paramMap.get("regMrktAgreYn") == null || paramMap.get("regMrktAgreYn").equals("")) {
			paramMap.put("regMrktAgreYn", "N");
		}
		if (paramMap.get("regNsltAgreYn") == null || paramMap.get("regAgreeEmail").equals("")) {
			paramMap.put("regNsltAgreYn", "N");
		}
		// 회원구분값 설정
		String mbrGbCd = "40";
		String curYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
		String mbrUno = curYear + "B";
		if (paramMap.get("regCompMail").toString().contains("@pwc.com")) {
			mbrGbCd = "30";
			mbrUno = curYear + "A";
		}
		paramMap.put("mbrGbCd", mbrGbCd);
		paramMap.put("mbrUno", mbrUno);
		paramMap.put("regMbrPwd", SHA256Util.getEncrypt(paramMap.get("regMbrPwd").toString(), saltKey));

		foCommonService.insertJoin(paramMap);//등록
		mav.addObject("msg", "success");

		return mav;
	}

	/**
	 * common 로그인 처리
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/signUp")
	public ModelAndView signUp(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		ModelAndView mav = new ModelAndView("jsonView");

		String mbrId = String.valueOf(paramMap.get("mbrId"));
		String mbrPwd = String.valueOf(paramMap.get("mbrPwd"));

		if (StringUtil.isNullOrBlank(mbrId) || StringUtil.isNullOrBlank(mbrPwd)) {
			log.info("로그인 필수 입력 오류");
			mav.addObject("msg", "아이디 또는 비밀번호를 입력해주세요.");
			return mav;
		}

		HttpSession session = request.getSession();

		//사용자 인증코드 조회 및 암호화 PW 세팅
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(mbrPwd, saltKey); //입력된 비밀번호+salt 암호된 값
		paramMap.put("pwEncrypt", pwEncrypt);

		paramMap.put("mbrId", mbrId);
		paramMap.put("sesnId", session.getId());

		introService.insertSesnAccsLog(paramMap);//세션접속로그 로그인 기록

		// 사용자 정보 조회
		SessionData sessionData = foCommonService.selectMbMbrInfo(paramMap);
		if (sessionData == null) {
			String msg = "아이디 또는 비밀번호가 올바르지않습니다.";

			// 실패카운트 갱신
			int failCnt = foCommonService.updatePwdFailCnt(paramMap);
			log.info("failCnt = {}", failCnt);
			if (failCnt > 0 && failCnt < 5) {
				msg = "아이디 또는 비밀번호가 올바르지않습니다.<br>(5회 오류 입력 시, 임시 비밀번호로 변경 처리됩니다.)";
			}
			if (failCnt >= 5) {
				msg = "비밀번호 5회 오류로 인해, 임시 비밀번호로 변경 처리하였습니다.<br>비밀번호 찾기를 이용하여 비밀번호 변경 후 이용해주시기 바랍니다.";
				if (failCnt == 5) {
					foCommonService.newPwd(paramMap);
				}
			}

			mav.addObject("msg", msg);
			return mav;
		}

		// 비밀번호 실패 카운트 초기화
		foCommonService.updatePwdFailCntReset(paramMap);

		session.setAttribute("FoMbrInfo", sessionData);
		mav.addObject("msg", "success");
		mav.addObject("rqtUri", session.getAttribute("rqtUri"));
		session.removeAttribute("rqtUri");

		return mav;
	}

	/**
	 * 로그아웃 처리
	 *
	 * @param request HttpServletRequest
	 * @return ModelAndView
	 */
	@RequestMapping("/common/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/";
	}

	/**
	 * 아이디 · 비밀번호 찾기 화면
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/findMe")
	public ModelAndView findMe() {
		return new ModelAndView("frontoffice/common/findMe");
	}

	/**
	 * 사용자정보 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/selectMyInfo")
	public ModelAndView selectMyInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {

		ModelAndView mav = new ModelAndView("jsonView");
		String msg = "";

		SessionData sessionData = foCommonService.selectMyInfo(paramMap);// 사용자 정보 조회

		String url = request.getScheme() + "//" + request.getServerName();

		if (sessionData != null) {
			sessionData = foCommonService.newPwd(url, sessionData);//임시비밀번호 발급
			msg = "success";
			mav.addObject("msg", msg);
			mav.addObject("myInfo", sessionData);
		}

		return mav;
	}

	/**
	 * 본인확인완료 화면
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/certificate")
	public ModelAndView certificate(@RequestParam Map<String, Object> paramMap) {

		ModelAndView mav = new ModelAndView("frontoffice/common/certificate");
		mav.addObject("myInfo", paramMap);

		return mav;
	}

	/**
	 * 정보수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/updateJoin")
	public ModelAndView updateJoin(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, @RequestParam(value = "regIntrCd", required = false) List<String> regIntrCd) {

		log.info("[@RequestParam]{}", paramMap);
		ModelAndView mav = new ModelAndView("jsonView");

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		paramMap.put("mbrUid", sessionData.getMbrUid());

		if (paramMap.get("regAgreeSms") == null || paramMap.get("regAgreeSms").equals("")) {
			paramMap.put("regAgreeSms", "N");
		}

		if (paramMap.get("regAgreeEmail") == null || paramMap.get("regAgreeEmail").equals("")) {
			paramMap.put("regAgreeEmail", "N");
		}

		if (paramMap.get("regNsltAgreYn") == null || paramMap.get("regNsltAgreYn").equals("")) {
			paramMap.put("regNsltAgreYn", "N");
		}

		if (paramMap.get("regMrktAgreYn") == null || paramMap.get("regMrktAgreYn").equals("")) {
			paramMap.put("regMrktAgreYn", "N");
		}

		if (paramMap.get("regNsltAgreYn") == null || paramMap.get("regAgreeEmail").equals("")) {
			paramMap.put("regNsltAgreYn", "N");
		}

		foCommonService.updateJoin(paramMap);//등록

		paramMap.put("mbrId", sessionData.getMbrId());
		paramMap.put("pwEncrypt", sessionData.getMbrPwd());
		paramMap.put("sesnId", session.getId());

		// 사용자 정보 조회
		sessionData = foCommonService.selectMbMbrInfo(paramMap);
		if (sessionData == null) {
			mav.addObject("msg", "저장실패");
			return mav;
		}

		session.setAttribute("FoMbrInfo", sessionData);

		mav.addObject("msg", "success");

		return mav;
	}


	/**
	 * 컨텐츠 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/content")
	public ModelAndView content(HttpServletRequest request, @RequestParam String contUid) {
		ModelAndView mav = new ModelAndView("jsonView");

		HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");
		String mbrUid = "";
		String mbrGbCd = "";
		if (sessionData != null) {
			mbrUid = sessionData.getMbrUid();
			mbrGbCd = sessionData.getMbrGbCd();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mbrUid", mbrUid);
		paramMap.put("contUid", contUid);
		paramMap.put("mbrGbCd", mbrGbCd);
		List<Map<String, Object>> info = foCommonService.selectContent(paramMap);

		if (info == null || info.size() == 0) {
			mav.addObject("msg", "컨텐츠 정보가 없습니다.");

			return mav;
		} else {
			Map<String, Object> dataMap = info.get(0);
			// 권한 체크
			if (dataMap.get("canAccess").equals("Y")) {
				mav.addObject("msg", "success");
				mav.addObject("info", info);
			} else if (dataMap.get("canAccess").equals("L")) {
				mav.addObject("msg", "로그인 후 이용 가능합니다.");
			} else if (dataMap.get("canAccess").equals("N")) {
				mav.addObject("msg", "조회 권한이 없습니다.");
			}
		}

		return mav;
	}

	@RequestMapping(value = "/common/mucontent")
	public ModelAndView mucontent(HttpServletRequest request, @RequestParam String contUid) {
		ModelAndView mav = new ModelAndView("jsonView");

		HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");
		String mbrUid = "";
		String mbrGbCd = "";
		if (sessionData != null) {
			mbrUid = sessionData.getMbrUid();
			mbrGbCd = sessionData.getMbrGbCd();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mbrUid", mbrUid);
		paramMap.put("contUid", contUid);
		paramMap.put("mbrGbCd", mbrGbCd);
		List<Map<String, Object>> info = foCommonService.selectMuContent(paramMap);

		if (info == null || info.size() == 0) {
			mav.addObject("msg", "컨텐츠 정보가 없습니다.");

			return mav;
		} else {
			Map<String, Object> dataMap = info.get(0);
			// 권한 체크
			if (dataMap.get("canAccess").equals("Y")) {
				mav.addObject("msg", "success");
				mav.addObject("info", info);
			} else if (dataMap.get("canAccess").equals("L")) {
				mav.addObject("msg", "로그인 후 이용 가능합니다.");
			} else if (dataMap.get("canAccess").equals("N")) {
				mav.addObject("msg", "조회 권한이 없습니다.");
			}
		}

		return mav;
	}

	/**
	 * 뉴스레터 신청 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/newsletter")
	public ModelAndView newsletter(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("jsonView");

		HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		//세션체크
		if (sessionData == null) {
			mav.addObject("msg", "N");
			return mav;
		}

		int info = foCommonService.selectNewsletter(sessionData);

		if (info > 0) {
			mav.addObject("msg", "Y");
		} else {
			mav.addObject("msg", "N");
		}

		return mav;
	}

	/**
	 * 뉴스레터 신청
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/saveNewsletter")
	public ModelAndView saveNewsletter(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("jsonView");

		HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		//세션체크
		if (sessionData == null) {
			mav.addObject("msg", "로그인 후 이용가능합니다.");
			return mav;
		}
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("mbrId", sessionData.getMbrId());
		paramMap.put("compMail", sessionData.getCompMail());
		paramMap.put("mbrUid", sessionData.getMbrUid());
		int info = foCommonService.saveNewsletter(paramMap);

		if (info > 0) {
			sessionData.setMailAgreYn("Y");
			sessionData.setMrktAgreYn("Y");
			sessionData.setNsltAgreYn("Y");
			session.setAttribute("FoMbrInfo", sessionData);
			mav.addObject("msg", "success");
		} else {
			mav.addObject("msg", "저장실패");
		}

		return mav;
	}

	/**
	 * 회원탈퇴 화면
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/withdrawalView")
	public ModelAndView withdrawalView() {
		return new ModelAndView("frontoffice/common/withdrawalView");
	}

	/**
	 * 회원탈퇴
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/withdrawal")
	public ModelAndView withdrawal(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, @RequestParam(value = "whdwlCausCd", required = true) String[] whdwlCausCdArry) {

		ModelAndView mav = new ModelAndView("jsonView");
		String msg = "";

		HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		//세션체크
		if (sessionData == null) {
			msg = "로그인후 이용해주세요.";
			mav.addObject("msg", msg);

			return mav;
		}

		//비밀번호 암호화
		String mbrPwd = String.valueOf(paramMap.get("mbrPwd"));
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(mbrPwd, saltKey); //입력된 비밀번호+salt 암호된 값

		if (!pwEncrypt.equals(sessionData.getMbrPwd())) {
			msg = "비밀번호가 일치하지 않습니다.";
			mav.addObject("msg", msg);

			return mav;
		}

		paramMap.put("mbrPwd", pwEncrypt);    //회원암호
		paramMap.put("mbrUid", sessionData.getMbrUid());//회원채번
		paramMap.put("mbrId", sessionData.getMbrId());//회원아이디
		paramMap.put("whdwlCausCd", StringUtils.join(whdwlCausCdArry));//탈퇴사유코드

		log.info("탈퇴사유코드:{}", paramMap.get("whdwlCausCd"));
		log.info("탈퇴사유설명:{}", paramMap.get("whdwlCausComnt"));
		log.info("최종수정아이디:{}", paramMap.get("mbrId"));
		log.info("회원채번:{}", paramMap.get("mbrUid"));
		log.info("회원암호:{}", paramMap.get("mbrPwd"));

		foCommonService.withdrawal(paramMap);

		session.invalidate();

		msg = "success";
		mav.addObject("msg", msg);

		return mav;

	}

	/**
	 * 비밀번호 확인
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/checkPwd")
	public ModelAndView checkPwd(HttpServletRequest request, @RequestParam String regMbrPwd) {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("isPwd", true);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");
		String pwEncrypt = SHA256Util.getEncrypt(regMbrPwd, saltKey); //입력된 비밀번호+salt 암호된 값
		if (!sessionData.getMbrPwd().equals(pwEncrypt)) {
			mav.addObject("isPwd", false);
		}

		return mav;
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/changePwd")
	public ModelAndView changePwd(HttpServletRequest request, @RequestParam String regMbrPwd) {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("isPwd", true);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		//비밀번호 변경
		sessionData.setMbrPwd(regMbrPwd);

		foCommonService.changePwd(sessionData);

		return mav;
	}

	/**
	 * 회원탈퇴 완료 화면
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/common/whdwlComplet")
	public ModelAndView whdwlComplet() {
		return new ModelAndView("frontoffice/common/whdwlComplet");
	}

	@RequestMapping(value = "/common/noPermission")
	public ModelAndView noPermission() {
		return new ModelAndView("frontoffice/common/noPermission");
	}


}
