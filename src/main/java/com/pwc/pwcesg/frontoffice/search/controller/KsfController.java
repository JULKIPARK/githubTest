package com.pwc.pwcesg.frontoffice.search.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pwc.pwcesg.frontoffice.search.common.CommonUtil;
import com.pwc.pwcesg.frontoffice.search.common.SetParameter;
import com.pwc.pwcesg.frontoffice.search.service.DocService;
import com.pwc.pwcesg.frontoffice.search.service.KsfService;
import com.pwc.pwcesg.frontoffice.search.service.MediaService;
import com.pwc.pwcesg.frontoffice.search.service.k_NewsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class Name : ksfController.java
 * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 컨트롤러
 *
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일     김승희            최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/search")
public class KsfController {	
	
	@Value("${konan.properties.ksfDomain}")
	private String ksfDomain;
	
	@Value("${konan.properties.ksfMaxResult}")
	private String ksfMaxResult;
	
	@Autowired
	private KsfService ksfService;
	
	
	/**
	 * 연관(추천) 검색어
	 */
	@RequestMapping(value = "/ksf/recommand", produces="application/json; charset=utf8")
	@ResponseBody
	public String getRecommandKwd(@RequestParam Map<String, String> map, Model model) {
		String domain = ksfDomain;
		String maxResult = "5";

		String result = null;
		
		log.info("recommand 1단계 ");
		log.info(map.toString());
		
		if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
			
			String kwd = map.get("term");
			log.info("recommand 2단계 ");
			
			int max_count = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxResult:map.get("max_count"));
			int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));
			
			log.info("recommand 3단계 ");
			
			result = ksfService.getRecommendKwd(domainNo, max_count, kwd);
			log.info("recommand >>>>> " + result);
			
		}
		return result;
	}
	
	
	
	
	
	
	
	
	/**
	 * 검색어 자동완성 단어 조회
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/akc", produces="application/json; charset=utf8")
	@ResponseBody
	public String getCompleteKwd(@RequestParam Map<String, String> map, Model model) throws Exception {
		String domain = ksfDomain;
		String maxRsesult = ksfMaxResult;
		
		String result = null;
		
		if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
			String kwd = map.get("term");
			String mode = (null == map.get("mode") || map.get("mode").isEmpty() )?"s":map.get("mode");
			int maxCount = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxRsesult :map.get("max_count"));
			int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));
			
			
			result = ksfService.getAutocomplete(kwd, mode, maxCount, domainNo);

		}
		return result;
	}	
	
	/**
	 * 오타교정 단어 조회
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/spell", produces="application/json; charset=utf8")
	@ResponseBody
	public String getSpellChek(@RequestParam Map<String, String> map, Model model) throws Exception {
		
		String result = null;
		
		if(StringUtils.isNotEmpty(map.get("term")) && map.get("term").length() > 0) {
			String kwd = map.get("term");
			result = ksfService.getSpellChek(kwd);

		}
		return result;
	}
	
	/**
	 * 인기검색어
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ksf/ppk", produces = "application/json; charset=utf8")
	@ResponseBody
	public String getPopularKwd(@RequestParam Map<String, String> map, Model model) throws Exception {
		
		log.debug( "/ksf/ppk.do");
		
		String domain = ksfDomain;
		String maxRsesult = ksfMaxResult;
		
		int maxCount = Integer.parseInt((null == map.get("max_count") || map.get("max_count").isEmpty() )? maxRsesult :map.get("max_count"));
		int domainNo = Integer.parseInt((null == map.get("domain") || map.get("domain").isEmpty() )?domain:map.get("domain"));		
		
		List<Map<String, String>> ppkList = ksfService.getPopularKwd(domainNo, maxCount);
		Gson gson = new Gson();
		
		
		log.debug( gson.toJson(ppkList));
		return gson.toJson(ppkList);

	}
}
