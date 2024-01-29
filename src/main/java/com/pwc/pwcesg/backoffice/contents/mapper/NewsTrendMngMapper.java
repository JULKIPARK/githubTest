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
public interface NewsTrendMngMapper {

	/**
	 * 뉴스 트렌드 키워드 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectNewsTrendKeywordList();

	/**
	 * 뉴스 트렌드 키워드별 뉴스 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectNewsTrendNewsList(int keywordUid);

	/**
	 * 뉴스 트렌드 키워드 노출여부 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateNewsTrendKeyword(Map<String, Object> paramMap);

	/**
	 * 뉴스 트렌드 뉴스 노출여부 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateNewsTrendNews(Map<String, Object> paramMap);

}
