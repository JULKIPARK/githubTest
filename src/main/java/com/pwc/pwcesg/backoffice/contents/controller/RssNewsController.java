package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.RssNewsService;
import com.pwc.pwcesg.backoffice.contents.service.TopicService;
import com.pwc.pwcesg.common.service.CommonService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 신규 뉴스 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class RssNewsController {

	private final RssNewsService rssNewsService;
	private final TopicService topicService;
	private final CommonService commonService;

	/**
	 * 신규 뉴스 관리 목록 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/rssNewsList")
	public ModelAndView rssNewsList() {
		return new ModelAndView("backoffice/contents/rssNews/rssNewsList");
	}

	/**
	 * 신규 뉴스 관리 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/selectRssNewsList")
	public ModelAndView selectRssNewsList(
		@RequestParam Map<String, Object> paramMap
	) {

		int pageNum = 1;
		if (paramMap.get("rssNewsPageNum") != null && !paramMap.get("rssNewsPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("rssNewsPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("rssNewsPageSize") != null && !paramMap.get("rssNewsPageSize")
			.equals("")) {
			pageSize = Integer.parseInt(
				(String) paramMap.get("rssNewsPageSize")); // 페이징 : Page 크기 건수
		}

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = rssNewsService.selectRssNewsList(paramMap);
		ModelAndView mav = new ModelAndView(
			"backoffice/contents/rssNews/rssNewsList :: #rssNewsListDiv");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		return mav;
	}

	/**
	 * 뉴스 관리 공통 데이터
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/rssNewsCommonDataList")
	public ModelAndView selectRssNewsCommonDataList() {
		log.info("/contents/rssNewsCommonDataList");

		ModelAndView mav = new ModelAndView("jsonView");

		//	데이터 패치
		mav.addObject("taskClsfList", commonService.selectCodeList("TASK_CLSF_CD"));
		mav.addObject("sbjtGbList", commonService.selectCodeList("SBJT_GB_CD"));
		List<Map<String, Object>> tpicMnuList = commonService.selectCodeList("TPIC_MNU_CD");
		List<Map<String, Object>> tpicList = topicService.selectTpicValidityList();
		tpicMnuList.forEach(item -> {
			List<Map<String, Object>> tpicMnuTpicList = new ArrayList<Map<String, Object>>();
			tpicList.forEach(item2 -> {
				if (item.get("cd").equals(item2.get("tpicMnuCd"))) {
					tpicMnuTpicList.add(item2);
				}
			});
			item.put("tpicList", tpicMnuTpicList);
		});
		mav.addObject("tpicMnuList", tpicMnuList);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 신규 뉴스 관리 상세 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/rssRegistView")
	public ModelAndView rssRegistView(@RequestParam String linkUrl) {
		log.info("/contents/rssRegistView linkUrl : " + linkUrl);

		ModelAndView mav = new ModelAndView("jsonView");

		Map<String, Object> item = rssNewsService.rssRegistView(linkUrl);
		if (item != null && item.get("contUid") != null) {
			List<Map<String, Object>> contTpicList = rssNewsService.rssRegistTopicList(
				(int) item.get("contUid"));
			item.put("contTpicList", contTpicList);
		}

		mav.addObject("item", item);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 신규 뉴스 등록
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/insertRss")
	public ModelAndView insertRss(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("[@RequestParam]{}", paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");

		paramMap.put("mbrId", sessionData.getMbrId());

		int rtnCnt = rssNewsService.insertRss(paramMap);
		if (rtnCnt == 1) {
			// 콘텐츠 부가정보 저장
			rtnCnt = rssNewsService.insertRssApnd(paramMap);
			if (rtnCnt == 1) {
				// 토픽맵핑정보 저장
				if (paramMap.get("tpicUid") != null && !paramMap.get("tpicUid").equals("")) {
					String[] tpicUidArry = paramMap.get("tpicUid").toString().split(",");
					for (String tpicUid : tpicUidArry) {
						paramMap.replace("tpicUid", tpicUid);

						rtnCnt = rssNewsService.insertRssTpic(paramMap);
					}
				}
			}
		}

		mav.addObject("msg", "success");
		return mav;
	}

	/**
	 * 신규 뉴스 일괄등록
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/contents/insertRssBatch")
	public ModelAndView insertRssBatch(
		HttpServletRequest request
		, @RequestParam List<String> chkRssInfo) {
		ModelAndView mav = new ModelAndView("jsonView");
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");

		Map<String, Object> paramMap = new HashedMap<>();
		paramMap.put("mbrId", sessionData.getMbrId());
		paramMap.put("chkRssInfo", chkRssInfo);

		mav.addObject("rtnCnt", rssNewsService.insertRssBatch(paramMap));
		return mav;
	}
}
