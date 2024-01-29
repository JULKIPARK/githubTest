package com.pwc.pwcesg.frontoffice.trend.dao;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pwc.pwcesg.frontoffice.search.common.CommonUtil;
import com.pwc.pwcesg.frontoffice.search.common.DCUtil;
import com.pwc.pwcesg.frontoffice.search.dao.RestModule;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;
import com.pwc.pwcesg.frontoffice.search.data.SearchVO;

import groovyjarjarantlr.collections.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class Trend_docDAO {

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
	
	@Value("${konan.properties.docHilight2}")
	private String docHilight2;

	
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
	 * ESG 자료실 - 기준.가이드라인 (guide)
	 */

	public RestResultVO guideSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		StringBuffer squery = new StringBuffer();
		
		
		//전체 검색
		if(paramVO.getKwd().isEmpty()) {
			//전체 검색 쿼리
			searchVO.setUrl(url);
			searchVO.setCharset(charset);	
			searchVO.setFields(docField);		
			searchVO.setFrom(docFrom);
			if(paramVO.getKwd().isEmpty()==false) {
				  searchVO.setHilightTxt(docHilight);
				}else {
				  searchVO.setHilightTxt(docHilight2);	
				}
			squery.append(URLEncoder.encode(query.toString(), charset) + URLEncoder.encode("cont_tp_lclsf_cd = '20'", charset));
			
			//ESG 키워드
			if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
				squery.append(URLEncoder.encode(" and tpic_cd_arry in {", charset)+ paramVO.getEnv() + URLEncoder.encode("}",charset));
			}
			
			//ESG 주제분류
			if(!paramVO.getSbjt_gb_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and sbjt_gb_nm in {",charset) + paramVO.getSbjt_gb_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//ESG 업무분류
			if(!paramVO.getTask_clsf_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and task_clsf_nm in {",charset)+ paramVO.getTask_clsf_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//자료출처
			if(!paramVO.getDmstc_yn().isEmpty()) {
				squery.append(URLEncoder.encode(" and dmstc_yn in{",charset)+paramVO.getDmstc_yn() +URLEncoder.encode("}",charset));
			}
			
			//언어
			if(!paramVO.getSply_lang_cd().isEmpty()) {
				squery.append(URLEncoder.encode(" and sply_lang_cd in{",charset)+paramVO.getSply_lang_cd() +URLEncoder.encode("}",charset));
			}
			
			//발간연도
			if(!paramVO.getPblcn_yy().isEmpty()) {
			    String yearTemp[] = paramVO.getPblcn_yy().split(",");
			    java.util.List<String> yearsList = new ArrayList<>(Arrays.asList(yearTemp));

			    boolean contains2020 = yearsList.remove("2020"); // 2020년도가 있으면 제거하고 true를 반환

			    if(yearsList.size() > 0) {
			    	squery.append(URLEncoder.encode(" and (pblcn_yy in {",charset));
			        for(int i = 0; i < yearsList.size(); i++) {
			        	squery.append(yearsList.get(i));
			            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
			            	squery.append(",");
			            }
			        }
			        squery.append(URLEncoder.encode("}",charset));
			        if(contains2020) {
			        	squery.append(URLEncoder.encode(" or pblcn_yy < 2021",charset));
			        }
			        squery.append(URLEncoder.encode(")",charset));
			    } else if(contains2020) {
			    	squery.append(URLEncoder.encode(" and pblcn_yy < 2021",charset));
			    }
			}
			
			
			//정렬 조건
			//(등록일 최신순)
			if("rd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by fst_ins_dt desc",charset));
			}
			//(발간일 최신순)
			if("pd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by edit_pblcn_dte desc",charset));
			}
			//(조회순)
			if("c".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by vw_cnt desc",charset));
			}
			//(정확도순)
			if("r".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by $relevance desc",charset));
			}	
			
			searchVO.setQuery(squery.toString());
			/*
			String restUrl = dcUtil.getRestURL2(paramVO, searchVO); //get방식 URL생성
			log.info(restUrl);

			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출		
			
			if(!success)
				return null;
			
			return restVO;
			*/
			if(paramVO.getKwd().isEmpty()==false) {
				  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
				log.info(restUrl); 
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
				if(!success)
					return null;
				
				return restVO;
				}else {
				  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
				log.info(restUrl);
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
				if(!success)
					return null;
				
				return restVO;
				}	
		} 
	
			
		//필드별 검색
		if(paramVO.getFields().equals("title")) {
			paramVO.setSrchFd("cont_nm");
		}else if(paramVO.getFields().equals("src")) {
			paramVO.setSrchFd("src_nm");
		}else if(paramVO.getFields().equals("choose_kwd")) {
			paramVO.setSrchFd("kwrd_arry");
		}
		
		
		String strNmFd = paramVO.getSrchFd().isEmpty()?"text_idx":paramVO.getSrchFd(); //선택되는 필드가 없으면 text_idx를 넣음

		//쿼리 부분		
		if(paramVO.getFields().equals("til_cont")) {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
		}else {
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
		}
		
		//동의어
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
			
		}else {
			query.append(" synonym('d0')");
		}

		//문서/기준가이드라인
		query.append(" and cont_tp_lclsf_cd = '20'");
		
		
		
		
		//필터 조건
		//ESG 키워드
		if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
			query.append(" and tpic_cd_arry in {"+ paramVO.getEnv() + "}");
		}
		
		//ESG 주제분류
		if(!paramVO.getSbjt_gb_nm().isEmpty()) {
			query.append(" and sbjt_gb_nm in {"+ paramVO.getSbjt_gb_nm() +"} allword");
		}
		
		//ESG 업무분류
		if(!paramVO.getTask_clsf_nm().isEmpty()) {
			query.append(" and task_clsf_nm in {"+ paramVO.getTask_clsf_nm() +"} allword");
		}
		
		
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
		    	squery.append(URLEncoder.encode(" and (pblcn_yy in {",charset));
		        for(int i = 0; i < yearsList.size(); i++) {
		        	squery.append(yearsList.get(i));
		            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
		            	squery.append(",");
		            }
		        }
		        squery.append(URLEncoder.encode("}",charset));
		        if(contains2020) {
		        	squery.append(URLEncoder.encode(" or pblcn_yy < 2021",charset));
		        }
		        squery.append(URLEncoder.encode(")",charset));
		    } else if(contains2020) {
		    	squery.append(URLEncoder.encode(" and pblcn_yy < 2021",charset));
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
		if(paramVO.getKwd().isEmpty()==false) {
			  searchVO.setHilightTxt(docHilight);
			}else {
			  searchVO.setHilightTxt(docHilight2);	
			}
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
		//URL 생성
		//String restUrl = dcUtil.getRestURL(paramVO, searchVO); //get방식 URL생성
		//log.info(restUrl);
		
		if(paramVO.getKwd().isEmpty()==false) {
			  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
			log.info(restUrl); 
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
			if(!success)
				return null;
			
			return restVO;
			}else {
			  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
			log.info(restUrl);
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
			if(!success)
				return null;
			
			return restVO;
			}		
		/*
		RestResultVO restVO = new RestResultVO();
		boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
		
		if(!success)
			return null;
		
		return restVO;
		*/
	}
	
	
	
	
	
	
	
	/**
	 * ESG 자료실 - 법.제도 (legal)
	 */

	public RestResultVO legalSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		StringBuffer squery = new StringBuffer();
		
		
		
		//전체 검색
		if(paramVO.getKwd().isEmpty()) {
			//전체 검색 쿼리
			searchVO.setUrl(url);
			searchVO.setCharset(charset);	
			searchVO.setFields(docField);		
			searchVO.setFrom(docFrom);
			if(paramVO.getKwd().isEmpty()==false) {
				  searchVO.setHilightTxt(docHilight);
				}else {
				  searchVO.setHilightTxt(docHilight2);	
				}
				
			squery.append(URLEncoder.encode(query.toString(), charset) + URLEncoder.encode("cont_tp_lclsf_cd = '30'", charset));
			
			//ESG 키워드
			if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
				squery.append(URLEncoder.encode(" and tpic_cd_arry in {", charset)+ paramVO.getEnv() + URLEncoder.encode("}",charset));
			}
			
			//ESG 주제분류
			if(!paramVO.getSbjt_gb_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and sbjt_gb_nm in {",charset) + paramVO.getSbjt_gb_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//ESG 업무분류
			if(!paramVO.getTask_clsf_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and task_clsf_nm in {",charset)+ paramVO.getTask_clsf_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//자료출처
			if(!paramVO.getDmstc_yn().isEmpty()) {
				squery.append(URLEncoder.encode(" and dmstc_yn in{",charset)+paramVO.getDmstc_yn() +URLEncoder.encode("}",charset));
			}
			
			//언어
			if(!paramVO.getSply_lang_cd().isEmpty()) {
				squery.append(URLEncoder.encode(" and sply_lang_cd in{",charset)+paramVO.getSply_lang_cd() +URLEncoder.encode("}",charset));
			}
			
			//발간연도
			if(!paramVO.getPblcn_yy().isEmpty()) {
			    String yearTemp[] = paramVO.getPblcn_yy().split(",");
			    java.util.List<String> yearsList = new ArrayList<>(Arrays.asList(yearTemp));

			    boolean contains2020 = yearsList.remove("2020"); // 2020년도가 있으면 제거하고 true를 반환

			    if(yearsList.size() > 0) {
			    	squery.append(URLEncoder.encode(" and (pblcn_yy in {",charset));
			        for(int i = 0; i < yearsList.size(); i++) {
			        	squery.append(yearsList.get(i));
			            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
			            	squery.append(",");
			            }
			        }
			        squery.append(URLEncoder.encode("}",charset));
			        if(contains2020) {
			        	squery.append(URLEncoder.encode(" or pblcn_yy < 2021",charset));
			        }
			        squery.append(URLEncoder.encode(")",charset));
			    } else if(contains2020) {
			    	squery.append(URLEncoder.encode(" and pblcn_yy < 2021",charset));
			    }
			}
			
			
			//정렬 조건
			//(등록일 최신순)
			if("rd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by fst_ins_dt desc",charset));
			}
			//(발간일 최신순)
			if("pd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by edit_pblcn_dte desc",charset));
			}
			//(조회순)
			if("c".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by vw_cnt desc",charset));
			}
			//(정확도순)
			if("r".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by $relevance desc",charset));
			}	
			
			
			searchVO.setQuery(squery.toString());
			
			if(paramVO.getKwd().isEmpty()==false) {
				  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
				log.info(restUrl); 
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
				if(!success)
					return null;
				
				return restVO;
				}else {
				  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
				log.info(restUrl);
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
				if(!success)
					return null;
				
				return restVO;
				}		
		}
			
		
		
		
		
		//필드별 검색
		if(paramVO.getFields().equals("title")) {
			paramVO.setSrchFd("cont_nm");
		}else if(paramVO.getFields().equals("src")) {
			paramVO.setSrchFd("src_nm");
		}else if(paramVO.getFields().equals("choose_kwd")) {
			paramVO.setSrchFd("kwrd_arry");
		}
		
		String strNmFd = paramVO.getSrchFd().isEmpty()?"text_idx":paramVO.getSrchFd(); //선택되는 필드가 없으면 text_idx를 넣음

		//쿼리 부분		
		if(paramVO.getFields().equals("til_cont")) {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
		}else {
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
		}
		
		//동의어
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
			
		}else {
			query.append(" synonym('d0')");
		}
		
		//문서/법제도
		query.append(" and cont_tp_lclsf_cd='30'");
		
		
		

		//필터 조건
		//ESG 키워드
		if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
			query.append(" and tpic_cd_arry in {"+ paramVO.getEnv() + "}");
		}
		
		//ESG 주제분류
		if(!paramVO.getSbjt_gb_nm().isEmpty()) {
			query.append(" and sbjt_gb_nm in {"+ paramVO.getSbjt_gb_nm() +"} allword");
		}
		
		//ESG 업무분류
		if(!paramVO.getTask_clsf_nm().isEmpty()) {
			query.append(" and task_clsf_nm in {"+ paramVO.getTask_clsf_nm() +"} allword");
		}
		
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
		if(paramVO.getKwd().isEmpty()==false) {
			  searchVO.setHilightTxt(docHilight);
			}else {
			  searchVO.setHilightTxt(docHilight2);	
			}
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
	
		
		if(paramVO.getKwd().isEmpty()==false) {
			  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
			log.info(restUrl); 
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
			if(!success)
				return null;
			
			return restVO;
			}else {
			  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
			log.info(restUrl);
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
			if(!success)
				return null;
			
			return restVO;
			}		
	}
	
	
	

	
	
	/**
	 * ESG 자료실 - 연구 (report)
	 */

	public RestResultVO reportSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		StringBuffer squery = new StringBuffer();
		
		
		
		//전체 검색
		if(paramVO.getKwd().isEmpty()) {
			//전체 검색 쿼리
			searchVO.setUrl(url);
			searchVO.setCharset(charset);	
			searchVO.setFields(docField);		
			searchVO.setFrom(docFrom);
			if(paramVO.getKwd().isEmpty()==false) {
				  searchVO.setHilightTxt(docHilight);
				}else {
				  searchVO.setHilightTxt(docHilight2);	
				}
			squery.append(URLEncoder.encode(query.toString(), charset) + URLEncoder.encode("cont_tp_lclsf_cd = '40'", charset));
			
			//ESG 키워드
			if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
				squery.append(URLEncoder.encode(" and tpic_cd_arry in {", charset)+ paramVO.getEnv() + URLEncoder.encode("}",charset));
			}
			
			//ESG 주제분류
			if(!paramVO.getSbjt_gb_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and sbjt_gb_nm in {",charset) + paramVO.getSbjt_gb_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//ESG 업무분류
			if(!paramVO.getTask_clsf_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and task_clsf_nm in {",charset)+ paramVO.getTask_clsf_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//자료출처
			if(!paramVO.getDmstc_yn().isEmpty()) {
				squery.append(URLEncoder.encode(" and dmstc_yn in{",charset)+paramVO.getDmstc_yn() +URLEncoder.encode("}",charset));
			}
			
			//언어
			if(!paramVO.getSply_lang_cd().isEmpty()) {
				squery.append(URLEncoder.encode(" and sply_lang_cd in{",charset)+paramVO.getSply_lang_cd() +URLEncoder.encode("}",charset));
			}
			
			//발간연도
			if(!paramVO.getPblcn_yy().isEmpty()) {
			    String yearTemp[] = paramVO.getPblcn_yy().split(",");
			    java.util.List<String> yearsList = new ArrayList<>(Arrays.asList(yearTemp));

			    boolean contains2020 = yearsList.remove("2020"); // 2020년도가 있으면 제거하고 true를 반환

			    if(yearsList.size() > 0) {
			    	squery.append(URLEncoder.encode(" and (pblcn_yy in {",charset));
			        for(int i = 0; i < yearsList.size(); i++) {
			        	squery.append(yearsList.get(i));
			            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
			            	squery.append(",");
			            }
			        }
			        squery.append(URLEncoder.encode("}",charset));
			        if(contains2020) {
			        	squery.append(URLEncoder.encode(" or pblcn_yy < 2021",charset));
			        }
			        squery.append(URLEncoder.encode(")",charset));
			    } else if(contains2020) {
			    	squery.append(URLEncoder.encode(" and pblcn_yy < 2021",charset));
			    }
			}
			
			
			//정렬 조건
			//(등록일 최신순)
			if("rd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by fst_ins_dt desc",charset));
			}
			//(발간일 최신순)
			if("pd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by edit_pblcn_dte desc",charset));
			}
			//(조회순)
			if("c".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by vw_cnt desc",charset));
			}
			//(정확도순)
			if("r".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by $relevance desc",charset));
			}	
			
			
			searchVO.setQuery(squery.toString());
			
			if(paramVO.getKwd().isEmpty()==false) {
				  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
				log.info(restUrl); 
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
				if(!success)
					return null;
				
				return restVO;
				}else {
				  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
				log.info(restUrl);
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
				if(!success)
					return null;
				
				return restVO;
				}		
		}
		
	
		
		//필드별 검색
		if(paramVO.getFields().equals("title")) {
			paramVO.setSrchFd("cont_nm");
		}else if(paramVO.getFields().equals("src")) {
			paramVO.setSrchFd("src_nm");
		}else if(paramVO.getFields().equals("choose_kwd")) {
			paramVO.setSrchFd("kwrd_arry");
		}
		
		String strNmFd = paramVO.getSrchFd().isEmpty()?"text_idx":paramVO.getSrchFd(); //선택되는 필드가 없으면 text_idx를 넣음
	
		//쿼리 부분		
		if(paramVO.getFields().equals("til_cont")) {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
		}else {
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
		}
		
		//동의어
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
			
		}else {
			query.append(" synonym('d0')");
		}
		
		//문서/보고연구
		query.append(" and cont_tp_lclsf_cd='40'");
		
		


		//필터 조건
		
		//ESG 키워드
		if(!paramVO.getEnv().isEmpty()) {
			query.append(" and tpic_cd_arry in {"+ paramVO.getEnv() + "}");
		}
		
		//ESG 주제분류
		if(!paramVO.getSbjt_gb_nm().isEmpty()) {
			query.append(" and sbjt_gb_nm in {"+ paramVO.getSbjt_gb_nm() +"} allword");
		}
		
		//ESG 업무분류
		if(!paramVO.getTask_clsf_nm().isEmpty()) {
			query.append(" and task_clsf_nm in {"+ paramVO.getTask_clsf_nm() +"} allword");
		}
		
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
		if(paramVO.getKwd().isEmpty()==false) {
			  searchVO.setHilightTxt(docHilight);
			}else {
			  searchVO.setHilightTxt(docHilight2);	
			}
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
		
		if(paramVO.getKwd().isEmpty()==false) {
			  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
			log.info(restUrl); 
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
			
				//if(!success)
				//return null;
			
			return restVO;
			}else {
			  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
			log.info(restUrl);
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
		     	//if(!success)
				//return null;
			
			return restVO;
			}		
	}
	
	/**
	 * 삼일인사이트 - 인사이트레포트
	 */
	
	public RestResultVO ins_insrpSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		StringBuffer squery = new StringBuffer();
			
		
		//전체 검색
		if(paramVO.getKwd().isEmpty()) {
			//전체 검색 쿼리
			searchVO.setUrl(url);
			searchVO.setCharset(charset);	
			searchVO.setFields(docField);		
			searchVO.setFrom(docFrom);	
			if(paramVO.getKwd().isEmpty()==false) {
				  searchVO.setHilightTxt(docHilight);
				}else {
				  searchVO.setHilightTxt(docHilight2);	
				}		
			squery.append(URLEncoder.encode(query.toString() + "cont_tp_lclsf_cd='10' and cont_tp_mclsf_cd='20'", charset));
			
			//ESG 키워드
			if(!paramVO.getEnv().isEmpty() || paramVO.getEnv() != "") {
				squery.append(URLEncoder.encode(" and tpic_cd_arry in {", charset)+ paramVO.getEnv() + URLEncoder.encode("}",charset));
			}
			
			//ESG 주제분류
			if(!paramVO.getSbjt_gb_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and sbjt_gb_nm in {",charset) + paramVO.getSbjt_gb_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//ESG 업무분류
			if(!paramVO.getTask_clsf_nm().isEmpty()) {
				squery.append(URLEncoder.encode(" and task_clsf_nm in {",charset)+ paramVO.getTask_clsf_nm() +URLEncoder.encode("} allword",charset));
			}
			
			//자료출처
			if(!paramVO.getDmstc_yn().isEmpty()) {
				squery.append(URLEncoder.encode(" and dmstc_yn in{",charset)+paramVO.getDmstc_yn() +URLEncoder.encode("}",charset));
			}
			
			//언어
			if(!paramVO.getSply_lang_cd().isEmpty()) {
				squery.append(URLEncoder.encode(" and sply_lang_cd in{",charset)+paramVO.getSply_lang_cd() +URLEncoder.encode("}",charset));
			}
			
			//발간연도
			if(!paramVO.getPblcn_yy().isEmpty()) {
			    String yearTemp[] = paramVO.getPblcn_yy().split(",");
			    java.util.List<String> yearsList = new ArrayList<>(Arrays.asList(yearTemp));

			    boolean contains2020 = yearsList.remove("2020"); // 2020년도가 있으면 제거하고 true를 반환

			    if(yearsList.size() > 0) {
			    	squery.append(URLEncoder.encode(" and (pblcn_yy in {",charset));
			        for(int i = 0; i < yearsList.size(); i++) {
			        	squery.append(yearsList.get(i));
			            if(i < yearsList.size() - 1) { // 마지막 요소가 아니면 , 를 추가
			            	squery.append(",");
			            }
			        }
			        squery.append(URLEncoder.encode("}",charset));
			        if(contains2020) {
			        	squery.append(URLEncoder.encode(" or pblcn_yy < 2021",charset));
			        }
			        squery.append(URLEncoder.encode(")",charset));
			    } else if(contains2020) {
			    	squery.append(URLEncoder.encode(" and pblcn_yy < 2021",charset));
			    }
			}
			
			
			//정렬 조건
			//(등록일 최신순)
			if("rd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by fst_ins_dt desc",charset));
			}
			//(발간일 최신순)
			if("pd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by edit_pblcn_dte desc",charset));
			}
			//(조회순)
			if("c".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by vw_cnt desc",charset));
			}
			//(정확도순)
			if("r".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by $relevance desc",charset));
			}			
			
			
			searchVO.setQuery(squery.toString());
			
			if(paramVO.getKwd().isEmpty()==false) {
				  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
				log.info(restUrl); 
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
				if(!success)
					return null;
				
				return restVO;
				}else {
				  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
				log.info(restUrl);
				RestResultVO restVO = new RestResultVO();
				boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
				//if(!success)
					//return null;
				
				return restVO;
				}	
		}
		
		
		
		
		
		//검색키워드 있을 때
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

		//쿼리 부분  24-01-26 분기처리 ()추가	
		if(paramVO.getFields().equals("til_cont")) {
			query = dcUtil.makeQuery("(cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
			
		}else {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			/*
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
			*/
		}
		
		//동의어  24-01-26 분기처리 ()추가
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
		  query.append("");	
		}else {
			if(paramVO.getFields().equals("til_cont")) {
			query.append(" synonym('d0'))");
			}else {
			query.append(" synonym('d0')");
			}
		}
		
		query.append(" and cont_tp_lclsf_cd='10' and cont_tp_mclsf_cd='20'");
		
		//날짜검색기간 조회
		if(!paramVO.getStartDate().isEmpty() && !paramVO.getEndDate().isEmpty()) {
			query = dcUtil.makeRangeQuery("fst_ins_dt", paramVO.getStartDate()+"000000", paramVO.getEndDate()+"999999", query);
		}
		
		
		
		//ESG 키워드
		if(!paramVO.getEnv().isEmpty()) {
			query.append(" and tpic_cd_arry in {"+ paramVO.getEnv() + "}");
		}
		
		//ESG 주제분류
		if(!paramVO.getSbjt_gb_nm().isEmpty()) {
			query.append(" and sbjt_gb_nm in {"+ paramVO.getSbjt_gb_nm() +"} allword");
		}
		
		//ESG 업무분류
		if(!paramVO.getTask_clsf_nm().isEmpty()) {
			query.append(" and task_clsf_nm in {"+ paramVO.getTask_clsf_nm() +"} allword");
		}
		
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
		if(paramVO.getKwd().isEmpty()==false) {
			  searchVO.setHilightTxt(docHilight);
			}else {
			  searchVO.setHilightTxt(docHilight2);	
			}
					
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
		
		if(paramVO.getKwd().isEmpty()==false) {
			  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
			log.info(restUrl); 
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
			if(!success)
				return null;
			
			return restVO;
			}else {
			  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
			log.info(restUrl);
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
			if(!success)
				return null;
			
			return restVO;
			}	
	}
	
	
	/**
	 * 삼일인사이트 - 뉴스레터
	 */
	
	public RestResultVO ins_NewsSearch(ParameterVO paramVO) throws Exception {
		SearchVO searchVO = new SearchVO();
		//쿼리 생성
		StringBuffer query = new StringBuffer();
		StringBuffer sbLog = new StringBuffer();
		StringBuffer squery = new StringBuffer();
		
		//전체 검색
		if(paramVO.getKwd().isEmpty()) {
			//전체 검색 쿼리
			searchVO.setUrl(url);
			searchVO.setCharset(charset);	
			searchVO.setFields(docField);		
			searchVO.setFrom(docFrom);	
			if(paramVO.getKwd().isEmpty()==false) {
				  searchVO.setHilightTxt(docHilight);
				}else {
				  searchVO.setHilightTxt(docHilight2);	
				}
				
			squery.append(URLEncoder.encode(query.toString(), charset) + URLEncoder.encode("cont_tp_lclsf_cd='10' and cont_tp_mclsf_cd='10'", charset));
			
			//정렬 조건
			//(등록일 최신순)
			if("rd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by fst_ins_dt desc",charset));
			}
			//(발간일 최신순)
			if("pd".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by edit_pblcn_dte desc, fst_ins_dt desc",charset));
			}
			//(조회순)
			if("c".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by vw_cnt desc",charset));
			}
			//(정확도순)
			if("r".equals(paramVO.getSort())) {
				squery.append(URLEncoder.encode(" order by $relevance desc",charset));
			}	
			
			
			searchVO.setQuery(squery.toString());
			
			String restUrl = dcUtil.getRestURL2(paramVO, searchVO); //get방식 URL생성
			log.info(restUrl);
			
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출		
			
			if(!success)
				return null;
			
			return restVO;
		}
		
		
		
		
		//검색키워드 있을 때
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

		//쿼리 부분  24-01-26 분기처리 ()추가	
		if(paramVO.getFields().equals("til_cont")) {
			query = dcUtil.makeQuery("(cont_nm", paramVO.getKwd(), "allword", query, "AND");
			query.append(dcUtil.makeQuery("document", paramVO.getKwd(), "allword", query, "AND")); //pdf 파일 색인 후에 적용해야 함..
			
		}else {
			query = dcUtil.makeQuery("cont_nm", paramVO.getKwd(), "allword", query, "AND");
			/*
			query = dcUtil.makeQuery(strNmFd, paramVO.getKwd(), "allword", query, "AND");
			*/
		}
		
		//동의어  24-01-26 분기처리 ()추가
		if(paramVO.getSrchFd().equals("kwrd_arry")) {
		  query.append("");	
		}else {
			if(paramVO.getFields().equals("til_cont")) {
			query.append(" synonym('d0'))");
			}else {
			query.append(" synonym('d0')");
			}
		}
		
		query.append(" and cont_tp_lclsf_cd='10' and cont_tp_mclsf_cd='10'");
		
		
		
		//날짜검색기간 조회
		if(!paramVO.getStartDate().isEmpty() && !paramVO.getEndDate().isEmpty()) {
			query = dcUtil.makeRangeQuery("fst_ins_dt", paramVO.getStartDate()+"000000", paramVO.getEndDate()+"999999", query);
		}
		
		
		
		//정렬 조건
		//(등록일 최신순)
		if("rd".equals(paramVO.getSort())) {
			query.append(" order by fst_ins_dt desc");
		}
		//(발간일 최신순)
		if("pd".equals(paramVO.getSort())) {
			query.append(" order by edit_pblcn_dte desc, fst_ins_dt desc");
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
		if(paramVO.getKwd().isEmpty()==false) {
			  searchVO.setHilightTxt(docHilight);
			}else {
			  searchVO.setHilightTxt(docHilight2);	
			}
					
		searchVO.setQuery(URLEncoder.encode(query.toString(), charset));
		searchVO.setLogInfo(URLEncoder.encode(sbLog.toString(), charset));
		
		if(paramVO.getKwd().isEmpty()==false) {
			  String restUrl = dcUtil.getRestURL(paramVO, searchVO);
			log.info(restUrl); 
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출	
			if(!success)
				return null;
			
			return restVO;
			}else {
			  String restUrl = dcUtil.getRestURL2(paramVO, searchVO);
			log.info(restUrl);
			RestResultVO restVO = new RestResultVO();
			boolean success = restModule.restSearch(restUrl, restVO, searchVO.getFields());  //get방식 호출
			if(!success)
				return null;
			
			return restVO;
			}
	}
	
}
