package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NwsTopicTrendEntity {

    Long topicTrendUid;

    Long topicUid;

    String baseDate;

    Integer dayCount;

    String label;

    Integer hits;

    Date regDt;

}
