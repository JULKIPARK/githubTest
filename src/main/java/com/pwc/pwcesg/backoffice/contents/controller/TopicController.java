package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.TopicService;
import com.pwc.pwcesg.config.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 토픽 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class TopicController {

    private final TopicService topicService;

    /**
     * 토픽 관리 목록 화면 조회
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/topicList")
    public ModelAndView topicList() {
        return new ModelAndView("backoffice/contents/topic/topicList");
    }

    /**
     * 토픽 관리 목록 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectTpicBySearchCondition")
    public ModelAndView selectTopicList(
            @RequestParam Map<String, Object> paramMap
    ) {
        log.info("/selectTopicList >> params : " + paramMap);

        int pageNum = 1;
        if (paramMap.get("topicPageNum") != null && !paramMap.get("topicPageNum").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("topicPageNum")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 10;
        if (paramMap.get("topicPageSize") != null && !paramMap.get("topicPageSize").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("topicPageSize")); // 페이징 : Page 크기 건수
        }

        PageMethod.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = topicService.selectTpicBySearchCondition(paramMap);
        ModelAndView mav = new ModelAndView("backoffice/contents/topic/topicList :: #topicListDiv");

        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 토픽 관리 상세 조회
     *
     * @param tpicUid
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectTpicByTpicUid")
    public ModelAndView selectTpicByTpicUid(@RequestParam String tpicUid) {
        log.info("/contents/selectTpicByTpicUid linkUrl : " + tpicUid);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("item", topicService.selectTpicByTpicUid(tpicUid));
        mav.addObject("apndItems", topicService.selectTpicApndByTpicUid(tpicUid));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 토픽정보 등록/수정
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/upsertByTopicPop")
    public ModelAndView upsertByTopicPop(
            @RequestParam(name = "rlsTgtCd") List<String> rlsTgtCds,
            @RequestParam(name = "tpicTpCd") List<String> tpicTpCds,
            @RequestParam(name = "apndCn") List<String> apndCns,
            @RequestParam(name = "apndTtlByService") List<String> apndTtlByServices,
            @RequestParam(name = "apndCnByService") List<String> apndCnByServices,
            @RequestParam(name = "apndTtlByExpert") List<String> apndTtlByExperts,
            @RequestParam(name = "spcalNmByExpert") List<String> spcalNmByExperts,
            @RequestParam(name = "spcalPosNmByExpert") List<String> spcalPosNmByExperts,
            @RequestParam(name = "spcalUrlByExpert") List<String> spcalUrlByExperts,
            @RequestParam Map<String, Object> paramMap
    ) throws Exception {

        log.info("/upsertByTopicPop >> params : " + paramMap);

        // todo case 별 유효성 검증 개발 필요!!

        Map<String, Object> convertParamMap = new HashMap<String, Object>(paramMap);

        convertParamMap.put("rlsTgtCds", rlsTgtCds);
        convertParamMap.put("tpicTpCds", tpicTpCds);
        convertParamMap.put("apndCns", apndCns);
        convertParamMap.put("apndTtlByServices", apndTtlByServices);
        convertParamMap.put("apndCnByServices", apndCnByServices);
        convertParamMap.put("apndTtlByExperts", apndTtlByExperts);
        convertParamMap.put("spcalNmByExperts", spcalNmByExperts);
        convertParamMap.put("spcalPosNmByExperts", spcalPosNmByExperts);
        convertParamMap.put("spcalUrlByExperts", spcalUrlByExperts);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("cnt", topicService.upsertByTopicPop(
                convertParamMap));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 토픽 목록 조회 (where 토픽구분코드, 토픽메뉴코드)
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectTpicByTpicGbCdAndTpicMnuCd")
    public ModelAndView selectTpicByTpicGbCdAndTpicMnuCd(
            @RequestParam Map<String, Object> paramMap
    ) {
        log.info("/selectTpicByTpicGbCdAndTpicMnuCd >> params : " + paramMap);

        List<Map<String, Object>> list = topicService.selectTpicByTpicGbCdAndTpicMnuCd(paramMap);
        ModelAndView mav = new ModelAndView(
                "backoffice/management/display/displayList :: #topicListDiv");

        mav.addObject("list", list);

        log.info("return : " + mav);

        return mav;
    }

}
