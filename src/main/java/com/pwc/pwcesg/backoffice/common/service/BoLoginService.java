package com.pwc.pwcesg.backoffice.common.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.common.mapper.BoLoginMapper;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.SHA256Util;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class BoLoginService {
	
    @Value("${memberInfo.saltKey}")
    private String saltKey; // 암호화된 비밀번호
	
    private final BoLoginMapper boLoginMapper;
    
    /**
     * 사용자정보 조회
     * @param paramMap
     * @return
     */
    public SessionData selectMbMbrInfo(Map<String, Object> paramMap) {
        return boLoginMapper.selectMbMbrInfo(paramMap);
    }
	
	/**
	* 사용자정보 조회
	* @param paramMap
	* @return
	*/
	public SessionData selectMyInfo(Map<String, Object> paramMap) {
		return boLoginMapper.selectMyInfo(paramMap);
	}
	
	/**
	* 임시비밀번호 발급
	* @param paramMap
	* @return
	*/
	public int newPwd(Map<String, Object> paramMap) {
		String newPw = randomPassWord(10);

		//사용자 인증코드 조회 및 암호화 PW 세팅
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(newPw, saltKey); //입력된 비밀번호+salt 암호된 값

		//비밀번호 변경
		paramMap.put("mbrPwd", pwEncrypt);
		int rtnVal = boLoginMapper.updatePwdByFail(paramMap);

		//이메일 발송
		//sessionData.setMbrPwd(newPw);

		return rtnVal;
	}
	
	/**
	 * 임의 비밀번호 생성
	 * @param pwLength : 비밀번호 자릿 수
	 * @return 임시비밀번호
	 */
	public static String randomPassWord(int pwLength) {
		String ranPw = "";
		try {
			//배열에 선언
			char[] pwCollection = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '$', '%', '^', '*'};
			Random rn = SecureRandom.getInstanceStrong();
			for (int i = 0; i < pwLength; i++) {
				int iRn = rn.nextInt();
				int selectRandomPw = (int) (Math.abs(iRn) % pwCollection.length);//Math.rondom()은 0.0이상 1.0미만의 난수를 생성해 준다.
				ranPw += pwCollection[selectRandomPw];
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return ranPw;
	}

	public int updatePwdFailCnt(Map<String, Object> paramMap) {
		int rtvVal = 0;

		try {
			rtvVal = boLoginMapper.updatePwdFailCnt(paramMap);
		} catch (Exception e) {
			rtvVal = 0;
		}

		return rtvVal;
	}

	public int updatePwdFailCntReset(Map<String, Object> paramMap) {
		int rtvVal = 0;

		rtvVal = boLoginMapper.updatePwdFailCntReset(paramMap);

		return rtvVal;
	}

	public Map<String, Object> selectMyRights(Map<String, Object> paramMap) {
		return boLoginMapper.selectMyRights(paramMap);
	}
    
}
