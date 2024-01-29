package com.pwc.pwcesg.frontoffice.action.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.frontoffice.action.service.ActionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * ESG 실행 Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/action/")
public class ActionController {

    private final ActionService actionService;

    /**
     * 환경 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "environmentView")
	public ModelAndView environmentView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/environmentView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		mav.addObject("MenuCd", "10");
		mav.addObject("list", actionService.selectActionTpicInfoList("10"));

		return mav;
	}

    /**
     * 사회 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "socialView")
	public ModelAndView socialView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/socialView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		mav.addObject("MenuCd", "20");
		mav.addObject("list", actionService.selectActionTpicInfoList("20"));
		return mav;
	}

    /**
     * 거버넌스 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "governanceView")
	public ModelAndView governanceView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/governanceView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		mav.addObject("MenuCd", "30");

		mav.addObject("list", actionService.selectActionTpicInfoList("30"));

		return mav;
	}

    /**
     * 금융 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "financeView")
	public ModelAndView financeView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/financeView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);
		mav.addObject("MenuCd", "40");

		mav.addObject("list", actionService.selectActionTpicInfoList("40"));

		return mav;
	}

    /**
     * ESG TAX
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "esgTaxView")
	public ModelAndView esgTaxView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/esgTaxView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);

		mav.addObject("MenuCd", "50");
		mav.addObject("list", actionService.selectActionTpicInfoList("50"));

		return mav;
	}

    /**
     * ESG Deal
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "esgDealView")
	public ModelAndView esgDealView(@RequestParam(value="tabId", required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/action/esgDealView");
		if(tabId == null)
			tabId = "1";
		mav.addObject("tabId", tabId);

		mav.addObject("MenuCd", "60");

		mav.addObject("list", actionService.selectActionTpicInfoList("60"));

		return mav;
	}
    /**
     * ESG 실행 >> 토픽 목록 조회
     * @param tpicGbCd : 토픽메뉴코드(10: E(환경), 20: S(사회), 30: G(거버넌스), 40: 금융, 50: ESG TAX, 60: ESG Deal)
     * @return ModelAndView
     */
	@RequestMapping(value = "selectActionTpicInfoList")
	public ModelAndView selectActionTpicInfoList(@RequestParam(name="tpicMnuCd", required=false) String tpicMnuCd){
		ModelAndView mav = new ModelAndView("jsonView");

		if(tpicMnuCd==null || "".equals(tpicMnuCd)) {
			tpicMnuCd = "10";//기본값 토픽메뉴코드(10: E(환경), 20: S(사회), 30: G(거버넌스), 40: 금융, 50: ESG TAX, 60: ESG Deal)
		}

		mav.addObject("list", actionService.selectActionTpicInfoList(tpicMnuCd));
		return mav;
	}

    /**
     * ESG 실행 >> 토픽 부가정보 조회
     * @param tpicUid : 토픽채번
     * @return ModelAndView
     */
	@RequestMapping(value = "selectActionTpicApndInfo")
	public ModelAndView selectActionTpicApndInfo(@RequestParam Map<String, String> paramMap){
		ModelAndView mav = new ModelAndView("jsonView");
		paramMap.put("tpicTpCd", "10");
		mav.addObject("info", actionService.selectActionTpicApndInfo(paramMap));

		return mav;
	}

    /**
     * ESG 실행 >> 컨텐츠 정보 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "selectActionContList")
	public ModelAndView selectActionContList(@RequestParam Map<String, String> paramMap){
		ModelAndView mav = new ModelAndView("jsonView");
		//ESG 실행 >> 환경 >> 토픽 >> 삼일 인사이트 목록
		mav.addObject("pwcInsightsList", actionService.selectActionPwcInsightsList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> 최신 동향 목록
		mav.addObject("trendList", actionService.selectActionTrendList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> ESG 영상모음 목록
		mav.addObject("videoList", actionService.selectActionVideoList(paramMap));

		//ESG 실행 >> 환경 >> 토픽 >> ESG 자료모음 목록(콘텐츠유형대분류코드(10:삼일발간물, 20:기준/가이드라인, 30:법/제도, 40:보고/연구, 50:교육/안내))
		//기준/가이드 라인 목록
		paramMap.put("contTpLclsfCd", "20");
		mav.addObject("standardList", actionService.selectActionBbsList(paramMap));
		//연구/보고 목록
		paramMap.put("contTpLclsfCd", "40");
		mav.addObject("researchList", actionService.selectActionBbsList(paramMap));
		//법/제도 목록
		paramMap.put("contTpLclsfCd", "30");
		mav.addObject("lawList", actionService.selectActionBbsList(paramMap));

		//코멘트 리스트
		//토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
		paramMap.put("tpicTpCd", "60");
		mav.addObject("apndList", actionService.selectActionTpicApndInfo(paramMap));

		//전문가 리스트
		//토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
		paramMap.put("tpicTpCd", "40");
		mav.addObject("spcalList", actionService.selectActionspcalList(paramMap));

		return mav;
	}
}