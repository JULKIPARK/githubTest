package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NwsTopicEntity {

    Long topicUid;

    Long parentTopicUid;

    String topic;

    Integer orderNo;

    String useYn;

    Date regDt;

}
