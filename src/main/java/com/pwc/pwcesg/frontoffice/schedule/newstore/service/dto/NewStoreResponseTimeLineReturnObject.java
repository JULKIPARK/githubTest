package com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewStoreResponseTimeLineReturnObject {

    @JsonProperty("total_hists")
    Integer totalHists;

    @JsonProperty("time_line")
    List<NewStoreResponseTimeLineReturnObjectTimeLine> timeLine;

}
