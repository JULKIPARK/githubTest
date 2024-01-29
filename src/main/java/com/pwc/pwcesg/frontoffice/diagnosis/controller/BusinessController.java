package com.pwc.pwcesg.frontoffice.diagnosis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.frontoffice.diagnosis.service.BusinessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(value = "/frontoffice")
public class BusinessController {

    @Value("${memberInfo.saltKey}")
    private String saltKey; // 암호화된 비밀번호

    private final BusinessService businessService;

    /**
     * 자가진단 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/businessInfo")
	public ModelAndView login(){
		return new ModelAndView("frontoffice/diagnosis/businessInfo");
	}

    /**
     * ESG 전략 계획
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/strategyView")
	public ModelAndView strategyView(@RequestParam String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/diagnosis/strategyView"+ tabId);

		mav.addObject("tabId", tabId);

		if (tabId.equals("1")) {
			mav.addObject("tpicUid", 34);
		} else if (tabId.equals("2")) {
			mav.addObject("tpicUid", 35);
		}

		return mav;
	}

    /**
     * ESG 과제 이행
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/environmentView")
	public ModelAndView environmentView(@RequestParam String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/diagnosis/environmentView"+tabId);

		mav.addObject("tabId", tabId);

		if (tabId.equals("1")) {
			mav.addObject("tpicUid", 36);
		} else if (tabId.equals("2")) {
			mav.addObject("tpicUid", 37);
		} else if (tabId.equals("3")) {
			mav.addObject("tpicUid", 38);
		} else if (tabId.equals("4")) {
			mav.addObject("tpicUid", 39);
		} else if (tabId.equals("5")) {
			mav.addObject("tpicUid", 40);
		} else if (tabId.equals("6")) {
			mav.addObject("tpicUid", 41);
		}

		return mav;
	}

    /**
     * 공시 · 평가 및 인증
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/certifiedView")
	public ModelAndView certifiedView(@RequestParam String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/diagnosis/certifiedView"+tabId);
		mav.addObject("tabId", tabId);

		if (tabId.equals("1")) {
			mav.addObject("tpicUid", 42);
		} else if (tabId.equals("2")) {
			mav.addObject("tpicUid", 43);
		} else if (tabId.equals("3")) {
			mav.addObject("tpicUid", 44);
		}

		return mav;
	}

    /**
     * 컨텐츠 정보 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/selectContList")
	public ModelAndView selectContList(@RequestParam Map<String, Object> paramMap){
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", businessService.selectContList(paramMap));


		return mav;
	}

	/*
	ESG 진단/계획 - ESG 과제이행 -ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) 환경 과제이행완료P007
	ESG 진단/계획 - ESG 과제이행 - ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) 사회 과제이행완료P007
	ESG 진단/계획 - ESG 과제이행 - ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) 거버넌스 과제이행완료P009
	ESG 진단/계획 - ESG 과제이행 - ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) 지속가능 금융완료P010
	ESG 진단/계획 - ESG 과제이행 - ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) ESG 영향평가완료P011
	ESG 진단/계획 - ESG 과제이행 - ESG 경영 활동 가이드 - ESG 과제 이행 - (탭) 사회 과제이행완료P012
	ESG 진단/계획 - ESG 공시/평가/인증 - ESG 경영 활동 가이드 - ESG 공시∙인증 - (탭) ESG 공시완료P013
	ESG 진단/계획 - ESG 공시/평가/인증 - ESG 경영 활동 가이드 - ESG 공시∙인증 - (탭) ESG 평가완료P014
	ESG 진단/계획 - ESG 공시/평가/인증 - ESG 경영 활동 가이드 - ESG 공시∙인증 - (탭) ESG 인증
    */
}