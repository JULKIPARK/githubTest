package com.pwc.pwcesg.frontoffice.newstrend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResGetKeywordsItem {

    Long keywordUid;

    Integer orderNo;

    String name;

    String title;

    Float pos;

}
