package com.pwc.pwcesg.frontoffice.qna.controller;

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
import com.pwc.pwcesg.frontoffice.qna.service.QnaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 문의·요청 Controller
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/qna/")
public class QnaController {

    private final QnaService qnaService;

    /**
     * Qna 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "qnaListView")
	public ModelAndView qnaListView(){
		return new ModelAndView("frontoffice/qna/qnaList");
	}

    /**
     * Qna 목록 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "selectQnaList")
	public ModelAndView selectQnaList(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap
			, @RequestParam(value="schAskTpChk", required = false) List<String> askTpCdList){
		paramMap.put("askTpCdList", askTpCdList);	//문의유형

        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
        PageMethod.startPage(pageNumber, pageSize);
		log.info("[@RequestParam]{}", paramMap);
		ModelAndView mav = new ModelAndView("jsonView");

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");
        paramMap.put("mbrId", sessionData.getMbrId());//회원채번

        // 목록 조회
        List<Map<String, Object>> list = qnaService.selectQnaList(paramMap);

        mav.addObject("page", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
		mav.addObject("list", list);

		return mav;
	}

    /**
     * 문의·요청 상세 조회
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "selectQna")
	public ModelAndView selectQna(@RequestParam String askUid) {
		log.info("[@RequestParam]{}", askUid);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("qnaInfo", qnaService.selectQna(askUid));

		return mav;
	}

    /**
     * Qna 등록
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "insertQna")
	public ModelAndView insertQna(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap){

		log.info("[@RequestParam]{}", paramMap);
		ModelAndView mav = new ModelAndView("jsonView");

		//등록자 정보
        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");
        paramMap.put("mbrId", sessionData.getMbrId());//회원채번
        paramMap.put("askMail", sessionData.getCompMail());//문의이메일
        paramMap.put("compNm", sessionData.getCoprNm());//회사명
        paramMap.put("coprUid", sessionData.getCoprUid());//소속채번

        qnaService.insertQna(paramMap);//Qna 등록
    	mav.addObject("msg", "success");

		return mav;
	}

    /**
     * Qna 수정
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "updateQna")
    public ModelAndView updateQna(
            HttpServletRequest request
            , @RequestParam Map<String, Object> paramMap){

        log.info("[@RequestParam]{}", paramMap);
        ModelAndView mav = new ModelAndView("jsonView");

        //등록자 정보
        HttpSession session = request.getSession(false);
        SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");
        paramMap.put("mbrId", sessionData.getMbrId());//회원채번

        qnaService.updateQna(paramMap);//Qna 등록
        mav.addObject("msg", "success");

        return mav;
    }
}
