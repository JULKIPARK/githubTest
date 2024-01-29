package com.pwc.pwcesg.backoffice.management.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConvertMapper {

	public List<Map<String, Object>> selectContentListForYoutube();

	public List<Map<String, Object>> selectContentListForMp4();

	int updateContentDurationInfo(Map<String, Object> paramMap);

}
