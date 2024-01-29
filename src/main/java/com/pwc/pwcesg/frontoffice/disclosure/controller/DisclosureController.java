package com.pwc.pwcesg.frontoffice.disclosure.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.frontoffice.disclosure.service.DisclosureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(value = "/frontoffice")
public class DisclosureController {

    private final DisclosureService disclosureService;
    /**
     * ESG 공시 인증 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/disclosure/disclosureView")
	public ModelAndView disclosureView(HttpServletRequest request,
			@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/disclosure/disclosureView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);

		mav.addObject("list", disclosureService.selectTpicInfoList());

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");

		//세션체크
		if(sessionData == null)
			mav.addObject("isLogin", "N");
		else
			mav.addObject("isLogin", "Y");

		return mav;
	}

    /**
     *   토픽 부가정보 조회
     * @param tpicUid : 토픽채번
     * @return ModelAndView
     */
	@RequestMapping(value = "/disclosure/selectTpicApndInfo")
	public ModelAndView selectTpicApndInfo(@RequestParam Map<String, String> paramMap){
		ModelAndView mav = new ModelAndView("jsonView");
		paramMap.put("tpicTpCd", "10");
		mav.addObject("info1", disclosureService.selectTpicApndInfo(paramMap));
		paramMap.put("tpicTpCd", "30");
		mav.addObject("info2", disclosureService.selectTpicApndInfo(paramMap));
		paramMap.put("tpicTpCd", "50");
		mav.addObject("info3", disclosureService.selectTpicApndInfo(paramMap));
		return mav;
	}

	 /**
     *  컨텐츠 정보 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/disclosure/selectContList")
	public ModelAndView selectContList(@RequestParam Map<String, String> paramMap){
		ModelAndView mav = new ModelAndView("jsonView");
		//ESG 실행 >> 환경 >> 토픽 >> 삼일 인사이트 목록
		mav.addObject("pwcInsightsList", disclosureService.selectPwcInsightsList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> 최신 동향 목록
		mav.addObject("trendList", disclosureService.selectTrendList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> ESG 영상모음 목록
		mav.addObject("videoList", disclosureService.selectVideoList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> ESG 자료모음 목록(콘텐츠유형대분류코드(10:삼일발간물, 20:기준/가이드라인, 30:법/제도, 40:보고/연구, 50:교육/안내))
		//기준/가이드 라인 목록
		paramMap.put("contTpLclsfCd", "20");
		mav.addObject("standardList", disclosureService.selectBbsList(paramMap));
		//연구/보고 목록
		paramMap.put("contTpLclsfCd", "40");
		mav.addObject("researchList", disclosureService.selectBbsList(paramMap));
		//법/제도 목록
		paramMap.put("contTpLclsfCd", "30");
		mav.addObject("lawList", disclosureService.selectBbsList(paramMap));



		return mav;
	}
    /**
     * ESG 평가 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/disclosure/appraisalView")
	public ModelAndView appraisalView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/disclosure/appraisalView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		return mav;
	}

    /**
     * ESG > 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/disclosure/certificationView")
	public ModelAndView certificationView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/disclosure/certificationView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		return mav;
	}

	@RequestMapping(value = "/disclosure/csrdSave")
	public ModelAndView selfDiagnosisSave(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap
		){

		ModelAndView mav = new ModelAndView("jsonView");
		String msg = "";

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");

		//세션체크
		if(sessionData == null) {
			msg="로그인후 이용해주세요.";
			mav.addObject("msg", msg);

	        return mav;
		}





		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mbrUid", sessionData.getMbrUid());//회원채번
		param.put("mbrId", sessionData.getMbrId());//회원아이디
		param.put("indstyLclsCd",paramMap.get("regIndstyLclsCd"));
		param.put("indstyMclsCd",paramMap.get("regIndstyMclsCd"));
		param.put("indstySclsCd",paramMap.get("regIndstySclsCd"));
		param.put("businessScale1Cd",paramMap.get("businessScale1Cd"));
		param.put("businessScale2Cd",paramMap.get("businessScale2Cd"));
		param.put("businessScale3Cd",paramMap.get("businessScale3Cd"));
		param.put("csrdSt1Cd",paramMap.get("businessScaleCd"));
		param.put("csrdSt2Cd",paramMap.get("csrd01"));
		param.put("csrdSt3Cd",paramMap.get("csrd02"));
		param.put("csrdSt4Cd",paramMap.get("csrd03"));
		param.put("csrdSt5Cd",paramMap.get("csrd04"));


		disclosureService.insertSave(param);

		msg="success";
		mav.addObject("msg", msg);
		mav.addObject("info", disclosureService.selectResult(param));

        return mav;

	}

	@RequestMapping(value = "/disclosure/csrdResult")
	public ModelAndView selfDiagnosisResult(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap
		){

		ModelAndView mav = new ModelAndView("jsonView");
		String msg = "";

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");

		//세션체크
		if(sessionData == null) {
			msg="로그인후 이용해주세요.";
			mav.addObject("msg", msg);

	        return mav;
		}


		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mbrUid", sessionData.getMbrUid());//회원채번
		param.put("mbrId", sessionData.getMbrId());//회원아이디
		param.put("csrdTgtResultUid",paramMap.get("csrdTgtResultUid"));


		msg="success";
		mav.addObject("msg", msg);
		mav.addObject("info", disclosureService.selectResult(param));

        return mav;

	}
}