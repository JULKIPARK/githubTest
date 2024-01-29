package com.pwc.pwcesg.frontoffice.newstrend.mapper;

import com.pwc.pwcesg.frontoffice.newstrend.mapper.dto.GetNwsTopicTrendDetailsDto;
import com.pwc.pwcesg.frontoffice.newstrend.mapper.dto.GetNwsTopicTrendListWithParentHitsDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsKeywordEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsNewsEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTrendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface NewsTrendMapper {

    List<NwsKeywordEntity> getNwsKeywordsLatest() throws Exception;

    List<NwsNewsEntity> getNwsNewsByKeywordUid(@Param("keywordUid") Long keywordUid) throws Exception;

    List<NwsTrendEntity> getNwsTrendsByKeywordUid(@Param("keywordUid") Long keywordUid) throws Exception;

    List<GetNwsTopicTrendListWithParentHitsDto> getNwsTopicTrendListWithParentHits() throws Exception;

    List<GetNwsTopicTrendDetailsDto> getNwsTopicTrendDetails(@Param("parentTopicUid") Long parentTopicUid) throws Exception;

}
