package com.pwc.pwcesg.frontoffice.newstrend.mapper.dto;

import lombok.Data;

@Data
public class GetNwsTopicTrendListWithParentHitsDto {

    Long topicUid;

    Long hits;

    String topic;

}
