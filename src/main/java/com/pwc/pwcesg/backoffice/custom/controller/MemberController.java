package com.pwc.pwcesg.backoffice.custom.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pwc.pwcesg.backoffice.custom.service.MemberService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 회원정보 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class MemberController {

	private final MemberService memberService;

	/**
	 * 회원정보 목록 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/memberList")
	public ModelAndView memberList() {
		ModelAndView mav = new ModelAndView("backoffice/custom/member/memberList");

		return mav;
	}

	/**
	 * 회원정보 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectMemberList")
	public ModelAndView selectMemberList(@RequestParam Map<String, Object> paramMap, @RequestParam(value = "schIntrCd") List<String> intrCdList) {
		log.info("/selectMemberList >> params : " + paramMap);
		paramMap.put("intrCdList", intrCdList);//ESG 관심영역

		int pageNum = 1;
		if (paramMap.get("mbrPageNum") != null && !paramMap.get("mbrPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("mbrPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("mbrPageSize") != null && !paramMap.get("mbrPageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("mbrPageSize")); // 페이징 : Page 크기 건수
		}

		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = memberService.selectMemberList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/custom/member/memberList :: #mbrListDiv");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 회원정보 목록 엑셀 다운로드
	 *
	 * @param request, response, paramMap, intrCdList
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectMemberListExcelDown")
	public void selectMemberListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap, @RequestParam(value = "schIntrCd") List<String> intrCdList) {
		log.info("/selectMemberListExcelDown paramMap : " + paramMap);

		paramMap.put("intrCdList", intrCdList);//ESG 관심영역
		memberService.selectMemberListExcelDown(response, paramMap);
	}

	/**
	 * 회원정보 상세 조회
	 *
	 * @param mbrUid
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectMemberView")
	public ModelAndView selectMemberView(@RequestParam String mbrUid) {
		log.info("/selectMemberView mbrUid : " + mbrUid);

		ModelAndView mav = new ModelAndView("jsonView");

		Map<String, Object> itemMap = memberService.selectMemberView(mbrUid);
		mav.addObject("item", itemMap);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 회원 메모정보 등록
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/inserMbrMemo")
	public ModelAndView inserMbrMemo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/inserMbrMemo >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", memberService.insertMemberMemo(paramMap));

		return mav;
	}

	@RequestMapping(value = "/custom/memberState")
	public ModelAndView memberState() {
		ModelAndView mav = new ModelAndView("backoffice/custom/member/memberState");

		return mav;
	}

	@RequestMapping(value = "/custom/selectMemberState")
	public ModelAndView selectMemberState() {
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("avgInfo", memberService.selectMemberAvgStateByOneYear());
		mav.addObject("list", memberService.selectMemberStateByOneMonth());

		return mav;
	}

	@RequestMapping(value = "/custom/selectEsgTgtActResult")
	public ModelAndView selectEsgTgtActResult() {
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", memberService.selectEsgTgtActResult());

		return mav;
	}

}
