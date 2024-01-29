package com.pwc.pwcesg.frontoffice.action.service;

import com.pwc.pwcesg.frontoffice.action.mapper.ActionMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * ESG 실행 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class ActionService {

    private final ActionMapper actionMapper;

	/**
	 * ESG 실행 >> 토픽 목록 조회
	 * @param tpicGbCd
	 * @return
	 */
	public List<Map<String, Object>> selectActionTpicInfoList(String tpicMnuCd){
        return actionMapper.selectActionTpicInfoList(tpicMnuCd);
	}

	/**
     * ESG 실행 >> 토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @param tpicUid : 토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
	 * @return
	 */
	public Map<String, String> selectActionTpicApndInfo(Map<String, String> paramMap){
        return actionMapper.selectActionTpicApndInfo(paramMap);
	}

	/**
     * ESG 실행 >> 토픽 부가정보 목록 조회
	 * @param tpicUid : 토픽채번
	 * @param tpicUid : 토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
	 * @return
	 */
	public List<Map<String, String>> selectActionTpicApndInfoList(Map<String, String> paramMap){
        return actionMapper.selectActionTpicApndInfoList(paramMap);
	}

	/**
     * ESG 실행 >> 삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectActionPwcInsightsList(Map<String, String> paramMap){
		List<Map<String, Object>> dataList = actionMapper.selectActionPwcInsightsList(paramMap);
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
	public List<Map<String, String>> selectActionTrendList(Map<String, String> paramMap){
        return actionMapper.selectActionTrendList(paramMap);
	}

	/**
     * ESG 실행 >> ESG 영상모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionVideoList(Map<String, String> paramMap){
        return actionMapper.selectActionVideoList(paramMap);
	}

	/**
     * ESG 실행 >> ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionBbsList(Map<String, String> paramMap){
        return actionMapper.selectActionBbsList(paramMap);
	}
	/**
     * ESG 실행 >> 토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @param tpicUid : 토픽유형코드(10: 핵심개념, 20: 삼일제공서비스요약, 30: 삼일제공서비스전체, 40: 전문가리스트, 50: 프레임워크)
	 * @return
	 */
	public List<Map<String, String>> selectActionspcalList(Map<String, String> paramMap){
        return actionMapper.selectActionspcalList(paramMap);
	}
}
