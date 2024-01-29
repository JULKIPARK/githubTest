package com.pwc.pwcesg.frontoffice.intro.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IntroMapper {
	
    /**
     * 접속로그 기록
     * @param paramMap
     * @return
     */
    public void insertAccsLog(Map<String, String> paramMap);
	
    /**
     * 세션접속로그 로그인 기록
     * @param paramMap
     * @return
     */
    public void insertSesnAccsLog(Map<String, Object> paramMap);
	
    /**
     * 세션접속로그 로그아웃 기록
     * @param paramMap
     * @return
     */
    public void updateSesnAccsLog(Map<String, Object> paramMap);
}
