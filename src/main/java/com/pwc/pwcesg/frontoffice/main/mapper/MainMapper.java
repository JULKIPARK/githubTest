package com.pwc.pwcesg.frontoffice.main.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 메인 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface MainMapper {
    public List<Map<String, Object>> selectMainList(Map<String, Object> paramMap);

    /**
     *  삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectPwcInsightsList(Map<String, String> paramMap);

	/**
     *  최신 동향 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectTrendList(Map<String, String> paramMap);



	/**
     *  ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectBbsList(Map<String, String> paramMap);


}
