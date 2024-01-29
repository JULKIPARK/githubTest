package com.pwc.pwcesg.frontoffice.about.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.frontoffice.about.mapper.AboutMapper;

import lombok.RequiredArgsConstructor;


/**
 * About Us Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class AboutService {
	
    private final AboutMapper aboutMapper;

    /**
     * About Us 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectAboutList(Map<String, Object> paramMap) {
        return aboutMapper.selectAboutList(paramMap);
    }
}
