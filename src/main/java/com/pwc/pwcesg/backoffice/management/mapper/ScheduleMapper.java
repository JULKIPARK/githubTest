package com.pwc.pwcesg.backoffice.management.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * ESG일정 관리 Mapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface ScheduleMapper {

	/**
	 * ESG일정 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectScheduleList(Map<String, Object> paramMap);

	/**
	 * ESG일정 관리 상세 조회
	 *
	 * @param schdlUid
	 * @return
	 */
	public Map<String, Object> selectScheduleView(String schdlUid);

	/**
	 * ESG일정 등록
	 *
	 * @param paramMap
	 * @return
	 */
	int insertSchdlInfo(Map<String, Object> paramMap);

	/**
	 * ESG일정 수정
	 *
	 * @param paramMap
	 * @return
	 */
	int updateSchdlInfo(Map<String, Object> paramMap);

	/**
	 * ESG일정 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	int deleteSchdlInfo(Map<String, Object> paramMap);
}
