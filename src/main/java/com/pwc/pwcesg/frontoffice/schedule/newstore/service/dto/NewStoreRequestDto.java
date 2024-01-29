package com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewStoreRequestDto {

    @JsonProperty("access_key")
    String accessKey;

    NewStoreRequestArgument argument;

}
