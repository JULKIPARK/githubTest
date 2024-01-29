package com.pwc.pwcesg.frontoffice.calendar.service;

import com.pwc.pwcesg.frontoffice.action.mapper.ActionMapper;
import com.pwc.pwcesg.frontoffice.calendar.mapper.CalendarMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 캘린더 Service
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService {

    private final CalendarMapper calendarMapper;

	/**
     * 캘린더 목록
	 * @return
	 */
	public List<Map<String, Object>> selectCalendarList(Map<String, Object> paramMap){
		List<Map<String, Object>> dataList = calendarMapper.selectCalendarList(paramMap);

        return dataList;
	}

}
