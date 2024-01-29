package com.pwc.pwcesg.backoffice.contents.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.contents.mapper.RssNewsMapper;

import lombok.RequiredArgsConstructor;

/**
 * 신규 뉴스 관리 Service
 *
 * @author N.J.Kim
 */
@RequiredArgsConstructor
@Service
public class RssNewsService {

	private final RssNewsMapper rssNewsMapper;

	/**
	 * 신규 뉴스 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectRssNewsList(Map<String, Object> paramMap) {
		return rssNewsMapper.selectRssNewsList(paramMap);
	}

	/**
	 * 신규 뉴스 관리 상세 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> rssRegistView(String linkUrl) {
		return rssNewsMapper.rssRegistView(linkUrl);
	}

	/**
	 * 뉴스 관리 상세 토픽 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> rssRegistTopicList(int contUid) {
		return rssNewsMapper.rssRegistTopicList(contUid);
	}

	/**
	 * 신규 뉴스 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRss(Map<String, Object> paramMap) {
		return rssNewsMapper.insertRss(paramMap);
	}

	/**
	 * 신규 뉴스 부가정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssApnd(Map<String, Object> paramMap) {
		return rssNewsMapper.insertRssApnd(paramMap);
	}

	/**
	 * 신규 뉴스 토픽정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssTpic(Map<String, Object> paramMap) {
		return rssNewsMapper.insertRssTpic(paramMap);
	}

	/**
	 * 신규 뉴스 일괄등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertRssBatch(Map<String, Object> paramMap) {
		return rssNewsMapper.insertRssBatch(paramMap);
	}
}
