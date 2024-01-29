package com.pwc.pwcesg.frontoffice.intro.beans;

import com.pwc.pwcesg.backoffice.common.service.BoLoginService;
import com.pwc.pwcesg.frontoffice.common.service.FoCommonService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.pwc.pwcesg.config.SessionData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FrontofficeInterceptor implements HandlerInterceptor {

	private final BoLoginService boLoginService;

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {

		String[] sessionURI = {
			"mypage/mypageListView"    //마이페이지
			, "mypage/mypageUser"
			, "common/withdrawalView"        //회원탈퇴
			, "qna/qnaListView"            //온라인문의
		};

		HttpSession session = request.getSession();
		session.setAttribute("isBackOffice", false);
		SessionData sessionData = (SessionData) session.getAttribute("FoMbrInfo");

		String mbrGbCd = "99";
		if (sessionData == null) {
			for (String chkUri : sessionURI) {
				String rqtUri = request.getRequestURI();
				if (rqtUri.indexOf(chkUri) >= 0) {
					session.setAttribute("rqtUri", rqtUri);
					response.sendRedirect(request.getContextPath() + "/common/login");
					log.info("[preHandle] - URI: [{}]", request.getRequestURI());
					log.info("[SessionData] null");
				}
			}
		} else {
			log.info("[SessionData] MbrNm: {}", sessionData.getMbrNm());
			log.info("[SessionData] MbrId: {}", sessionData.getMbrId());
			mbrGbCd = sessionData.getMbrGbCd();
		}

		// 권한 체크
		if (request.getRequestURI().indexOf("/sems/") == -1) {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("accsUri", request.getRequestURI());
			paramMap.put("mbrGbCd", mbrGbCd);
			Map<String, Object> rightInfo = boLoginService.selectMyRights(paramMap);
			log.info("[rightInfo] {}", rightInfo);
			if (rightInfo != null) {
//			sessionData.setRights((String)rightInfo.get("rights"));
//			session.setAttribute("FoMbrInfo", sessionData);

				if (((String)rightInfo.get("rights")).indexOf("R") == -1) {
					response.sendRedirect(request.getContextPath() + "/common/noPermission");
					return false;
				}
			}
		}

		return true;
	}
}