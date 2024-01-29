package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.DocumentService;
import com.pwc.pwcesg.backoffice.contents.service.SrcService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
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
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SrcController {

	private final SrcService srcService;
	private final DocumentService documentService;

	/**
	 * 출처정보 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/srcList")
	public ModelAndView srcList() {
		return new ModelAndView("backoffice/contents/contentInfo/srcList");
	}

	/**
	 * 출처정보 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectSrcList")
	public ModelAndView selectSrcList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectSrcList >> params : " + paramMap);

		int pageNum = 1;
		if (paramMap.get("srcPageNum") != null && !paramMap.get("srcPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("srcPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("srcPageSize") != null && !paramMap.get("srcPageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("srcPageSize")); // 페이징 : Page 크기 건수
		}

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = srcService.selectSrcList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/srcList :: #srcListDiv");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 출처정보 상세 조회
	 *
	 * @param srcUid
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectSrcView")
	public ModelAndView selectSrcView(@RequestParam String srcUid) {
		log.info("/selectSrcView srcUid : " + srcUid);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("item", srcService.selectSrcView(srcUid));

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 출처정보 목록 엑셀 다운로드
	 *
	 * @param request, response, paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectSrcListExcelDown")
	public void selectMemberListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		log.info("/selectSrcListExcelDown paramMap : " + paramMap);

		srcService.selectSrcListExcelDown(response, paramMap);
	}

	/**
	 * 출처정보 등록/수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/insertSrcView")
	public ModelAndView insertSrcView(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/insertSrcView >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", srcService.insertSrcView(paramMap));

		return mav;
	}

	/**
	 * 출처정보 수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/updateSrcView")
	public ModelAndView updateSrcView(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/updateSrcView >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", srcService.updateSrcView(paramMap));

		return mav;
	}

}
