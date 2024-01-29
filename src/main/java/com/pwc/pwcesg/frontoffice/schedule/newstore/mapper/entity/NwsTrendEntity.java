package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NwsTrendEntity {

    Long trendUid;

    Long keywordUid;

    String label;

    Integer hits;

    Date regDt;

}
