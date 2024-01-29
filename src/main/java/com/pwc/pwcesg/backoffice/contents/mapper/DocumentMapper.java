package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 문서·영상 관리 Mapper
 *
 * @author N.J.Kim
 */
@Mapper
public interface DocumentMapper {

	/**
	 * 콘텐츠정보 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap);

	/**
	 * 출처 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> selectSrcSearchPopList(Map<String, Object> paramMap);

	/**
	 * 콘텐츠정보(ST_CONT_INFO) 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertDocInfo(Map<String, Object> paramMap);

	/**
	 * 콘텐츠부가정보(ST_CONT_APND_INFO) 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertContApndInfo(List<Map<String, Object>> paramMap);

	/**
	 * 콘텐츠토픽정보(DP_CONT_TPIC_INFO) 등록
	 *
	 * @param paramMap
	 * @return
	 */
	public int insertContTpicInfo(Map<String, Object> paramMap);

	/**
	 * 문서·영상 관리 상세 조회
	 *
	 * @param contUid
	 * @return
	 */
	public Map<String, Object> selectContentView(int contUid);

	/**
	 * 문서·영상 관리 추가 정보 목록 조회
	 *
	 * @param contUid
	 * @return
	 */
	public List<Map<String, Object>> selectApndContentList(int contUid);

	/**
	 * 뉴스 관리 상세 토픽 목록 조회
	 *
	 * @param contUid
	 * @return
	 */
	public List<Map<String, Object>> selectContentTopicList(int contUid);

	/**
	 * 첨부파일 목록 조회
	 *
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> selectAtacFileList(List<String> list);

	/**
	 * 콘텐츠정보(ST_CONT_INFO) 삭제
	 *
	 * @param list
	 * @return
	 */
	public int deleteStContInfo(List<String> list);

	/**
	 * 콘텐츠부가정보(ST_CONT_APND_INFO) 삭제
	 *
	 * @param list
	 * @return
	 */
	public int deleteStContApndInfo(List<String> list);
	public int deleteStContApndInfoOne(Map<String, Object> paramMap);

	/**
	 * 콘텐츠토픽정보(DP_CONT_TPIC_INFO) 삭제
	 *
	 * @param list
	 * @return
	 */
	public int deleteDpContTpicInfo(List<String> list);

	/**
	 * 첨부상세정보(CM_ATAC_DTL_INFO) 삭제
	 *
	 * @param list
	 * @return
	 */
	public int deleteCmAtacDtlInfo(List<Map<String, Object>> list);

	/**
	 * 콘텐츠정보(ST_CONT_INFO) 수정
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateDocInfo(Map<String, Object> paramMap);

	/**
	 * 콘텐츠부가정보(ST_CONT_APND_INFO) 수정
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateContApndInfo(Map<String, Object> paramMap);

	/**
	 * 콘텐츠토픽정보(DP_CONT_TPIC_INFO) 수정
	 *
	 * @param paramMap
	 * @return
	 */
	public int updateContTpicInfo(Map<String, Object> paramMap);

	/**
	 * 콘텐츠토픽정보(DP_CONT_TPIC_INFO) 삭제
	 *
	 * @param paramMap
	 * @return
	 */
	public int deleteContTpicInfo(int contUid);


	/**
	 * 문서·영상 관리 수정
	 *
	 * @param paramMap
	 * @return ModelAndView
	 */
	public int updateDocumentView(Map<String, Object> paramMap);

//	================================================================================================
//	DELETED ========================================================================================
//	================================================================================================

//	/**
//	 * 첨부파일정보(CM_ATAC_FILE_INFO) 삭제
//	 *
//	 * @param atacFileUidList
//	 * @return
//	 */
//	public int deleteCmAtacFileInfo(List<Map<String, Object>> list);
}
