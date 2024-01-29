package com.pwc.pwcesg.frontoffice.qna.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 문의·요청 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface QnaMapper {
    
    /**
     * 문의·요청 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectQnaList(Map<String, Object> paramMap);

    
    /**
     * 문의·요청 상세 조회
     * @param paramMap
     * @return ModelAndView
     */
    public Map<String, Object> selectQna(String askUid);


    /**
     * 문의·요청 등록
     * @param paramMap
     * @return
     */
    public int insertQna(Map<String, Object> paramMap);

    /**
     * 문의·요청 수정
     * @param paramMap
     * @return
     */
    public int updateQna(Map<String, Object> paramMap);
}
