package com.pwc.pwcesg.frontoffice.diagnosis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.pwc.pwcesg.config.SessionData;

@Mapper
public interface DiagnosisMapper {

	/**
	 * 회원정보 조회
	 * @param paramMap
	 * @return
	 */
    public SessionData selectMbMbrInfo(Map<String, Object> paramMap);


    /**
     * 설문 등록
     * @param paramMap
     * @return
     */
    public int insertSave(Map<String, Object> paramMap);

    /**
     * 답변 등록
     * @param paramMap
     * @return
     */
    public int insertAwSave(List<Map<String, Object>> paramMapList);


    public Map<String, Object> selectDiagnosisResult(Map<String, Object> paramMap);


    public List<Map<String, Object>> selectDiagnosisResultAw(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectDiagnosisResultSum(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectDiagnosisResultSubSum(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectDiagnosisResultChart1(Map<String, Object> paramMap);

    public List<Map<String, Object>> selectDiagnosisResultChart2(Map<String, Object> paramMap);


}
