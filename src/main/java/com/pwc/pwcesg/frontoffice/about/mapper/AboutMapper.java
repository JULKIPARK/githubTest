package com.pwc.pwcesg.frontoffice.about.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * About Us Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface AboutMapper {
    
    /**
     * About Us 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
    public List<Map<String, Object>> selectAboutList(Map<String, Object> paramMap);
}
