package com.pwc.pwcesg.backoffice.contents.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.contents.mapper.NewsMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 뉴스 관리 Service
 * @author N.J.Kim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NewsService {
	
    private final NewsMapper newsMapper;

    /**
     * 뉴스 관리 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectNewsList(Map<String, Object> paramMap) {
        return newsMapper.selectNewsList(paramMap);
    }

    /**
     * 뉴스 목록 엑셀 다운로드
     * @param response, paramMap
     * @return String
     */
    public void selectNewsListExcelDown(HttpServletResponse response, Map<String, Object> paramMap) {
        Workbook workbook = new XSSFWorkbook();
        try{
            Sheet sheet = workbook.createSheet("뉴스 목록");

            //숫자 포맷은 아래 numberCellStyle을 적용시킬 것이다다
            CellStyle numberCellStyle = workbook.createCellStyle();
            numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

            //파일명
            final String fileName = "뉴스목록";

            //헤더
            final String[] header = {
                "No"
                , "종류"
                , "등록상태"
                , "컨텐츠 번호"
                , "뉴스제목"
                , "뉴스 발간일자"
                , "출처"
                , "작성자"
                , "업무분류"
                , "주제분류"
                , "전시여부"
                , "일부공개대상"
                , "검색여부"
                , "토픽분류"
                , "등록일"
                , "등록자"
                , "수정일"
                , "수정자"
            };

            //필드
            final String[] field = {
                "{No}"
                , "contKindCdNm"
                , "contStCdNm"
                , "contUno"
                , "contNm"
                , "pblcnDt"
                , "srcNm"
                , "wrtrNm"
                , "taskClsfCdNm"
                , "sbjtGbCdNm"
                , "dpTpCdNm"
                , "rlsTgtCdNmArry"
                , "srchPsbltyYnNm"
                , "tpicNmArry"
                , "fstInsDt"
                , "fstInsMbrNm"
                , "lstUpdDt"
                , "lstUpdMbrNm"
            };
            Row row = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header[i]);
            }

            //바디
            List<Map<String, Object>> list = newsMapper.selectNewsList(paramMap);
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
     * 뉴스 상태 변경
     *
     * @param contUidList
     * @return ModelAndView
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
    public int updateContSt(List<String> contUidList) {
        log.info("/updateContSt params : " + contUidList);
        for (String contUid : contUidList) {
            log.info("[콘텐츠채번]{}", contUid);
        }

        //콘텐츠정보(ST_CONT_INFO) 삭제
        newsMapper.updateContSt(contUidList);

        return contUidList.size();
    }

    /**
     * 뉴스 관리 상세 조회
     * @param contUid
     * @return
     */
    public Map<String, Object> selectNewsView(int contUid) {
        return newsMapper.selectNewsView(contUid);
    }

    /**
     * 뉴스 관리 상세 토픽 목록 조회
     *
     * @param contUid
     * @return
     */
    public List<Map<String, Object>> selectNewsTopicList(int contUid) {
        return newsMapper.selectNewsTopicList(contUid);
    }

    /**
     * 컨텐츠  > 문서 등록
     *
     * @param paramMap
     * @return
     * @return ModelAndView
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
    public int updateNewsInfo(Map<String, Object> paramMap) {
        Map<String, Object> originMap = newsMapper.selectNewsView(Integer.parseInt((String)paramMap.get("contUid")));

        log.info("DocumentService.paramMap=>{}", paramMap);

        newsMapper.updateNewsInfo(paramMap);

        // 콘텐츠토픽맵핑정보(DP_CONT_TPIC_INFO) 등록
        if (paramMap.get("tpicUids") != null && !paramMap.get("tpicUids").equals("")) {
            // 기존 데이터 삭제
            newsMapper.deleteContTpicInfo(Integer.parseInt((String)paramMap.get("contUid")));

            List<Integer> tpicUids = (List<Integer>)paramMap.get("tpicUids");
            for (Integer tpicUid : tpicUids) {
                paramMap.replace("tpicUid", tpicUid);

                newsMapper.insertContTpicInfo(paramMap);
            }
        }

        return 1;
    }
}
