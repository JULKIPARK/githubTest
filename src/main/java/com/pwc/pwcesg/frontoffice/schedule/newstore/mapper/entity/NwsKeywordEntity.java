package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NwsKeywordEntity {

    Long keywordUid;

    String baseDate;

    Integer dayCount;

    Integer orderNo;

    Integer id;

    String name;

    String title;

    Integer level;

    Float weight;

    Date regDt;

    Float pos;

}
