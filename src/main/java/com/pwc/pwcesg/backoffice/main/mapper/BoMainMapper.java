package com.pwc.pwcesg.backoffice.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 메인 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface BoMainMapper {
	
	/**
	 * 오늘의 할 일
	 * @return
	 */
    public Map<String, Object> selectToDo();
	
	/**
	 * 회원 현황
	 * @return
	 */
    public List<Map<String, Object>> selectMbrState();
	
	/**
	 * 회원 현황 집계
	 * @return
	 */
    public List<Map<String, Object>> selectMbrStateSum();
	
	/**
	 * 온라인 문의 현황
	 * @return
	 */
    public List<Map<String, Object>> selectAskCtCnt();
	
	/**
	 * 미확인 온라인 문의 목록
	 * @return
	 */
    public List<Map<String, Object>> selectUnconfirmedAskList();
}
