package com.pwc.pwcesg.frontoffice.trend.service;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.frontoffice.search.dao.MediaDAO;
import com.pwc.pwcesg.frontoffice.search.dao.NewsDAO;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;
import com.pwc.pwcesg.frontoffice.trend.dao.Trend_docDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
	
	private final Trend_docDAO trend_docDAO;
	private final MediaDAO mediaDAO;
	private final NewsDAO newsDAO;
	
	
	///////////////////////////////////////////////////////////////////
	/* DOCUMNET에서 호출*/
	
	/**
	 * ESG자료실 - 기준가이드라인
	 */
	public RestResultVO guideSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = trend_docDAO.guideSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}

	
	/**
	 * ESG자료실 - 법제도
	 */
	public RestResultVO legalSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = trend_docDAO.legalSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	
	
	/**
	 * ESG자료실 - 연구보고
	 */
	public RestResultVO reportSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = trend_docDAO.reportSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}

	
	/**
	 * 삼일인사이트 - 인사이트레포트
	 */
	public RestResultVO ins_insrpSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = trend_docDAO.ins_insrpSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	
	
	/**
	 * 삼일인사이트 - 뉴스레터
	 */
	public RestResultVO ins_NewsSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = trend_docDAO.ins_NewsSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	
	
	
	
	//////////////////////////////////////////////////////////////////
	/* MEDIA에서 호출 */
	
	/**
	 * ESG자료실 - 미디어
	 */
	public RestResultVO esg_mediaSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = mediaDAO.esg_mediaSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	
	
	/**
	 * 삼일인사이트 - 미디어
	 */
	public RestResultVO samilSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = mediaDAO.samilSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////
	/* NEWS에서 호출 */
	
	/**
	 * ESG 뉴스
	 */
	public RestResultVO newsSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = newsDAO.NewsSearch(paramVO);
		
		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}
	

	
	
	
	//예외 처리
	private Exception processException(String string) {
		// TODO Auto-generated method stub
		return null;
	}
    
    
}
