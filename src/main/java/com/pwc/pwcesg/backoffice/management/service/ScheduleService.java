package com.pwc.pwcesg.backoffice.management.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.management.mapper.ScheduleMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ESG일정 관리
 *
 * @author N.J.Kim
 */
@RequiredArgsConstructor
@Service
public class ScheduleService {

	private final ScheduleMapper scheduleMapper;

	/**
	 * ESG일정 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectScheduleList(Map<String, Object> paramMap) {
		return scheduleMapper.selectScheduleList(paramMap);
	}

	/**
	 * ESG일정 상세 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectScheduleView(String schdlUid) {
		return scheduleMapper.selectScheduleView(schdlUid);
	}

	/**
	 * ESG일정 등록
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public int insertSchedule(Map<String, Object> paramMap) {
		return scheduleMapper.insertSchdlInfo(paramMap);
	}

	/**
	 * ESG일정 수정
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public int updateSchedule(Map<String, Object> paramMap) {
		return scheduleMapper.updateSchdlInfo(paramMap);
	}

	/**
	 * ESG일정 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int deleteSchedule(Map<String, Object> paramMap) {
		return scheduleMapper.deleteSchdlInfo(paramMap);
	}
}
