package com.pwc.pwcesg.frontoffice.calendar.controller;

import com.pwc.pwcesg.frontoffice.calendar.mapper.CalendarMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import com.pwc.pwcesg.frontoffice.about.service.AboutService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/calendar/")
public class CalendarController {

	private final CalendarMapper calendarService;

	/**
	 * calendar 화면
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "calendarView")
	public ModelAndView calendarView() {
		return new ModelAndView("frontoffice/calendar/calendar");
	}

	/**
	 * calendar 목록 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "selectCalendarList")
	public ModelAndView selectCalendarList(@RequestParam Map<String, Object> paramMap) {

		ModelAndView mav = new ModelAndView("jsonView");

		List<Map<String, Object>> list = calendarService.selectCalendarList(paramMap);
		List<Map<String, Object>> rtnList = new ArrayList<>();

		for(int i=0; i<list.size(); i++) {
			Map<String, Object> item = list.get(i);

			if (item.get("schdlGbCd").equals("10")) {
				item.remove("end");
			}
			if (item.get("schdlGbCd").equals("30")) {
				item.remove("start");
				item.remove("end");
				item.put("etc_start", item.get("udtmnSchdl"));
			}

			if (item.get("allow").equals("")) {
				item.remove("allow");
			}
			item.remove("schdlGbCd");
			item.remove("schdlUid");
			item.remove("udtmnSchdl");
			item.remove("udtmnSchdlYy");
			item.remove("udtmnSchdlMm");
		}

		mav.addObject("list", list);

//		log.info("return : " + mav);

		return mav;
	}

}