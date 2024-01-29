package com.pwc.pwcesg.backoffice.custom.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 문의 · 요청 관리 CsMapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface CsMapper {

	/**
	 * 문의 · 요청 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectCsList(Map<String, Object> paramMap);

	/**
	 * 문의/요청 관리 상세조회
	 *
	 * @param askUid
	 * @return
	 */
	Map<String, Object> selectViewByCsDetailEditPop(String askUid);

	void updateAskInfoToAskStCdByAskUid(Map<String, Object> paramMap);

	int insertAnsrInfo(Map<String, Object> paramMap);

	int updateAnsrInfo(Map<String, Object> paramMap);


	int upsertAnsrInfoToAskUid(Map<String, Object> paramMap);

}
