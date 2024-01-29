package com.pwc.pwcesg.frontoffice.trend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.config.util.PagingUtil;
import com.pwc.pwcesg.frontoffice.search.common.CommonUtil;
import com.pwc.pwcesg.frontoffice.search.common.SetParameter;
import com.pwc.pwcesg.frontoffice.search.data.CountVO;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;
import com.pwc.pwcesg.frontoffice.trend.service.SearchService;
import com.pwc.pwcesg.frontoffice.trend.service.TrendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/trend")
public class TrendSearchController {

	private final SearchService searchService;
	private final SetParameter setParameter;
	private final CommonUtil commonUtil;
	private CountVO countVO = new CountVO();
	
	
	@Value("${konan.properties.url}")
	private String url;
	
	
	/**
	 * ESG 자료실
	 */
	@RequestMapping("/bbsList")
	public String resultSearch(@RequestParam Map<String, String> map, Model model) throws Exception{

		log.info("ESG자료실 ---- START");
		// 파라미터 세팅
		ParameterVO paramVO = setParameter.setParameter(map);
	
		
		//전체 API 호출
		//기준 가이드라인
		if(paramVO.getKwd() == null || paramVO.getKwd() == "") { //검색어 null로 들어왔을 때
			RestResultVO result;
			int total = 0;
			
			
			if ("TOTAL".equals(paramVO.getCategory())) {
				paramVO.setCategory("GUIDE");
				paramVO.setPageSize(15);
				
				result = searchService.guideSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("guideRow", result.getRows());
				model.addAttribute("guideList", result.getResult());
				model.addAttribute("guideTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}else if ("GUIDE".equals(paramVO.getCategory())) { //기준 가이드라인
				paramVO.setPageSize(15);
				
				result = searchService.guideSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("guideRow", result.getRows());
				model.addAttribute("guideList", result.getResult());
				model.addAttribute("guideTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}else if ("LEGAL".equals(paramVO.getCategory())) { //법 제도
				paramVO.setPageSize(15);
				
				result = searchService.legalSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("legalRow", result.getRows());
				model.addAttribute("legalList", result.getResult());
				model.addAttribute("legalTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}else if ("REPORT".equals(paramVO.getCategory())) { //연구 보고
				paramVO.setPageSize(15);
				
				result = searchService.reportSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("reportRow", result.getRows());
				model.addAttribute("reportList", result.getResult());
				model.addAttribute("reportTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}else if ("MEDIA".equals(paramVO.getCategory())) { //미디어
				paramVO.setPageSize(12);
				
				result = searchService.esg_mediaSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("mediaRow", result.getRows());
				model.addAttribute("mediaList", result.getResult());
				model.addAttribute("mediaTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
			}
		}
				
		
		
		// 카테고리별 API 호출
		setCategoryModel(model, paramVO);

		// 파라미터
		model.addAttribute("params", paramVO);
		log.info("ESG자료실 ---- END");		
		return "frontoffice/trend/bbsList";
	}
	
	
	
	private Model setCategoryModel(Model model, ParameterVO paramVO) throws Exception {
		RestResultVO result;
		int total = 0;
		int pageSize = paramVO.getPageSize();
		int pageNum = paramVO.getPageNum();
		
		
		/*
		 * // 카테고리 여부 if (paramVO.getKwd().length() > 0) {
		 */
			// 기준.가이드 라인
			if ("GUIDE".equals(paramVO.getCategory())) {
				paramVO.setPageSize(15);
				
				result = searchService.guideSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("guideRow", result.getRows());
				model.addAttribute("guideList", result.getResult());
				model.addAttribute("guideTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}
			// 법.제도
			if ("LEGAL".equals(paramVO.getCategory())) {
				paramVO.setPageSize(15);
				
				result = searchService.legalSearch(paramVO);
				total += result.getTotal();
	
				model.addAttribute("legalRow", result.getRows());
				model.addAttribute("legalList", result.getResult());
				model.addAttribute("legalTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}
			// 연구.보고
			if ("REPORT".equals(paramVO.getCategory())) {
				paramVO.setPageSize(15);
				
				result = searchService.reportSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("reportRow", result.getRows());
				model.addAttribute("reportList", result.getResult());
				model.addAttribute("reportTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.esg_mediaSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
			}
			// 미디어
			if ("MEDIA".equals(paramVO.getCategory())) {
				paramVO.setPageSize(12);
				
				result = searchService.esg_mediaSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("mediaRow", result.getRows());
				model.addAttribute("mediaList", result.getResult());
				model.addAttribute("mediaTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.guideSearch(paramVO);
				model.addAttribute("guideTotal", result.getTotal());
				
				result = searchService.legalSearch(paramVO);
				model.addAttribute("legalTotal", result.getTotal());
				
				result = searchService.reportSearch(paramVO);
				model.addAttribute("reportTotal", result.getTotal());
				
			}
	
			
			//---> 페이징 처리
			Map<String, Object> pageMap = new HashMap<>();
			
			double ceilSize = Math.ceil((double)total / pageSize);
			int totalPage = (int) ceilSize;
				
			int prePage = 0;
			int nextPage = 0;
			int startRow = 0;
			int endRow = 0;
			
			startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
			endRow = startRow + pageSize * (pageNum > 0 ? 1 : 0);
			
			if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < totalPage) {
                nextPage = pageNum + 1;
            }
		          
            
            int currNavigatePage = 0;
            int navigatePages = 0;
            int pages = 0;
            int startPage = 0;
            
            
			if (totalPage > 10) {
				currNavigatePage = (int) ((pageNum - 1) / 10);
				navigatePages = currNavigatePage * 10;
				startPage = navigatePages;
				int maxNavigatePage = (int) (totalPage / 10);
				
				if (currNavigatePage == maxNavigatePage) {
					pages = (totalPage - (maxNavigatePage * 10));
				} else {
					pages = 10;
				}
            	
            } else {
            	pages = totalPage;
            	startPage = 0;
            }
            
            
            int[] navigatepageNums = new int[pages];
            
            for(int i=0; i < pages; i++) {
            	navigatepageNums[i] = startPage + i + 1;
            }  
            
            int navigateFirstPage = 0; // navigateFirstPage 변수 추가

            if (pageNum == 1) {
                navigateFirstPage = 0;
            } else {
                navigateFirstPage = prePage; // 이전 페이지로 설정
            }
            
            pageMap.put("totalCount", total);
	        pageMap.put("totalPage", totalPage);
	        pageMap.put("pageNumber", pageNum);
	        pageMap.put("size", pageSize);
	        pageMap.put("startRow", startRow);
	        pageMap.put("endRow",endRow);
	        pageMap.put("total", total);
	        pageMap.put("pages", pageNum);
	        pageMap.put("prePage", prePage);
	        pageMap.put("nextPage", nextPage);
	        pageMap.put("navigatePages", navigatePages);
	        pageMap.put("navigateFirstPage", navigateFirstPage);
	        pageMap.put("navigateLastPage", totalPage);			
	        pageMap.put("navigatepageNums", navigatepageNums);
			
	        
	        model.addAttribute("PageInfo", pageMap);// 페이징 정보 추출
			model.addAttribute("total", total);

		//}		
		
		return model;
	}

	
	
	
	
	
	
	
	//////////////////////////////////////////////////////
	
	
	/**
	 * 삼일 인사이트
	 */
	@RequestMapping("/insightsList")
	public String ins_resultSearch(@RequestParam Map<String, String> map, Model model) throws Exception{

		log.info("삼일인사이트 ---- START");
		// 파라미터 세팅
		ParameterVO paramVO = setParameter.setParameter(map);	
		

		
		//전체 API 호출
		//기준 가이드라인
		if(paramVO.getKwd() == null || paramVO.getKwd() == "") { //검색어 null로 들어왔을 때
			RestResultVO result;
			int total = 0;
			
			
			if ("TOTAL".equals(paramVO.getCategory()) || "SAMIL".equals(paramVO.getCategory())) {
				paramVO.setCategory("SAMIL");
				paramVO.setPageSize(12);
				
				result = searchService.ins_insrpSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("samilRow", result.getRows());
				model.addAttribute("samilList", result.getResult());
				model.addAttribute("samilTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
				
			}else if ("MEDIA".equals(paramVO.getCategory())) { //미디어 영상
				paramVO.setPageSize(12);
				
				result = searchService.samilSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("mediaRow", result.getRows());
				model.addAttribute("mediaList", result.getResult());
				model.addAttribute("mediaTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
				
			}else if ("NEWS".equals(paramVO.getCategory())) { //뉴스레터
				paramVO.setPageSize(10);
				
				result = searchService.ins_NewsSearch(paramVO);
				total += result.getTotal();
			
				model.addAttribute("newsRow", result.getRows());
				model.addAttribute("newsList", result.getResult());
				model.addAttribute("newsTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
			}else if ("PLATFORM".equals(paramVO.getCategory())) { //ESG 전문가 리스트
	
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
			}
		}
		
		// 카테고리별 API 호출
		setCategoryModel_1(model, paramVO);
		
		// 파라미터
		model.addAttribute("params", paramVO);
		
		log.info("삼일인사이트 ---- END");
		return "frontoffice/trend/insightsList";
	}
	
	
	
	private Model setCategoryModel_1(Model model, ParameterVO paramVO) throws Exception {
		RestResultVO result;
		int total = 0;
		int pageSize = paramVO.getPageSize();
		int pageNum = paramVO.getPageNum();
		
		
		// 카테고리 여부
		/* if (paramVO.getKwd().length() > 0) { */
			// 인사이트 레포트
			if ("SAMIL".equals(paramVO.getCategory())) {
				paramVO.setPageSize(12);
				
				result = searchService.ins_insrpSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("samilRow", result.getRows());
				model.addAttribute("samilList", result.getResult());
				model.addAttribute("samilTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
			}
			// 미디어
			if ("MEDIA".equals(paramVO.getCategory())) {
				paramVO.setPageSize(12);
				
				result = searchService.samilSearch(paramVO);
				total += result.getTotal();
	
				model.addAttribute("mediaRow", result.getRows());
				model.addAttribute("mediaList", result.getResult());
				model.addAttribute("mediaTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
			}
			// 뉴스레터
			if ("NEWS".equals(paramVO.getCategory())) {
				paramVO.setPageSize(10);
				
				result = searchService.ins_NewsSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("newsRow", result.getRows());
				model.addAttribute("newsList", result.getResult());
				model.addAttribute("newsTotal", result.getTotal());
				
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
			}else if ("PLATFORM".equals(paramVO.getCategory())) { //뉴스레터
	
				//건수 같이 날리기
				result = searchService.ins_insrpSearch(paramVO);
				model.addAttribute("samilTotal", result.getTotal());
				
				result = searchService.samilSearch(paramVO);
				model.addAttribute("mediaTotal", result.getTotal());
				
				result = searchService.ins_NewsSearch(paramVO);
				model.addAttribute("newsTotal", result.getTotal());
			}

			
			//---> 페이징 처리
			Map<String, Object> pageMap = new HashMap<>();
			
			double ceilSize = Math.ceil((double)total / pageSize);
			int totalPage = (int) ceilSize;

			int prePage = 0;
			int nextPage = 0;
			int startRow = 0;
			int endRow = 0;
			
			startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
			endRow = startRow + pageSize * (pageNum > 0 ? 1 : 0);
			
			if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < totalPage) {
                nextPage = pageNum + 1;
            }
		          
            
            int currNavigatePage = 0;
            int navigatePages = 0;
            int pages = 0;
            int startPage = 0;
            
			if (totalPage > 10) {
				currNavigatePage = (int) ((pageNum - 1) / 10);
				navigatePages = currNavigatePage * 10;
				startPage = navigatePages;
				int maxNavigatePage = (int) (totalPage / 10);
				
				if (currNavigatePage == maxNavigatePage) {
					pages = (totalPage - (maxNavigatePage * 10));
				} else {
					pages = 10;
				}
            	
            } else {
            	pages = totalPage;
            	startPage = 0;
            }
            
            
            int[] navigatepageNums = new int[pages];
            
            for(int i=0; i < pages; i++) {
            	navigatepageNums[i] = startPage + i + 1;
            }  
            
            
            pageMap.put("totalCount", total);
	        pageMap.put("totalPage", totalPage);
	        pageMap.put("pageNumber", pageNum);
	        pageMap.put("size", pageSize);
	        pageMap.put("startRow", startRow);
	        pageMap.put("endRow",endRow);
	        pageMap.put("total", total);
	        pageMap.put("pages", pageNum);
	        pageMap.put("prePage", prePage);
	        pageMap.put("nextPage", nextPage);
	        pageMap.put("navigatePages", navigatePages);
	        pageMap.put("navigateFirstPage", 0);
	        pageMap.put("navigateLastPage", totalPage);			
	        pageMap.put("navigatepageNums", navigatepageNums);
			
	        
	        model.addAttribute("PageInfo", pageMap);// 페이징 정보 추출
			

		//}
	
		
		return model;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//////////////////////////////////////////////////////
	
	
	
		@RequestMapping("/esgNewsList")
		public String news_resultSearch(@RequestParam Map<String, String> map, Model model) throws Exception{
		
			log.info("esg뉴스 ---- START");
			// 파라미터 세팅
			ParameterVO paramVO = setParameter.setParameter(map);
			
		
			//전체 API 호출
			if(paramVO.getKwd() == null || paramVO.getKwd() == "") { //검색어 null로 들어왔을 때
				RestResultVO result;
				int total = 0;
				
				if ("TOTAL".equals(paramVO.getCategory())) {
					paramVO.setCategory("NEWS");
					paramVO.setPageSize(10);
					
					result = searchService.newsSearch(paramVO);
					total += result.getTotal();
				
					model.addAttribute("newsRow", result.getRows());
					model.addAttribute("newsList", result.getResult());
					model.addAttribute("newsTotal", result.getTotal());
				}
				
				model.addAttribute("esgNewsListTotal", total);
		
			}
			
			
			
			// 카테고리별 API 호출
			setCategoryModel_2(model, paramVO);
			
			// 파라미터
			model.addAttribute("params", paramVO);
			
			log.info("esg뉴스 ---- END");		
			return "frontoffice/trend/esgNewsList";
		}
		
		
		
		private Model setCategoryModel_2(Model model, ParameterVO paramVO) throws Exception {
			RestResultVO result;
			int total = 0;
			int pageSize = paramVO.getPageSize();
			int pageNum = paramVO.getPageNum();
			
			
			// 카테고리 여부
			//if (paramVO.getKwd().length() > 0) {
				// 뉴스레터
				if ("NEWS".equals(paramVO.getCategory())) {
					paramVO.setPageSize(10);
					
					result = searchService.newsSearch(paramVO);
					total += result.getTotal();
				
					model.addAttribute("newsRow", result.getRows());
					model.addAttribute("newsList", result.getResult());
					model.addAttribute("newsTotal", result.getTotal());
				}
				
				
				//---> 페이징 처리
				Map<String, Object> pageMap = new HashMap<>();
				
				double ceilSize = Math.ceil((double)total / pageSize);
				int totalPage = (int) ceilSize;
				
				int prePage = 0;
				int nextPage = 0;
				int startRow = 0;
				int endRow = 0;
				
				startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
				endRow = startRow + pageSize * (pageNum > 0 ? 1 : 0);
				
				if (pageNum > 1) {
					prePage = pageNum - 1;
				}
				if (pageNum < totalPage) {
					nextPage = pageNum + 1;
				}
				
				
				int currNavigatePage = 0;
				int navigatePages = 0;
				int pages = 0;
				int startPage = 0;
				
				if (totalPage > 10) {
				currNavigatePage = (int) ((pageNum - 1) / 10);
				navigatePages = currNavigatePage * 10;
				startPage = navigatePages;
				int maxNavigatePage = (int) (totalPage / 10);
				
				if (currNavigatePage == maxNavigatePage) {
					pages = (totalPage - (maxNavigatePage * 10));
				} else {
					pages = 10;
				}
				
				} else {
					pages = totalPage;
					startPage = 0;
				}
				
				
				int[] navigatepageNums = new int[pages];
				
				for(int i=0; i < pages; i++) {
					navigatepageNums[i] = startPage + i + 1;
				}  
				
				
				pageMap.put("totalCount", total);
				pageMap.put("totalPage", totalPage);
				pageMap.put("pageNumber", pageNum);
				pageMap.put("size", pageSize);
				pageMap.put("startRow", startRow);
				pageMap.put("endRow",endRow);
				pageMap.put("total", total);
				pageMap.put("pages", pageNum);
				pageMap.put("prePage", prePage);
				pageMap.put("nextPage", nextPage);
				pageMap.put("navigatePages", navigatePages);
				pageMap.put("navigateFirstPage", 0);
				pageMap.put("navigateLastPage", totalPage);			
				pageMap.put("navigatepageNums", navigatepageNums);
				
				
				model.addAttribute("PageInfo", pageMap);// 페이징 정보 추출
			
			
			//}
			
			
			return model;
		}
	
	
	
	

}