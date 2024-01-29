package com.pwc.pwcesg.backoffice.popup.service;

import com.pwc.pwcesg.backoffice.contents.mapper.DocumentMapper;
import com.pwc.pwcesg.backoffice.handler.FileHandler;
import com.pwc.pwcesg.backoffice.popup.mapper.PopupMapper;
import com.pwc.pwcesg.common.mapper.CommonMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * 콘텐츠 관리 Service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PopupService {

	private final PopupMapper popupMapper;

	/**
	 * 콘텐츠 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap) {
		return popupMapper.selectContentList(paramMap);
	}




}
