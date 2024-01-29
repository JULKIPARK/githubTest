package com.pwc.pwcesg.backoffice.contents.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 콘텐츠 정보 관리 Mapper
 * @author N.J.Kim
 *
 */
@Mapper
public interface ContentInfoMapper {

	/**
	 * 콘텐츠정보 목록 조회
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap);

	/**
	 * 콘텐츠정보 상세 조회
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> selectContentListView(Map<String, Object> paramMap);

	/**
	 * 출처정보 목록 조회
	 * @param paramMap
	 * @return
	 */
    public List<Map<String, Object>> selectSrcList(Map<String, Object> paramMap);

    /**
     * 출처정보 상세 조회
     * @param srcUid
     * @return
     */
    public Map<String, Object> selectSrcView(String srcUid);

	/**
	 * 출처정보 등록
	 * @param paramMap
	 * @return
	 */
	int insertSrcInfo(Map<String, Object> paramMap);

	/**
	 * 출처정보 수정
	 * @param paramMap
	 * @return
	 */
	int updateSrcInfo(Map<String, Object> paramMap);

	Map<String, Object> selectSrcDetailBySrcUid(String srcUid);

	List<Map<String, Object>> selectContentBySrcUid(String srcUid);

	int updateSrcInfoToSrcNmBySrcId(Map<String, Object> paramMap);

	List<Map<String, Object>> selectContentSearchPopList(Map<String, Object> paramMap);

	List<Map<String, Object>> selectSrcSearchPopView(Map<String, Object> paramMap);

	int insertContInfo(Map<String, Object> paramMap);

	int updateContInfo(Map<String, Object> paramMap);

	Map<String, Object> selectShowByContentUpsertPop(String contUid);

	int insertContRlsTgtInfo(Map<String, Object> paramMap);

	int insertKwordInfo(Map<String, Object> paramMap);

	void deleteContRlsTgtInfo(String contUid);

	void deleteKwordInfo(String contUid);

	void upsertContApndInfo(List<Map> contApndData);

	List<Map<String, Object>> selectContApndAtacByContUid(String contUid);

	List<Map<String, Object>> selectContentTopicByTpicUid(Map<String, Object> paramMap);
}
