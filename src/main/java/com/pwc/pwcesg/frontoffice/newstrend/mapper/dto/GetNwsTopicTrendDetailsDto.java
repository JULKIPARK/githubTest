package com.pwc.pwcesg.frontoffice.newstrend.mapper.dto;

import lombok.Data;

@Data
public class GetNwsTopicTrendDetailsDto {

    String label;

    Long topicUid;

    String topic;

    Long hits;

}
