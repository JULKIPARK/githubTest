package com.pwc.pwcesg.backoffice.contents.service;

import com.pwc.pwcesg.backoffice.contents.mapper.NewsTrendMngMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 신규 뉴스 관리 Service
 *
 * @author N.J.Kim
 */
@RequiredArgsConstructor
@Service
public class NewsTrendMngService {

	private final NewsTrendMngMapper newsTrendMapper;

	/**
	 * 뉴스 트렌드 키워드 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectNewsTrendKeywordList() {
		return newsTrendMapper.selectNewsTrendKeywordList();
	}

	/**
	 * 뉴스 트렌드 키워드 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectNewsTrendNewsList(int keywordUid) {
		return newsTrendMapper.selectNewsTrendNewsList(keywordUid);
	}

	/**
	 * 뉴스 트렌드 키워드 노출여부 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateNewsTrendKeyword(Map<String, Object> paramMap) {
		return newsTrendMapper.updateNewsTrendKeyword(paramMap);
	}

	/**
	 * 뉴스 트렌드 뉴스 노출여부 갱신
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateNewsTrendNews(Map<String, Object> paramMap) {
		return newsTrendMapper.updateNewsTrendNews(paramMap);
	}
}
