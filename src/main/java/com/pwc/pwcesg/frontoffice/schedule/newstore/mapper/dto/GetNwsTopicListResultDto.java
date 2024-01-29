package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GetNwsTopicListResultDto {

    Long topicUid;

    Long parentTopicUid;

    String topic;

    Integer orderNo;

    String useYn;

    Date regDt;

}
