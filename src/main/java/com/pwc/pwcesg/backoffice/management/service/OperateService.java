package com.pwc.pwcesg.backoffice.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.management.mapper.OperateMapper;

import lombok.RequiredArgsConstructor;

/**
 * 운영 관리 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class OperateService {
	
    private final OperateMapper operateMapper;

    /**
     * 관리자활동로그 목록 조회
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectOperateList(Map<String, Object> paramMap) {
        return operateMapper.selectOperateList(paramMap);
    }

    /**
     * 관리자화면 목록 조회
     *
     * @return
     */
    public List<Map<String, Object>> selectMnuScrList() {
        return operateMapper.selectMnuScrList();
    }

}
