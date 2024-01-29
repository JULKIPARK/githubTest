package com.pwc.pwcesg.frontoffice.diagnosis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.pwc.pwcesg.config.SessionData;

@Mapper
public interface BusinessMapper {

	/**
	 *  조회
	 * @param paramMap
	 * @return
	 */
    public  List<Map<String, Object>> selectContList(Map<String, Object> paramMap);
}
