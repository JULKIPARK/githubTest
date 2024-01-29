package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pwc.pwcesg.backoffice.contents.service.NewsService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.Arrays;
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
 * 뉴스 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/sems")
public class NewsController {

    private final NewsService newsService;

    /**
     * 뉴스 관리 목록 화면 조회
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/newsList")
    public ModelAndView newsList() {
        ModelAndView mav = new ModelAndView("backoffice/contents/news/newsList");

        return mav;
    }

    /**
     * 뉴스 관리 목록 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectNewsList")
    public ModelAndView selectNewsList(@RequestParam Map<String, Object> paramMap) {
        log.info("/selectNewsList >> params : " + paramMap);

        int pageNum = 1;
        if (paramMap.get("docPageNum") != null && !paramMap.get("docPageNum").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("docPageNum")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 10;
        if (paramMap.get("docPageSize") != null && !paramMap.get("docPageSize").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("docPageSize")); // 페이징 : Page 크기 건수
        }
        PageHelper.startPage(pageNum, pageSize);

        // 조회조건 조정 schRlsTgtChk
        if (paramMap.get("schRlsTgtChk") != null && !paramMap.get("schRlsTgtChk").equals("")) {
            List<String> schRlsTgtList = Arrays.asList(
                paramMap.get("schRlsTgtChk").toString().split(","));
            paramMap.put("schRlsTgtList", schRlsTgtList);
        }

        List<Map<String, Object>> list = newsService.selectNewsList(paramMap);
        String viewName = "backoffice/contents/news/newsList :: #docListDiv";
        if (paramMap.get("viewName") != null) {
            viewName = (String)paramMap.get("viewName");
        }
        ModelAndView mav = new ModelAndView(viewName);

        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     *  뉴스 관리 목록 엑셀 다운로드
     *
     * @param request, response, paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectNewsListExcelDown")
    public void selectNewsListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        log.info("/selectNewsListExcelDown paramMap : " + paramMap);

        newsService.selectNewsListExcelDown(response, paramMap);
    }

    /**
     * 뉴스 상태 변경
     *
     * @param contUidList
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/updateContSt")
    public ModelAndView updateContSt(@RequestParam(name = "contUids[]") List<String> contUidList) {
        log.info("/updateContSt params : " + contUidList);
        ModelAndView mav = new ModelAndView("jsonView");

        mav.addObject("deleteCnt", newsService.updateContSt(contUidList));
        return mav;
    }

    /**
     * 뉴스 관리 상세 조회
     *
     * @param contUid
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/selectNewsView")
    public ModelAndView selectNewsView(@RequestParam int contUid) {
        log.info("/sems/contents/selectDocumentView linkUrl : " + contUid);

        ModelAndView mav = new ModelAndView("jsonView");

        Map<String, Object> item = newsService.selectNewsView(contUid);
        if (item != null && item.get("contUid") != null) {
            List<Map<String, Object>> contTpicList = newsService.selectNewsTopicList((int) item.get("contUid"));
            item.put("contTpicList", contTpicList);
        }

        mav.addObject("item", item);

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 뉴스 등록
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/contents/updateNewsInfo")
    public ModelAndView updateNewsInfo(
        HttpServletRequest request
        , @RequestParam Map<String, Object> paramMap    //파라미터
        , @RequestParam(required = false, name = "rlsTgtCd") List<String> rlsTgtCds
        //일부 공개 대상
        , @RequestParam(required = false, name = "tpicUid") List<Integer> tpicUids
        //ESG토픽
    ) {
        HttpSession session = request.getSession();
        SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
        paramMap.put("fstInsId", sessionData.getMbrId()); //최초등록아이디
        paramMap.put("lstUpdId", sessionData.getMbrId()); //최종수정아이디

        //일부 공개 대상 문자열로 변형
        if (rlsTgtCds != null) {
            paramMap.put("rlsTgtCds", String.join(",", rlsTgtCds));
        }
        if (paramMap.get("pirdStngUseYn") == null) {
            paramMap.put("pirdStngUseYn", "N");
        }

        paramMap.put("tpicUids", tpicUids); // ESG토픽

        ModelAndView mav = new ModelAndView("jsonView");
        log.info("/updateNewsInfo params : " + paramMap);
        newsService.updateNewsInfo(paramMap);

        return mav;
    }

}
