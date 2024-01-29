package com.pwc.pwcesg.frontoffice.newstrend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResGetTrendsItem {

    Long trendUid;

    Long keywordUid;

    String label;

    Integer hits;

}
