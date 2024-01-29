package com.pwc.pwcesg.frontoffice.disclosure.service;

import com.pwc.pwcesg.frontoffice.disclosure.mapper.DisclosureMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class DisclosureService {

    private final DisclosureMapper disclosureMapper;

	/**
	 *   토픽 목록 조회
	 * @param tpicGbCd
	 * @return
	 */
	public List<Map<String, Object>> selectTpicInfoList(){
        return disclosureMapper.selectTpicInfoList();
	}
	/**
     *  토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @param tpicUid : 토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
	 * @return
	 */
	public Map<String, String> selectTpicApndInfo(Map<String, String> paramMap){
        return disclosureMapper.selectTpicApndInfo(paramMap);
	}
	/**
     * ESG 실행 >> 삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectPwcInsightsList(Map<String, String> paramMap){
		List<Map<String, Object>> dataList = disclosureMapper.selectPwcInsightsList(paramMap);
		for(int i = 0 ; i < dataList.size() ;i++)
		{
			paramMap.put("contUid",dataList.get(i).get("contUid").toString());
		}
        return dataList;
	}

	/**
     * ESG 실행 >> 최신 동향 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectTrendList(Map<String, String> paramMap){
        return disclosureMapper.selectTrendList(paramMap);
	}

	/**
     * ESG 실행 >> ESG 영상모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectVideoList(Map<String, String> paramMap){
        return disclosureMapper.selectVideoList(paramMap);
	}

	/**
     * ESG 실행 >> ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectBbsList(Map<String, String> paramMap){
        return disclosureMapper.selectBbsList(paramMap);
	}
    /**
     *  저장
     * @param paramMap
     * @return
     */
    public int insertSave(Map<String, Object> paramMap) {
        return disclosureMapper.insertSave(paramMap);
    }

    public Map<String, Object> selectResult(Map<String, Object> paramMap) {
        return disclosureMapper.selectResult(paramMap);
    }


}
