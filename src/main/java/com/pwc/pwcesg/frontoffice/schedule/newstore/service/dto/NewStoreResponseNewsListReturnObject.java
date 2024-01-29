package com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewStoreResponseNewsListReturnObject {

    @JsonProperty("total_hits")
    Integer totalHits;

    List<NewStoreResponseNewsListReturnObjectDocuments> documents;

}
