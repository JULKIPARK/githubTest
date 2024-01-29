package com.pwc.pwcesg.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionData implements Serializable {

   private static final long serialVersionUID = 1L;

   private String mbrUid;			/* 회원채번 */
   private String mbrUno;			/* 회원고유번호 */
   private String mbrId;			/* 회원아이디 */
   private String mbrPwd;			/* 회원암호 */
   private String mbrGbCd;			/* 회원구분코드(10: 그룹A, 20: 그룹B, 30: 그룹C, 40: 그룹D) */
   private String mbrNm;			/* 회원명 */
   private String mbrStCd;			/* 회원상태코드회원상태코드(10:가입, 20: 탈퇴신청, 30: 탈퇴) */
   private String joinDt;			/* 가입일 */
   private String whdwlAplyDt;		/* 탈퇴신청일 */
   private String whdwlAprvDt;		/* 탈퇴승인일 */
   private String deptNm;			/* 부서명 */
   private String posNm;			/* 직급명 */
   private String compMail;			/* 회사이메일 */
   private String mailChkYn;		/* 이메일확인여부 */
   private String mailChkDt;		/* 이메일확인일 */
   private String pwdFailCnt;		/* 암호실패건수 */
   private String pwdRctlyChngDt;	/* 암호최근변경일 */
   private String mrktAgreYn;		/* 마케팅동의여부 */
   private String mrktAgreDt;		/* 마케팅동의일 */
   private String nsltAgreYn;		/* 뉴스레터동의여부 */
   private String nsltAgreDt;		/* 뉴스레터동의일 */
   private String intrCdArry;		/* 관심코드배열 */
   private String cphonNo;			/* 휴대폰번호 */
   private String coprPhonNo;			/* 팩스번호 */
   private String coprUid;			/* 소속채번 */
   private String coprNm;			/* 소속명 */
   private String whdwlCausCd;		/* 탈퇴사유코드(10: 컨텐츠 부족, 20: 회원 혜택 부족, 30: 개인정보 유출 우려, 40: 사이트 이용 불편, 50: 이용빈도 낮음, 60: 기타) */
   private String whdwlCausComnt;	/* 탈퇴사유설명 */
   private String mailAgreYn;
   private String mailAgreDt;
   private String smsAgreYn;
   private String smsAgreDt;
   private String fstInsDt;			/* 최초등록일 */
   private String fstInsId;			/* 최초등록아이디 */
   private String lstUpdDt;			/* 최종수정일 */
   private String lstUpdId;			/* 최종수정아이디 */
   private String rights;
}
