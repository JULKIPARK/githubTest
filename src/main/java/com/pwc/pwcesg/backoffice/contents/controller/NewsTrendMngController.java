package com.pwc.pwcesg.backoffice.contents.controller;

import com.pwc.pwcesg.backoffice.contents.service.NewsTrendMngService;
import com.pwc.pwcesg.config.SessionData;
import java.util.Arrays;
import java.util.HashMap;
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
 * 뉴스 트렌드 관리 Controller
 *
 * @author Edwin, Park
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class NewsTrendMngController {

	private final NewsTrendMngService newsTrendService;
//	private final TopicService topicService;
//	private final CommonService commonService;

	/**
	 * 뉴스 트렌드 키워드 관리 목록 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/newsTrendList")
	public ModelAndView rssNewsList() {
		return new ModelAndView("backoffice/contents/newsTrend/newsTrendList");
	}

	/**
	 * 뉴스 트렌드 키워드 관리 목록 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/selectNewsTrendKeywordList")
	public ModelAndView selectNewsTrendKeywordList() {
		List<Map<String, Object>> list = newsTrendService.selectNewsTrendKeywordList();
//		ModelAndView mav = new ModelAndView("jsonView");
		ModelAndView mav = new ModelAndView(
			"backoffice/contents/newsTrend/newsTrendList :: #resultDiv");

		String updateDt = "";
		if (list != null) {
			updateDt = list.get(0).get("regDt").toString();
		}

		mav.addObject("updateDate", "("+updateDt+")");
		mav.addObject("list", list);
		log.debug("[list] {}", list);

		return mav;
	}

	/**
	 * 뉴스 트렌드 키워드별 뉴스리 목록 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/selectNewsTrendNewsList")
	public ModelAndView selectNewsTrendNewsList(@RequestParam int keywordUid) {
		List<Map<String, Object>> list = newsTrendService.selectNewsTrendNewsList(keywordUid);
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", list);
		log.debug("[list] {}", list);

		return mav;
	}

	/**
	 * 뉴스 트렌드 키워드 노출여부 갱신
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/updateNewsTrendKeyword")
	public ModelAndView updateNewsTrendKeyword(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("[@RequestParam]{}", paramMap);

		ModelAndView mav = new ModelAndView("jsonView");

		String paramStr = paramMap.get("keywordUid").toString();
		String[] paramAry = paramStr.split(",");
		for(String param : paramAry) {
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("keywordUid", param.split("\\|")[0]);
			paramMap2.put("dpYn", param.split("\\|")[1]);

			int rtnCnt = newsTrendService.updateNewsTrendKeyword(paramMap2);
		}

		mav.addObject("msg", "success");
		return mav;
	}

	/**
	 * 뉴스 트렌드 뉴스 노출여부 갱신
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/updateNewsTrendNews")
	public ModelAndView updateNewsTrendNews(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("[@RequestParam]{}", paramMap);

		ModelAndView mav = new ModelAndView("jsonView");

		String paramStr = paramMap.get("newsUid").toString();
		String[] paramAry = paramStr.split(",");
		for(String param : paramAry) {
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("newsUid", param.split("\\|")[0]);
			paramMap2.put("dpYn", param.split("\\|")[1]);

			int rtnCnt = newsTrendService.updateNewsTrendNews(paramMap2);
		}

		mav.addObject("msg", "success");
		return mav;
	}

}
