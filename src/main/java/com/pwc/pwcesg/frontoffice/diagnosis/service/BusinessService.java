package com.pwc.pwcesg.frontoffice.diagnosis.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.frontoffice.diagnosis.mapper.BusinessMapper;

import lombok.RequiredArgsConstructor;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class BusinessService {

    private final BusinessMapper businessMapper;

    /**
     *   조회
     * @param paramMap
     * @return
     */
    public  List<Map<String, Object>> selectContList(Map<String, Object> paramMap) {
        return businessMapper.selectContList(paramMap);
    }


}
