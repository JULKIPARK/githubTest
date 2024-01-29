package com.pwc.pwcesg.backoffice.common.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.pwc.pwcesg.config.SessionData;

@Mapper
public interface BoLoginMapper {
	
	/**
	 * 회원정보 조회
	 * @param paramMap
	 * @return
	 */
    public SessionData selectMbMbrInfo(Map<String, Object> paramMap);
    
    /**
     * 사용자정보 조회
     * @param paramMap
     * @return
     */
    public SessionData selectMyInfo(Map<String, Object> paramMap);
    
    /**
     * 임시비밀번호 발급
     * @param paramMap
     * @return
     */
    public int updatePwd(SessionData sessionData);

	public int updatePwdFailCnt(Map<String, Object> paramMap);
	public int updatePwdByFail(Map<String, Object> paramMap);
	public int updatePwdFailCntReset(Map<String, Object> paramMap);

	public Map<String, Object> selectMyRights(Map<String, Object> paramMap);
}
