package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 콘텐츠 정보 관리 Mapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface SrcMapper {

	/**
	 * 출처정보 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectSrcList(Map<String, Object> paramMap);

	/**
	 * 출처정보 상세 조회
	 *
	 * @param srcUid
	 * @return
	 */
	public Map<String, Object> selectSrcView(String srcUid);

	/**
	 * 출처정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	int insertSrcInfo(Map<String, Object> paramMap);

	/**
	 * 출처정보 수정
	 *
	 * @param paramMap
	 * @return
	 */
	int updateSrcInfo(Map<String, Object> paramMap);
}
