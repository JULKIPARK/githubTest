package com.pwc.pwcesg.backoffice.contents.service;

import com.pwc.pwcesg.backoffice.contents.mapper.DocumentMapper;
import com.pwc.pwcesg.backoffice.handler.FileHandler;
import com.pwc.pwcesg.common.mapper.CommonMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 문서·영상 관리 Service
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentService {

	private final DocumentMapper documentMapper;
	private final CommonMapper commonMapper;
	private final FileHandler fileHandler;

	/**
	 * 콘텐츠정보 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap) {
		return documentMapper.selectContentList(paramMap);
	}

	/**
	 * 콘텐츠정보 목록 엑셀 다운로드
	 *
	 * @param response, paramMap
	 * @return String
	 */
	public void selectContentListExcelDown(HttpServletResponse response, Map<String, Object> paramMap) {
		Workbook workbook = new XSSFWorkbook();
		try {
			Sheet sheet = workbook.createSheet("컨텐츠 목록");

			//숫자 포맷은 아래 numberCellStyle을 적용시킬 것이다다
			CellStyle numberCellStyle = workbook.createCellStyle();
			numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

			//파일명
			final String fileName = "컨텐츠목록";

			//헤더
			final String[] header = {"No", "종류", "등록방식", "등록상태", "컨텐츠 번호", "컨텐츠명", "출처", "국내/해외", "언어", "유형분류", "유형세분류", "업무분류", "주제분류", "형태", "전시여부", "일부공개대상", "다운로드가능", "검색여부", "토픽분류", "등록일", "등록자", "수정일", "수정자"};

			//필드
			final String[] field = {"{No}", "contKindCdNm", "contRmthdCdNm", "contStCdNm", "contUno", "contNm", "srcNm", "dmstcYnNm", "splyLangCdNm", "contTpLclsfCdNm", "contTpMclsfCdNm", "taskClsfCdNm", "sbjtGbCdNm", "contSplyTpCdNm", "dpTpCdNm", "rlsTgtCdNmArry", "dwnldPsbltyYnNm", "srchPsbltyYnNm", "tpicNmArry", "fstInsDt", "fstInsMbrNm", "lstUpdDt", "lstUpdMbrNm"};
			Row row = sheet.createRow(0);
			for (int i = 0; i < header.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(header[i]);
			}

			//바디
			List<Map<String, Object>> list = documentMapper.selectContentList(paramMap);
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i + 1);  //헤더 이후로 데이터가 출력되어야하니 +1
				Map<String, Object> user = list.get(i);
				Iterator<String> itrKeys = user.keySet().iterator();
				Cell cell = null;
				int celCnt = 0;

				for (int i2 = 0; i2 < header.length; i2++) {
					cell = row.createCell(celCnt);
					if (field[i2].equals("{No}")) {
						cell.setCellValue((i + 1) + "");
					} else {
						cell.setCellValue(((user.get(field[i2]) == null ? "" : user.get(field[i2]))) + "");
					}
					celCnt++;
				}
			}

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
			//파일명은 URLEncoder로 감싸주는게 좋다!

			workbook.write(response.getOutputStream());
			workbook.close();
		} catch (IOException e) {
			log.error("SrcService{}", e.getMessage());
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("SrcService{}", e.getMessage());
			}
		}
	}

	/**
	 * 출처 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectSrcSearchPopList(Map<String, Object> paramMap) {
		return documentMapper.selectSrcSearchPopList(paramMap);
	}

	/**
	 * 컨텐츠  > 문서 등록
	 *
	 * @param paramMap
	 * @return
	 * @return ModelAndView
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int insertDocInfo(Map<String, Object> paramMap) {
		log.info("DocumentService.paramMap=>{}", paramMap);

		/* 콘텐츠 유형 판단 */
		String gbn = "docs";
		int contApndSeqNo = 1;
		if (paramMap.get("contKindCd").toString().equals("10")) {    // 문서
			if (paramMap.get("contRmthdCd").toString().equals("10")) {
				gbn = "docs";
				if (paramMap.get("contSplyTpCd").toString().equals("30")) {
					gbn = "epub";
				}
			} else if (paramMap.get("contRmthdCd").toString().equals("20")) {
				gbn = "url";
				paramMap.put("contSplyTpCd", "80");
			}
		} else if (paramMap.get("contKindCd").toString().equals("20")) {    // 영상
			if (paramMap.get("contRmthdCd").toString().equals("10")) {
				gbn = "movie";
			} else if (paramMap.get("contRmthdCd").toString().equals("30")) {
				gbn = "url";
				paramMap.put("contSplyTpCd", "95");
			}
		} else if (paramMap.get("contKindCd").toString().equals("30")) {    // 원포인트영상
			gbn = "one-point";
			contApndSeqNo = 0;
		}

		/* 메인 데이터 파일 처리 */
		int atacFileUid = 0;
		String linkUrl = (String)paramMap.get("linkUrl");
		MultipartFile uploadFile = (MultipartFile) paramMap.get("uploadFile");    // 컨텐츠업로드
		MultipartFile uploadPdfFile = (MultipartFile) paramMap.get("uploadPdfFile");    // 컨텐츠업로드
		MultipartFile uploadImage = (MultipartFile) paramMap.get("uploadImage");    // 대표이미지
		Map<String, Object> uploadFileMap = null;
		Map<String, Object> uploadPdfFileMap = null;
		Map<String, Object> uploadImageMap = null;

		log.info("gbn=>{}", gbn);
		log.info("uploadFile=>{}", uploadFile);
		log.info("uploadImage=>{}", uploadImage);

		// 첨부파일 처리
		if ((uploadFile != null && !uploadFile.isEmpty()) || uploadImage != null && !uploadImage.isEmpty()) {
			// 첨부파일 번호 채번
			atacFileUid = commonMapper.getIntAtacFileUid();

			// 컨텐츠 업로드파일 처리
			if (uploadFile != null && !uploadFile.isEmpty()) {
				// 컨텐츠 파일저장
				uploadFileMap = fileHandler.parseFileInfo(uploadFile, atacFileUid + "", gbn);
				if (uploadFileMap.get("linkUrl") != null && !uploadFileMap.get("linkUrl").equals("")) {
					linkUrl = (String) uploadFileMap.get("linkUrl");
				}

				// epub pdf파일 처리
				if (paramMap.get("contSplyTpCd").toString().equals("30")) {
					if (uploadPdfFile != null && !uploadPdfFile.isEmpty()) {
						uploadFileMap = fileHandler.parseFileInfo(uploadPdfFile, atacFileUid + "", "epub");
					} else {
						uploadFileMap = null;
					}
				}

				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				if (uploadFileMap != null) {
					uploadFileMap.put("atacFileUid", atacFileUid);//첨부파일채번
					uploadFileMap.put("fstInsId", paramMap.get("fstInsId")); //최초등록아이디
					uploadFileMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
					commonMapper.insertAtacDtlInfo(uploadFileMap);
				}
			}

			// 대표이미지 처리
			if (uploadImage != null && !uploadImage.isEmpty()) {
				// 대표이미지 파일저장
				uploadImageMap = fileHandler.parseFileInfo(uploadImage, atacFileUid + "", "thumbnail");

				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				uploadImageMap.put("atacFileUid", atacFileUid);//첨부파일채번
				uploadImageMap.put("fstInsId", paramMap.get("fstInsId")); //최초등록아이디
				uploadImageMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				commonMapper.insertAtacDtlInfo(uploadImageMap);
			}
		}

		// 콘텐츠정보(ST_CONT_INFO) 등록
		if (atacFileUid > 0) {
			paramMap.put("atacFileUid", atacFileUid);
		}
		paramMap.put("linkUrl", linkUrl);
		documentMapper.insertDocInfo(paramMap);

		// 콘텐츠부가정보(ST_CONT_APND_INFO) 등록
		List<Map<String, Object>> list = new ArrayList<>();
		paramMap.put("seqNo", contApndSeqNo);
		if (uploadFile != null && !uploadFile.isEmpty()) {
			paramMap.put("atacDtlUid", 1);
		}
		list.add(paramMap);
		documentMapper.insertContApndInfo(list);

		// 콘텐츠토픽맵핑정보(DP_CONT_TPIC_INFO) 등록
		if (paramMap.get("tpicUids") != null && !paramMap.get("tpicUids").equals("")) {
			List<Integer> tpicUids = (List<Integer>) paramMap.get("tpicUids");
			for (Integer tpicUid : tpicUids) {
				paramMap.replace("tpicUid", tpicUid);
				documentMapper.insertContTpicInfo(paramMap);
			}
		}

		/* 원포인트레슨 데이터 파일 처리 */
		if (gbn.equals("one-point")) {
			// 첨부파일 처리
			List<MultipartFile> uploadFiles = (List<MultipartFile>) paramMap.get("uploadFiles");    // 컨텐츠업로드
			List<MultipartFile> uploadImages = (List<MultipartFile>) paramMap.get("uploadImages");    // 대표이미지

			String gbn2 = "movie";
			for (int i = 0; i < uploadFiles.size(); i++) {
				// 첨부파일 번호 채번
				atacFileUid = commonMapper.getIntAtacFileUid();

				// 컨텐츠 업로드파일 처리
				MultipartFile uploadFilesItem = uploadFiles.get(i);
				// 컨텐츠 파일저장
				uploadFileMap = fileHandler.parseFileInfo(uploadFilesItem, atacFileUid + "", gbn);
				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				uploadFileMap.put("atacFileUid", atacFileUid);//첨부파일채번
				uploadFileMap.put("fstInsId", paramMap.get("fstInsId")); //최초등록아이디
				uploadFileMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				commonMapper.insertAtacDtlInfo(uploadFileMap);
				if (uploadFileMap.get("linkUrl") != null && !uploadFileMap.get("linkUrl").equals("")) {
					linkUrl = (String) uploadFileMap.get("linkUrl");
				}

				// 대표이미지 처리
				MultipartFile uploadImagesItem = uploadImages.get(i);
				// 대표이미지 파일저장
				uploadImageMap = fileHandler.parseFileInfo(uploadImagesItem, atacFileUid + "", "thumbnail");
				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				uploadImageMap.put("atacFileUid", atacFileUid);//첨부파일채번
				uploadImageMap.put("fstInsId", paramMap.get("fstInsId")); //최초등록아이디
				uploadImageMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				commonMapper.insertAtacDtlInfo(uploadImageMap);

				// 콘텐츠부가정보(ST_CONT_APND_INFO) 등록
				List<Map<String, Object>> list2 = new ArrayList<>();
				paramMap.put("atacFileUid", atacFileUid);//첨부파일채번
				paramMap.put("atacDtlUid", 1);
				paramMap.put("seqNo", ++contApndSeqNo);
				paramMap.put("linkUrl", linkUrl);
				paramMap.put("contSplyTpCd", "20");
				paramMap.put("lesnNm", ((List<String>) paramMap.get("lesnNms")).get(i));//레슨명
				paramMap.put("vodQuntyHh", ((List<String>) paramMap.get("vodQuntyHhs")).get(i));//영상분량시
				paramMap.put("vodQuntyMi", ((List<String>) paramMap.get("vodQuntyMis")).get(i));//영상분량분
				paramMap.put("vodQuntySec", ((List<String>) paramMap.get("vodQuntySecs")).get(i));//영상분량초
				paramMap.put("vodComnt", ((List<String>) paramMap.get("vodComnts")).get(i));//영상설명
				list2.add(paramMap);
				documentMapper.insertContApndInfo(list2);
			}
		}

		return 1;
	}

	/**
	 * 문서·영상 관리 상세 조회
	 *
	 * @param contUid
	 * @return
	 */
	public Map<String, Object> selectContentView(int contUid) {
		return documentMapper.selectContentView(contUid);
	}

	/**
	 * 문서·영상 관리 추가 정보 목록 조회
	 *
	 * @param contUid
	 * @return
	 */
	public List<Map<String, Object>> selectApndContentList(int contUid) {
		return documentMapper.selectApndContentList(contUid);
	}

	/**
	 * 뉴스 관리 상세 토픽 목록 조회
	 *
	 * @param contUid
	 * @return
	 */
	public List<Map<String, Object>> selectContentTopicList(int contUid) {
		return documentMapper.selectContentTopicList(contUid);
	}

	/**
	 * 컨텐츠  > 문서 수정
	 *
	 * @param paramMap
	 * @return
	 * @return ModelAndView
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int updateDocInfo(Map<String, Object> paramMap) {
		log.info("DocumentService.paramMap=>{}", paramMap);

		// 원본데이터 조회
		Map<String, Object> originMap = documentMapper.selectContentView(Integer.parseInt((String) paramMap.get("contUid")));
		List<Map<String, Object>> contApndlist = documentMapper.selectApndContentList(Integer.parseInt((String) paramMap.get("contUid")));

		/* 콘텐츠 유형 판단 */
		String gbn = "docs";
		int contApndSeqNo = 1;
		if (paramMap.get("contKindCd").toString().equals("10")) {    // 문서
			if (paramMap.get("contRmthdCd").toString().equals("10")) {
				gbn = "docs";
				if (paramMap.get("contSplyTpCd").toString().equals("30")) {
					gbn = "epub";
				}
			} else if (paramMap.get("contRmthdCd").toString().equals("20")) {
				gbn = "url";
				paramMap.put("contSplyTpCd", "80");
			}
		} else if (paramMap.get("contKindCd").toString().equals("20")) {    // 영상
			if (paramMap.get("contRmthdCd").toString().equals("10")) {
				gbn = "movie";
			} else if (paramMap.get("contRmthdCd").toString().equals("30")) {
				gbn = "url";
				paramMap.put("contSplyTpCd", "95");
			}
		} else if (paramMap.get("contKindCd").toString().equals("30")) {    // 원포인트영상
			gbn = "one-point";
			contApndSeqNo = 0;
		}
		log.info("gbn=>{}", gbn);

		/* 메인 데이터 파일 처리 */
		int atacFileUid = 0;
		if (originMap.get("atacFileUid") != null) {
			atacFileUid = (Integer)originMap.get("atacFileUid");
		}
		String linkUrl = (String) paramMap.get("linkUrl");
		if (linkUrl == null || linkUrl.equals("")) {
			linkUrl = (String)originMap.get("linkUrl");
		}
		MultipartFile uploadFile = (MultipartFile) paramMap.get("uploadFile");    // 컨텐츠업로드
		MultipartFile uploadPdfFile = (MultipartFile) paramMap.get("uploadPdfFile");    // 컨텐츠업로드
		MultipartFile uploadImage = (MultipartFile) paramMap.get("uploadImage");    // 대표이미지
		Map<String, Object> uploadFileMap = null;
		Map<String, Object> uploadPdfFileMap = null;
		Map<String, Object> uploadImageMap = null;
		log.info("uploadFile=>{}", uploadFile);
		log.info("uploadImage=>{}", uploadImage);

		// 첨부파일 처리
		if ((uploadFile != null && !uploadFile.isEmpty()) || uploadImage != null && !uploadImage.isEmpty()) {
			// 컨텐츠 업로드파일 처리
			if (uploadFile != null && !uploadFile.isEmpty()) {
				// 컨텐츠 파일저장
				uploadFileMap = fileHandler.parseFileInfo(uploadFile, atacFileUid + "", gbn);

				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				uploadFileMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				if (!paramMap.get("contSplyTpCd").toString().equals("30")) {
					commonMapper.upsertAtacDtlInfo(uploadFileMap);
				}
				if (uploadFileMap.get("linkUrl") != null && !uploadFileMap.get("linkUrl").equals("")) {
					linkUrl = (String) uploadFileMap.get("linkUrl");
				}
			}

			// 대표이미지 처리
			if (uploadImage != null && !uploadImage.isEmpty()) {
				// 대표이미지 파일저장
				uploadImageMap = fileHandler.parseFileInfo(uploadImage, atacFileUid + "", "thumbnail");

				// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
				uploadImageMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				commonMapper.upsertAtacDtlInfo(uploadImageMap);
			}
		}

		// epub pdf파일 처리
		if (paramMap.get("contSplyTpCd") != null && paramMap.get("contSplyTpCd").toString().equals("30")) {
			if (uploadPdfFile != null && !uploadPdfFile.isEmpty()) {
				uploadFileMap = fileHandler.parseFileInfo(uploadPdfFile, atacFileUid + "", gbn);
				uploadFileMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
				commonMapper.upsertAtacDtlInfo(uploadFileMap);
			}
		}

		// 콘텐츠정보(ST_CONT_INFO) 수정
		if (atacFileUid > 0) {
			paramMap.put("atacFileUid", atacFileUid);
		}
		paramMap.put("linkUrl", linkUrl);
		documentMapper.updateDocInfo(paramMap);

		// 콘텐츠부가정보(ST_CONT_APND_INFO) 등록
		List<Map<String, Object>> list = new ArrayList<>();
		paramMap.put("seqNo", contApndSeqNo);
		if (uploadFile != null && !uploadFile.isEmpty()) {
			paramMap.put("atacDtlUid", 1);
		}
		list.add(paramMap);
		for (Map<String, Object> item : list) {
			documentMapper.updateContApndInfo(item);
		}

		// 콘텐츠토픽맵핑정보(DP_CONT_TPIC_INFO) 등록
		if (paramMap.get("tpicUids") != null && !paramMap.get("tpicUids").equals("")) {
			// 기존 데이터 삭제
			documentMapper.deleteContTpicInfo(Integer.parseInt((String) paramMap.get("contUid")));

			List<Integer> tpicUids = (List<Integer>) paramMap.get("tpicUids");
			for (Integer tpicUid : tpicUids) {
				paramMap.replace("tpicUid", tpicUid);

				documentMapper.insertContTpicInfo(paramMap);
			}
		}

		/* 원포인트레슨 데이터 파일 처리 */
		if (gbn.equals("one-point")) {
			List<MultipartFile> uploadFiles = (List<MultipartFile>) paramMap.get("uploadFiles");    // 컨텐츠업로드
			List<MultipartFile> uploadImages = (List<MultipartFile>) paramMap.get("uploadImages");    // 대표이미지
			int maxSeqNo = 0;
			String mode = "";

			// 삭제건 먼저 처리
			for (int i = 0; i < contApndlist.size(); i++) {
				Map<String, Object> tblItem = contApndlist.get(i);
				String tblSeqNo = tblItem.get("seqNo").toString();
				if (!tblSeqNo.equals("0")) {
//					log.info("i = {} / tblSeqNo = {}", i, tblSeqNo);
					boolean isFind = false;
					for (int i2 = 0; i2 < uploadFiles.size(); i2++) {
						String inputSeqNo = ((List<String>) paramMap.get("seqNos")).get(i2);
//						log.info("i2 = {} / inputSeqNo = {}", i2, inputSeqNo);
						if (tblSeqNo.equals(inputSeqNo)) {
							isFind = true;
						}
					}
//					log.info("isFind = {}", isFind);
					if (!isFind) {
						log.info("데이터 삭제 !!!! {}", tblSeqNo);
						documentMapper.deleteStContApndInfoOne(tblItem);
					}
				}
				maxSeqNo = Integer.parseInt(tblSeqNo);
			}

			// 갱신/추가 처리
			for (int i = 0; i < uploadFiles.size(); i++) {

				String inputSeqNo = ((List<String>) paramMap.get("seqNos")).get(i);
				log.info("i = {} / inputSeqNo = {}", i, inputSeqNo);
				Map<String, Object> paramItem = new HashMap<>();
				if (!inputSeqNo.equals("")) {
					log.info("UPDATE inputSeqNo = {}", inputSeqNo);
					mode = "U";
					// 원 data 구하기
					for (int i2 = 0; i2 < contApndlist.size(); i2++) {
						Map<String, Object> tblItem = contApndlist.get(i2);
						String tblSeqNo = tblItem.get("seqNo").toString();
						if (tblSeqNo.equals(inputSeqNo)) {
							// 원 data의 atacFileUid 값 얻기
							paramItem.put("contUid", paramMap.get("contUid"));
							paramItem.put("seqNo", tblSeqNo);
							paramItem.put("linkUrl", tblItem.get("linkUrl"));
							paramItem.put("contSplyTpCd", "20");
							paramItem.put("vodQuntyHh", ((List<String>) paramMap.get("vodQuntyHhs")).get(i));
							paramItem.put("vodQuntyMi", ((List<String>) paramMap.get("vodQuntyMis")).get(i));
							paramItem.put("vodQuntySec", ((List<String>) paramMap.get("vodQuntySecs")).get(i));
							paramItem.put("lesnNm", ((List<String>) paramMap.get("lesnNms")).get(i));
							paramItem.put("vodComnt", ((List<String>) paramMap.get("vodComnts")).get(i));
							paramItem.put("lstUpdId", paramMap.get("lstUpdId"));

							atacFileUid = Integer.parseInt(tblItem.get("atacFileUid").toString());
						}
					}
				} else {
					log.info("INSERT inputSeqNo");
					mode = "I";
					// 새로운 atacFileUid 값 얻기
					atacFileUid = commonMapper.getIntAtacFileUid();

					paramItem.put("contUid", paramMap.get("contUid"));
					paramItem.put("contKindCd", paramMap.get("contKindCd"));
					paramItem.put("seqNo", ++maxSeqNo);
					paramItem.put("contRmthdCd", paramMap.get("contRmthdCd"));
					paramItem.put("contSplyTpCd", "20");
					paramItem.put("vodQuntyHh", ((List<String>) paramMap.get("vodQuntyHhs")).get(i));
					paramItem.put("vodQuntyMi", ((List<String>) paramMap.get("vodQuntyMis")).get(i));
					paramItem.put("vodQuntySec", ((List<String>) paramMap.get("vodQuntySecs")).get(i));
					paramItem.put("lesnNm", ((List<String>) paramMap.get("lesnNms")).get(i));
					paramItem.put("vodComnt", ((List<String>) paramMap.get("vodComnts")).get(i));
					paramItem.put("atacFileUid", atacFileUid);
					paramItem.put("fstInsId", paramMap.get("fstInsId"));
					paramItem.put("lstUpdId", paramMap.get("lstUpdId"));

				}
				log.info("atacFileUid = {}", atacFileUid);
				log.info("paramItem = {}", paramItem);

				// 업로드되는 컨텐츠가 있을 경우 ATAC_DTL_INFO 테이블에서 atacTpCd가 20인 데이터 upsert
				uploadFile = (MultipartFile) uploadFiles.get(i);
				if (uploadFile != null && !uploadFile.isEmpty()) {
					log.info("Upload File 처리");
					// 컨텐츠 파일저장
					uploadFileMap = fileHandler.parseFileInfo(uploadFile, atacFileUid + "", "movie");

					// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
					uploadFileMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
					commonMapper.upsertAtacDtlInfo(uploadFileMap);
					if (uploadFileMap.get("linkUrl") != null && !uploadFileMap.get("linkUrl").equals("")) {
						paramItem.put("linkUrl", (String) uploadFileMap.get("linkUrl"));
					}
					paramItem.put("atacDtlUid", 1);
				}

				// 대표이미지 처리
				uploadImage = (MultipartFile) uploadImages.get(i);
				if (uploadImage != null && !uploadImage.isEmpty()) {
					log.info("Upload Image 처리");
					// 대표이미지 파일저장
					uploadImageMap = fileHandler.parseFileInfo(uploadImage, atacFileUid + "", "thumbnail");

					// 컨텐츠 첨부상세정보(CM_ATAC_DTL_INFO) 등록
					uploadImageMap.put("lstUpdId", paramMap.get("lstUpdId")); //최종수정아이디
					commonMapper.upsertAtacDtlInfo(uploadImageMap);
				}

				// 콘텐츠부가정보(ST_CONT_APND_INFO) 처리
				if (mode.equals("I")) {
					List<Map<String, Object>> list2 = new ArrayList<>();
					list2.add(paramItem);
					documentMapper.insertContApndInfo(list2);
				} else if (mode.equals("U")) {
					documentMapper.updateContApndInfo(paramItem);
				}
			}
		}

		return 1;
	}

	/**
	 * 문서·영상 관리 삭제
	 *
	 * @param contUidList
	 * @return ModelAndView
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int deleteDocument(List<String> contUidList) {
		log.info("/getDate params : " + contUidList);
		for (String contUid : contUidList) {
			log.info("[콘텐츠채번]{}", contUid);
		}
		//컨텐츠 파일조회
		List<Map<String, Object>> deleteFileList = documentMapper.selectAtacFileList(contUidList);

		//콘텐츠정보(ST_CONT_INFO) 삭제
		documentMapper.deleteStContInfo(contUidList);

		//콘텐츠부가정보(ST_CONT_APND_INFO) 삭제
		documentMapper.deleteStContApndInfo(contUidList);

		//콘텐츠토픽정보(DP_CONT_TPIC_INFO) 삭제
		documentMapper.deleteDpContTpicInfo(contUidList);

		if (deleteFileList != null && deleteFileList.size() > 0) {
			//첨부상세정보(CM_ATAC_DTL_INFO) 삭제
			documentMapper.deleteCmAtacDtlInfo(deleteFileList);

			//첨부파일삭제
			for (Map<String, Object> deleteFile : deleteFileList) {
				fileHandler.deleteFile((String) deleteFile.get("cnvnFileFpath"));
			}
		}

		return contUidList.size();
	}


	/**
	 * 문서·영상 관리 수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	public int updateDocumentView(Map<String, Object> paramMap) {
		return documentMapper.updateDocumentView(paramMap);
	}

}
