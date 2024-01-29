package com.pwc.pwcesg.frontoffice.common.service;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.SHA256Util;
import com.pwc.pwcesg.frontoffice.common.mapper.FoCommonMapper;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 메인 Service
 *
 * @author N.J.Kim
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FoCommonService {

	@Value("${memberInfo.saltKey}")
	private String saltKey; // 암호화된 비밀번호

	@Value("${azure.communication.accesskey}")
	private String azureCommunicationAccesskey;

	private final FoCommonMapper foCommonMapper;

	/**
	 * 아이디 중복확인
	 *
	 * @param mbrId
	 * @return
	 */
	public boolean checkDuplicate(String mbrId) {
		boolean isDuplicate = true;
		if (foCommonMapper.checkDuplicate(mbrId) == 0) {
			isDuplicate = false;
		}

		return isDuplicate;
	}

	/**
	 * 이메일 중복확인
	 *
	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email) {
		boolean isEamil = false;
		if (foCommonMapper.checkEmail(email) == 0) {
			isEamil = true;
		}

		return isEamil;
	}

	/**
	 * 이메일발송
	 *
	 * @param email, code, url, maxTime
	 * @return
	 */
	public void ckSendEmail(String email, String code, String url, String maxTime) {
		String MailContent = ""
			+ "    <div style=\"display:inline-block; padding:30px 30px;  width:100%; background:#f2f2f2; box-sizing:border-box;\">\r\n"
			+ "        <div style=\"width:100%; background:#fff;  background:#fff; padding:60px 3% 100px; box-sizing:border-box\">\r\n"
			+ "        	<div style=\"width:160px; display:inline-block;\"><img src=\"https://samilesg.blob.core.windows.net/image/site/logo_new.png\" style=\"width:100%;\"></div>\r\n"
			+ "        	<div style=\"width:100%; padding:100px 0p 80px\">\r\n"
			+ "        		<div style=\"font-size:36px; font-family:'NotoB'; text-align:center; color;#2d2d2d; margin-top:30px;\">이메일 인증 안내</div>\r\n"
			+ "        		<div style=\"font-size:20px; text-align:center; color:#2d2d2d; margin-top:32px; line-height:32px;\">고객님 안녕하세요.<br/>⌜삼일 ESG⌟ 와 함께 해주셔서 감사합니다.</div>\r\n"
			+ "        	</div>\r\n"
			+ "        	<div style=\"width:100%; padding:60px 0px; border-bottom:1px solid #dedede\">\r\n"
			+ "        		<div style=\"width:100%; text-align:Center; font-size:16px; line-height:26px; color:#464646\">\r\n"
			+ "                이메일 인증을 위하여 아래의 인증번호를 회원 가입창에 입력하고,<br/>\r\n"
			+ "                해당 화면 내 <b style=\"color:#D04A02; font-family:'NotoB'\">[인증번호 확인]</b> 버튼을 눌러주세요.\r\n"
			+ "                </div>\r\n"
			+ "        		<div style=\"width:100%; padding:40px; background:#f2f2f2; box-sizing:border-box; margin-top:24px;\">\r\n"
			+ "        			<div style=\"width:100%;; text-align:center; color:#464646; font-family:'NotoB';\">인증번호</div>\r\n"
			+ "        			<div style=\"width:100%; text-align:Center; color:#D04A02; font-family:'NotoB'; font-size:28px; margin-top:20px;\">" + code + "</div>\r\n"
			+ "        		</div>\r\n"
			+ "        		<div style=\"color:#D04A02; text-align:Right; width:100%; font-size:14px; margin-top:12px;\">* " + maxTime + "까지 인증하셔야 합니다.</div>\r\n"
			+ "        	</div>\r\n"
			+ "        	<div style=\"width:100%; padding-top:80px;\">\r\n"
			+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px;\">본 메일은 발신전용 메일이며 회신 처리되지 않습니다.</div>\r\n"
			+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px; margin-top:16px;\">© 2023 PwC Korea. All rights reserved.</div>\r\n"
			+ "        	</div>\r\n"
			+ "        </div>\r\n"
			+ "    </div>\r\n";

		// Azure Portal 리소스에서 연결 문자열을 가져올 수 있습니다.
		String connectionString = "endpoint=https://samilesg-sendmail.korea.communication.azure.com/;accesskey=" + azureCommunicationAccesskey;

		EmailClient emailClient = new EmailClientBuilder().connectionString(connectionString).buildClient();
		EmailAddress toAddress = new EmailAddress(email);
		EmailMessage emailMessage = new EmailMessage().setSenderAddress("DoNotReply@samilesg.com").setToRecipients(toAddress).setSubject("[Samil ESG]이메일 인증").setBodyHtml(MailContent);

		SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);
		PollResponse<EmailSendResult> result = poller.waitForCompletion();
	}

	/**
	 * 회원 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertJoin(Map<String, Object> paramMap) {
		return foCommonMapper.insertJoin(paramMap);
	}

	/**
	 * 사용자정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public SessionData selectMbMbrInfo(Map<String, Object> paramMap) {
		return foCommonMapper.selectMbMbrInfo(paramMap);
	}

	/**
	 * 사용자정보 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public SessionData selectMyInfo(Map<String, Object> paramMap) {
		return foCommonMapper.selectMyInfo(paramMap);
	}

	/**
	 * 임시비밀번호 발급
	 *
	 * @param url, sessionData
	 * @return
	 */
	public SessionData newPwd(String url, SessionData sessionData) {
		String newPw = randomPassWord(10);

		//사용자 인증코드 조회 및 암호화 PW 세팅
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(newPw, saltKey); //입력된 비밀번호+salt 암호된 값

		//비밀번호 변경
		sessionData.setMbrPwd(pwEncrypt);
		foCommonMapper.updatePwd(sessionData);

		//이메일 발송
		//sessionData.setMbrPwd(newPw);

		pwSendEmail(sessionData.getCompMail(), url, sessionData.getMbrNm(), sessionData.getMbrId(), newPw);

		return sessionData;
	}
	public int newPwd(Map<String, Object> paramMap) {
		String newPw = randomPassWord(10);

		//사용자 인증코드 조회 및 암호화 PW 세팅
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(newPw, saltKey); //입력된 비밀번호+salt 암호된 값

		//비밀번호 변경
		paramMap.put("mbrPwd", pwEncrypt);
		int rtnVal = foCommonMapper.updatePwdByFail(paramMap);

		//이메일 발송
		//sessionData.setMbrPwd(newPw);

		return rtnVal;
	}

	/**
	 * 임시 비밀번호 이메일발송
	 *
	 * @param email, url, userName, userId, userPw
	 * @return
	 */
	public void pwSendEmail(String email, String url, String userName, String userId, String userPw) {
		String MailContent = ""
			+ "    <div style=\"display:inline-block; padding:30px 30px;  width:100%; background:#f2f2f2; box-sizing:border-box;\">\r\n"
			+ "        <div style=\"width:100%; background:#fff;  background:#fff; padding:60px 3% 100px; box-sizing:border-box\">\r\n"
			+ "        	<div style=\"width:160px; display:inline-block;\"><img src=\"https://samilesg.blob.core.windows.net/image/site/logo_new.png\" style=\"width:100%;\"></div>\r\n"
			+ "        	<div style=\"width:100%; padding:100px 0px 80px; border-bottom:1px solid #dedede\">\r\n"
			+ "        		<div style=\"font-size:36px; font-family:'NotoB'; text-align:center; color:#2d2d2d\">아이디∙임시비밀번호 안내</div>\r\n"
			+ "        		<div style=\"font-size:20px; text-align:center; color:#2d2d2d; margin-top:32px; line-height:32px;\">" + userName + "님 안녕하세요.<br/>고객님의 아이디와 임시 비밀번호를 전송해 드립니다.</div>\r\n"
			+ "        	</div>\r\n"
			+ "        	<div style=\"width:100%; padding:60px 0px; border-bottom:1px solid #dedede;\">\r\n"
			+ "        		<div style=\"width:100%; text-align:center; font-size:16px; line-height:26px;\">\r\n"
			+ "                임시비밀번호로 로그인 후, <br/>사용하실 비밀번호로 변경하여 주시기 바랍니다.\r\n"
			+ "        		</div>\r\n" + "        		<div style=\"width:100%; padding:40px; background:#f2f2f2; box-sizing:border-box; margin-top:24px;\">\r\n"
			+ "        			<span style=\"width:100%; display:flex;\">\r\n"
			+ "        				<div style=\"width:50%; font-size:24px; color:#464646\">아이디</div>\r\n"
			+ "        				<div style=\"width:50%; font-size:24px; color:#D04A02; font-family:'NotoB'; text-align:right;\">" + userId + "</div>\r\n"
			+ "        			</span>\r\n"
			+ "        			<span style=\"width:100%; display:flex; margin-top:24px;\">\r\n"
			+ "        				<div style=\"width:50%; font-size:24px; color:#464646\">임시비밀번호</div>\r\n"
			+ "        				<div style=\"width:50%; font-size:24px; color:#D04A02; font-family:'NotoB'; text-align:right;\">" + userPw + "</div>\r\n"
			+ "        			</span>\r\n"
			+ "        		</div>\r\n"
			+ "        		<div style=\"width:380px; background:#D04A02; padding:19px 24px; box-sizing:border-box; color:#fff; font-size:18px; margin:24px auto 0; text-align:Center;\">\r\n"
			+ "        			<a href=\"https://www.samilesg.com/common/login\" style=\"color:#fff;\">⌜삼일 ESG⌟ 로그인 화면으로 이동하기</a>\r\n"
			+ "        		</div>\r\n"
			+ "        	</div>\r\n"
			+ "        	<div style=\"width:100%; padding-top:80px;\">\r\n"
			+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px;\">본 메일은 발신전용 메일이며 회신 처리되지 않습니다.</div>\r\n"
			+ "        		<div style=\"width:100%; color:#7d7d7d; font-size:14px; margin-top:16px;\">© 2023 PwC Korea. All rights reserved.</div>\r\n"
			+ "        	</div>\r\n"
			+ "        </div>\r\n"
			+ "    </div>\r\n";

		// Azure Portal 리소스에서 연결 문자열을 가져올 수 있습니다.
		String connectionString = "endpoint=https://samilesg-sendmail.korea.communication.azure.com/;accesskey=" + azureCommunicationAccesskey;

		EmailClient emailClient = new EmailClientBuilder().connectionString(connectionString).buildClient();
		EmailAddress toAddress = new EmailAddress(email);
		EmailMessage emailMessage = new EmailMessage().setSenderAddress("DoNotReply@samilesg.com").setToRecipients(toAddress).setSubject("[Samil ESG]임시 비밀번호").setBodyHtml(MailContent);

		SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage, null);
		PollResponse<EmailSendResult> result = poller.waitForCompletion();
	}

	/**
	 * 회원 수정
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateJoin(Map<String, Object> paramMap) {
		return foCommonMapper.updateJoin(paramMap);
	}




	/**
	 * 뉴스레터 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public int selectNewsletter(SessionData sessionData) {
		return foCommonMapper.selectNewsletter(sessionData);
	}

	/**
	 * 뉴스레터 신청
	 *
	 * @param paramMap
	 * @return
	 */
	public int saveNewsletter(Map<String, String> paramMap) {
		foCommonMapper.saveMbrNewsletter(paramMap);
		return foCommonMapper.saveNewsletter(paramMap);
	}

	/**
	 * 컨텐츠 조회
	 *
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> selectContent(Map<String, Object> paramMap) {

		List<Map<String, Object>> dataList = foCommonMapper.selectContent(paramMap);
		String canAccess = "Y";

		/* 권한 체크() : 첫번째 컨텐츠만 체크 */
		if (dataList.size() > 0) {
			Map<String, Object> dataMap = dataList.get(0);
			// 전체열람(DP_TP_CD:40)은 패스
			dataMap.put("can_access", "Y");
			if (!dataMap.get("dpTpCd").equals("40")) {
				// 전체공개(DP_TP_CD:10)는 로그인 여부만 체크
				if (dataMap.get("dpTpCd").equals("10") && paramMap.get("mbrUid").equals("")) {
					dataMap.put("can_access", "L");
				}
				// 일부공개(DP_TP_CD:20)
				if (dataMap.get("dpTpCd").equals("20")) {
					if (paramMap.get("mbrUid").equals("")) {
						dataMap.put("can_access", "L");
					} else {
						if (!dataMap.get("rlsTgtCdArry").toString().contains(paramMap.get("mbrGbCd").toString())) {
							dataMap.put("can_access", "N");
						}
					}
				}
				// 비전시(DP_TP_CD:30)
				if (dataMap.get("dpTpCd").equals("30")) {
					dataMap.put("can_access", "N");
				}
			}
			canAccess = (String)dataMap.get("canAccess");
		}

		if (!paramMap.get("mbrUid").equals("") && dataList.size() > 0 && canAccess.equals("Y")) //읽기 기록
		{
			foCommonMapper.deleteContentLog(paramMap);
			foCommonMapper.insertContentLog(paramMap);
		}
		foCommonMapper.updateViewCnt(paramMap);
		return dataList;

	}

	/**
	 * 컨텐츠 조회
	 *
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> selectMuContent(Map<String, Object> paramMap) {

		List<Map<String, Object>> dataList = foCommonMapper.selectMuContent(paramMap);
		String canAccess = "Y";

		/* 권한 체크() : 첫번째 컨텐츠만 체크 */
		if (dataList.size() > 0) {
			Map<String, Object> dataMap = dataList.get(0);
			// 전체열람(DP_TP_CD:40)은 패스
			dataMap.put("can_access", "Y");
			if (!dataMap.get("dpTpCd").equals("40")) {
				// 전체공개(DP_TP_CD:10)는 로그인 여부만 체크
				if (dataMap.get("dpTpCd").equals("10") && paramMap.get("mbrUid").equals("")) {
					dataMap.put("can_access", "L");
				}
				// 일부공개(DP_TP_CD:20)
				if (dataMap.get("dpTpCd").equals("20")) {
					if (paramMap.get("mbrUid").equals("")) {
						dataMap.put("can_access", "L");
					} else {
						if (!dataMap.get("rlsTgtCdArry").toString().contains(paramMap.get("mbrGbCd").toString())) {
							dataMap.put("can_access", "N");
						}
					}
				}
				// 비전시(DP_TP_CD:30)
				if (dataMap.get("dpTpCd").equals("30")) {
					dataMap.put("can_access", "N");
				}
			}
			canAccess = (String)dataMap.get("canAccess");
		}

		if (!paramMap.get("mbrUid").equals("") && dataList.size() > 0 && canAccess.equals("Y")) //읽기 기록
		{
			foCommonMapper.deleteContentLog(paramMap);
			foCommonMapper.insertContentLog(paramMap);
		}
		return dataList;

	}

	/**
	 * 회원 탈퇴
	 *
	 * @param paramMap
	 * @return
	 */
	public int withdrawal(Map<String, Object> paramMap) {
		return foCommonMapper.withdrawal(paramMap);
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param paramMap
	 * @return
	 */
	public SessionData changePwd(SessionData sessionData) {
		//사용자 인증코드 조회 및 암호화 PW 세팅
		String pwEncrypt = "";
		pwEncrypt = SHA256Util.getEncrypt(sessionData.getMbrPwd(), saltKey); //입력된 비밀번호+salt 암호된 값

		//비밀번호 변경
		sessionData.setMbrPwd(pwEncrypt);
		foCommonMapper.updatePwd(sessionData);

		return sessionData;
	}

	/**
	 * 임의 비밀번호 생성
	 *
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
			rtvVal = foCommonMapper.updatePwdFailCnt(paramMap);
		} catch (Exception e) {
			rtvVal = 0;
		}

		return rtvVal;
	}

	public int updatePwdFailCntReset(Map<String, Object> paramMap) {
		int rtvVal = 0;

		rtvVal = foCommonMapper.updatePwdFailCntReset(paramMap);

		return rtvVal;
	}




}
