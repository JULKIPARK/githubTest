package com.pwc.pwcesg.backoffice.management.service;

import com.pwc.pwcesg.backoffice.handler.FileHandler;
import com.pwc.pwcesg.config.SessionData;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.management.mapper.CurationMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 큐레이션 관리 Service
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CurationService {

	private final CurationMapper curationMapper;
	private final FileHandler fileHandler;

	/**
	 * 큐레이션 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicList(Map<String, Object> paramMap) {
		return curationMapper.selectTpicList(paramMap);
	}

	/**
	 * 큐레이션 토픽 정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectTpicInfo(Map<String, Object> paramMap) {
		Map<String, Object> rtnMap = curationMapper.selectTpicInfo(paramMap);
		rtnMap.put("apndList", curationMapper.selectTpicApndList(paramMap));
		return rtnMap;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContTpicList(Map<String, Object> paramMap) {
		return curationMapper.selectContTpicList(paramMap);
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 엑셀 다운로드
	 * @param response, paramMap
	 * @return String
	 */
	public void selectContTpicListExcelDown(HttpServletResponse response, Map<String, Object> paramMap) {
		Workbook workbook = new XSSFWorkbook();
		try{
			Sheet sheet = workbook.createSheet("컨텐츠 목록");

			CellStyle numberCellStyle = workbook.createCellStyle();
			numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

			//파일명
			final String fileName = "큐레이션컨텐츠목록";

			//헤더
			final String[] header = {
				"No"
				, "전시여부"
				, "정렬방식"
				, "전시순서"
				, "종류"
				, "컨텐츠 번호"
				, "컨텐츠명"
				, "출처"
				, "언어"
				, "유형분류"
				, "유형세분류"
				, "업무분류"
				, "주제분류"
				, "등록방식"
				, "형태"
				, "등록일"
				, "수정일"
			};

			//필드
			final String[] field = {
				"{No}"
				, "dpYn"
				, "dpFixYnNm"
				, "dpSeq"
				, "contKindCdNm"
				, "contUno"
				, "contNm"
				, "srcNm"
				, "splyLangCdNm"
				, "contTpLclsfCdNm"
				, "contTpMclsfCdNm"
				, "taskClsfCdNm"
				, "sbjtGbCdNm"
				, "contRmthdCdNm"
				, "contSplyTpCdNm"
				, "fstInsDt"
				, "lstUpdDt"
			};
			Row row = sheet.createRow(0);
			for (int i = 0; i < header.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(header[i]);
			}

			//바디
			List<Map<String, Object>> list = curationMapper.selectContTpicList(paramMap);
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i + 1);  //헤더 이후로 데이터가 출력되어야하니 +1
				Map<String, Object> user = list.get(i);
				Iterator<String> itrKeys = user.keySet().iterator();
				Cell cell = null;
				int celCnt = 0;

				for (int i2 = 0; i2 < header.length; i2++) {
					cell = row.createCell(celCnt);
					if (field[i2].equals("{No}")) {
						cell.setCellValue((i+1)+"");
					} else {
						cell.setCellValue(((user.get(field[i2]) == null ? "" : user.get(field[i2])))+"");
					}
					celCnt++;
				}
			}


			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8")+".xlsx");
			//파일명은 URLEncoder로 감싸주는게 좋다!

			workbook.write(response.getOutputStream());
			workbook.close();
		}catch(IOException e){
			log.error("SrcService{}", e.getMessage());
		}finally {
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("SrcService{}", e.getMessage());
			}
		}
	}

	/**
	 * 큐레이션 토픽 전문가 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicSpecialistList(Map<String, Object> paramMap) {
		return curationMapper.selectTpicSpecialistList(paramMap);
	}
	public List<Map<String, Object>> selectTpicSpecialistAll(Map<String, Object> paramMap) {
		return curationMapper.selectTpicSpecialistAll(paramMap);
	}

	/**
	 * 큐레이션 토픽 목록 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int upsertTpicList(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicGbCd = paramMap.get("tpicGbCd").toString();
		String tpicMnuCd = paramMap.get("tpicMnuCd").toString();
		String[] tpicUids = paramMap.get("tpicUids").toString().split(",", -1);
		String[] dpYns = paramMap.get("dpYns").toString().split(",", -1);
		String[] dpSeqs = paramMap.get("dpSeqs").toString().split(",", -1);
		String[] tpicNms = paramMap.get("tpicNms").toString().split(",", -1);
		for (int i = 0 ; i < tpicUids.length; i++) {
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("fstInsId", paramMap.get("fstInsId"));
			paramMap2.put("lstUpdId", paramMap.get("lstUpdId"));
			paramMap2.put("tpicUid", tpicUids[i]);
			paramMap2.put("tpicNm", tpicNms[i]);
			paramMap2.put("tpicGbCd", tpicGbCd);
			paramMap2.put("tpicMnuCd", tpicMnuCd);
			paramMap2.put("dpYn", dpYns[i]);
			paramMap2.put("dpSeq", (dpSeqs[i].equals("")) ? 999 : Integer.parseInt(dpSeqs[i]));

			if (tpicUids[i].equals("0") && !tpicNms[i].equals("")) {
				rtvVal += curationMapper.insertTpicInfo(paramMap2);
			} else {
				rtvVal += curationMapper.updateTpicInfo(paramMap2);
			}
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 토픽 정보 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int updateTpicList(Map<String, Object> paramMap) {
		return curationMapper.updateTpicInfo(paramMap);
	}

	/**
	 * 큐레이션 토픽 추가 정보 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int upsertTpicApndInfo(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicTpCd = (String)paramMap.get("tpicTpCd");
		String seqNo = (String)paramMap.get("seqNo");

		if (seqNo.equals("")) {
			if (tpicTpCd.equals("10") || tpicTpCd.equals("50")) {
				paramMap.put("seqNo", "1");
				paramMap.put("subSeqNo", "0");
			}
			rtvVal += curationMapper.insertTpicApndInfo(paramMap);
		} else {
			rtvVal += curationMapper.updateTpicApndInfo(paramMap);
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 추가
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int addContTpicList(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicUid = paramMap.get("tpicUid").toString();

		for (String contUid : (List<String>)paramMap.get("contUidList")) {
			log.info("[콘텐츠채번]{}", contUid);
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("tpicUid", tpicUid);
			paramMap2.put("contUid", contUid);
			paramMap2.put("fstInsId", paramMap.get("fstInsId").toString());
			paramMap2.put("lstUpdId", paramMap.get("lstUpdId").toString());

			rtvVal += curationMapper.insertContTpicInfo(paramMap2);
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 콘텐츠 토픽 목록 삭제
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int deleteContTpicList(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicUid = paramMap.get("tpicUid").toString();
		String[] contUids = paramMap.get("contUids").toString().split(",", -1);
		for (int i = 0 ; i < contUids.length; i++) {
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("tpicUid", tpicUid);
			paramMap2.put("contUid", contUids[i]);

			rtvVal += curationMapper.deleteContTpicInfo(paramMap2);
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 컨텐츠 토픽 목록 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int upsertContTpicList(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicUid = paramMap.get("tpicUid").toString();
		String[] actions = paramMap.get("actions").toString().split(",", -1);
		String[] contUids = paramMap.get("contUids").toString().split(",", -1);
		String[] dpYns = paramMap.get("dpYns").toString().split(",", -1);
		String[] dpFixYns = paramMap.get("dpFixYns").toString().split(",", -1);
		String[] dpSeqs = paramMap.get("dpSeqs").toString().split(",", -1);
		for (int i = 0 ; i < contUids.length; i++) {
			Map<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("fstInsId", paramMap.get("fstInsId"));
			paramMap2.put("lstUpdId", paramMap.get("lstUpdId"));
			paramMap2.put("tpicUid", tpicUid);
			paramMap2.put("contUid", contUids[i]);
			paramMap2.put("dpYn", dpYns[i]);
			paramMap2.put("dpFixYn", dpFixYns[i]);
			paramMap2.put("dpSeq", (dpSeqs[i].equals("")) ? 999 : Integer.parseInt(dpSeqs[i]));

			if (actions[i].equals("U")) {
				rtvVal += curationMapper.updateContTpicInfo(paramMap2);
			} else {
//				rtvVal += curationMapper.insertTpicInfo(paramMap2);
			}
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 토픽 전문가 목록 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int upsertTpicSpecialistList(Map<String, Object> paramMap) {
		int rtvVal = 0;

		String tpicUid = paramMap.get("tpicUid").toString();

		// 기존 데이터 삭제
		curationMapper.deleteTpicSpecialistInfo(paramMap);

		// Insert
		List<String> spcalUids = (List<String>) paramMap.get("spcalUids");
		for (int i = 0 ; i < spcalUids.size(); i++) {
			paramMap.put("spcalUid", spcalUids.get(i));
			rtvVal += curationMapper.insertTpicSpecialistInfo(paramMap);
		}

		return rtvVal;
	}

	/**
	 * 큐레이션 토픽 전문가 정보 저장
	 *
	 * @param paramMap
	 * @return int
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int saveSpecialistInfo(Map<String, Object> paramMap) {
		log.info("DocumentService.paramMap=>{}", paramMap);

		MultipartFile uploadImage = (MultipartFile) paramMap.get("uploadImage");    // 대표이미지
		Map<String, Object> uploadImageMap = null;

		// 대표이미지 처리
		if (uploadImage != null && !uploadImage.isEmpty()) {
			// 대표이미지 파일저장
			uploadImageMap = fileHandler.parseFileInfo2(uploadImage, "", "spcal_photo");
			paramMap.put("spcalPhoto", uploadImageMap.get("linkUrl"));
		}

		if (paramMap.get("spcalUid") != null && !paramMap.get("spcalUid").equals("")) {
			// 수정
			curationMapper.updateSpecialistInfo(paramMap);
		} else {
			// 추가
			curationMapper.insertSpecialistInfo(paramMap);
		}

		return 1;
	}

}