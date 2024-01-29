package com.pwc.pwcesg.backoffice.custom.controller;

import com.pwc.pwcesg.backoffice.custom.service.RoleGroupService;
import com.pwc.pwcesg.config.SessionData;
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
 * 권한그룹정보 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class RoleGroupController {

	private final RoleGroupService roleGroupService;

	/**
	 * 권한그룹정보 목록 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/roleGroupList")
	public ModelAndView roleGroupList() {
		ModelAndView mav = new ModelAndView("backoffice/custom/roleGroup/roleGroupList");

		return mav;
	}

	/**
	 * 권한그룹정보 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectRoleGroupList")
	public ModelAndView selectRoleGroupList(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectRoleGroupList >> params : " + paramMap);

		List<Map<String, Object>> list = roleGroupService.selectRoleGroupList(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/custom/roleGroup/roleGroupList :: #roleGroupListDiv");

		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 권한그룹별 프로그램 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectAppListByRoleGroupCd")
	public ModelAndView selectAppListByRoleGroupCd(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectAppsListByRoleGroupCd >> params : " + paramMap);

		List<Map<String, Object>> list = roleGroupService.selectAppListByRoleGroupCd(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/custom/roleGroup/roleGroupList :: #appListDiv");

		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 권한그룹별 회원 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/custom/selectMemberListByMbrGbCd")
	public ModelAndView selectMemberListByMbrGbCd(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectMemberListByMbrGbCd paramMap : " + paramMap);

		List<Map<String, Object>> list = roleGroupService.selectMemberListByMbrGbCd(paramMap);
		ModelAndView mav = new ModelAndView("backoffice/custom/roleGroup/roleGroupList :: #memberListDiv");

		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	@RequestMapping(value = "/custom/selectMemberRightByMbrGbCd")
	public ModelAndView selectMemberRightByMbrGbCd(@RequestParam Map<String, Object> paramMap) {
		log.info("/selectMemberRightByMbrGbCd paramMap : " + paramMap);

		List<Map<String, Object>> list = roleGroupService.selectMemberRightByMbrGbCd(paramMap);
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("list", list);

		log.info("return : " + mav);

		return mav;
	}

	@RequestMapping(value = "/custom/updateMemberRoleGroup")
	public ModelAndView updateMemberRoleGroup(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/updateMemberRoleGroup >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		String[] strArray = paramMap.get("mbrUids").toString().split(",");
		List<Integer> mbrUidList = new ArrayList<>();
		for (String s : strArray) {
			mbrUidList.add(Integer.parseInt(s));
		}
		paramMap.put("mbrUidList", mbrUidList);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", roleGroupService.updateMemberRoleGroup(paramMap));

		return mav;
	}

	@RequestMapping(value = "/custom/updateMemberGroupRight")
	public ModelAndView updateMemberGroupRight(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		log.info("/updateMemberGroupRight >> params : " + paramMap);

		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("cnt", roleGroupService.updateMemberGroupRight(paramMap));

		return mav;
	}

}
