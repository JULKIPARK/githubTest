package com.pwc.pwcesg.frontoffice.mypage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 마이페이지 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface MypageMapper {

    /**
     * 마이페이지 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectMypageList(Map<String, Object> paramMap);

    /**
     * 자가진단 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectDiagnosisList(Map<String, Object> paramMap);

    /**
     * 마이페이지 CSRD 대상여부 진단 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectCsrdList(Map<String, Object> paramMap);

    /**
     * 마이페이지 컨텐츠 수 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectViewCount(Map<String, Object> paramMap);

}
