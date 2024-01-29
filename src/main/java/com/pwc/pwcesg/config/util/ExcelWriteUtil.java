package com.pwc.pwcesg.config.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelWriteUtil {

    private XSSFWorkbook wb = null;
    private XSSFSheet sh = null;
    private XSSFCellStyle dataStyle = null;
    private XSSFCellStyle headerStyle = null;
    private ArrayList<String> headerCodeList = null;

    /**
     * 엑셀 파일 다운로드
     * @param request - 사용자 요청 객체
     * @param response - 서버 응답 객체
     * @param param - 엑셀 파일 정보
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String excelDownload(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param) throws IOException {
        ServletOutputStream out = null;
        String fileName = String.valueOf(param.get("fileName"));
        String result = "success";

        try {
            wb = new XSSFWorkbook();

            Font font = wb.createFont();
            headerStyle = wb.createCellStyle();
            dataStyle = wb.createCellStyle();

            //헤더 스타일
            font.setColor(IndexedColors.WHITE.getIndex());

            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);

            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFont(font);

            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            //데이터 스타일
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            List<Map<String, String>> dataList = (List<Map<String, String>>) param.get("dataList");
            Map<String, Object> headerInfo = (Map<String, Object>) param.get("headerList");

            //log.info("sheetName :: " + (String) param.get("sheetName") + " dataList :: " + dataList.toString() + " headerList :: " + headerInfo.toString());
            /*
             * 시트 생성
             */
            sh = wb.createSheet((String) param.get("sheetName"));

            // 가로 세로
            Row row = null;
            Cell cell = null;

            if(dataList.size() > 0){

                // 엑셀 헤더 생성
                row = sh.createRow(0);

                headerCodeList = new ArrayList<String>();

                for(String headerCode : headerInfo.keySet()) {
                    headerCodeList.add(headerCode);
                }

                for (int i = 0, iMax = headerCodeList.size(); i < iMax; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue((double) headerInfo.get(headerCodeList.get(i)));
                }

                // 엑셀 내용 생성
                for (int r = 0, rMax = dataList.size(); r < rMax; r++) {
                    Map<String, String> dataMap = dataList.get(r);
                    row = sh.createRow(r+1);

                    for (int i = 0, iMax = headerCodeList.size(); i < iMax; i++) {
                        cell = row.createCell(i);
                        cell.setCellStyle(dataStyle);
                        cell.setCellValue(StringUtil.nvl(String.valueOf(dataMap.get(headerCodeList.get(i)))));
                    }
                }

                // 셀 너비 조절
                for(int i=0; i < dataList.get(0).size(); i++){
                    sh.autoSizeColumn(i);
                    sh.setColumnWidth(i, (sh.getColumnWidth(i))+(short)1024);
                }
            }

            // 응답 헤더 설정
            response.setContentType("Application/Msexcel");
            setDisposition(fileName, request, response);

            out = response.getOutputStream();

            // 엑셀 파일 작성
            wb.write(out);
            out.flush();
        } catch(IOException e) {
            log.error("엑셀 다운로드 오류 발생");
            result = "error";
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (wb != null) {
                wb.close();
            }
        }

        return result;
    }

    /**
     * Disposition 지정
     * @param filename - 다운로드 파일명
     * @param request - 사용자 요청 객체
     * @param response - 서버 응답 객체
     * @throws IOException
     * @throws Exception
     */
    private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String browser = getBrowser(request);
        String dispositionPrefix = "attachment; filename=";
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new IOException("Not supported browser");
        }

        response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

        if ("Opera".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
    }

    /**
     * 브라우저 구분 조회
     * @param request - 사용자 요청 객체
     * @return 브라우저 구분
     */
    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }
}