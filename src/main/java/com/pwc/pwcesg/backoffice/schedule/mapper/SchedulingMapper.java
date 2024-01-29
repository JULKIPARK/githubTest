package com.pwc.pwcesg.backoffice.schedule.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SchedulingMapper {

	/**
	 * RSS 기준정보 목록 조회
	 *
	 * @return
	 */
	public List<Map<String, Object>> selectRssBaseInfoList();

	/**
	 * 신규 뉴스 등록
	 *
	 * @param newsList
	 */
	public void registNews(List<Map<String, String>> list);

	/**
	 * RSS 최종작성일 수정
	 *
	 * @return
	 */
	public int updateBaseInfo(Map<String, Object> list);

//	/**
//	 * 일자별집계
//	 * @return
//	 */
//    public void insertBtDteGrpAggr();
}