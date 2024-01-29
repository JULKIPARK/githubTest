package com.pwc.pwcesg.frontoffice.search.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.pwc.pwcesg.frontoffice.search.common.DCUtil;
import com.pwc.pwcesg.frontoffice.search.controller.SearchController;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;

import lombok.extern.slf4j.Slf4j;

/**
 * Class Name : RestModule.java
 * Description : 검색엔진이용하여 검색결과 조회 모듈
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
@Component("restModule")
public class RestModule {

	@Autowired
	private DCUtil dc;

	@Value("${konan.properties.charset}")
	private String charset;
	
	@Value("${konan.properties.realLic}")
	private boolean realLic;

	
	//개발라이선스 표시 문자열
	private String txtWarning ="\\(WARNING: EVALUATION COPY\\[SEARCH\\]\\)";
	
	
	/**
	 * restFull API이용하여 검색엔진  리턴결과를 gson을통해 파싱하여 객체로 전달한다.
	 * @param restUrl 호출 URL
	 * @param restVO	입력vo
	 * @param selectField 조회필드명
	 * @return
	 */
	public boolean restSearch(String restUrl, RestResultVO restVO, String selectField) {
		//String charset = konanPropertiesService.getString("charset");
		
		StringBuffer sb = dc.getUrlData(restUrl, charset);

		// 결과 파싱
		try{
			Gson gson = new Gson();
			
			JsonObject jsonObject = gson.fromJson( sb.toString(), JsonObject.class);
			JsonObject rsObject = jsonObject.get("result").getAsJsonObject() ;
			
			//결과 set
			restVO.setStatus( jsonObject.get("status").getAsString() );
			restVO.setTotal(rsObject.get("total_count").getAsLong() );
			
			JsonArray arr = rsObject.get("rows").getAsJsonArray();
			
			//boolean realLic = konanPropertiesService.getBoolean("realLic");
			List<Map<String, String>> list = new ArrayList<> ();
			String[] fields = selectField.split(",");
			Map<String, String> map;
			JsonObject fieldobj;
			for(JsonElement element : arr){
				map = new HashMap<>();				
				fieldobj = (element.getAsJsonObject()).get("fields").getAsJsonObject();
				
				for(String field:fields){
					if( realLic) {
						map.put(field, fieldobj.get(field).getAsString());
					}else{					
						map.put(field, fieldobj.get(field).getAsString().replaceAll(txtWarning, ""));
					}
				}
				
				list.add(map);
				map = null;
			}
			
			restVO.setResult(list);

		} catch (JsonParseException e){
			log.error("konan search engine search error...", e);
			return false;
		}
		return true;

	}

	/**
	 * restFull API이용하여 검색엔진  리턴결과를 gson을통해 파싱하여 객체로 전달한다.
	 * @param restUrl 호출 URL
	 * @param restVO	입력vo
	 * @param selectField 조회필드명
	 * @return
	 */
	public boolean restSearchPost(String restUrl, String postParamData, RestResultVO restVO, String selectField) {
		//String charset = konanPropertiesService.getString("charset");
	
		StringBuffer sb = dc.getUrlDataPost(restUrl,postParamData, charset);
		
		// 결과 파싱
		try{
			Gson gson = new Gson();
			
			JsonObject jsonObject = gson.fromJson( sb.toString(), JsonObject.class);
			JsonObject rsObject = jsonObject.get("result").getAsJsonObject() ;
			
			//결과 set
			restVO.setStatus( jsonObject.get("status").getAsString() );
			restVO.setTotal(rsObject.get("total_count").getAsLong() );
			
			JsonArray arr = rsObject.get("rows").getAsJsonArray();

			//boolean realLic = konanPropertiesService.getBoolean("realLic");
			List<Map<String, String>> list = new ArrayList<> ();
			String[] fields = selectField.split(",");
			Map<String, String> map;
			JsonObject fieldobj;
			for(JsonElement element : arr){
				map = new HashMap<>();				
				fieldobj = (element.getAsJsonObject()).get("fields").getAsJsonObject();
				
				for(String field:fields){
					if( realLic) {
						map.put(field, fieldobj.get(field).getAsString());
					}else{					
						map.put(field, fieldobj.get(field).getAsString().replaceAll(txtWarning, ""));
					}
				}
				
				list.add(map);
				map = null;
			}
			
			restVO.setResult(list);

		} catch (JsonParseException e){
			log.error("konan search engine search error...", e);
			return false;
		}
		return true;

	}	
	
	/**
	 * restFull API total 파싱 JSON
	 */
	
	
	
	
	
	
	/**
	 * restFull API이용하여 검색엔진 리턴결과를JSONParser를 통해 파싱하여 객체로 전달한다.
	 * @param restUrl 호출 URL
	 * @param restVO	입력vo
	 * @param selectField 조회필드명
	 * @return
	 */
	public boolean restSearchJSONParser(String restUrl, RestResultVO restVO, String selectField) {
		//String charset = konanPropertiesService.getString("charset");
		StringBuffer sb = dc.getUrlData(restUrl, charset);
		
		// 결과 파싱
		try{

			// 파싱하는 부분
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(sb.toString());
			
			JSONObject jsonObj = (JSONObject) obj;
			restVO.setStatus((String)jsonObj.get("status"));
			
			JSONObject resultObj = (JSONObject) jsonObj.get("result");
			restVO.setTotal((long)resultObj.get("total_count"));
			
			JSONArray arr = (JSONArray) resultObj.get("rows");
			int arrCnt = arr.size();
			int fieldCnt = 0;
			//boolean realLic = konanPropertiesService.getBoolean("realLic");
			
			if(arr != null && arrCnt > 0) {
				String[] fields = selectField.split(",");
				Map<String, String> map;
				List<Map<String, String>> list = new ArrayList<> ();
						
				JSONObject result;
				JSONObject record;
				fieldCnt = fields.length;
				for(int i=0; i<arrCnt; i++) {
					map = new HashMap<String, String> ();
					
					result = (JSONObject) arr.get(i);
					record = (JSONObject) result.get("fields");
					
					for(int j=0; j<fieldCnt; j++) {
						if( !realLic) {
							map.put(fields[j], ((String)record.get(fields[j])).replaceAll(txtWarning, "") );	
						}else {
							map.put(fields[j], (String)record.get(fields[j]));	
						}
					}
					
					list.add(map);
					map = null;
				}
				
				restVO.setResult(list);
			}		
			
		} catch (Exception e){
			log.error("konan search engine search error...", e);
			return false;
		}
		return true;

	}
}
