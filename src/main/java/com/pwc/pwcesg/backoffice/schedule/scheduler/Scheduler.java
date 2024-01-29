package com.pwc.pwcesg.backoffice.schedule.scheduler;

import com.pwc.pwcesg.backoffice.schedule.service.SchedulingService;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
public class Scheduler {

	@Autowired
	private SchedulingService schedulingService;

	@Scheduled(cron = "0 00 02 * * ?")
	private void registNews() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			if (ip.getHostAddress().equals("4.230.9.118") || ip.getHostAddress().equals("10.0.0.6")) {
				log.info("News gathering Start..{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
				rssRegist();//RSS 신규뉴스 수집
			} else {
				log.info("Reject News gathering...", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			log.info("News gathering complete..{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
		}
	}


	/**
	 * RSS 신규뉴스 수집
	 */
	private void rssRegist() {
		List<Map<String, Object>> rssBaseInfolist = schedulingService.selectRssBaseInfoList();

		for (Map<String, Object> rssBaseInfo : rssBaseInfolist) {
			List<Map<String, String>> newsList = parser(rssBaseInfo);

			if (!newsList.isEmpty()) {
				schedulingService.registNews(newsList, rssBaseInfo);
			}
		}
	}

	/**
	 * RSS 수집
	 *
	 * @param rssInfo RSS 기준정보
	 * @return
	 */
	private List<Map<String, String>> parser(Map<String, Object> rssInfo) {
		SAXBuilder saxBuilder = new SAXBuilder();
		List<Map<String, String>> items = new ArrayList<>();

		String url = rssInfo.get("sectnUrl") + "";//RSS URL
		String splyBzentyCd = rssInfo.get("splyBzentyCd") + "";//제공업체
		String lstBuildDt = rssInfo.get("lstBuildDt") + "";//RSS 최종작성일

		try {
			Document doc = (Document) saxBuilder.build(url);
			Element root = doc.getRootElement();

			Element e_channel = root.getChild("channel");
			String lstBldDt = e_channel.getChildText("lastBuildDate");//RSS작성일

			//RSS 최종작성일 체크
			if (!lstBuildDt.equals(lstBldDt)) {
				List<Element> children = e_channel.getChildren("item");
				Iterator<Element> iter = children.iterator();
				while (iter.hasNext()) {
					Map<String, String> itemMap = new HashMap<>();

					Element e = iter.next();
					itemMap.put("pblcnDt", e.getChildTextTrim("pubDate"));        // 발행일
					itemMap.put("imgUrl", (e.getChildTextTrim("image") + "").replace("\"", ""));            // 이미지경로
					itemMap.put("linkUrl", e.getChildTextTrim("link"));            // 링크경로
					itemMap.put("splyBzentyCd", splyBzentyCd);                    // 제공업체코드
					itemMap.put("wrtrNm", e.getChildTextTrim("author"));        // 작성자명
					itemMap.put("ttl", e.getChildTextTrim("title"));            // 제목
					itemMap.put("sumry", e.getChildTextTrim("description"));    // 요약
					itemMap.put("cn", e.getChildTextTrim("content"));            // 내용
					items.add(itemMap);
				}
			}

		} catch (IOException e) {
			log.error("IOException: {}", e);
		} catch (JDOMException e) {
			log.error("JDOMException: {}", e);
		}

		return items;
	}

//	/**
//	 * 일자별집계
//	 */
//	public void dailyTally() {
//		schedulingService.insertBtDteGrpAggr();
//	}
}