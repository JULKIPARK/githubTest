package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 토픽 관리 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface TopicMapper {

	/**
	 * 토픽 유효 목록 조회
	 * @return
	 */
	public List<Map<String, Object>> selectTpicValidityList();




	
	/**
	 * 토픽 관리 목록 조회
	 * @param paramMap
	 * @return
	 */
    public List<Map<String, Object>> selectTpicBySearchCondition(Map<String, Object> paramMap);
    
    /**
     * 토픽 관리 상세 조회
     * @param tpicUid
     * @return
     */
    public Map<String, Object> selectTpicByTpicUid(String tpicUid);

	void insertTpicInfo(Map<String, Object> paramMap);

	void updateTpicInfo(Map<String, Object> paramMap);

	void deleteTpicRlsTgtInfo(String tpicUid);

	void upsertTpicApndInfo(List<Map> tpicApndData);

	void insertTpicRlsTgtInfo(Map rlsTgtCdMap);

	List selectTpicApndByTpicUid(String tpicUid);

	List<Map<String, Object>> selectTpicByTpicGbCdAndTpicMnuCd(Map<String, Object> paramMap);

//	================================================================================================
//	DELETED ========================================================================================
//	================================================================================================

}
