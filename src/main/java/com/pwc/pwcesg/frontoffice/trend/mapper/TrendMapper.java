package com.pwc.pwcesg.frontoffice.trend.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ESG 자료·동향 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface TrendMapper {
	
    /**
     * 삼일인싸이트 - 토픽 목록 조회
     */
    public List<Map<String, Object>> selectDpTpicInfoList(Map<String, Object> paramMap);
	
    /**
     * 삼일인싸이트 - 발간연도 목록조회
     */
    public List<Map<String, Object>> selectPblcnYyList(Map<String, Object> paramMap);
    
    /**
     * 삼일인싸이트 - 레포트 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectPwcReportList(Map<String, Object> paramMap);
    
    /**
     * 삼일인싸이트 - 미디어 영상 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectPwcMediaList(Map<String, Object> paramMap);
	
    /**
     * 삼일인싸이트 - 뉴스목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectPwcNewsList(Map<String, Object> paramMap);
    
    /**
     * ESG 자료실 > 기준·가이드라인 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectBbsGuideList(Map<String, Object> paramMap);
    
    /**
     * ESG 자료 > 법·제도 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsLowList(Map<String, Object> paramMap);
    
    /**
     * ESG 자료 > 연구·보고 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsResearchList(Map<String, Object> paramMap);
    
    /**
     * ESG 자료 > 미디어영상 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsMediaList(Map<String, Object> paramMap);
    
    /**
     * ESG 뉴스 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectEsgNewsList(Map<String, Object> paramMap);
    
    /**
     * ESG 교육 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectEducationList(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectEducationList2(Map<String, Object> paramMap);

    public List<Map<String, String>> selectTrendList(Map<String, String> paramMap);
}
