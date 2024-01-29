package com.pwc.pwcesg.backoffice.management.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 큐레이션 관리 Mapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface CurationMapper {

	/**
	 * 큐레이션 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicList(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 추가 정보 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicApndList(Map<String, Object> paramMap);

	/**
	 * 큐레이션 콘텐츠 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContTpicList(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 전문가 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicSpecialistList(Map<String, Object> paramMap);
	public List<Map<String, Object>> selectTpicSpecialistAll(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 정보 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 추가 정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertTpicApndInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 추가 정보 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateTpicApndInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 콘텐츠 토픽 정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertContTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 콘텐츠 토픽 정보 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateContTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 콘텐츠 토픽 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	public int deleteContTpicInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 전문가 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	public int deleteTpicSpecialistInfo(Map<String, Object> paramMap);

	/**
	 * 큐레이션 토픽 전문가 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertTpicSpecialistInfo(Map<String, Object> paramMap);

	public int insertSpecialistInfo(Map<String, Object> paramMap);
	public int updateSpecialistInfo(Map<String, Object> paramMap);

}
