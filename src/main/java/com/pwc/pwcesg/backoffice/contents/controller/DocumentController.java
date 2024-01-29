package com.pwc.pwcesg.backoffice.contents.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.pwc.pwcesg.backoffice.contents.service.DocumentService;
import com.pwc.pwcesg.backoffice.contents.service.TopicService;
import com.pwc.pwcesg.common.service.CommonService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.PagingUtil;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 문서·영상 관리 Controller
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class DocumentController {

	private final DocumentService documentService;
	private final CommonService commonService;
	private final TopicService topicService;

	/**
	 * 문서·영상 관리 화면 조회
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/document/documentList")
	public ModelAndView contentList() {
		ModelAndView mav = new ModelAndView("backoffice/contents/document/documentList");

		return mav;
	}

	/**
	 * 문서·영상 관리 공통 데이터
	 *
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/documentCommonDataList")
	public ModelAndView documentCommonDataList() {
		ModelAndView mav = new ModelAndView("jsonView");

		List<Map<String, Object>> tpicMnuList = commonService.selectCodeList("TPIC_MNU_CD");
		List<Map<String, Object>> tpicList = topicService.selectTpicValidityList();
		mav.addObject("tpicList", tpicList);
		tpicMnuList.forEach(item -> {
			List<Map<String, Object>> tpicMnuTpicList = new ArrayList<Map<String, Object>>();
			tpicList.forEach(item2 -> {
				if (item.get("cd").equals(item2.get("tpicMnuCd"))) {
					tpicMnuTpicList.add(item2);
				}
			});
			item.put("tpicList", tpicMnuTpicList);
		});
		mav.addObject("tpicMnuList", tpicMnuList);

		return mav;
	}

	/**
	 * 문서·영상 관리 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectDocumentList")
	public ModelAndView selectDocumentList(@RequestParam Map<String, Object> paramMap) {
		log.info("/getDate params : " + paramMap);

		int pageNum = 1;
		if (paramMap.get("docPageNum") != null && !paramMap.get("docPageNum").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("docPageNum")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("docPageSize") != null && !paramMap.get("docPageSize").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("docPageSize")); // 페이징 : Page 크기 건수
		}
		PageMethod.startPage(pageNum, pageSize);

		// 조회조건 조정 schRlsTgtChk
		if (paramMap.get("schRlsTgtChk") != null && !paramMap.get("schRlsTgtChk").equals("")) {
			List<String> schRlsTgtList = Arrays.asList(paramMap.get("schRlsTgtChk").toString().split(","));
			paramMap.put("schRlsTgtList", schRlsTgtList);
		}

		List<Map<String, Object>> list = documentService.selectContentList(paramMap);
		String viewName = "backoffice/contents/document/documentList :: #docListDiv";
		if (paramMap.get("viewName") != null) {
			viewName = (String) paramMap.get("viewName");
		}
		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 문서·영상 관리 목록 엑셀 다운로드
	 *
	 * @param request, response, paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectDocumentListExcelDown")
	public void selectMemberListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		log.info("/selectDocumentListExcelDown paramMap : " + paramMap);

		documentService.selectContentListExcelDown(response, paramMap);
	}

	/**
	 * 출처 목록 조회
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectSrcSearchPopList")
	public ModelAndView selectSrcSearchPopView(@RequestParam Map<String, Object> paramMap) {
		int pageNum = 1;
		if (paramMap.get("optPageNumSrcSearchPop") != null && !paramMap.get("optPageNumSrcSearchPop").equals("")) {
			pageNum = Integer.parseInt((String) paramMap.get("optPageNumSrcSearchPop")); // 페이징 : 현재 Page 번호
		}
		int pageSize = 10;
		if (paramMap.get("optPageSizeSrcSearchPop") != null && !paramMap.get("optPageSizeSrcSearchPop").equals("")) {
			pageSize = Integer.parseInt((String) paramMap.get("optPageSizeSrcSearchPop")); // 페이징 : Page 크기 건수
		}

		PageMethod.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = documentService.selectSrcSearchPopList(paramMap);
		//		ModelAndView mav = new ModelAndView("backoffice/contents/contentInfo/srcSearchPop :: #divSrcSearchPopList");
		ModelAndView mav = new ModelAndView("backoffice/contents/document/documentDetailEditPop :: #divSrcSearchPopList");

		mav.addObject("list", list);
		mav.addObject("PageInfo", PagingUtil.pageInfoDTO(PageInfo.of(list)));// 페이징 정보 추출

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 컨텐츠  > 문서 등록
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/insertDocInfo")
	public ModelAndView insertDocInfo(HttpServletRequest request
		, @RequestParam Map<String, Object> paramMap
		//파라미터
		, @RequestParam(required = false, name = "uploadFile") MultipartFile uploadFile
		, @RequestParam(required = false, name = "uploadPdfFile") MultipartFile uploadPdfFile
		//컨텐츠파일
		, @RequestParam(required = false, name = "uploadImage") MultipartFile uploadImage
		//대표이미지
		, @RequestParam(required = false, name = "rlsTgtCd") List<String> rlsTgtCds
		//일부 공개 대상
		, @RequestParam(required = false, name = "tpicUid") List<Integer> tpicUids
		//ESG토픽
		, @RequestParam(required = false, name = "uploadFiles") List<MultipartFile> uploadFiles
		//원포인트레슨 컨텐츠파일
		, @RequestParam(required = false, name = "uploadImages") List<MultipartFile> uploadImages
		//원포인트레슨 대표이미지
		, @RequestParam(required = false, name = "lesnNms") List<String> lesnNms
		//레슨명
		, @RequestParam(required = false, name = "vodQuntyHhs") List<String> vodQuntyHhs
		//영상분량시
		, @RequestParam(required = false, name = "vodQuntyMis") List<String> vodQuntyMis
		//영상분량분
		, @RequestParam(required = false, name = "vodQuntySecs") List<String> vodQuntySecs
		//영상분량초
		, @RequestParam(required = false, name = "vodComnts") List<String> vodComnts
		//영상설명
	) {
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); // 최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); // 최종수정아이디

		// 문서/영상
		if (rlsTgtCds != null) {
			paramMap.put("rlsTgtCds", String.join(",", rlsTgtCds));    // ESG토픽
		}
		if (paramMap.get("pirdStngUseYn") == null) {
			paramMap.put("pirdStngUseYn", "N");
		}
		paramMap.put("tpicUids", tpicUids); // ESG토픽
		paramMap.put("uploadFile", uploadFile);// 컨텐츠파일
		paramMap.put("uploadPdfFile", uploadPdfFile);// 컨텐츠파일
		paramMap.put("uploadImage", uploadImage);// 대표이미지

		// 원포인트레슨
		paramMap.put("uploadFiles", uploadFiles);// 원포인트레슨 컨텐츠파일
		paramMap.put("uploadImages", uploadImages);// 원포인트레슨 대표이미지
		paramMap.put("lesnNms", lesnNms);//레슨명
		paramMap.put("vodQuntyHhs", vodQuntyHhs);//영상분량시
		paramMap.put("vodQuntyMis", vodQuntyMis);//영상분량분
		paramMap.put("vodQuntySecs", vodQuntySecs);//영상분량초
		paramMap.put("vodComnts", vodComnts);//영상설명

		ModelAndView mav = new ModelAndView("jsonView");
		log.info("/insertDocInfo params : " + paramMap);
		documentService.insertDocInfo(paramMap);

		return mav;
	}

	/**
	 * 문서·영상 관리 상세 조회
	 *
	 * @param contUid
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/selectDocumentView")
	public ModelAndView selectDocumentView(@RequestParam int contUid) {
		log.info("/sems/contents/selectDocumentView linkUrl : " + contUid);

		ModelAndView mav = new ModelAndView("jsonView");

		Map<String, Object> item = documentService.selectContentView(contUid);
		if (item != null && item.get("contUid") != null) {
			List<Map<String, Object>> contApndList = documentService.selectApndContentList((int) item.get("contUid"));
			item.put("contApndList", contApndList);

			List<Map<String, Object>> contTpicList = documentService.selectContentTopicList((int) item.get("contUid"));
			item.put("contTpicList", contTpicList);
		}

		mav.addObject("item", item);

		log.info("return : " + mav);

		return mav;
	}

	/**
	 * 컨텐츠  > 문서 등록
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/updateDocInfo")
	public ModelAndView updateDocInfo(HttpServletRequest request
		, @RequestParam Map<String, Object> paramMap
		//파라미터
		, @RequestParam(required = false, name = "uploadFile") MultipartFile uploadFile
		, @RequestParam(required = false, name = "uploadPdfFile") MultipartFile uploadPdfFile
		//컨텐츠파일
		, @RequestParam(required = false, name = "uploadImage") MultipartFile uploadImage
		//대표이미지
		, @RequestParam(required = false, name = "rlsTgtCd") List<String> rlsTgtCds
		//일부 공개 대상
		, @RequestParam(required = false, name = "tpicUid") List<Integer> tpicUids
		//ESG토픽
		, @RequestParam(required = false, name = "uploadFiles") List<MultipartFile> uploadFiles
		//원포인트레슨 컨텐츠파일
		, @RequestParam(required = false, name = "uploadImages") List<MultipartFile> uploadImages
		//원포인트레슨 대표이미지
		, @RequestParam(required = false, name = "seqNos") List<String> seqNos
		, @RequestParam(required = false, name = "lesnNms") List<String> lesnNms
		//레슨명
		, @RequestParam(required = false, name = "vodQuntyHhs") List<String> vodQuntyHhs
		//영상분량시
		, @RequestParam(required = false, name = "vodQuntyMis") List<String> vodQuntyMis
		//영상분량분
		, @RequestParam(required = false, name = "vodQuntySecs") List<String> vodQuntySecs
		//영상분량초
		, @RequestParam(required = false, name = "vodComnts") List<String> vodComnts
		//영상설명
	) {
		HttpSession session = request.getSession();
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");
		paramMap.put("fstInsId", sessionData.getMbrId()); // 최초등록아이디
		paramMap.put("lstUpdId", sessionData.getMbrId()); // 최종수정아이디

		// 문서/영상
		if (rlsTgtCds != null) {
			paramMap.put("rlsTgtCds", String.join(",", rlsTgtCds));    // ESG토픽
		}
		if (paramMap.get("pirdStngUseYn") == null) {
			paramMap.put("pirdStngUseYn", "N");
		}
		paramMap.put("tpicUids", tpicUids); // ESG토픽
		paramMap.put("uploadFile", uploadFile);//컨텐츠파일
		paramMap.put("uploadPdfFile", uploadPdfFile);//컨텐츠파일
		paramMap.put("uploadImage", uploadImage);//대표이미지

		// 원포인트레슨
		paramMap.put("uploadFiles", uploadFiles);// 원포인트레슨 컨텐츠파일
		paramMap.put("uploadImages", uploadImages);// 원포인트레슨 대표이미지
		paramMap.put("seqNos", seqNos);
		paramMap.put("lesnNms", lesnNms);//레슨명
		paramMap.put("vodQuntyHhs", vodQuntyHhs);//영상분량시
		paramMap.put("vodQuntyMis", vodQuntyMis);//영상분량분
		paramMap.put("vodQuntySecs", vodQuntySecs);//영상분량초
		paramMap.put("vodComnts", vodComnts);//영상설명

		ModelAndView mav = new ModelAndView("jsonView");
		log.info("/updateDocInfo params : " + paramMap);
		documentService.updateDocInfo(paramMap);

		return mav;
	}

	/**
	 * 문서·영상 관리 삭제
	 *
	 * @param contUidList
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/sems/contents/deleteDocument")
	public ModelAndView deleteDocument(@RequestParam(name = "contUids[]") List<String> contUidList) {
		log.info("/getDate params : " + contUidList);
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("deleteCnt", documentService.deleteDocument(contUidList));
		return mav;
	}

}
