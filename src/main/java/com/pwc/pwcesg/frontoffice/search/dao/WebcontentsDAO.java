package com.pwc.pwcesg.frontoffice.search.dao;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pwc.pwcesg.frontoffice.search.common.CommonUtil;
import com.pwc.pwcesg.frontoffice.search.common.DCUtil;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;
import com.pwc.pwcesg.frontoffice.search.data.SearchVO;

import groovyjarjarantlr.collections.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WebcontentsDAO {

	/** konan properties */
	@Value("${konan.properties.site}")
	private String site;
	
	@Value("${konan.properties.url}")
	private String url;
	
	@Value("${konan.properties.charset}")
	private String charset;
	
	@Value("${konan.properties.webContentsField}")
	private String webContentsField;
	
	@Value("${konan.properties.webContentsFrom}")
	private String webContentsFrom;
	
	@Value("${konan.properties.webContentsHilight}")
	private String webContentsHilight;

	
	/** 엔진 공통 유틸 */
	@Resource(name="dcUtil")
	private DCUtil dcUtil;
	
	/** common util Setting */
	@Resource(name="commonUtil")
	private CommonUtil commonUtil;
	
	/** REST 모듈 */
	@Resource(name="restModule")
	private RestModule restModule;
	

	/**
	 * 키워드에 맞는 문서관리 내용 리턴
	 * 
	 * @param kwd
	 * @throws IOException 
	 */

	public RestResultVO WebcontentsSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		
		//필드별 검색
		if (paramVO.getFields().equals("all")) {
			paramVO.setSrchFd("");
		}else if(paramVO.getFields().equals("title")) {
			paramVO.setSrchFd("page_title");
		}else if(paramVO.getFields().equals("content")) {
			paramVO.setSrchFd("page_content");
		}else if(paramVO.getFields().equals("src")) {
			paramVO.setSrchFd("");
		}else if(paramVO.getFields().equals("choose_kwd")) {
			paramVO.setSrchFd("");
		}
		
		
		String strNmFd = paramVO.getSrchFd().isEmpty()?"text_idx":paramVO.getSrchFd(); //선택되는 필드가 없으면 text_idx를 넣음

		//쿼리 부분
		query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
		
		//동의어
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
			
		}else {
			query.append(" synonym('d0')");
		}
		
		
		//로그기록 
		//SITE@keyword
		sbLog.append(site + "@" + paramVO.getKwd());
		
		searchVO.setUrl(url);
		searchVO.setCharset(charset);
		searchVO.setFields(webContentsField);
		searchVO.setFrom(webContentsFrom);
		searchVO.setHilightTxt(webContentsHilight);
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
		
		//URL 생성
		String restUrl = dcUtil.getRestURL(paramVO, searchVO); //get방식 URL생성
		log.info(restUrl);
		
		RestResultVO restVO = new RestResultVO();
		boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
		
		if(!success)
			return null;
		
		return restVO;
	}
}
