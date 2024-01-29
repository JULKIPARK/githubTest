package com.pwc.pwcesg.backoffice.contents.service;

import com.pwc.pwcesg.backoffice.contents.mapper.SrcMapper;
import com.pwc.pwcesg.common.mapper.CommonMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class SrcService {

	private final SrcMapper srcMapper;
	private final CommonMapper commonMapper;

	/**
	 * 출처정보 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectSrcList(Map<String, Object> paramMap) {
		return srcMapper.selectSrcList(paramMap);
	}

	/**
	 * 출처정보 상세 조회
	 *
	 * @param srcUid
	 * @return
	 */
	public Map<String, Object> selectSrcView(String srcUid) {
		return srcMapper.selectSrcView(srcUid);
	}

	/**
	 * 출처정보 목록 엑셀 다운로드
	 * @param response, paramMap
	 * @return String
	 */
	public void selectSrcListExcelDown(HttpServletResponse response, Map<String, Object> paramMap) {
		Workbook workbook = new XSSFWorkbook();
		try{
			Sheet sheet = workbook.createSheet("출처 목록");

			//숫자 포맷은 아래 numberCellStyle을 적용시킬 것이다다
			CellStyle numberCellStyle = workbook.createCellStyle();
			numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

			//파일명
			final String fileName = "출처목록";

			//헤더
			final String[] header = {
				"No"
				, "출처번호"
				, "출처명"
				, "국내/해외"
				, "컨텐츠 수 합계"
				, "문서 컨텐츠 수"
				, "영상 컨텐츠 수"
			};

			//필드
			final String[] field = {
				"{No}"
				, "srcUno"
				, "srcNm"
				, "dmstcYnNm"
				, "contTotCnt"
				, "contDocCnt"
				, "contMovCnt"
			};
			Row row = sheet.createRow(0);
			for (int i = 0; i < header.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(header[i]);
			}

			//바디
			List<Map<String, Object>> list = srcMapper.selectSrcList(paramMap);
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow(i + 1);  //헤더 이후로 데이터가 출력되어야하니 +1
				Map<String, Object> user = list.get(i);
				Iterator<String> itrKeys = user.keySet().iterator();
				Cell cell = null;
				int celCnt = 0;

				for (int i2 = 0; i2 < header.length; i2++) {
					log.info("[field]{}", field[i2]);
					cell = row.createCell(celCnt);
					if (field[i2].equals("{No}")) {
						cell.setCellValue((i+1)+"");
					} else {
						cell.setCellValue(user.get(field[i2])+"");
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
	 * 출처정보 수정
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public int insertSrcView(Map<String, Object> paramMap) {
		return srcMapper.insertSrcInfo(paramMap);
	}

	/**
	 * 출처정보 수정
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public int updateSrcView(Map<String, Object> paramMap) {
		return srcMapper.updateSrcInfo(paramMap);
	}
}
