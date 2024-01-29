package com.pwc.pwcesg.common;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetRSS {

	public static void main(String args[]) {
		String URL = "http://www.impacton.net/rss/allArticle.xml";
		List<Map<String, String>> items = new GetRSS().parser(URL, "10");
		
		for(Map<String, String> item : items) {
			Iterator<String> iterator = item.keySet().iterator();
			while(iterator.hasNext()){
				String key = (String) iterator.next();
				log.info("\n[KEY]{}\n[Value]{}", key, item.get(key));
			}
		}
	}
	
	/**
	 * 
	 * @param url	뉴스 URL
	 * @param splyBzentyCd	제공업체 코드
	 * @return
	 */
	public List<Map<String, String>> parser(String url, String splyBzentyCd) {
		SAXBuilder saxBuilder = new SAXBuilder();
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		
		try {
			Document doc = (Document) saxBuilder.build(url);
			Element root = doc.getRootElement();

			Element e_channel = root.getChild("channel");
			List<Element> children = e_channel.getChildren("item");
			Iterator<Element> iter = children.iterator();
			while(iter.hasNext()){
				Map<String, String> itemMap = new HashMap<String, String>();

				Element e = iter.next();
				itemMap.put("pubDate", e.getChildTextTrim("pubDate"));			// 발행일
				itemMap.put("imgUrl", e.getChildTextTrim("image"));				// 이미지경로
				itemMap.put("linkUrl", e.getChildTextTrim("link"));				// 링크경로
				itemMap.put("splyBzentyCd", splyBzentyCd);						// 제공업체코드
				itemMap.put("author", e.getChildTextTrim("author"));			// 작성자명
				itemMap.put("exprDt", e.getChildTextTrim("exprDt"));			// 만료일
				itemMap.put("title", e.getChildTextTrim("title"));				// 제목
				itemMap.put("description", e.getChildTextTrim("description"));	// 요약
				itemMap.put("content", e.getChildTextTrim("content"));			// 내용
				itemMap.put("dpYn", "N");										// 전시여부
				itemMap.put("fstInsId", "System");							// 최초등록아이디
				itemMap.put("lstInsId", "System");							// 최종수정아이디
				items.add(itemMap);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} 

		return items;
	}
}