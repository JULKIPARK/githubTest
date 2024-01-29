package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.ContentInfoService;
import com.pwc.pwcesg.common.service.CommonService;
import com.pwc.pwcesg.config.util.FileUtil;
import com.pwc.pwcesg.config.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ContentInfoController {

    private final ContentInfoService contentInfoService;

    private final CommonService commonService;

    @Value("${fileInfo.contents.uploadPath}")
    String contentsFilePath;

    /**
     * 콘텐츠정보 화면 조회
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/contentList")
    public ModelAndView contentList() {
        return new ModelAndView("backoffice/contents/contentInfo/contentList");
    }

    /**
     * 콘텐츠정보 목록 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectContentList2")
    public ModelAndView selectContentList(@RequestParam Map<String, Object> paramMap
            , @RequestParam(value = "schRlsTgtChk") List<String> rlsTgtCdList) {
        log.info("/selectContentList >> params : " + paramMap);

        paramMap.put("rlsTgtCdList", rlsTgtCdList);    //일부공개대상

        int pageNum = 1;
        if (paramMap.get("contPageNum") != null && !paramMap.get("contPageNum").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("contPageNum")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 20;
        if (paramMap.get("contPageSize") != null && !paramMap.get("contPageSize").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("contPageSize")); // 페이징 : Page 크기 건수
        }

        PageMethod.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = contentInfoService.selectContentList(paramMap);
        ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/contentList :: #contentListDiv");

        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 콘텐츠정보 동적 로딩
     */
    @RequestMapping(value = "/sems/contents/selectShowToModeByContentUpsertPop2")
    public ModelAndView selectShowToModeByContentUpsertPop(@RequestParam String contUid) {
        log.info("/selectShowToModeByContentUpsertPop contUid : " + contUid);

        String sFragment = "";

        if (contUid.equals("")) {
            sFragment = "backoffice/contents/contentInfo/contRegPop :: fragmentByContRegPop";
        } else {
            sFragment = "backoffice/contents/contentInfo/contModPop :: fragmentByContModPop";
        }
        ModelAndView mav = new ModelAndView(sFragment);

        log.info("getViewName:{}", mav.getViewName());

        mav.addObject("item", contentInfoService.selectShowByContentUpsertPop(contUid));
        mav.addObject("itemApndList", contentInfoService.selectContApndAtacByContUid(contUid));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 콘텐츠정보 상세 조회
     *
     * @param contUid
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectShowByContentUpsertPop2")
    public ModelAndView selectShowByContentUpsertPop(@RequestParam String contUid) {
        log.info("/selectShowByContentUpsertPop contUid : " + contUid);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("item", contentInfoService.selectShowByContentUpsertPop(contUid));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 콘텐츠정보 등록/수정
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/upsertByContRegPop2")
    public ModelAndView upsertByContRegPop(
            @RequestParam(name = "uploadImage") MultipartFile uploadImage,
            @RequestParam(required = false, name = "uploadFile") MultipartFile uploadFile,
            @RequestParam(required = false, name = "uploadLessons[]") List<MultipartFile> uploadLessons,
            @RequestParam(name = "rlsTgtCd", required = false) List<String> rlsTgtCds,
            @RequestParam(required = false, name = "lesnNm") List<String> lesnNms,
            @RequestParam(required = false, name = "vodQuntyHh") List<String> vodQuntyHhs,
            @RequestParam(required = false, name = "vodQuntyMi") List<String> vodQuntyMis,
            @RequestParam(required = false, name = "vodQuntySec") List<String> vodQuntySecs,
            @RequestParam(required = false, name = "vodComnt") List<String> vodComnts,
            @RequestParam Map<String, Object> paramMap
    ) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");

        if (paramMap.get("contKindCd") == null) {
            log.info("### required param contKindCd is null");
            return mav;
        }

        /**
         * 필수값 체크
         * 문서 10
         * 영상 20
         * 영상(원포인트레슨) 30
         */
        if (
                paramMap.get("contNm") == null ||
                        paramMap.get("pblcnGbCd") == null ||
                        paramMap.get("pblcnYy") == null ||
                        paramMap.get("pblcnMm") == null ||
                        paramMap.get("pblcnDd") == null ||
                        paramMap.get("srcNm") == null ||
                        paramMap.get("splyLangCd") == null ||
                        paramMap.get("contTpLclsfCd") == null ||
                        paramMap.get("contTpMclsfCd") == null ||
                        paramMap.get("taskClsfCd") == null ||
                        paramMap.get("tpicMnuCd") == null ||
                        paramMap.get("dpTpCd") == null ||
                        paramMap.get("contNm").equals("") ||
                        paramMap.get("pblcnGbCd").equals("") ||
                        paramMap.get("pblcnYy").equals("") ||
                        paramMap.get("pblcnMm").equals("") ||
                        paramMap.get("pblcnDd").equals("") ||
                        paramMap.get("srcNm").equals("") ||
                        paramMap.get("splyLangCd").equals("") ||
                        paramMap.get("contTpLclsfCd").equals("") ||
                        paramMap.get("contTpMclsfCd").equals("") ||
                        paramMap.get("taskClsfCd").equals("") ||
                        paramMap.get("tpicMnuCd").equals("") ||
                        paramMap.get("dpTpCd").equals("")
        ) {
            log.info("### required param check fail");
            return mav;
        }

        if (paramMap.get("contKindCd").equals("10")) {
            if (paramMap.get("contRmthdCd") == null || paramMap.get("contRmthdCd").equals("") ||
                    (paramMap.get("contRmthdCd").equals("20") && (paramMap.get("linkUrl") == null || paramMap.get("linkUrl").equals("")))) {
                log.info("### required param (contRmthdCd,linkUrl) check fail");
                return mav;
            }
        } else if (paramMap.get("contKindCd").equals("20")) {
            if (paramMap.get("contRmthdCd") == null || paramMap.get("contRmthdCd").equals("") ||
                    (paramMap.get("contRmthdCd").equals("10") && uploadFile == null) ||
                    (paramMap.get("contRmthdCd").equals("30") && (paramMap.get("linkUrl") == null || paramMap.get("linkUrl").equals("")))) {
                log.info("### required param (contRmthdCd,linkUrl) or uploadFile check fail");
                return mav;
            }
        }


        log.info("/upsertByContentUpsertPop >> params : " + paramMap);

        // todo case 별 유효성 검증 개발 필요!!

        Map<String, Object> convertParamMap = new HashMap<String, Object>(paramMap);

        convertParamMap.put("rlsTgtCds", rlsTgtCds);
        convertParamMap.put("lesnNms", lesnNms);
        convertParamMap.put("vodQuntyHhs", vodQuntyHhs);
        convertParamMap.put("vodQuntyMis", vodQuntyMis);
        convertParamMap.put("vodQuntySecs", vodQuntySecs);
        convertParamMap.put("vodComnts", vodComnts);


        mav.addObject("cnt", contentInfoService.upsertByContRegPop(convertParamMap, uploadImage, uploadFile, uploadLessons));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 콘텐츠정보 검색 팝업
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/sems/contents/selectContentSearchPopList2")
    public ModelAndView selectContentSearchPopList(@RequestParam Map<String, Object> paramMap) {
        log.info("/selectContentSearchPopList >> paramMap : " + paramMap);

        int pageNum = 1;
        if (paramMap.get("optPageNumberContentSearchPop") != null && !paramMap.get("optPageNumberContentSearchPop").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("optPageNumberContentSearchPop")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 10;
        if (paramMap.get("optPageSizeContentSearchPop") != null && !paramMap.get("optPageSizeContentSearchPop").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("optPageSizeContentSearchPop")); // 페이징 : Page 크기 건수
        }
        PageMethod.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = contentInfoService.selectContentSearchPopList(paramMap);
        ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/contentSearchPop :: #divContentList");

        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 출처정보 화면 조회
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/srcList2")
    public ModelAndView srcList() {
        return new ModelAndView("backoffice/contents/contentInfo/srcList");
    }

    /**
     * 출처정보 목록 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectSrcList2")
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
        List<Map<String, Object>> list = contentInfoService.selectSrcList(paramMap);
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
    @RequestMapping(value = "/sems/contents/selectSrcView2")
    public ModelAndView selectSrcView(@RequestParam String srcUid) {
        log.info("/selectSrcView srcUid : " + srcUid);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("item", contentInfoService.selectSrcView(srcUid));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 출처정보 등록/수정
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/upsertSrcView2")
    public ModelAndView upsertSrcView(@RequestParam Map<String, Object> paramMap) {
        log.info("/upsertSrcView >> params : " + paramMap);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("cnt", contentInfoService.upsertSrcView(paramMap));

        return mav;
    }

    /**
     * 출처정보 상세
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectSrcDetailEditView2")
    public ModelAndView selectSrcDetailEditView(@RequestParam Map<String, Object> paramMap) {
        log.info("/selectSrcDetailView >> paramMap : " + paramMap);

        Map<String, Object> info = contentInfoService.selectSrcDetailBySrcUid(paramMap.get("srcUidByDetailEditPop").toString());

        int pageNum = 1;
        if (paramMap.get("srcDetailEditPopPageNum") != null && !paramMap.get("srcDetailEditPopPageNum").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("srcDetailEditPopPageNum")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 10;
        if (paramMap.get("srcDetailEditPopPageSize") != null && !paramMap.get("srcDetailEditPopPageSize").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("srcDetailEditPopPageSize")); // 페이징 : Page 크기 건수
        }
        PageMethod.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = contentInfoService.selectContentBySrcUid(paramMap.get("srcUidByDetailEditPop").toString());
        ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/srcDetailEditPop :: #srcDetailEditPopListDiv");

        mav.addObject("info", info);
        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list))); // 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 출처정보 출처명 수정
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/updateSrcInfoToSrcNmBySrcId2")
    public ModelAndView updateSrcInfoToSrcNmBySrcId(@RequestParam Map<String, Object> paramMap) {
        log.info("/updateSrcInfoToSrcNmBySrcId >> params : " + paramMap);

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("cnt", contentInfoService.updateSrcInfoToSrcNmBySrcId(paramMap));

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 출처 검색
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectSrcSearchPopView2")
    public ModelAndView selectSrcSearchPopView(@RequestParam Map<String, Object> paramMap) {
        log.info("/selectSrcSearchPopView >> params : " + paramMap);

        int pageNum = 1;
        if (paramMap.get("optPageNumSrcSearchPop") != null && !paramMap.get("optPageNumSrcSearchPop").equals("")) {
            pageNum = Integer.parseInt((String) paramMap.get("optPageNumSrcSearchPop")); // 페이징 : 현재 Page 번호
        }
        int pageSize = 10;
        if (paramMap.get("optPageSizeSrcSearchPop") != null && !paramMap.get("optPageSizeSrcSearchPop").equals("")) {
            pageSize = Integer.parseInt((String) paramMap.get("optPageSizeSrcSearchPop")); // 페이징 : Page 크기 건수
        }

        PageMethod.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = contentInfoService.selectSrcSearchPopView(paramMap);
        ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/srcSearchPop :: #divSrcSearchPopList");

        mav.addObject("list", list);
        mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

        log.info("return : " + mav);

        return mav;
    }

    /**
     * 파일 업로드
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/uploadFile2")
    public ModelAndView uploadFile(MultipartHttpServletRequest multiRequest,
                                   @RequestParam Map<String, Object> paramMap
    ) {
        ModelAndView mav = new ModelAndView("jsonView");
        FileUtil fileUtil = new FileUtil();
        //파일명
        List<String> filesName = new ArrayList<>();
        filesName.add("123_123.png");//파일명(첨부파일채번_첨부상세채번.확장자)
        System.out.println("[uploadPath]" + contentsFilePath);
        //파일처리 Param
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("uploadPath", contentsFilePath);                        //업로드 경로
        param.put("uploadFiles", multiRequest.getFiles("uploadFiles"));    //List<MultipartFile> 업로드 파일
        param.put("saveFileName", filesName);                            //List<String> 업로드파일명
        List<Map<String, Object>> filesInfoList = fileUtil.fileUpload(param);

        for (Map<String, Object> fileinfo : filesInfoList) {
            log.info("[filePath]{}", fileinfo.get("filePath"));//파일경로
            log.info("[fileName]{}", fileinfo.get("fileName"));//파일명
            log.info("[fileMg]{}", fileinfo.get("fileMg"));//파일Size
        }

        log.info("/selectSrcSearchPopView >> params : " + multiRequest);

        return mav;
    }

    /**
     * 콘텐츠 목록 조회 (in 콘텐츠채번 where 콘텐츠토픽 메핑테이블의 토픽채번)
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/sems/contents/selectContentTopicByTpicUid2")
    public ModelAndView selectContentTopicByTpicUid(
            @RequestParam Map<String, Object> paramMap
    ) {
        log.info("/selectContentByTpicUid >> params : " + paramMap);

        List<Map<String, Object>> list = contentInfoService.selectContentTopicByTpicUid(paramMap);
        ModelAndView mav = new ModelAndView("backoffice/management/display/displayList :: #contentTopicListDiv");

        mav.addObject("list", list);

        log.info("return : " + mav);

        return mav;
    }

}
