package com.pwc.pwcesg.frontoffice.newstrend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResGetNewsItem {

    Long newsUid;

    Long keywordUid;

    Integer orderNo;

    String newsId;

    String title;

    String hilight;

    String publishedAt;

    String provider;

    String providerLinkPage;

}
