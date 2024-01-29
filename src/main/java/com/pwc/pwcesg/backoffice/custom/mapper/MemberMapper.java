package com.pwc.pwcesg.backoffice.custom.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	
	/**
	 * 회원정보 목록 조회
	 * @param paramMap
	 * @return
	 */
    public List<Map<String, Object>> selectMemberList(Map<String, Object> paramMap);

	/**
	 * 회원정보 상세 조회
	 * @param mbrUid
	 * @return
	 */
	public Map<String, Object> selectMemberView(String mbrUid);

	public int insertMemberMemo(Map<String, Object> paramMap);

	public List<Map<String, Object>> selectMemberSelfCheckHstList(String mbrUid);

	public List<Map<String, Object>> selectMemberCsrdHstList(String mbrUid);

	public List<Map<String, Object>> selectMemberAskCntList(String mbrUid);

	public List<Map<String, Object>> selectMemberStateByOneMonth();

	public Map<String, Object> selectMemberAvgStateByOneYear();

	public List<Map<String, Object>> selectEsgTgtActResult();

}
