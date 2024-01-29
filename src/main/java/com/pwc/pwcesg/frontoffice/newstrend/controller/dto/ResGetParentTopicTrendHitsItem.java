package com.pwc.pwcesg.frontoffice.newstrend.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResGetParentTopicTrendHitsItem {

    Long topicUid;

    Long hits;

    String topic;

}
