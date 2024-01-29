package com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NwsNewsEntity {

    Long newsUid;

    Long keywordUid;

    Integer orderNo;

    String newsId;

    String title;

    String hilight;

    String publishedAt;

    String envelopedAt;

    String dateline;

    String provider;

    String category;

    String categoryIncident;

    String byline;

    String providerLinkPage;

    String printingPage;

    Date regDt;

}
