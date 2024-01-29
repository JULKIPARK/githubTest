package com.pwc.pwcesg.backoffice.custom.service;

import com.pwc.pwcesg.backoffice.custom.mapper.MemberMapper;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
	
    private final MemberMapper memberMapper;

    /**
     * 회원정보 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectMemberList(Map<String, Object> paramMap) {
    	log.info("[MemberService]{}", paramMap);
        return memberMapper.selectMemberList(paramMap);
    }

    /**
     * 회원정보 목록 엑셀 다운로드
     * @param response, paramMap
     * @return String
     */
    public void selectMemberListExcelDown(HttpServletResponse response, Map<String, Object> paramMap) {
        Workbook workbook = new XSSFWorkbook();
    	try{
            Sheet sheet = workbook.createSheet("사용자 목록");

            //숫자 포맷은 아래 numberCellStyle을 적용시킬 것이다다
            CellStyle numberCellStyle = workbook.createCellStyle();
            numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

            //파일명
            final String fileName = "사용자목록";
            
            //헤더
            final String[] header = {
    				"No"
    				, "회원번호"
    				, "아이디"
    				, "회원명"
    				, "회원구분"
    				, "소속"
    				, "부서명"
    				, "직급"
    				, "소속이메일"
    				, "소속전화"
    				, "ESG관심영역"
    				, "개인정보 제3자 제공 동의"
    				, "뉴스레터"
    				, "상태"
					, "가입일"
					, "메모"
                };

			//필드
			final String[] field = {
				"{No}"
				, "mbrUno"
				, "mbrId"
				, "mbrNm"
				, "mbrGbNm"
				, "coprNm"
				, "deptNm"
				, "posNm"
				, "compMail"
				, "coprPhonNo"
				, "intrCdNmArry"
				, "mrktAgreYn"
				, "nsltAgreYn"
				, "mbrStNm"
				, "joinDt"
				, "memoDesc"
			};
            Row row = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header[i]);
            }

            //바디
        	List<Map<String, Object>> list = memberMapper.selectMemberList(paramMap);
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
			log.error("MemberService{}", e.getMessage());
        }finally {
        	try {
				workbook.close();
			} catch (IOException e) {
				log.error("MemberService{}", e.getMessage());
			}
        }
    }

	/**
	 * 회원정보 상세 조회
	 * @param mbrUid
	 * @return
	 */
	public Map<String, Object> selectMemberView(String mbrUid) {
		Map<String, Object> itemMap = memberMapper.selectMemberView(mbrUid);

		itemMap.put("self_check_hst_list", selectMemberSelfCheckHstList(mbrUid));
		itemMap.put("csrd_hst_list", selectMemberCsrdHstList(mbrUid));
		itemMap.put("ask_cnt_list", selectMemberAskCntList(mbrUid));

		return itemMap;
	}

	/**
	 * 회원정보 메모 등록
	 *
	 * @param paramMap
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public int insertMemberMemo(Map<String, Object> paramMap) {
		return memberMapper.insertMemberMemo(paramMap);
	}

	public List<Map<String, Object>> selectMemberSelfCheckHstList(String mbrUid) {
		log.info("[MemberService]{}", mbrUid);
		return memberMapper.selectMemberSelfCheckHstList(mbrUid);
	}

	public List<Map<String, Object>> selectMemberCsrdHstList(String mbrUid) {
		log.info("[MemberService]{}", mbrUid);
		return memberMapper.selectMemberCsrdHstList(mbrUid);
	}

	public List<Map<String, Object>> selectMemberAskCntList(String mbrUid) {
		log.info("[MemberService]{}", mbrUid);
		return memberMapper.selectMemberAskCntList(mbrUid);
	}

	public List<Map<String, Object>> selectMemberStateByOneMonth() {
		return memberMapper.selectMemberStateByOneMonth();
	}

	public Map<String, Object> selectMemberAvgStateByOneYear() {
		return memberMapper.selectMemberAvgStateByOneYear();
	}

	public List<Map<String, Object>> selectEsgTgtActResult() {
		return memberMapper.selectEsgTgtActResult();
	}



}
