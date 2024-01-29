package com.pwc.pwcesg.backoffice.custom.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleGroupMapper {


	List<Map<String, Object>> selectRoleGroupList(Map<String, Object> paramMap);

	List<Map<String, Object>> selectAppListByRoleGroupCd(Map<String, Object> paramMap);

	List<Map<String, Object>> selectMemberListByMbrGbCd(Map<String, Object> paramMap);

	List<Map<String, Object>> selectMemberRightByMbrGbCd(Map<String, Object> paramMap);

	int updateMemberRoleGroup(Map<String, Object> paramMap);

	int updateMemberGroupRight(Map<String, Object> paramMap);

}
