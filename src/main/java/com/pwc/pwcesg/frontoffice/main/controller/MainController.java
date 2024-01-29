package com.pwc.pwcesg.frontoffice.main.controller;

import com.pwc.pwcesg.frontoffice.main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 메인 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainService mainService;

    @RequestMapping("/frontoffice")
    public String home() {
        return "index";
    }

    /**
     * 메인 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/")
    public String selectnewsList() {
        return "/frontoffice/main/main";
    }

    /**
     * 컨텐츠 정보 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/main/selectContList")
    public ModelAndView selectContList(@RequestParam Map<String, String> paramMap) {
        ModelAndView mav = new ModelAndView("jsonView");
        // 삼일 인사이트 목록

        paramMap.put("contKindCd", "10");
        mav.addObject("pwcInsightsList1", mainService.selectPwcInsightsList(paramMap));

        paramMap.put("contKindCd", "20");
        mav.addObject("pwcInsightsList2", mainService.selectPwcInsightsList(paramMap));
        // 최신 동향 목록
        mav.addObject("trendList", mainService.selectTrendList(paramMap));

        //기준/가이드 라인 목록
        paramMap.put("contTpLclsfCd", "20");
        mav.addObject("standardList", mainService.selectBbsList(paramMap));
        //연구/보고 목록
        paramMap.put("contTpLclsfCd", "40");
        mav.addObject("researchList", mainService.selectBbsList(paramMap));
        //법/제도 목록
        paramMap.put("contTpLclsfCd", "30");
        mav.addObject("lawList", mainService.selectBbsList(paramMap));


        return mav;
    }
}
