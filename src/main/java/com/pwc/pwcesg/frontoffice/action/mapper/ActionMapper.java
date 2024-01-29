package com.pwc.pwcesg.frontoffice.action.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActionMapper {

	/**
	 * ESG 실행 >> 토픽 목록 조회
	 * @param tpicGbCd
	 * @return
	 */
	public List<Map<String, Object>> selectActionTpicInfoList(String tpicMnuCd);

	/**
     * ESG 실행 >> 토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @return
	 */
	public Map<String, String> selectActionTpicApndInfo(Map<String, String> paramMap);

	/**
     * ESG 실행 >> 토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionTpicApndInfoList(Map<String, String> paramMap);

	/**
     * ESG 실행 >> 삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectActionPwcInsightsList(Map<String, String> paramMap);

	/**
     * ESG 실행 >> 최신 동향 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionTrendList(Map<String, String> paramMap);

	/**
     * ESG 실행 >> ESG 영상모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionVideoList(Map<String, String> paramMap);

	/**
     * ESG 실행 >> ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectActionBbsList(Map<String, String> paramMap);



	/**
     * ESG 실행>> 전문가
	 * @param paramMap : 콘텐츠채번
	 * @return
	 */
	public List<Map<String, String>> selectActionspcalList(Map<String, String> paramMap);
}
