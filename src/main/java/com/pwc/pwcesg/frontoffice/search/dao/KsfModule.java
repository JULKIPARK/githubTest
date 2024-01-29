package com.pwc.pwcesg.frontoffice.search.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pwc.pwcesg.frontoffice.search.HomeController;
import com.pwc.pwcesg.frontoffice.search.common.DCUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * Class Name : KsfModule.java
 * Description : 검색엔진의 KSF 모듈이용하여 검색결과 조회 모듈
 *
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일                       최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */

@Slf4j
@Component("ksfModule")
public class KsfModule {

	
	@Value("${konan.properties.charset}")
	private String charset;
	
	@Value("${konan.properties.ksfUrl}")
	private String ksfU;
	
	@Value("${konan.properties.moduleUrl}")
	private String moduleUrl;

	@Autowired
	private DCUtil dc;

	
	
	
	/**
	 * 연관검색어
	 */
	public String getRecommandKwd(int domainNo, int max_count, String kwd)
    {
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append(ksfU);
        ksfUrl.append("suggest");
        ksfUrl.append("?target=related");
        ksfUrl.append("&domain_no=" + domainNo); 
        ksfUrl.append("&term=" + dc.urlEncode(kwd, charset));
        ksfUrl.append("&max_count=" + max_count);             
      
        log.debug("recommand url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
        
    }
	
	
	
	
	/**
	 * 금칙어
	 */
	public Boolean getcensoredKwd(String kwd, int domainNo) {
		//String charset = konanPropertiesService.getString("charset"); 
		String ksf = ksfU;
		//String moduleUrl = konanPropertiesService.getString("moduleUrl");
		
		StringBuffer ksfUrl = new StringBuffer();

		/** 형태소 분석 코드 */
		String url = moduleUrl + "/mod/kma?text="+dc.urlEncode(kwd, charset)+"&format=json&charset=utf8&language=korean&option=";
		StringBuffer sb1 = dc.getUrlData(url, charset);

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(sb1.toString(), JsonObject.class);
        JsonArray sentsArray = jsonObject.getAsJsonArray("sents");
        JsonArray wordsArray = sentsArray.get(0).getAsJsonObject().getAsJsonArray("words");

        // "lemmas" 안의 문자열들 추출하여 String 배열에 담기
        String[] lemmasStrings = new String[wordsArray.size()];
        for (int i = 0; i < wordsArray.size(); i++) {
            JsonArray lemmasArray = wordsArray.get(i).getAsJsonObject().getAsJsonArray("nbest");
            JsonElement lemmasStringElement = lemmasArray.get(0).getAsJsonObject().getAsJsonArray("lemmas").get(0).getAsJsonObject().get("string");
            lemmasStrings[i] = lemmasStringElement.getAsString();
        }
        
		Boolean result = false;
        System.out.println("추출된 lemmas 문자열들: ");
        for (String str : lemmasStrings) {
			/* ksfUrl.append( "http://192.168.116.137:7614/ksf/api/"); */
        	ksfUrl.append(ksf);
            ksfUrl.append("censored");
    		ksfUrl.append("?domain_no=" + domainNo);
    		ksfUrl.append("&term=" + dc.urlEncode(str, charset));
    		log.debug("censored url : " + ksfUrl.toString()); 
    		StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
    		
    		String word = sb.toString();

    		word = word.replaceAll("[\"\\[\\]]", ""); // 쌍따옴표와 대괄호 제거
            word = word.replaceAll(",", " "); // 콤마를 띄어쓰기로 변경
            
            if(str.equals(word)) {
            	result = true;
            }
        }
        
		return result;
	}
	


    /** 
     * 검색어 자동완성 메소드 (getAutocomplete).
     * 
     * @param seed 키워드
     * @param maxResultCount 최대 결과 수
     * @param flag 결과 형식 플래그 (앞, 뒤 단어 일치 여부)
     * @param mode 첫단어, 끝단어
     * @param domainNo 모듈 도메인 번호
     * @return String
     */
    public String getAutocomplete(String seed, String mode, int maxResultCount, int domainNo)
    {
    	//String charset = konanPropertiesService.getString("charset"); 
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append(ksfU);
        ksfUrl.append("suggest");
        ksfUrl.append("?target=complete");
        ksfUrl.append("&term=" + dc.urlEncode(seed,charset) );
        ksfUrl.append("&domain_no=" + domainNo); 
        ksfUrl.append("&max_count=" + maxResultCount);             
      
        log.debug("suggest url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
        
    }
	
	
	/**
	 * 인기검색어를 조회하
	 * @param domainNo 도메인
	 * @param maxResult 검색결과 수
	 * @return
	 */
	public List<Map<String, String>> getPopularKwd(int domainNo, int maxResult) {
		StringBuffer ksfUrl = new StringBuffer();
		ksfUrl.append(ksfU);
		ksfUrl.append("rankings");
		ksfUrl.append("?domain_no=" + domainNo);
		ksfUrl.append("&max_count=" + maxResult);				 
		
		log.debug("rankings url : " + ksfUrl.toString());
		//String charset = konanPropertiesService.getString("charset");  
		StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);

		// 결과 파싱
		try{
			
			
          JSONParser parser = new JSONParser();
          Object obj = parser.parse(sb.toString());
          
          JSONArray arr = (JSONArray) obj;

			List<Map<String, String>> list = new ArrayList<> ();
			Map<String, String> map;
			JSONArray ppk;
			for( Object o : arr){
				map = new HashMap<>();				
				ppk =(JSONArray) o;
				
				map.put("ppk", ppk.get(0).toString());
				map.put("meta", ppk.get(1).toString());
				

				
				list.add(map);
				map = null;
			}
			
			return list;

		} catch (ParseException e){
			log.error("konan search engine search error...", e);
			return null;
		}
		
	}

	
    /**
     * 오타교정
     * @param term 키워드
     * @return String 
     */
    public String getSpellChek(String term)
    {
    	//String charset = konanPropertiesService.getString("charset");
        StringBuffer ksfUrl = new StringBuffer();
        ksfUrl.append(ksfU);
        ksfUrl.append("suggest");
        ksfUrl.append("?target=spell");
        ksfUrl.append("&term=" + dc.urlEncode(term, charset ) );    
      
        log.debug("spell url : " + ksfUrl.toString());
        
        StringBuffer sb = dc.getUrlData(ksfUrl.toString(), charset);
        return sb.toString();
    } 
	
	
}
