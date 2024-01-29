package com.pwc.pwcesg.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	/**
	 * 파일 그룹 ID 조회
	 *
	 * @return Map<String, Object>
	 */
	public int getIntAtacFileUid();

	/**
	 * 파일 상세 정보 등록
	 *
	 * @param param
	 * @return void
	 */
	public void insertAtacDtlInfo(Map<String, Object> param);

	/**
	 * 파일 상세 정보 수정
	 *
	 * @param param
	 * @return void
	 */
	public void updateAtacDtlInfo(Map<String, Object> param);
	public void upsertAtacDtlInfo(Map<String, Object> param);
	public void updateAtacDtlInfoByEpubPdf(Map<String, Object> param);


	/**
	 * 공통 코드 목록 조회
	 *
	 * @param grpCd
	 * @return List<Map < String, Object>>
	 */
	public List<Map<String, Object>> selectCodeList(String grpCd);











	/**
	 * 하위 공통 코드 목록 조회
	 *
	 * @param grpCd
	 * @return
	 */
	public List<Map<String, Object>> selectUnderCodeList(Map<String, Object> paramMap);

	/**
	 * 파일 목록 조회
	 *
	 * @param Map<String,Object>
	 * @return List<Map < String, Object>>
	 */
	public List<Map<String, Object>> selectFileList(Map<String, Object> param);

	/**
	 * 파일 그룹 정보 등록
	 *
	 * @param Map<String,Object>
	 * @return void
	 */
	public void insertAtacFileInfo(Map<String, Object> param);

	/**
	 * 파일 그룹 정보 삭제
	 *
	 * @param Map<String, Object>
	 * @return int
	 */
	public int deleteFileGroup(Map<String, Object> param);

	/**
	 * 파일 상세 정보 삭제
	 *
	 * @param Map<String, Object>
	 * @return int
	 */
	public int deleteFileDetail(Map<String, Object> param);

	/**
	 * 파일 상세 번호 세팅
	 *
	 * @param String
	 * @return int
	 */
	public int getAtacDtlUid(String fileNo);

}