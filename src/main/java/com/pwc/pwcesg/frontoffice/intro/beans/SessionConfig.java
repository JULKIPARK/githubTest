package com.pwc.pwcesg.frontoffice.intro.beans;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.frontoffice.intro.service.IntroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@WebListener
@RequiredArgsConstructor
public class SessionConfig implements HttpSessionListener{

	private IntroService introService;
    
    @Override
    public void sessionCreated(HttpSessionEvent hse) {
    	log.info("[HttpSessionEvent]{}", hse);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
		HttpSession session = hse.getSession();
		SessionData mbMbrInfo = (SessionData)session.getAttribute("mbMbrInfo");
		if(mbMbrInfo!=null) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("mbrId", mbMbrInfo.getMbrId());
			paramMap.put("sesnId", session.getId());
	        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()); 
	        introService = (IntroService) context.getBean("IntroService");
			introService.updateSesnAccsLog(paramMap);//세션접속로그 로그아웃 기록
		}
    }
}