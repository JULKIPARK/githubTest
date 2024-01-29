package com.pwc.pwcesg.frontoffice.mypage.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.frontoffice.mypage.mapper.MypageMapper;

import lombok.RequiredArgsConstructor;


/**
 * 마이페이지 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class MypageService {

    private final MypageMapper mypageMapper;

    /**
     * 마이페이지 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectMypageList(Map<String, Object> paramMap) {
        return mypageMapper.selectMypageList(paramMap);
    }

    /**
     * 자가진단 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectDiagnosisList(Map<String, Object> paramMap) {
        return mypageMapper.selectDiagnosisList(paramMap);
    }

    /**
     * 마이페이지 CSRD 대상여부 진단 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectCsrdList(Map<String, Object> paramMap) {
        return mypageMapper.selectCsrdList(paramMap);
    }
    /**
     * 마이페이지 컨텐츠 수 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectViewCount(Map<String, Object> paramMap) {
        return mypageMapper.selectViewCount(paramMap);
    }

}
