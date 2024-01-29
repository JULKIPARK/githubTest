package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 뉴스 관리 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface NewsMapper {
	
	/**
	 * 뉴스 관리 목록 조회
	 * @param paramMap
	 * @return
	 */
    public List<Map<String, Object>> selectNewsList(Map<String, Object> paramMap);

	/**
	 * 뉴스 상태 변경
	 *
	 * @param list
	 * @return
	 */
	public int updateContSt(List<String> list);
    
    /**
     * 뉴스 관리 상세 조회
     * @param contUid
     * @return
     */
	public Map<String, Object> selectNewsView(int contUid);

	/**
	 * 뉴스 관리 상세 토픽 목록 조회
	 *
	 * @param contUid
	 * @return
	 */
	public List<Map<String, Object>> selectNewsTopicList(int contUid);

	public int updateNewsInfo(Map<String, Object> paramMap);
	public int deleteContTpicInfo(int contUid);
	public int insertContTpicInfo(Map<String, Object> paramMap);

}
