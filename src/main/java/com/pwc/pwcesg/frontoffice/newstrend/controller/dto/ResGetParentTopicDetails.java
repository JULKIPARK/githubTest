package com.pwc.pwcesg.frontoffice.newstrend.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ResGetParentTopicDetails {

    List<ResGetParentTopicDetailsItem> details;

}