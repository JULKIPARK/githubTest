package com.pwc.pwcesg.frontoffice.trend.controller;

import com.pwc.pwcesg.frontoffice.trend.service.TrendService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * ESG 자료·동향 Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/trend/")
public class TrendController {
    private final TrendService trendService;
    /**
     * ESG 교육 목록 화면
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "educationList")
    public ModelAndView educationList(){
        return new ModelAndView("frontoffice/trend/educationList");
    }

    @RequestMapping(value = "selectEducationList")
    public ModelAndView selectEducationList(@RequestParam Map<String, Object> paramMap){
        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("list", trendService.selectEducationList2(paramMap));
        return mav;
    }

    @RequestMapping(value = "educationList/selectContList")
    public ModelAndView selectContList(@RequestParam Map<String, String> paramMap){
        ModelAndView mav = new ModelAndView("jsonView");
        // 최신 동향 목록
        log.info("######1");
        mav.addObject("trendList", trendService.selectTrendList(paramMap));
        log.info("######2");
        return mav;
    }
    
//    private final TrendService trendService;
//    
//    /**
//     * PwC 인사이트 화면
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "insightsList")
//	public ModelAndView insightsList(){
//		return new ModelAndView("frontoffice/trend/insightsList");
//	}
//
//    /**
//     * 삼일인싸이트 - 토픽 목록 조회
//     */
//	@RequestMapping(value = "selectDpTpicInfoList")
//    public ModelAndView selectDpTpicInfoList(@RequestParam Map<String, Object> paramMap){
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("tpicList", trendService.selectDpTpicInfoList(paramMap));
//        return mav;
//    }
//	
//	/**
//     * 삼일인싸이트 - 발간연도 목록조회
//	 * @param paramMap
//	 * @return
//	 */
//	@RequestMapping(value = "selectPblcnYyList")
//    public ModelAndView selectPblcnYyList(@RequestParam Map<String, Object> paramMap){
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("pblcnYyList", trendService.selectPblcnYyList(paramMap));
//        return mav;
//    }
//    
//    /**
//     * PwC 인사이트 - 레포트 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectPwcReportList")
//	public ModelAndView selectPwcReportList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/insightsList :: #reportListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectPwcReportList(paramMap);
//        
//        mav.addObject("reportPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("reportList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * PwC 인사이트 - 미디어 영상 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectPwcMediaList")
//	public ModelAndView selectPwcMediaList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/insightsList :: #mediaListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectPwcMediaList(paramMap);
//        
//        mav.addObject("mediaPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("mediaList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * PwC 인사이트 - 뉴스레터 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectPwcNewsList")
//	public ModelAndView selectPwcNewsList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/insightsList :: #newsListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectPwcNewsList(paramMap);
//        mav.addObject("newsPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("newsList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 자료 목록 화면
//     * @param paramMap
//     * @return ModelAndView
//     */
//	/*
//	@RequestMapping(value = "bbsList")
//	public ModelAndView bbsList(){
//		return new ModelAndView("frontoffice/trend/bbsList");
//	}
//	*/
//    
//	
//    /**
//     * ESG 자료 > 기준·가이드라인 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectBbsGuideList")
//	public ModelAndView selectBbsGuideList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/bbsList :: #guideListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectBbsGuideList(paramMap);
//        
//        mav.addObject("guidePage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("guideList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 자료 > 법·제도 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectBbsLowList")
//	public ModelAndView selectBbsLowList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/bbsList :: #lowListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectBbsLowList(paramMap);
//        
//        mav.addObject("lowPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("lowList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 자료 > 연구·보고 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectBbsResearchList")
//	public ModelAndView selectBbsResearchList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/bbsList :: #researchListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectBbsResearchList(paramMap);
//        
//        mav.addObject("researchPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("researchList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 자료 > 미디어영상 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectBbsMediaList")
//	public ModelAndView selectBbsMediaList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/bbsList :: #mediaListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectBbsMediaList(paramMap);
//        
//        mav.addObject("mediaPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("mediaList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 뉴스 목록 화면
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "esgNewsList")
//	public ModelAndView esgNewsList(){
//		return new ModelAndView("frontoffice/trend/esgNewsList");
//	}
//    
//    /**
//     * ESG 뉴스 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectEsgNewsList")
//	public ModelAndView selectEsgNewsList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/esgNewsList :: #newsListDiv");
//		
//        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
//        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
//        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
//        PageMethod.startPage(pageNumber, pageSize);
//
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectEsgNewsList(paramMap);
//        
//        mav.addObject("newsPage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
//		mav.addObject("newsList", list);
//		
//		return mav;
//	}
//    
//    /**
//     * ESG 교육 목록 화면
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "educationList")
//	public ModelAndView educationList(){
//		return new ModelAndView("frontoffice/trend/educationList");
//	}
//    
//    /**
//     * ESG 교육 목록 조회
//     * @param paramMap
//     * @return ModelAndView
//     */
//	@RequestMapping(value = "selectEducationList")
//	public ModelAndView selectEducationList(@RequestParam Map<String, Object> paramMap){
//		log.info("[@RequestParam]{}", paramMap);
//		ModelAndView mav = new ModelAndView("frontoffice/trend/educationList :: #educationListDiv");
//		
//        // 목록 조회
//        List<Map<String, Object>> list = trendService.selectEducationList(paramMap);
//        
//		mav.addObject("list", list);
//		
//		return mav;
//	}
}