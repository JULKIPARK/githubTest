package com.pwc.pwcesg.backoffice.popup.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 콘텐츠 관리 Mapper
 */
@Mapper
public interface PopupMapper {

	/**
	 * 콘텐츠 목록 조회
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectContentList(Map<String, Object> paramMap);

}
