package com.pwc.pwcesg.backoffice.management.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.management.service.ScheduleService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * ESG일정 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ScheduleController {

	private final ScheduleService scheduleService;

	/**
	 * ESG일정 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/schdlList")
	public ModelAndView schdlList() {
		return new ModelAndView("backoffice/management/schedule/scheduleList");
	}

	/**
	 * ESG일정 관리 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectScheduleList")
	public ModelAndView selectScheduleList(@RequestParam Map<String, Object> paramMap, @RequestParam(value = "SchSchdlTpCd") List<String> SchSchdlTpCdList) {
		log.info("/selectScheduleList >> params : " + paramMap);
		paramMap.put("SchSchdlTpCdList", SchSchdlTpCdList);

		int pageNum = 1;
		int pageSize = 10000;

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = scheduleService.selectScheduleList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/management/schedule/scheduleList :: #schdlListDiv");

		List<Map<String, Object>> list1 = new ArrayList<>();
		List<Map<String, Object>> list2 = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).get("schdlGbCd").equals("30")) {
				list2.add(list.get(i));
			} else {
				list1.add(list.get(i));
			}
		}


		mav.addObject("list", list1);
		mav.addObject("list2", list2);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list1)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * ESG일정 상세 조회
	 *
	 * @param schdlUid
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectScheduleView")
	public ModelAndView selectScheduleView(HttpServletRequest request, @RequestParam String schdlUid) {
		log.info("/selectScheduleView schdlUid : " + schdlUid);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");

		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> item = scheduleService.selectScheduleView(schdlUid);
		item.put("mbrGbCd", sessionData.getMbrGbCd());
		mav.addObject("item", item);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * ESG일정 등록
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/insertSchedule")
	public ModelAndView insertSchedule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/insertSchedule >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", scheduleService.insertSchedule(paramMap));

		return mav;
	}

	/**
	 * ESG일정 수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/updateSchedule")
	public ModelAndView updateSchedule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/updateSchedule >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", scheduleService.updateSchedule(paramMap));

		return mav;
	}

	/**
	 * ESG일정 삭제
	 *
	 * @param schdlUids
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/deleteSchedule")
	public ModelAndView deleteSchedule(@RequestParam Map<String, Object> paramMap, @RequestParam(value = "schdlUid") List<String> schdlUids) {
		paramMap.put("schdlUids", schdlUids);
		log.info("/deleteSchedule >> paramMap : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", scheduleService.deleteSchedule(paramMap));

		return mav;
	}
}
