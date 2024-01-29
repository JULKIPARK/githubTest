package com.pwc.pwcesg.frontoffice.disclosure.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DisclosureMapper {

	/**
	 *   토픽 목록 조회
	 * @param tpicGbCd
	 * @return
	 */
	public List<Map<String, Object>> selectTpicInfoList();
	/**
     *   토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @return
	 */
	public Map<String, String> selectTpicApndInfo(Map<String, String> paramMap);
	/**
     *  토픽 부가정보 조회
	 * @param tpicUid : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectTpicApndInfoList(Map<String, String> paramMap);

	/**
     *  삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectPwcInsightsList(Map<String, String> paramMap);

	/**
     *  최신 동향 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectTrendList(Map<String, String> paramMap);

	/**
     *  ESG 영상모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectVideoList(Map<String, String> paramMap);

	/**
     *  ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectBbsList(Map<String, String> paramMap);



    /**
     * 설문 등록
     * @param paramMap
     * @return
     */
    public int insertSave(Map<String, Object> paramMap);


    public Map<String, Object> selectResult(Map<String, Object> paramMap);



}
