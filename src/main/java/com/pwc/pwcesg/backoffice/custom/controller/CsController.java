package com.pwc.pwcesg.backoffice.custom.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.custom.service.CsService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 문의 · 요청 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

	private final CsService csService;

	/**
	 * 문의 · 요청 관리 목록 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/custom/csList")
	public ModelAndView csList(@RequestParam(required = false) String askUid) {
		log.info("[askUid]{}", askUid);

		ModelAndView mav = new ModelAndView("backoffice/custom/cs/csList");
		mav.addObject("askUid", askUid);

		return mav;
	}

	/**
	 * 문의 · 요청 관리 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/custom/selectCsList")
	public ModelAndView selectCsList(@RequestParam Map<String, Object> paramMap, @RequestParam(value = "schAskTpChk") List<String> askTpCdList) {
		log.info("/selectCsList >> params : " + paramMap);
		paramMap.put("askTpCdList", askTpCdList);    //문의유형

		int pageNum = 1;
		if (paramMap.get("csPageNum") != null && !paramMap.get("csPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("csPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("csPageSize") != null && !paramMap.get("csPageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("csPageSize")); // 페이징 : Page 크기 건수
		}

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = csService.selectCsList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/custom/cs/csList :: #csListDiv");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 문의/답변 관리 상세조회
	 *
	 * @param askUid
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/custom/selectViewByCsDetailEditPop")
	public ModelAndView selectViewByCsDetailEditPop(@RequestParam String askUid) {
		log.info("/selectCsView askUid : " + askUid);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("item", csService.selectViewByCsDetailEditPop(askUid));

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 문의/답변 등록/수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/custom/upsertAnsrInfoToAskUid")
	public ModelAndView upsertAnsrInfoToAskUid(HttpServletRequest request
		, @RequestParam Map<String, Object> paramMap
		, @RequestParam(required = false, name = "uploadFile") MultipartFile uploadFile) throws Exception {
		log.info("askStCd : " + paramMap.get("askStCd"));
		log.info("askUid : " + paramMap.get("askUid"));

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		// Request Validation!!
		if (paramMap.get("askUid").equals("")) {
			log.error("### upsertAnsrInfoToAskUid ==> askUid is not empty");
			throw new IOException("### upsertAnsrInfoToAskUid ==> askUid is not empty");
		}
		paramMap.put("uploadFile", uploadFile);// 컨텐츠파일

		log.info("/upsertAnsrInfoToAskUid >> params : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", csService.upsertAnsrInfoToAskUid(paramMap));

		return mav;
	}

}
