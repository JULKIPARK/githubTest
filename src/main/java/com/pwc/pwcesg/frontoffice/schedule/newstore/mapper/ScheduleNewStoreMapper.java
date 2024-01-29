package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper;

import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.dto.GetNwsTopicListParamsDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.dto.GetNwsTopicListResultDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsKeywordEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsNewsEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTopicTrendEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTrendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleNewStoreMapper {

    Integer getLastKeywordDayCount(@Param("baseDate") String baseDate) throws Exception;

    int addNwsKeyword(NwsKeywordEntity params) throws Exception;

    int addNwsNews(NwsNewsEntity params) throws Exception;

    int addNwsTrend(NwsTrendEntity params) throws Exception;

    List<GetNwsTopicListResultDto> getNwsTopicList(GetNwsTopicListParamsDto params) throws Exception;

    Integer getLastTopicTrendDayCount(@Param("baseDate") String baseDate) throws Exception;

    int addNwsTopicTrend(NwsTopicTrendEntity params) throws Exception;

}
