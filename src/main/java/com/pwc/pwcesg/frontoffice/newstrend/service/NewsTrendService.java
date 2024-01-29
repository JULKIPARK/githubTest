package com.pwc.pwcesg.frontoffice.newstrend.service;

import com.pwc.pwcesg.frontoffice.newstrend.controller.dto.*;
import com.pwc.pwcesg.frontoffice.newstrend.mapper.NewsTrendMapper;
import com.pwc.pwcesg.frontoffice.newstrend.mapper.dto.GetNwsTopicTrendDetailsDto;
import com.pwc.pwcesg.frontoffice.newstrend.mapper.dto.GetNwsTopicTrendListWithParentHitsDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsKeywordEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsNewsEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTrendEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class NewsTrendService {

    private final NewsTrendMapper newsTrendMapper;

    public NewsTrendService(NewsTrendMapper newsTrendMapper) {
        this.newsTrendMapper = newsTrendMapper;
    }

    public ResGetKeywords getKeywords() throws Exception {
        List<NwsKeywordEntity> queryGetNwsKeywordsLatest = newsTrendMapper.getNwsKeywordsLatest();
        return ResGetKeywords.builder()
                .keywords(
                        Arrays.asList(
                                queryGetNwsKeywordsLatest.stream().map(item ->
                                        ResGetKeywordsItem.builder()
                                                .keywordUid(item.getKeywordUid())
                                                .orderNo(item.getOrderNo())
                                                .name(item.getName())
                                                .title(item.getTitle())
                                                .pos(item.getPos())
                                                .build()
                                ).toArray(ResGetKeywordsItem[]::new)
                        )

                )
                .build();
    }

    public ResGetNews getNews(ReqGetNews params) throws Exception {

        if (params.getKeywordUid() == null || params.getKeywordUid() == 0L) {
            throw new RuntimeException();
        }

        List<NwsNewsEntity> queryGetNwsNewsByKeywordUid = newsTrendMapper.getNwsNewsByKeywordUid(params.getKeywordUid());
        return ResGetNews.builder()
                .news(
                        Arrays.asList(
                                queryGetNwsNewsByKeywordUid.stream().map(item ->
                                        ResGetNewsItem.builder()
                                                .newsUid(item.getNewsUid())
                                                .keywordUid(item.getKeywordUid())
                                                .orderNo(item.getOrderNo())
                                                .newsId(item.getNewsId())
                                                .title(item.getTitle())
                                                .hilight(item.getHilight())
                                                .publishedAt(item.getPublishedAt())
                                                .provider(item.getProvider())
                                                .providerLinkPage(item.getProviderLinkPage())
                                                .build()
                                ).toArray(ResGetNewsItem[]::new)
                        )
                )
                .build();
    }

    public ResGetTrends getTrends(ReqGetTrends params) throws Exception {
        if (params.getKeywordUid() == null || params.getKeywordUid() == 0L) {
            throw new RuntimeException();
        }
        List<NwsTrendEntity> queryGetNwsTrendsByKeywordUid = newsTrendMapper.getNwsTrendsByKeywordUid(params.getKeywordUid());
        return ResGetTrends.builder()
                .trends(
                        Arrays.asList(
                                queryGetNwsTrendsByKeywordUid.stream().map(item ->
                                        ResGetTrendsItem.builder()
                                                .trendUid(item.getTrendUid())
                                                .keywordUid(item.getKeywordUid())
                                                .label(item.getLabel())
                                                .hits(item.getHits())
                                                .build()
                                ).toArray(ResGetTrendsItem[]::new))
                )
                .build();
    }

    public ResGetParentTopicTrendHits getParentTopicTrendHits() throws Exception {
        List<GetNwsTopicTrendListWithParentHitsDto> queryGetNwsTopicTrendListWithParentHits = newsTrendMapper.getNwsTopicTrendListWithParentHits();
        return ResGetParentTopicTrendHits.builder()
                .topicTrends(
                        Arrays.asList(
                                queryGetNwsTopicTrendListWithParentHits.stream().map(item ->
                                        ResGetParentTopicTrendHitsItem.builder()
                                                .topicUid(item.getTopicUid())
                                                .hits(item.getHits())
                                                .topic(item.getTopic())
                                                .build()
                                ).toArray(ResGetParentTopicTrendHitsItem[]::new))
                )
                .build();
    }

    public ResGetParentTopicDetails getParentTopicDetails(ReqGetParentTopicDetails params) throws Exception {
        if(params.getParentTopicUid() == null || params.getParentTopicUid() == 0L) {
            throw new RuntimeException();
        }
        List<GetNwsTopicTrendDetailsDto> queryGetNwsTopicTrendDetails = newsTrendMapper.getNwsTopicTrendDetails(params.getParentTopicUid());
        return ResGetParentTopicDetails.builder()
                .details(
                        Arrays.asList(
                                queryGetNwsTopicTrendDetails.stream().map(item ->
                                        ResGetParentTopicDetailsItem.builder()
                                                .label(item.getLabel())
                                                .topicUid(item.getTopicUid())
                                                .hits(item.getHits())
                                                .topic(item.getTopic())
                                                .build()
                                ).toArray(ResGetParentTopicDetailsItem[]::new))
                )
                .build();
    }

}
