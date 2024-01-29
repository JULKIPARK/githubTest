package com.pwc.pwcesg.frontoffice.intro.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.frontoffice.intro.mapper.IntroMapper;

import lombok.RequiredArgsConstructor;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service("IntroService")
public class IntroService {
	
    private final IntroMapper introMapper;
    
    /**
     * 접근이력 기록
     * @param paramMap
     * @return
     */
    public void insertAccsLog(Map<String, String> paramMap) {
        introMapper.insertAccsLog(paramMap);
    }
	
    /**
     * 세션접속로그 로그인 기록
     * @param paramMap
     * @return
     */
    public void insertSesnAccsLog(Map<String, Object> paramMap){
        introMapper.insertSesnAccsLog(paramMap);
    }
	
    /**
     * 세션접속로그 로그아웃 기록
     * @param paramMap
     * @return
     */
    public void updateSesnAccsLog(Map<String, Object> paramMap){
        introMapper.updateSesnAccsLog(paramMap);
    }
    
}
