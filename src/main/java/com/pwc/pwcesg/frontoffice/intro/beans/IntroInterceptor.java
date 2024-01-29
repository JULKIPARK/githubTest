package com.pwc.pwcesg.frontoffice.intro.beans;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.web.servlet.HandlerInterceptor;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.frontoffice.intro.service.IntroService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntroInterceptor implements HandlerInterceptor{
	
	private final IntroService introService;
    
	@Override
	public boolean preHandle(
				HttpServletRequest request,
				HttpServletResponse response,
				Object handler
		) throws Exception {
		
		Map<String, String> paramMap = new HashedMap<>();
		paramMap.put("accsUri", request.getRequestURI());//접속URI
		paramMap.put("deviceTpCd", isDevice(request));//디바이스유형코드()
		paramMap.put("deviceIp", request.getRemoteAddr());//디바이스아이피
		paramMap.put("usrAgnt", request.getHeader("User-Agent"));//사용자에이젼트
		
		HttpSession session = request.getSession();
		SessionData mbMbrInfo = (SessionData)session.getAttribute("mbMbrInfo");
		if (request.getRequestURI().indexOf("/sems") >= 0) {
			mbMbrInfo = (SessionData)session.getAttribute("BoMbrInfo");
		}
		String MbrId = "";
		if(mbMbrInfo!=null) {
			MbrId = mbMbrInfo.getMbrId();
		}
		paramMap.put("mbrId", MbrId);//회원아이디
		paramMap.put("sesnId", request.getSession().getId());//세션아이디
		paramMap.put("inflwChnlCd", request.getHeader("Referer"));//유입채널코드
		
		introService.insertAccsLog(paramMap);

		return true;
	}
	
	/**
	 * 디바이스유형코드(10:PC, 20:모바일, 30:타블렛) 조회
	 * @param req
	 * @return
	 */
	public static String isDevice(HttpServletRequest req) {
		String IS_MOBILE = "MOBILE";
		String IS_PHONE = "PHONE";
		
	    String userAgent = req.getHeader("User-Agent").toUpperCase();
		
	    if(userAgent.indexOf(IS_MOBILE) > -1) {
	        if(userAgent.indexOf(IS_PHONE) == -1) {
	        	return "20";//모바일
	        }
			else {
			    return "30";//태블릿
			}
	    } else {
	    	return "10";//PC
	    }
	}
}