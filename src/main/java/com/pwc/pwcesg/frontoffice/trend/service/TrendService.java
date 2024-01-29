package com.pwc.pwcesg.frontoffice.trend.service;

import com.pwc.pwcesg.frontoffice.common.mapper.FoCommonMapper;
import com.pwc.pwcesg.frontoffice.trend.mapper.TrendMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * ESG 자료·동향 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class TrendService {

    private final TrendMapper trendMapper;
    private final FoCommonMapper foCommonMapper;

	
    /**
     * 삼일인싸이트 - 토픽 목록 조회
     */
    public List<Map<String, Object>> selectDpTpicInfoList(Map<String, Object> paramMap){
        return trendMapper.selectDpTpicInfoList(paramMap);
    }
	
    /**
     * 삼일인싸이트 - 발간연도 목록조회
     */
    public List<Map<String, Object>> selectPblcnYyList(Map<String, Object> paramMap){
        return trendMapper.selectPblcnYyList(paramMap);
    }
    
    /**
     * PwC 인사이트 - 레포트 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectPwcReportList(Map<String, Object> paramMap) {
        return trendMapper.selectPwcReportList(paramMap);
    }
    
    /**
     * PwC 인사이트 - 미디어 영상 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectPwcMediaList(Map<String, Object> paramMap) {
        return trendMapper.selectPwcMediaList(paramMap);
    }
    
    /**
     * PwC 인사이트 - 뉴스목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectPwcNewsList(Map<String, Object> paramMap) {
        return trendMapper.selectPwcNewsList(paramMap);
    }
    
    /**
     * ESG 자료실 > 기준·가이드라인 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsGuideList(Map<String, Object> paramMap) {
        return trendMapper.selectBbsGuideList(paramMap);
    }
    
    /**
     * ESG 자료 > 법·제도 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsLowList(Map<String, Object> paramMap) {
        return trendMapper.selectBbsLowList(paramMap);
    }
    
    /**
     * ESG 자료 > 연구·보고 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsResearchList(Map<String, Object> paramMap) {
        return trendMapper.selectBbsResearchList(paramMap);
    }
    
    /**
     * ESG 자료 > 미디어영상 목록 조회
     * @param paramMap
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectBbsMediaList(Map<String, Object> paramMap) {
        return trendMapper.selectBbsMediaList(paramMap);
    }
    
    /**
     *  ESG 뉴스목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectEsgNewsList(Map<String, Object> paramMap) {
        return trendMapper.selectEsgNewsList(paramMap);
    }
    
    /**
     * ESG 교육 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectEducationList(Map<String, Object> paramMap) {
        return trendMapper.selectEducationList(paramMap);
    }

    public List<Map<String, Object>> selectEducationList2(Map<String, Object> paramMap){

        List<Map<String, Object>> dataList = trendMapper.selectEducationList2(paramMap);
        for(int i = 0 ; i < dataList.size() ;i++)
        {
            paramMap.put("contUid",dataList.get(i).get("contUid"));
        }
        return dataList;

    }

    /**
     *  최신 뉴스 목록
     * @param paramMap : 토픽채번
     * @return
     */
    public List<Map<String, String>> selectTrendList(Map<String, String> paramMap){
        return trendMapper.selectTrendList(paramMap);
    }
    
}
