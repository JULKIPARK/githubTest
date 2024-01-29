package com.pwc.pwcesg.frontoffice.search.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pwc.pwcesg.frontoffice.search.common.CommonUtil;
import com.pwc.pwcesg.frontoffice.search.common.SetParameter;
import com.pwc.pwcesg.frontoffice.search.data.CountVO;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;
import com.pwc.pwcesg.frontoffice.search.service.DocService;
import com.pwc.pwcesg.frontoffice.search.service.KsfService;
import com.pwc.pwcesg.frontoffice.search.service.MediaService;
import com.pwc.pwcesg.frontoffice.search.service.WebcontentsService;
import com.pwc.pwcesg.frontoffice.search.service.k_NewsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class Name : SearchController.java Description : 통합검색 조회를 위한 컨트롤러
 *
 * Modification Information
 *
 * 수정일 수정자 수정내용 -------------------- -----------
 * --------------------------------------- 2017년 12월 00일 김승희 최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/search")
public class SearchController {


	private final DocService docService;
	private final k_NewsService newsService; //bean에서 같은 이름이 있어서 변경해줘야 함
	private final MediaService mediaService;
	private final WebcontentsService webcontentsService;
	private final KsfService ksfService;
	
	private final SetParameter setParameter;
	
	private final CommonUtil commonUtil;
	
	private CountVO countVO = new CountVO();
	
	
	@Value("${konan.properties.url}")
	private String url;
	
	@Value("${konan.properties.ksfDomain}")
	private String ksfDomain;
	
	@Value("${konan.properties.ksfMaxResult}")
	private String ksfMaxResult;
	

	

	/**
	 * 메인 검색 페이지
	 */

	@RequestMapping("/main")
	public String main(@RequestParam Map<String, String> map, Model model) throws Exception {

		log.info("=========> search main BEGIN");
		// 파라미터 세팅
		ParameterVO paramVO = setParameter.setParameter(map);

		// 카테고리별 API 호출
		setCategoryModel(model, paramVO);

		// 파라미터
		model.addAttribute("params", paramVO);
		log.info("=========> search main END");

		return "frontoffice/search/search";
	}

	/**
	 * 모델 세팅 부분을 분리 카테고리 : 게시판 조회
	 * 
	 * @return Model
	 * 
	 * @throws Exception
	 */
	private Model setCategoryModel(Model model, ParameterVO paramVO) throws Exception {
		RestResultVO result;
		int total = 0;
		int pageSize = paramVO.getPageSize();
		int pageNum = paramVO.getPageNum();
		
		
		// 카테고리 여부
		if (paramVO.getKwd().length() > 0) {
			// 웹컨텐츠
			if (commonUtil.easyChkEqual("TOTAL,WEBCONTENTS", paramVO.getCategory(), ",", false)) {
				if(paramVO.getCategory().equals("WEBCONTENTS")) {
					paramVO.setPageSize(10);
				}
				result = webcontentsService.WebcontentsSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("webContentsRow", result.getRows());
				model.addAttribute("webContentsList", result.getResult());
				model.addAttribute("webContentsTotal", result.getTotal());

				if(paramVO.getCategory().equals("TOTAL")) {
					countVO.setWebCount((int) result.getTotal());
				}

			}				
			// 문서
			if (commonUtil.easyChkEqual("TOTAL,DOC", paramVO.getCategory(), ",", false)) {
				if(paramVO.getCategory().equals("DOC")) {
					paramVO.setPageSize(10);
				}
				result = docService.DocSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("docRow", result.getRows());
				model.addAttribute("docList", result.getResult());
				model.addAttribute("docTotal", result.getTotal());
				
				if(paramVO.getCategory().equals("TOTAL")) {
					countVO.setDocCount((int) result.getTotal());
				}

			}
			if(commonUtil.easyChkEqual("TOTAL,NEWS", paramVO.getCategory(), ",", false)) {
				if(paramVO.getCategory().equals("NEWS")) {
					paramVO.setPageSize(10);
				}
				result = newsService.NewsSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("newsRow", result.getRows());
				model.addAttribute("newsList", result.getResult());
				model.addAttribute("newsTotal", result.getTotal());
				
				if(paramVO.getCategory().equals("TOTAL")) {
					countVO.setNewsCount((int) result.getTotal());
				}

			}
			if(commonUtil.easyChkEqual("TOTAL,MEDIA", paramVO.getCategory(), ",", false)) {
				if(paramVO.getCategory().equals("MEDIA")) {
					paramVO.setPageSize(10);
				}
				result = mediaService.MediaSearch(paramVO);
				total += result.getTotal();

				model.addAttribute("mediaRow", result.getRows());
				model.addAttribute("mediaList", result.getResult());
				model.addAttribute("mediaTotal", result.getTotal());
				
				if(paramVO.getCategory().equals("TOTAL")) {
					countVO.setMediaCount((int) result.getTotal());
				}
				
			}
			if(paramVO.getCategory().equals("TOTAL")) {
				countVO.setTotalCount(total);
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
			
	        
	        
	        
			//-> ksf 연관검색어 호출
	        int domainNo = 0;
			int max_count = 5;
			String kwd = paramVO.getKwd();
			
			String sg_result = null;
			
			sg_result = ksfService.getRecommendKwd(domainNo, max_count, kwd);
			
			String[] sgarr = sg_result.substring(1, sg_result.length() -1).replace("\"","").split(",");

			
			if(sgarr.length > 1) {
				countVO.setRecommandResult(sgarr);
			}else if(sgarr.length == 1){
				countVO.setRecommandResult(null);
			}
			
		
			//계속 total값을 가지고 있을려고 countVO 사용. 페이지 렌더링 때문에 값이 사라짐
			model.addAttribute("total", countVO.getTotalCount());
			model.addAttribute("webTotal", countVO.getWebCount());
			model.addAttribute("docTotal", countVO.getDocCount());
			model.addAttribute("newsTotal", countVO.getNewsCount());
			model.addAttribute("mediaTotal", countVO.getMediaCount());
			model.addAttribute("recommandResult", countVO.getRecommandResult());
			model.addAttribute("PageInfo", pageMap);// 페이징 정보 추출
			

		}
		
		//검색어 null이거나 ''일 때
		if(paramVO.getKwd() == null || paramVO.getKwd() == "") { //검색어 null로 들어왔을 때
			model.addAttribute("total", 0);
			model.addAttribute("webTotal", 0);
			model.addAttribute("docTotal", 0);
			model.addAttribute("newsTotal", 0);
			model.addAttribute("mediaTotal", 0);
			model.addAttribute("recommandResult", "");
		}
		
		
		return model;
	}

}
