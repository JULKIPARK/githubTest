package com.pwc.pwcesg.backoffice.popup.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.DocumentService;
import com.pwc.pwcesg.backoffice.contents.service.TopicService;
import com.pwc.pwcesg.backoffice.popup.service.PopupService;
import com.pwc.pwcesg.common.service.CommonService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 팝업 Controller
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PopupController {

	private final PopupService popupService;

	/**
	 * 콘텐츠 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/popup/selectContentList")
	public ModelAndView selectContentList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectContentList params : " + paramMap);

		int pageNum = 1;
		if (paramMap.get("docPageNum") != null && !paramMap.get("docPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("docPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("docPageSize") != null && !paramMap.get("docPageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("docPageSize")); // 페이징 : Page 크기 건수
		}
		PageMethod.startPage(pageNum, pageSize);

		// 조회조건 조정 schRlsTgtChk
		if (paramMap.get("schContUnos") != null && !paramMap.get("schContUnos").equals("")) {
			List<String> schContUnoList = Arrays.asList(paramMap.get("schContUnos").toString().split(","));
			paramMap.put("schContUnoList", schContUnoList);
		}

		List<Map<String, Object>> list = popupService.selectContentList(paramMap);
		String viewName = "backoffice/popup/selectContentsPop :: #docListDiv";
		if (paramMap.get("viewName") != null) {
			viewName = (String) paramMap.get("viewName");
		}
		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}



}
