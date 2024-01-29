package com.pwc.pwcesg.backoffice.management.service;

import com.pwc.pwcesg.backoffice.management.mapper.ConvertMapper;
import com.pwc.pwcesg.backoffice.management.mapper.ScheduleMapper;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ESG일정 관리
 *
 * @author N.J.Kim
 */
@RequiredArgsConstructor
@Service
public class ConvertService {

	private final ConvertMapper convertMapper;

	public List<Map<String, Object>> selectContentListForYoutube() {
		return convertMapper.selectContentListForYoutube();
	}

	public List<Map<String, Object>> selectContentListForMp4() {
		return convertMapper.selectContentListForMp4();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int updateContentDurationInfo(Map<String, Object> paramMap) {
		return convertMapper.updateContentDurationInfo(paramMap);
	}





//	/**
//	 * ESG일정 상세 조회
//	 *
//	 * @param paramMap
//	 * @return
//	 */
//	public Map<String, Object> selectScheduleView(String schdlUid) {
//		return scheduleMapper.selectScheduleView(schdlUid);
//	}
//
//	/**
//	 * ESG일정 등록
//	 *
//	 * @param paramMap
//	 * @return
//	 */
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
//		SQLException.class})
//	public int insertSchedule(Map<String, Object> paramMap) {
//		return scheduleMapper.insertSchdlInfo(paramMap);
//	}
//
//	/**
//	 * ESG일정 삭제
//	 *
//	 * @param paramMap
//	 * @return
//	 */
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
//	public int deleteSchedule(Map<String, Object> paramMap) {
//		return scheduleMapper.deleteSchdlInfo(paramMap);
//	}
}
