package com.pwc.pwcesg.backoffice.main.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.main.service.BoMainService;
import com.pwc.pwcesg.config.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 메인 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class BoMainController {

    private final BoMainService mainService;

    /**
     * 메인 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems")
    public String selectnewsList() {
        log.info("/sems");

        return "backoffice/main/mainView";
    }

    /**
     * 오늘의 할 일
     *
     * @return
     */
    @RequestMapping(value = "/sems/main/selectToDo")
    public ModelAndView selectToDo() {
        log.info("/sems/main/selectToDo");

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("item", mainService.selectToDo());

        return mav;
    }

    /**
     * 회원 현황
     *
     * @return
     */
    @RequestMapping(value = "/sems/main/selectMbrState")
    public ModelAndView selectMbrState() {
        log.info("/sems/main/selectMbrState");

//        ModelAndView mav = new ModelAndView("jsonView");
        ModelAndView mav = new ModelAndView("backoffice/main/mainView :: #mbrStateDiv");
        mav.addObject("list", mainService.selectMbrState());

        return mav;
    }

    /**
     * 회원 현황 집계
     *
     * @return
     */
    @RequestMapping(value = "/sems/main/selectMbrStateSum")
    public ModelAndView selectMbrStateSum() {
        log.info("/sems/main/selectMbrStateSum");

        ModelAndView mav = new ModelAndView("backoffice/main/mainView :: #mbrStateSumDiv");
        mav.addObject("listSum", mainService.selectMbrStateSum());

        return mav;
    }

    /**
     * 온라인 문의 현황
     *
     * @return
     */
    @RequestMapping(value = "/sems/main/selectAskCtCnt")
    public ModelAndView selectAskCtCnt() {
        log.info("/sems/main/selectMbrStateSum");

        ModelAndView mav = new ModelAndView("backoffice/main/mainView :: #askCtCntDiv");
        mav.addObject("askStCntList", mainService.selectAskCtCnt());

        return mav;
    }

    /**
     * 미확인 온라인 문의 목록
     *
     * @return
     */
    @RequestMapping(value = "/sems/main/selectUnconfirmedAskList")
    public ModelAndView selectUnconfirmedAskList(@RequestParam Map<String, Object> paramMap) {
        log.info("/sems/main/selectUnconfirmedAskList");
        log.info("[@RequestParam]{}", paramMap);

        ModelAndView mav = new ModelAndView("jsonView");

        // DB 쿼리 페이징 설정(현재페이지, 페이지크기)
        int pageNumber = Integer.parseInt((String)paramMap.get("pageNumber"));
        int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));
        PageMethod.startPage(pageNumber, pageSize);

        // 목록 조회
        List<Map<String, Object>> list = mainService.selectUnconfirmedAskList();

        mav.addObject("pagingInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 목록 데이터 페이징 처리
        mav.addObject("dataList", list);

        return mav;
    }
}
