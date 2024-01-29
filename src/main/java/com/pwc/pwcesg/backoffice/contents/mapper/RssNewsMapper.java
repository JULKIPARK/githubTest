package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 신규 뉴스 관리 Mapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface RssNewsMapper {

	/**
	 * 신규 뉴스 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectRssNewsList(Map<String, Object> paramMap);

	/**
	 * 신규 뉴스 관리 상세 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> rssRegistView(String linkUrl);

	/**
	 *  뉴스 관리 상세 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> rssRegistTopicList(int contUid);

	/**
	 * 신규 뉴스 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRss(Map<String, Object> paramMap);

	/**
	 * 신규 뉴스 부가정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssApnd(Map<String, Object> paramMap);

	/**
	 * 신규 뉴스 토픽정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssTpic(Map<String, Object> paramMap);

	/**
	 * 신규 뉴스 일괄등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssBatch(Map<String, Object> paramMap);
}
