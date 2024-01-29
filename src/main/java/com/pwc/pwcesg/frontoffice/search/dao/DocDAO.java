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
public class DocDAO {

	/** konan properties */
	@Value("${konan.properties.site}")
	private String site;
	
	@Value("${konan.properties.url}")
	private String url;
	
	@Value("${konan.properties.charset}")
	private String charset;
	
	@Value("${konan.properties.docField}")
	private String docField;
	
	@Value("${konan.properties.docFrom}")
	private String docFrom;
	
	@Value("${konan.properties.docHilight}")
	private String docHilight;

	
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

	public RestResultVO DocSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();	
		
		
		//필드별 검색
		if (paramVO.getFields().equals("all")) {
			paramVO.setSrchFd("");
		}else if(paramVO.getFields().equals("title")) {
			paramVO.setSrchFd("cont_nm");
		}else if(paramVO.getFields().equals("src")) {
			paramVO.setSrchFd("src_nm");
		}else if(paramVO.getFields().equals("choose_kwd")) {
			paramVO.setSrchFd("kwrd_arry");
		}
		
		String strNmFd = paramVO.getSrchFd().isEmpty()?"text_idx":paramVO.getSrchFd(); //선택되는 필드가 없으면 text_idx를 넣음

		//쿼리 부분	
		if(paramVO.getFields().equals("content")) {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
		}else {
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
		}
		
		
		//동의어
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
			query.append("");
		}else {
			query.append(" synonym('d0')");
		}

		//필터 조건
		//자료출처
		if(!paramVO.getDmstc_yn().isEmpty()) {
			query.append(" and dmstc_yn in{"+paramVO.getDmstc_yn() +"}");
		}
		
		//언어
		if(!paramVO.getSply_lang_cd().isEmpty()) {
			query.append(" and sply_lang_cd in{"+paramVO.getSply_lang_cd() +"}");
		}
		
		//발간연도
		if(!paramVO.getPblcn_yy().isEmpty()) {
		    String yearTemp[] = paramVO.getPblcn_yy().split(",");
		    java.util.List<String> yearsList = new ArrayList<>(Arrays.asList(yearTemp));

		    boolean contains2020 = yearsList.remove("2020"); // 2020년도가 있으면 제거하고 true를 반환

		    if(yearsList.size() > 0) {
		        query.append(" and (pblcn_yy in {");
		        for(int i = 0; i < yearsList.size(); i++) {
		            query.append(yearsList.get(i));
		            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
		                query.append(",");
		            }
		        }
		        query.append("}");
		        if(contains2020) {
		            query.append(" or pblcn_yy < 2021");
		        }
		        query.append(")");
		    } else if(contains2020) {
		        query.append(" and pblcn_yy < 2021");
		    }
		}
		
		
		

		//정렬 조건
		//(등록일 최신순)
		if("rd".equals(paramVO.getSort())) {
			query.append(" order by fst_ins_dt desc");
		}
		//(발간일 최신순)
		if("pd".equals(paramVO.getSort())) {
			query.append(" order by edit_pblcn_dte desc");
		}
		//(조회순)
		if("c".equals(paramVO.getSort())) {
			query.append(" order by vw_cnt desc");
		}
		//(정확도순)
		if("r".equals(paramVO.getSort())) {
			query.append(" order by $relevance desc");
		}
		
		
		//로그기록 
		//SITE@keyword
		sbLog.append(site + "@" + paramVO.getKwd());
		
		searchVO.setUrl(url);
		searchVO.setCharset(charset);
		searchVO.setFields(docField);
		searchVO.setFrom(docFrom);
		searchVO.setHilightTxt(docHilight);
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
