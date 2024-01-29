package com.pwc.pwcesg.backoffice.management.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.management.service.ConvertService;
import com.pwc.pwcesg.backoffice.management.service.ScheduleService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * ESG일정 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ConvController {

	private final ConvertService convertService;

	@RequestMapping(value = "/sems/management/convert")
	public ModelAndView schdlList() {
		return new ModelAndView("backoffice/management/convert/convert");
	}

	@RequestMapping(value = "/sems/management/selectContentListForYoutube")
	public ModelAndView selectContentListForYoutube(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectContentListForYoutube >> params : " + paramMap);

		List<Map<String, Object>> list = convertService.selectContentListForYoutube();
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", list);

		return mav;
	}

	@RequestMapping(value = "/sems/management/selectContentListForMp4")
	public ModelAndView selectContentListForMp4(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectContentListForMp4 >> params : " + paramMap);

		List<Map<String, Object>> list = convertService.selectContentListForMp4();
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", list);

		return mav;
	}

	@RequestMapping(value = "/sems/management/updateContentDurationInfo")
	public ModelAndView updateContentDurationInfo(@RequestParam Map<String, Object> paramMap) {
		log.info("/updateContentDurationInfo >> params : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", convertService.updateContentDurationInfo(paramMap));

		return mav;
	}



	/**
	 * ESG일정 상세 조회
	 *
	 * @param schdlUid
	 * @return ModelAndView
	 */
//	@RequestMapping(value = "/sems/management/selectScheduleView")
//	public ModelAndView selectScheduleView(HttpServletRequest request, @RequestParam String schdlUid) {
//		log.info("/selectScheduleView schdlUid : " + schdlUid);
//
//		HttpSession session = request.getSession();
//		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
//
//		ModelAndView mav = new ModelAndView("jsonView");
//		Map<String, Object> item = scheduleService.selectScheduleView(schdlUid);
//		item.put("mbrGbCd", sessionData.getMbrGbCd());
//		mav.addObject("item", item);
//
//		log.info("return : " + mav);
//
//		return mav;
//	}

	/**
	 * ESG일정 등록
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
//	@RequestMapping(value = "/sems/management/insertSchedule")
//	public ModelAndView insertSchedule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
//		log.info("/insertSchedule >> params : " + paramMap);
//
//		HttpSession session = request.getSession();
//		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
//		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
//		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디
//
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("cnt", scheduleService.insertSchedule(paramMap));
//
//		return mav;
//	}


	/**
	 * ESG일정 삭제
	 *
	 * @param schdlUids
	 * @return ModelAndView
	 */
//	@RequestMapping(value = "/sems/management/deleteSchedule")
//	public ModelAndView deleteSchedule(@RequestParam Map<String, Object> paramMap, @RequestParam(value = "schdlUid") List<String> schdlUids) {
//		paramMap.put("schdlUids", schdlUids);
//		log.info("/deleteSchedule >> paramMap : " + paramMap);
//
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("cnt", scheduleService.deleteSchedule(paramMap));
//
//		return mav;
//	}
}
