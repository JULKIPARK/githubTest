package com.pwc.pwcesg.frontoffice.calendar.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CalendarMapper {

	/**
	 * 캘린더 목록 조회
	 * @return
	 */
	public List<Map<String, Object>> selectCalendarList(Map<String, Object> paramMap);

}
