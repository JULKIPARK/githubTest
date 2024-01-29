package com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewStoreRequestArgument {

    String query;

    @JsonProperty("published_at")
    NewStoreRequestArgumentPublishedAt publishedAt;

    List<String> category;

    NewStoreRequestArgumentSort sort;

    @JsonProperty("return_form")
    String returnForm;

    @JsonProperty("return_size")
    String returnSize;

    List<String> fields;

}
