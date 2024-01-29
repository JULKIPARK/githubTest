package com.pwc.pwcesg.backoffice.contents.service;

import com.pwc.pwcesg.backoffice.contents.mapper.TopicMapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 신규 뉴스 관리 Service
 *
 * @author N.J.Kim
 */
@RequiredArgsConstructor
@Service
public class TopicService {

	private final TopicMapper tpicMapper;

	/**
	 * 토픽 유효 목록 조회
	 *
	 * @return
	 */
	public List<Map<String, Object>> selectTpicValidityList() {
		return tpicMapper.selectTpicValidityList();
	}






	/**
	 * 토픽 관리 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectTpicBySearchCondition(Map<String, Object> paramMap) {
		return tpicMapper.selectTpicBySearchCondition(paramMap);
	}

	/**
	 * 토픽 관리 상세 조회
	 *
	 * @param tpicUid
	 * @return
	 */
	public Map<String, Object> selectTpicByTpicUid(String tpicUid) {
		return tpicMapper.selectTpicByTpicUid(tpicUid);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public String upsertByTopicPop(Map<String, Object> paramMap) {
		String tpicUid = "0";

		List<Map> tpicApndData = new ArrayList<>();

		List<String> rlsTgtCds = (List<String>) paramMap.get("rlsTgtCds");
		List<String> tpicTpCds = (List<String>) paramMap.get("tpicTpCds");
		List<String> apndCns = (List<String>) paramMap.get("apndCns");
		List<String> apndTtlByServices = (List<String>) paramMap.get("apndTtlByServices");
		List<String> apndCnByServices = (List<String>) paramMap.get("apndCnByServices");
		List<String> apndTtlByExperts = (List<String>) paramMap.get("apndTtlByExperts");
		List<String> spcalNmByExperts = (List<String>) paramMap.get("spcalNmByExperts");
		List<String> spcalPosNmByExperts = (List<String>) paramMap.get("spcalPosNmByExperts");
		List<String> spcalUrlByExperts = (List<String>) paramMap.get("spcalUrlByExperts");

		int listCount = 0;

		for (String tpicTpCd : tpicTpCds) {
			listCount++;

			if (tpicTpCd.equals("10")) {
				Map tpicApndMap = new HashMap();

				tpicApndMap.put("tpicUid", paramMap.get("tpicUid"));            /* 토픽채번 */
				tpicApndMap.put("tpicTpCd", tpicTpCd);            /* 토픽유형코드 */
				tpicApndMap.put("seqNo", 1);        /* 순번 */
				//                tpicApndMap.put("sumryComnt"	, paramMap.get("tpicUid"));				/* 요약설명 */
				//                tpicApndMap.put("apndTtl"		, paramMap.get("tpicUid"));			/* 부가제목 */
				tpicApndMap.put("apndCn", apndCns.get(listCount - 1));            /* 부가내용 */
				//                tpicApndMap.put("spcalNm"		, paramMap.get("tpicUid"));			/* 전문가명 */
				//                tpicApndMap.put("spcalPosNm"	, paramMap.get("tpicUid"));				/* 전문가직급명 */
				//                tpicApndMap.put("spcalMail"		, paramMap.get("tpicUid"));			/* 전문가이메일 */
				//                tpicApndMap.put("spcalTelno"	, paramMap.get("tpicUid"));				/* 전문가전번 */
				tpicApndMap.put("fstInsId", "ADMIN");            /* 최초등록아이디 */
				tpicApndMap.put("lstUpdId", "ADMIN");            /* 최종수정아이디 */

				tpicApndData.add(tpicApndMap);
			}
			if (tpicTpCd.equals("20")) {      // 삼일제공서비스
				int svcCnt = 0;

				for (String apndTtl : apndTtlByServices) {
					svcCnt++;

					Map tpicApndMap = new HashMap();

					tpicApndMap.put("tpicUid", paramMap.get("tpicUid"));            /* 토픽채번 */
					tpicApndMap.put("tpicTpCd", tpicTpCd);            /* 토픽유형코드 */
					tpicApndMap.put("seqNo", svcCnt);        /* 순번 */
					//                tpicApndMap.put("sumryComnt"	, paramMap.get("tpicUid"));				/* 요약설명 */
					tpicApndMap.put("apndTtl", apndTtlByServices.get(svcCnt - 1));            /* 부가제목 */
					tpicApndMap.put("apndCn", apndCnByServices.get(svcCnt - 1));            /* 부가내용 */
					//                tpicApndMap.put("spcalNm"		, paramMap.get("tpicUid"));			/* 전문가명 */
					//                tpicApndMap.put("spcalPosNm"	, paramMap.get("tpicUid"));				/* 전문가직급명 */
					//                tpicApndMap.put("spcalMail"		, paramMap.get("tpicUid"));			/* 전문가이메일 */
					//                tpicApndMap.put("spcalTelno"	, paramMap.get("tpicUid"));				/* 전문가전번 */
					tpicApndMap.put("fstInsId", "ADMIN");            /* 최초등록아이디 */
					tpicApndMap.put("lstUpdId", "ADMIN");            /* 최종수정아이디 */

					tpicApndData.add(tpicApndMap);
				}
			}
			if (tpicTpCd.equals("30")) {      // 전문가리스트
				int expertCnt = 0;

				for (String apndTtl : apndTtlByExperts) {
					expertCnt++;

					Map tpicApndMap = new HashMap();

					tpicApndMap.put("tpicUid", paramMap.get("tpicUid"));            /* 토픽채번 */
					tpicApndMap.put("tpicTpCd", tpicTpCd);            /* 토픽유형코드 */
					tpicApndMap.put("seqNo", expertCnt);        /* 순번 */
					//                tpicApndMap.put("sumryComnt"	, paramMap.get("tpicUid"));				/* 요약설명 */
					tpicApndMap.put("apndTtl", apndTtlByExperts.get(expertCnt - 1));            /* 부가제목 */
					//                    tpicApndMap.put("apndCn"		, paramMap.get("tpicUid"));			/* 부가내용 */
					tpicApndMap.put("spcalNm", spcalNmByExperts.get(expertCnt - 1));            /* 전문가명 */
					tpicApndMap.put("spcalPosNm", spcalPosNmByExperts.get(expertCnt - 1));                /* 전문가직급명 */
					tpicApndMap.put("spcalMail", spcalUrlByExperts.get(expertCnt - 1));            /* 전문가이메일 */
					//                tpicApndMap.put("spcalTelno"	, paramMap.get("tpicUid"));				/* 전문가전번 */
					tpicApndMap.put("fstInsId", "ADMIN");            /* 최초등록아이디 */
					tpicApndMap.put("lstUpdId", "ADMIN");            /* 최종수정아이디 */

					tpicApndData.add(tpicApndMap);
				}

			}
			if (tpicTpCd.equals("40")) {      // 프레임워크
				Map tpicApndMap = new HashMap();

				tpicApndMap.put("tpicUid", paramMap.get("tpicUid"));            /* 토픽채번 */
				tpicApndMap.put("tpicTpCd", tpicTpCd);            /* 토픽유형코드 */
				tpicApndMap.put("seqNo", 1);        /* 순번 */
				//                tpicApndMap.put("sumryComnt"	, paramMap.get("tpicUid"));				/* 요약설명 */
				//                tpicApndMap.put("apndTtl"		, paramMap.get("tpicUid"));			/* 부가제목 */
				tpicApndMap.put("apndCn", apndCns.get(listCount - 1));            /* 부가내용 */
				//                tpicApndMap.put("spcalNm"		, paramMap.get("tpicUid"));			/* 전문가명 */
				//                tpicApndMap.put("spcalPosNm"	, paramMap.get("tpicUid"));				/* 전문가직급명 */
				//                tpicApndMap.put("spcalMail"		, paramMap.get("tpicUid"));			/* 전문가이메일 */
				//                tpicApndMap.put("spcalTelno"	, paramMap.get("tpicUid"));				/* 전문가전번 */
				tpicApndMap.put("fstInsId", "ADMIN");            /* 최초등록아이디 */
				tpicApndMap.put("lstUpdId", "ADMIN");            /* 최종수정아이디 */

				tpicApndData.add(tpicApndMap);
			}
		}

		if (paramMap.get("tpicUid").equals("")) {
			tpicMapper.insertTpicInfo(paramMap);
			tpicUid = paramMap.get("tpicUid").toString();
		} else {
			tpicMapper.updateTpicInfo(paramMap);
			tpicUid = paramMap.get("tpicUid").toString();
			tpicMapper.deleteTpicRlsTgtInfo(tpicUid);
		}

		if (tpicApndData.size() > 0) {
			for (Map tpicApndItem : tpicApndData) {
				tpicApndItem.put("tpicUid", tpicUid);
			}
			tpicMapper.upsertTpicApndInfo(tpicApndData);
		}

		if (rlsTgtCds.size() > 0) {
			Map rlsTgtCdMap = new HashMap();
			rlsTgtCdMap.put("tpicUid", tpicUid);
			rlsTgtCdMap.put("rlsTgtCd", rlsTgtCds);

			tpicMapper.insertTpicRlsTgtInfo(rlsTgtCdMap);
		}

		return tpicUid;
	}

	public List selectTpicApndByTpicUid(String tpicUid) {
		return tpicMapper.selectTpicApndByTpicUid(tpicUid);
	}

	public List<Map<String, Object>> selectTpicByTpicGbCdAndTpicMnuCd(Map<String, Object> paramMap) {
		return tpicMapper.selectTpicByTpicGbCdAndTpicMnuCd(paramMap);
	}

//	================================================================================================
//	DELETED ========================================================================================
//	================================================================================================

}
