package com.pwc.pwcesg.backoffice.management.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pwc.pwcesg.backoffice.management.service.CurationService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.io.IOException;
import java.util.HashMap;
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

import java.util.List;
import java.util.Map;

/**
 * 큐레이션 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class CurationController {

	private final CurationService curationService;

	/**
	 * 큐레이션 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectCurationList")
	public ModelAndView selectCurationList(Map<String, Object> paramMap) {
		log.info("/selectCurationList params : " + paramMap);

		ModelAndView mav = new ModelAndView("backoffice/management/curation/curationList");

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 큐레이션 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/selectTpicList")
	public ModelAndView selectTpicList(@RequestParam Map<String, Object> paramMap) {
		log.info("/getDate params : " + paramMap);

		List<Map<String, Object>> list = curationService.selectTpicList(paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 큐레이션 토픽 정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/selectTpicInfo")
	public ModelAndView selectTpicInfo(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectTpicInfo params : " + paramMap);

		Map<String, Object> info = curationService.selectTpicInfo(paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("info", info);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/selectContTpicList")
	public ModelAndView selectContTpicList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectContTpicList params : " + paramMap);

		List<Map<String, Object>> list = curationService.selectContTpicList(paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 엑셀 다운로드
	 *
	 * @param request, response, paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/management/selectContTpicListExcelDown")
	public void selectContTpicListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		log.info("/selectContTpicListExcelDown paramMap : " + paramMap);

		curationService.selectContTpicListExcelDown(response, paramMap);
	}

	/**
	 * 큐레이션 토픽 목록 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/upsertTpicList")
	public ModelAndView upsertTpicList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		log.info("/upsertTpicList >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.upsertTpicList(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 토픽 정보 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/upsertTpicInfo")
	public ModelAndView updateTpicInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		log.info("/upsertTpicInfo >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.updateTpicList(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 토픽 추가 정보 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/upsertTpicApndInfo")
	public ModelAndView updateTpicApndInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		log.info("/upsertTpicApndInfo >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.upsertTpicApndInfo(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 삭제
	 *
	 * @param contUidList
	 * @return
	 */
	@RequestMapping(value = "/sems/management/addContTpicList")
	public ModelAndView deleteDocument(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, @RequestParam(name = "contUids[]") List<String> contUidList) {
		paramMap.put("contUidList", contUidList);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		log.info("/addContTpicList params : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("resultCnt", curationService.addContTpicList(paramMap));
		return mav;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/deleteContTpicList")
	public ModelAndView deleteContTpicList(@RequestParam Map<String, Object> paramMap) throws Exception {
		log.info("/deleteContTpicList >> params : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.deleteContTpicList(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 컨텐츠 토픽 목록 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/upsertContTpicList")
	public ModelAndView upsertContTpicList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		log.info("/upsertContTpicList >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.upsertContTpicList(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 토픽 전문가 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/selectTpicSpecialistList")
	public ModelAndView selectTpicSpecialistList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectContTpicList params : " + paramMap);

		List<Map<String, Object>> list = curationService.selectTpicSpecialistList(paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}
	@RequestMapping(value = "/sems/management/selectTpicSpecialistAll")
	public ModelAndView selectTpicSpecialistAll(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectTpicSpecialistAll params : " + paramMap);

		List<Map<String, Object>> list = curationService.selectTpicSpecialistAll(paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 큐레이션 토픽 전문가 목록 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/upsertTpicSpecialistInfo")
	public ModelAndView upsertTpicSpecialistInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap, @RequestParam(required = false, name = "spcalUids") List<String> spcalUids) throws Exception {
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		paramMap.put("spcalUids", spcalUids);

		log.info("/upsertTpicSpecialistInfo >> params : " + paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", curationService.upsertTpicSpecialistList(paramMap));

		return mav;
	}

	/**
	 * 큐레이션 토픽 전문가 정보 저장
	 *
	 * @param request, paramMap
	 * @return
	 */
	@RequestMapping(value = "/sems/management/saveSpecialistInfo")
	public ModelAndView saveSpecialistInfo(HttpServletRequest request
		, @RequestParam Map<String, Object> paramMap
		, @RequestParam(required = false, name = "uploadImage") MultipartFile uploadImage
	) {
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); // 최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); // 최종수정아이디

		paramMap.put("uploadImage", uploadImage);// 대표이미지

		ModelAndView mav = new ModelAndView("jsonView");
		log.info("/insertDocInfo params : " + paramMap);
		curationService.saveSpecialistInfo(paramMap);

		return mav;
	}

}
