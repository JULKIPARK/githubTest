package com.pwc.pwcesg.frontoffice.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import com.pwc.pwcesg.frontoffice.mypage.service.MypageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 마이페이지 Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/mypage/")
public class MypageController {

    private final MypageService mypageService;

	/**
	 * 마이페이지 목록 화면
	 * @return ModelAndView
	 */
	@RequestMapping(value = "mypageUser")
	public ModelAndView mypageUser(){
		return new ModelAndView("frontoffice/mypage/mypageUserView");
	}








    /**
     * 마이페이지 목록 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "mypageListView")
	public ModelAndView mypageListView(HttpServletRequest request){

		ModelAndView mav = new ModelAndView("frontoffice/mypage/mypageListView");

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mbrUid", sessionData.getMbrUid());//회원채번
		//자가진단정보
        List<Map<String, Object>> dList = mypageService.selectDiagnosisList(paramMap);
		mav.addObject("dList", dList);
		//csrd 대상여부
        List<Map<String, Object>> cList = mypageService.selectCsrdList(paramMap);
		mav.addObject("cList", cList);

		//컨텐츠 카운터
        List<Map<String, Object>> vList = mypageService.selectViewCount(paramMap);
		mav.addObject("vList", vList);

		return mav;
	}

    /**
     * 마이페이지 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "selectMypageList")
	public ModelAndView selectMypageList(HttpServletRequest request,@RequestParam Map<String, Object> paramMap){
		log.info("[@RequestParam]{}", paramMap);
		ModelAndView mav = new ModelAndView("jsonView");

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");


		paramMap.put("mbrUid", sessionData.getMbrUid());//회원채번

        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
        PageMethod.startPage(pageNumber, pageSize);

        // 목록 조회
        List<Map<String, Object>> list = mypageService.selectMypageList(paramMap);

        mav.addObject("mypagePage", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
		mav.addObject("mypageList", list);

		return mav;
	}
    /**
     * 마이페이지 비밀번호 확인 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "mypageUserPwd")
	public ModelAndView mypageUserPwd(){
		return new ModelAndView("frontoffice/mypage/mypageUserPwdView");
	}
    /**
     * 마이페이지 비밀번호 변경 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "mypageUserPwdModify")
	public ModelAndView mypageUserPwdModify(){
		return new ModelAndView("frontoffice/mypage/mypageUserPwdModifyView");
	}

//	================================================================================================
//	DELETED ========================================================================================
//	================================================================================================

}