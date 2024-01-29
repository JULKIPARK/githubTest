package com.pwc.pwcesg.backoffice.management.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 운영 관리 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface OperateMapper {

	/**
	 * 관리자활동로그 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectOperateList(Map<String, Object> paramMap);

	/**
	 * 관리자화면 목록 조회
	 *
	 * @return
	 */
	public List<Map<String, Object>> selectMnuScrList();

}
