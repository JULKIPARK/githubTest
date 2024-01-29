package com.pwc.pwcesg.frontoffice.footer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 기타링크 Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(value = "/frontoffice")
public class FooterController {
    
    /**
     * 개인정보처리방침
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/privacyView")
	public ModelAndView privacyView(){
		ModelAndView mav = new ModelAndView("frontoffice/footer/privacyView");
		
		return mav;
	}
	
    /**
     * Cookie policy
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/cookiePolicy")
	public ModelAndView cookiePolicy(){
		ModelAndView mav = new ModelAndView("frontoffice/footer/cookiePolicy");
		
		return mav;
	}
    
    /**
     * Legal disclaimer
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/disclaimer")
	public ModelAndView disclaimer(){
		ModelAndView mav = new ModelAndView("frontoffice/footer/disclaimer");
		
		return mav;
	}
    
    /**
     * About site provider
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/siteProvider")
	public ModelAndView siteProvider(@RequestParam(required = false) String tabId){
		ModelAndView mav = new ModelAndView("frontoffice/footer/siteProvider");
		mav.addObject("tabId", tabId);
		
		return mav;
	}
    
    /**
     * Site map
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/siteMap")
	public ModelAndView siteMap(){
		ModelAndView mav = new ModelAndView("frontoffice/footer/siteMap");
		
		return mav;
	}
    /**
     * search
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/footer/search")
	public ModelAndView search(){
		ModelAndView mav = new ModelAndView("frontoffice/footer/search");
		
		return mav;
	}	
}