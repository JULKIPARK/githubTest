package com.pwc.pwcesg.frontoffice.diagnosis.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.frontoffice.diagnosis.mapper.DiagnosisMapper;

import lombok.RequiredArgsConstructor;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class DiagnosisService {

    private final DiagnosisMapper diagnosisMapper;

    /**
     * 사용자정보 조회
     * @param paramMap
     * @return
     */
    public SessionData selectMbMbrInfo(Map<String, Object> paramMap) {
        return diagnosisMapper.selectMbMbrInfo(paramMap);
    }
    /**
     *  저장
     * @param paramMap
     * @return
     */
    public int insertSave(Map<String, Object> paramMap) {
        return diagnosisMapper.insertSave(paramMap);
    }
    /**
     *  답변 저장
     * @param paramMap
     * @return
     */
    public int insertAwSave(List<Map<String, Object>> paramMapList) {
        return diagnosisMapper.insertAwSave(paramMapList);
    }

    public Map<String, Object> selectDiagnosisResult(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResult(paramMap);
    }


    public List<Map<String, Object>> selectDiagnosisResultAw(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResultAw(paramMap);
    }

    public List<Map<String, Object>> selectDiagnosisResultSum(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResultSum(paramMap);
    }

    public List<Map<String, Object>> selectDiagnosisResultSubSum(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResultSubSum(paramMap);
    }

    public List<Map<String, Object>> selectDiagnosisResultChart1(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResultChart1(paramMap);
    }
    public List<Map<String, Object>> selectDiagnosisResultChart2(Map<String, Object> paramMap) {
        return diagnosisMapper.selectDiagnosisResultChart2(paramMap);
    }
}
