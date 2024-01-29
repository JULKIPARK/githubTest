package com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewStoreResponseNewsListReturnObjectDocuments {

    @JsonProperty("news_id")
    String newsId;

    String title;

    String hilight;

    @JsonProperty("published_at")
    String publishedAt;

    @JsonProperty("enveloped_at")
    String envelopedAt;

    String dateline;

    String provider;

    List<String> category;

    @JsonProperty("category_incident")
    List<String> categoryIncident;

    String byline;

    @JsonProperty("provider_link_page")
    String providerLinkPage;

    @JsonProperty("printing_page")
    String printingPage;

}
