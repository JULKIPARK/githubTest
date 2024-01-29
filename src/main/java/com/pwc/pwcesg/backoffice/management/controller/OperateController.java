package com.pwc.pwcesg.backoffice.management.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.management.service.OperateService;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OperateController {

	private final OperateService operateService;

	/**
	 * 관리자활동로그 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/operateList")
	public ModelAndView schdlList() {
		return new ModelAndView("backoffice/management/operate/operateList");
	}

	/**
	 * 관리자활동로그 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectOperateList")
	public ModelAndView selectOperateList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectOperateList >> params : " + paramMap);

		int pageNum = 1;
		if (paramMap.get("operatePageNum") != null && !paramMap.get("operatePageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("operatePageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("operatePageSize") != null && !paramMap.get("operatePageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("operatePageSize")); // 페이징 : Page 크기 건수
		}

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = operateService.selectOperateList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/management/operate/operateList :: #operateListDiv");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 관리자화면 목록 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectMnuScrList")
	public ModelAndView selectMnuScrList() {
		List<Map<String, Object>> list = operateService.selectMnuScrList();
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", list);
		log.info("return : " + mav);

		return mav;
	}

}
