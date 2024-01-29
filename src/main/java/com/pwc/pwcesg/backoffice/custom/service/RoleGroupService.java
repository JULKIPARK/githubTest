package com.pwc.pwcesg.backoffice.custom.service;

import com.pwc.pwcesg.backoffice.custom.mapper.RoleGroupMapper;
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
public class RoleGroupService {

	private final RoleGroupMapper roleGroupMapper;

	public List<Map<String, Object>> selectRoleGroupList(Map<String, Object> paramMap) {
		return roleGroupMapper.selectRoleGroupList(paramMap);
	}

	public List<Map<String, Object>> selectAppListByRoleGroupCd(Map<String, Object> paramMap) {
		return roleGroupMapper.selectAppListByRoleGroupCd(paramMap);
	}

	public List<Map<String, Object>> selectMemberListByMbrGbCd(Map<String, Object> paramMap) {
		return roleGroupMapper.selectMemberListByMbrGbCd(paramMap);
	}

	public List<Map<String, Object>> selectMemberRightByMbrGbCd(Map<String, Object> paramMap) {
		return roleGroupMapper.selectMemberRightByMbrGbCd(paramMap);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int updateMemberRoleGroup(Map<String, Object> paramMap) {
		return roleGroupMapper.updateMemberRoleGroup(paramMap);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public int updateMemberGroupRight(Map<String, Object> paramMap) {
		return roleGroupMapper.updateMemberGroupRight(paramMap);
	}


}
