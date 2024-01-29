package com.pwc.pwcesg.backoffice.intro.beans;

import com.pwc.pwcesg.backoffice.common.service.BoLoginService;
import com.pwc.pwcesg.config.SessionData;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
public class BackofficeInterceptor implements HandlerInterceptor {

	private final BoLoginService boLoginService;

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {

		log.info("[preHandle] - URI: [{}]", request.getRequestURI());

		HttpSession session = request.getSession();
		session.setAttribute("isBackOffice", true);
		SessionData sessionData = (SessionData) session.getAttribute("BoMbrInfo");

		if (sessionData == null) {
			log.info("[SessionData] null");
			response.sendRedirect(request.getContextPath() + "/sems/common/login");
			return false;
		}

		// 권한 체크
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accsUri", request.getRequestURI());
		paramMap.put("mbrGbCd", sessionData.getMbrGbCd());
		log.info("mbrGbCd = {}", sessionData.getMbrGbCd());
		Map<String, Object> rightInfo = boLoginService.selectMyRights(paramMap);
		log.info("[rightInfo] {}", rightInfo);
		if (rightInfo != null) {
			sessionData.setRights((String)rightInfo.get("rights"));
			session.setAttribute("BoMbrInfo", sessionData);

			if (((String)rightInfo.get("rights")).indexOf("R") == -1) {
				response.sendRedirect(request.getContextPath() + "/sems/common/noPermission");
				return false;
			}
		}

		return true;
	}
}
