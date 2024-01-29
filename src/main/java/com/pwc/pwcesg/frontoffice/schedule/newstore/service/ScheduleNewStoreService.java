package com.pwc.pwcesg.frontoffice.schedule.newstore.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.ScheduleNewStoreMapper;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.dto.GetNwsTopicListParamsDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.dto.GetNwsTopicListResultDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsKeywordEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsNewsEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTopicTrendEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.mapper.entity.NwsTrendEntity;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreRequestArgument;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreRequestArgumentPublishedAt;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreRequestDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseDto;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseNewsListReturnObject;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseNewsListReturnObjectDocuments;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseTimeLineReturnObject;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseTimeLineReturnObjectTimeLine;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseWordCloudReturnObject;
import com.pwc.pwcesg.frontoffice.schedule.newstore.service.dto.NewStoreResponseWordCloudReturnObjectNodes;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ScheduleNewStoreService {

	@Value("${newstore.apiKey}")
	private String newStoreApiKey;

	//    private String baseKeyword = "지속가능 OR ((경영 OR 보고서) AND ESG) OR 그린워싱 OR ISSB OR (지속가능성 AND IFRS) OR 국제지속가능성기준위원회 OR CSRD OR (지속가능성 AND (EU OR 유럽)) OR ESRS OR EFRAG OR (기후 AND (SEC OR 미국 증권거래위원회)) OR GRI OR TCFD OR 기후변화 OR (기후 AND 재무 OR 시나리오 OR 리스크 OR 공시)  OR TNFD OR (자연 AND (재무 OR 영향)) OR 생물다양성 OR SBTN OR (LEAP AND 접근법) OR 자연자본 OR PBAF OR (택소노미 AND EU) K-택소노미 OR EU Taxonomy OR 녹색 분류체계 OR 사회적 분류체계  OR 지속가능성 실사지침 OR 공급망 실사법 OR Corporate Sustainability Due Diligence Directive OR ISSA 5000 OR (ESG AND (인증 OR 검증)) OR (지속가능 AND (인증 OR 검증)) OR (IESBA AND 지속가능) OR (IAASB AND 지속가능)  OR ((공시 OR 중대성) AND ESG) OR KSSB  OR CBAM OR 탄소국경 OR 배출권 OR (ETS AND 탄소)  OR (평가 AND ESG) OR 한국ESG기준원 OR MSCI OR CDP OR DJSI OR 서스틴베스트 OR KCGS OR SBTi OR CF100 OR (Scope AND 온실가스)  OR 재생에너지 OR 친환경에너지 OR RE100 OR 내부 탄소가격  OR 순환경제 OR ((폐기물 OR 재활용) AND 순환)  OR 전과정평가 OR LCA OR 친환경 제품 인증 OR ((인적자원 OR 고용) AND ESG) OR (다양성 AND 포용성) OR DE&I  OR 인권 실사 OR (인권 AND (기업 OR 평가 OR 경영 OR 리스크)) OR ((협력사 OR 공급망) AND ESG) OR ((분쟁광물 OR RBA) AND 공급망)  OR ((사회 공헌 OR 동반성장 OR 상생 협력 OR 지역사회) AND 기업)  OR ((소비자 OR VOC) AND ESG) OR 금융접근성  OR (이사회 AND (구성 OR ESG OR 전문성 OR 다양성)) OR ((지배구조 OR 거버넌스) AND ESG) OR (사외이사 AND ESG) OR (주주 AND (의결권 OR 권리)) OR 주주행동주의 OR 주주친화 OR 이해관계자 자본주의 OR (경영 AND 윤리) OR ((내부고발 OR 공정거래위원회) AND 기업) OR 내부자거래 OR 불공정거래  OR ((위험관리 OR 리스크관리) AND ESG) OR (영향평가 AND (환경 OR 사회)) OR 탄소세 OR 탄소가격제 OR 환경세  OR (ESG AND (세금 OR 세무)) OR 세무투명성 OR BEPS  OR PCAF OR 탄소회계 OR 금융배출량 OR ((채권 OR ICMA OR 국제자본시장협회) AND ESG) OR 녹색채권 OR 지속가능연계채권   OR (투자 AND ESG) OR UN PRI OR 유엔 책임투자원칙 OR 책임 투자 OR 스튜어드십 코드 OR ESG DD OR ((실사 OR M&A OR 인수합병 OR 가치평가 OR 가치창출) AND ESG)";
	private String baseKeyword = "지속가능 OR ESG OR 그린워싱 OR ISSB OR CSRD OR ESRS OR EFRAG OR (SEC AND 기후) OR GRI OR TCFD OR TNFD OR 온실가스 OR 기후변화 OR 에너지전환 OR 순환경제 OR 생물다양성 OR (ESG AND 공급망관리) OR (지속가능 AND 소비) OR 윤리경영 OR 기후리스크 OR 금융배출량 OR 탄소배출 OR 탄소국경세 OR 주주권리";

	private ObjectMapper objectMapper;

	{
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private final ScheduleNewStoreMapper scheduleNewStoreMapper;

	public ScheduleNewStoreService(ScheduleNewStoreMapper scheduleNewStoreMapper) {
		this.scheduleNewStoreMapper = scheduleNewStoreMapper;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public void runNews() throws Exception {

		List<NewStoreResponseWordCloudReturnObjectNodes> resultGetTop5Keyword = this.getTop5Keyword();
		log.info("resultGetTop5Keyword = [{}]", resultGetTop5Keyword.toString());

		if (resultGetTop5Keyword.isEmpty()) {
			return;
		}

		LocalDate localDate = LocalDate.now();
		String baseDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Integer queryGetLastKeywordDayCount = scheduleNewStoreMapper.getLastKeywordDayCount(
			baseDate);

		for (int i = 0; i < resultGetTop5Keyword.size(); i++) {

			NewStoreResponseWordCloudReturnObjectNodes keywordItem = resultGetTop5Keyword.get(i);

			NwsKeywordEntity paramAddNwsKeyword = new NwsKeywordEntity();
			paramAddNwsKeyword.setBaseDate(baseDate);
			paramAddNwsKeyword.setDayCount(
				(queryGetLastKeywordDayCount == null ? 0 : queryGetLastKeywordDayCount) + 1);
			paramAddNwsKeyword.setOrderNo(i + 1);
			paramAddNwsKeyword.setId(keywordItem.getId());
			paramAddNwsKeyword.setName(keywordItem.getName());
			paramAddNwsKeyword.setLevel(keywordItem.getLevel());
			paramAddNwsKeyword.setWeight(keywordItem.getWeight());

			log.debug("paramAddNwsKeyword = [{}]", paramAddNwsKeyword);
			scheduleNewStoreMapper.addNwsKeyword(paramAddNwsKeyword);

			List<NewStoreResponseNewsListReturnObjectDocuments> resultGetNewsList = this.getNewsList(
				keywordItem.getName() + " AND (" + this.baseKeyword + ")");

			for (int ii = 0; ii < resultGetNewsList.size(); ii++) {
				NewStoreResponseNewsListReturnObjectDocuments newsItem = resultGetNewsList.get(ii);
				NwsNewsEntity paramAddNwsNews = new NwsNewsEntity();
				paramAddNwsNews.setKeywordUid(paramAddNwsKeyword.getKeywordUid());
				paramAddNwsNews.setOrderNo(ii + 1);
				paramAddNwsNews.setNewsId(newsItem.getNewsId());
				paramAddNwsNews.setTitle(newsItem.getTitle());
				paramAddNwsNews.setHilight(newsItem.getHilight());
				paramAddNwsNews.setPublishedAt(newsItem.getPublishedAt());
				paramAddNwsNews.setEnvelopedAt(newsItem.getEnvelopedAt());
				paramAddNwsNews.setDateline(newsItem.getDateline());
				paramAddNwsNews.setProvider(newsItem.getProvider());
				paramAddNwsNews.setCategory(String.join(",", newsItem.getCategory()));
				paramAddNwsNews.setCategoryIncident(
					String.join(",", newsItem.getCategoryIncident()));
				paramAddNwsNews.setByline(newsItem.getByline());
				paramAddNwsNews.setProviderLinkPage(newsItem.getProviderLinkPage());
				paramAddNwsNews.setPrintingPage(newsItem.getPrintingPage());

				log.debug("paramAddNwsNews = [{}]", paramAddNwsNews);
				scheduleNewStoreMapper.addNwsNews(paramAddNwsNews);
			}

			List<NewStoreResponseTimeLineReturnObjectTimeLine> resultGetNewsTrends = this.getNewsTrends(
				keywordItem.getName() + " AND (" + this.baseKeyword + ")");
			for (int ii = 0; ii < resultGetNewsTrends.size(); ii++) {
				NewStoreResponseTimeLineReturnObjectTimeLine trendItem = resultGetNewsTrends.get(
					ii);
				NwsTrendEntity paramAddNwsTrend = new NwsTrendEntity();
				paramAddNwsTrend.setKeywordUid(paramAddNwsKeyword.getKeywordUid());
				paramAddNwsTrend.setLabel(trendItem.getLabel());
				paramAddNwsTrend.setHits(trendItem.getHits());

				log.debug("paramAddNwsTrend = [{}]", paramAddNwsTrend);
				scheduleNewStoreMapper.addNwsTrend(paramAddNwsTrend);
			}

		}

	}

	private List<NewStoreResponseWordCloudReturnObjectNodes> getTop5Keyword() {

		log.info("getTop5Keyword Start =====>");

		List<NewStoreResponseWordCloudReturnObjectNodes> returnList = null;
		List<String> categoryList = new ArrayList<String>();
		categoryList.add("002000000");
		categoryList.add("003001000");
		categoryList.add("003002000");
		categoryList.add("003006000");
		categoryList.add("003010000");
		categoryList.add("005000000");

		try {

			Calendar fromCal = Calendar.getInstance();
			fromCal.add(Calendar.DATE, -1);

			Calendar untilCal = Calendar.getInstance();
			untilCal.add(Calendar.DATE, 0);

			NewStoreResponseDto resultCallApiWordCloud = this.callApiWordCloud(
				NewStoreRequestDto.builder()
					.accessKey(this.newStoreApiKey)
					.argument(NewStoreRequestArgument.builder()
						.query(this.baseKeyword)
						.publishedAt(NewStoreRequestArgumentPublishedAt.builder()
							.from(fromCal.getTime())
							.until(untilCal.getTime())
							.build()
						)
						.category(categoryList)
						.build()
					)
					.build());

			NewStoreResponseWordCloudReturnObject wordCloudReturnObject = this.objectMapper.readValue(
				resultCallApiWordCloud.getReturnObject(),
				new TypeReference<NewStoreResponseWordCloudReturnObject>() {
				});

			log.info("wordCloudReturnObject = [{}]", wordCloudReturnObject.toString());

			returnList = new ArrayList<>(wordCloudReturnObject.getNodes()
				.subList(0, Math.min(wordCloudReturnObject.getNodes().size(), 20)));

		} catch (Exception e) {
			e.printStackTrace();
			returnList = new ArrayList<>();
		}

		return returnList;

	}

	private List<NewStoreResponseNewsListReturnObjectDocuments> getNewsList(String keyword) {

		List<NewStoreResponseNewsListReturnObjectDocuments> returnList = null;
		List<String> categoryList = new ArrayList<String>();
		categoryList.add("002000000");
		categoryList.add("003001000");
		categoryList.add("003002000");
		categoryList.add("003006000");
		categoryList.add("003010000");
		categoryList.add("005000000");

		try {

			Calendar fromCal = Calendar.getInstance();
			fromCal.add(Calendar.DATE, -6);

			Calendar untilCal = Calendar.getInstance();
			untilCal.add(Calendar.DATE, 0);

			NewStoreResponseDto resultCallApiNewsList = this.callApiNewsList(
				NewStoreRequestDto.builder()
					.accessKey(this.newStoreApiKey)
					.argument(NewStoreRequestArgument.builder()
							.query(keyword)
							.publishedAt(NewStoreRequestArgumentPublishedAt.builder()
								.from(fromCal.getTime())
								.until(untilCal.getTime())
								.build()
							)
							.category(categoryList)
//                            .sort(NewStoreRequestArgumentSort.builder().date("desc").build())
							.returnForm("0")
							.returnSize("20")
							.fields(
								Arrays.asList(
									"news_id",
									"published_at",
									"title",
									"provider",
									"byline",
									"category",
									"category_incident",
									"provider_link_page",
									"printing_page"
								)
							)
							.build()
					)
					.build());

			NewStoreResponseNewsListReturnObject newsListReturnObject = this.objectMapper.readValue(
				resultCallApiNewsList.getReturnObject(),
				new TypeReference<NewStoreResponseNewsListReturnObject>() {
				});

			returnList = new ArrayList<>(newsListReturnObject.getDocuments()
				.subList(0, Math.min(newsListReturnObject.getDocuments().size(), 20)));

		} catch (Exception e) {
			e.printStackTrace();
			returnList = new ArrayList<>();
		}

		return returnList;

	}

	private List<NewStoreResponseTimeLineReturnObjectTimeLine> getNewsTrends(String keyword) {

		List<NewStoreResponseTimeLineReturnObjectTimeLine> returnList = null;
		List<String> categoryList = new ArrayList<String>();
		categoryList.add("002000000");
		categoryList.add("003001000");
		categoryList.add("003002000");
		categoryList.add("003006000");
		categoryList.add("003010000");
		categoryList.add("005000000");

		try {

			Calendar fromCal = Calendar.getInstance();
			fromCal.add(Calendar.DATE, -6);

			Calendar untilCal = Calendar.getInstance();
			untilCal.add(Calendar.DATE, 0);

			NewStoreResponseDto resultCallApiTimeLine = this.callApiTimeLine(
				NewStoreRequestDto.builder()
					.accessKey(this.newStoreApiKey)
					.argument(NewStoreRequestArgument.builder()
						.query(keyword)
						.publishedAt(NewStoreRequestArgumentPublishedAt.builder()
							.from(fromCal.getTime())
							.until(untilCal.getTime())
							.build()
						)
						.category(categoryList)
						.build()
					)
					.build());

			NewStoreResponseTimeLineReturnObject timeLineReturnObject = this.objectMapper.readValue(
				resultCallApiTimeLine.getReturnObject(),
				new TypeReference<NewStoreResponseTimeLineReturnObject>() {
				});

			returnList = timeLineReturnObject.getTimeLine();

		} catch (Exception e) {
			e.printStackTrace();
			returnList = new ArrayList<>();
		}

		return returnList;

	}

	private NewStoreResponseDto callApiTopNKeyword(NewStoreRequestDto params) {
		return this.callApiNewStore("api-newstore/v2/search/topn-keyword.json", params);
	}

	private NewStoreResponseDto callApiWordCloud(NewStoreRequestDto params) {
		return this.callApiNewStore("api-newstore/v2/search/word_cloud.json", params);
	}

	private NewStoreResponseDto callApiNewsList(NewStoreRequestDto params) {
		return this.callApiNewStore("api-newstore/v2/search/newsList.json", params);
	}

	private NewStoreResponseDto callApiTimeLine(NewStoreRequestDto params) {
		return this.callApiNewStore("api-newstore/v2/search/time_line.json", params);
	}

	private NewStoreResponseDto callApiNewStore(String path, Object body) {

		log.info("callApiNewStore Start =====> path : [{}], body : [{}]", path, body.toString());

		String scheme = "https";
		String host = "www.newstore.or.kr";
		int port = 443;

		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme(scheme).host(host)
			.port(port).path(path).build(false);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Arrays.asList(MediaType.ALL));

		NewStoreResponseDto response = null;
		try {

			this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			String bodyStr = this.objectMapper.writeValueAsString(body);
			log.info("callApiNewStore bodyStr =====> [{}]", bodyStr);

			HttpEntity<String> httpEntity = new HttpEntity<>(bodyStr, httpHeaders);

			ResponseEntity<String> responseEntity = new RestTemplate().exchange(
				uriComponents.toUri(), HttpMethod.POST, httpEntity, String.class);

			log.info("callApiNewStore responseEntity =====> [{}]", responseEntity.toString());
			response = this.objectMapper.readValue(responseEntity.getBody(),
				new TypeReference<NewStoreResponseDto>() {
				});
			log.info("callApiNewStore response =====> [{}]", response.toString());

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return response;

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,
		SQLException.class})
	public void runTopic() throws Exception {

		GetNwsTopicListParamsDto paramsGetNwsTopicListParent = new GetNwsTopicListParamsDto();
		paramsGetNwsTopicListParent.setParentTopicUid(0L);
		paramsGetNwsTopicListParent.setUseYn("Y");

		log.debug("paramsGetNwsTopicListParent = [{}]", paramsGetNwsTopicListParent);
		List<GetNwsTopicListResultDto> queryGetNwsTopicListParent = scheduleNewStoreMapper.getNwsTopicList(
			paramsGetNwsTopicListParent);

		LocalDate localDate = LocalDate.now();
		String baseDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Integer queryGetLastTopicTrendDayCount = scheduleNewStoreMapper.getLastTopicTrendDayCount(
			baseDate);

		Calendar fromCal = Calendar.getInstance();
		fromCal.add(Calendar.DATE, -1);

		Calendar untilCal = Calendar.getInstance();
		untilCal.add(Calendar.DATE, 0);

		for (GetNwsTopicListResultDto parentTopic : queryGetNwsTopicListParent) {

			GetNwsTopicListParamsDto paramsGetNwsTopicList = new GetNwsTopicListParamsDto();
			paramsGetNwsTopicList.setParentTopicUid(parentTopic.getTopicUid());
			paramsGetNwsTopicList.setUseYn("Y");

			log.debug("paramsGetNwsTopicList = [{}]", paramsGetNwsTopicList);
			List<GetNwsTopicListResultDto> queryGetNwsTopicList = scheduleNewStoreMapper.getNwsTopicList(
				paramsGetNwsTopicList);

			for (GetNwsTopicListResultDto topic : queryGetNwsTopicList) {

				List<NewStoreResponseTimeLineReturnObjectTimeLine> resultGetNewsTrends = this.getNewsTrends(
					topic.getTopic());
				for (int i = 0; i < resultGetNewsTrends.size(); i++) {
					NewStoreResponseTimeLineReturnObjectTimeLine trendItem = resultGetNewsTrends.get(
						i);

					NwsTopicTrendEntity paramAddNwsTopicTrend = new NwsTopicTrendEntity();
					paramAddNwsTopicTrend.setTopicUid(topic.getTopicUid());
					paramAddNwsTopicTrend.setBaseDate(baseDate);
					paramAddNwsTopicTrend.setDayCount((queryGetLastTopicTrendDayCount == null ? 0
						: queryGetLastTopicTrendDayCount) + 1);
					paramAddNwsTopicTrend.setLabel(trendItem.getLabel());
					paramAddNwsTopicTrend.setHits(trendItem.getHits());

					log.debug("paramAddNwsTopicTrend = [{}]", paramAddNwsTopicTrend);
					scheduleNewStoreMapper.addNwsTopicTrend(paramAddNwsTopicTrend);
				}

				Thread.sleep(300L);

			}

		}

	}

}
