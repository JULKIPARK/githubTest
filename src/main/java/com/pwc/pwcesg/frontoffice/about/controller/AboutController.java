package com.pwc.pwcesg.frontoffice.about.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.config.util.PagingUtil;
import com.pwc.pwcesg.frontoffice.about.service.AboutService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * About Us Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/about/")
public class AboutController {
    
    private final AboutService aboutService;
    
    /**
     * About Us 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "aboutListView")
	public ModelAndView aboutListView(){
		return new ModelAndView("frontoffice/about/aboutList");
	}
    
    /**
     * About Us 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "selectAboutList")
	public ModelAndView selectAboutList(@RequestParam Map<String, Object> paramMap){
		log.info("[@RequestParam]{}", paramMap);
		ModelAndView mav = new ModelAndView("frontoffice/about/aboutList :: #aboutListDiv");
		
        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
        PageMethod.startPage(pageNumber, pageSize);

        // 목록 조회
        List<Map<String, Object>> list = aboutService.selectAboutList(paramMap);
        
        mav.addObject("aboutPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
		mav.addObject("aboutList", list);
		
		return mav;
	}
}