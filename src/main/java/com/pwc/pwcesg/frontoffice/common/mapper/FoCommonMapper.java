package com.pwc.pwcesg.frontoffice.common.mapper;

import com.pwc.pwcesg.config.SessionData;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoCommonMapper {

	/**
	 * 아이디 중복확인
	 *
	 * @param mbrId
	 * @return
	 */
	public int checkDuplicate(String mbrId);

	/**
	 * 이메일 중복확인
	 *
	 * @param email
	 * @return
	 */
	public int checkEmail(String email);

	/**
	 * 회원정보 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertJoin(Map<String, Object> paramMap);

	/**
	 * 회원정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public SessionData selectMbMbrInfo(Map<String, Object> paramMap);

	/**
	 * 사용자정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public SessionData selectMyInfo(Map<String, Object> paramMap);

	/**
	 * 임시비밀번호 발급
	 *
	 * @param sessionData
	 * @return
	 */
	public int updatePwd(SessionData sessionData);

	/**
	 * 회원정보 수정
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateJoin(Map<String, Object> paramMap);





	/**
	 * 뉴스레터 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public int selectNewsletter(SessionData sessionData);

	/**
	 * 뉴스레터 신청
	 *
	 * @param paramMap
	 * @return
	 */
	public int saveNewsletter(Map<String, String> paramMap);

	public int saveMbrNewsletter(Map<String, String> paramMap);

	/**
	 * 컨텐츠 조회
	 *
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> selectContent(Map<String, Object> paramMap);

	public List<Map<String, Object>> selectMuContent(Map<String, Object> paramMap);



	/**
	 * 컨텐츠 조회기록
	 *
	 * @param string
	 * @return
	 */
	public int deleteContentLog(Map<String, Object> paramMap);

	public int insertContentLog(Map<String, Object> paramMap);

	public int updateViewCnt(Map<String, Object> paramMap);

	/**
	 * 회원 탈퇴
	 *
	 * @param paramMap
	 * @return
	 */
	public int withdrawal(Map<String, Object> paramMap);






	public int updatePwdFailCnt(Map<String, Object> paramMap);
	public int updatePwdByFail(Map<String, Object> paramMap);
	public int updatePwdFailCntReset(Map<String, Object> paramMap);


}
