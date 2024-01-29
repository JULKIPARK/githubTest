package com.pwc.pwcesg.backoffice.common.controller;

import com.pwc.pwcesg.backoffice.common.service.BoLoginService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.SHA256Util;
import com.pwc.pwcesg.config.util.StringUtil;
import com.pwc.pwcesg.frontoffice.intro.service.IntroService;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/sems")
public class BoLoginController {

    @Value("${memberInfo.saltKey}")
    private String saltKey; // 암호화된 비밀번호

    private final BoLoginService boLoginService;
    private final IntroService introService;

    /**
     * 로그인 화면
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/common/login")
    public ModelAndView login() {
        return new ModelAndView("backoffice/common/login");
    }

    /**
     * common
     * 로그인 처리
     *
     * @param paramMap
     * @return ModelAndView
     */
    @RequestMapping(value = "/common/signUp")
    public ModelAndView signUp(
        HttpServletRequest request
        , @RequestParam Map<String, Object> paramMap
    ) {
        ModelAndView mav = new ModelAndView("jsonView");

        String mbrId = String.valueOf(paramMap.get("mbrId"));
        String mbrPwd = String.valueOf(paramMap.get("mbrPwd"));

        if (StringUtil.isNullOrBlank(mbrId) || StringUtil.isNullOrBlank(mbrPwd)) {
            log.info("로그인 필수 입력 오류");
            mav.addObject("msg", "아이디 또는 비밀번호를 입력해주세요.");
            return mav;
        }

        //사용자 인증코드 조회 및 암호화 PW 세팅
        String pwEncrypt = "";
        pwEncrypt = SHA256Util.getEncrypt(mbrPwd, saltKey); //입력된 비밀번호+salt 암호된 값
        paramMap.put("pwEncrypt", pwEncrypt);

        System.out.println("[pwEncrypt]" + pwEncrypt);

        // 사용자 정보 조회
        SessionData sessionData = boLoginService.selectMbMbrInfo(paramMap);
        if (sessionData == null) {
            String msg = "아이디 또는 비밀번호가 올바르지않습니다.";

            // 실패카운트 갱신
            int failCnt = boLoginService.updatePwdFailCnt(paramMap);
            log.info("failCnt = {}", failCnt);
            if (failCnt > 0 && failCnt < 5) {
                msg = "아이디 또는 비밀번호가 올바르지않습니다.\n(5회 오류 입력 시, 임시 비밀번호로 변경 처리됩니다.)";
            }
            if (failCnt >= 5) {
                msg = "비밀번호 5회 오류로 인해, 임시 비밀번호로 변경 처리하였습니다.\n비밀번호 찾기를 이용하여 비밀번호 변경 후 이용해주시기 바랍니다.";
                if (failCnt == 5) {
                    boLoginService.newPwd(paramMap);
                }
            }

            mav.addObject("msg", msg);
            return mav;
        }


        // 비밀번호 실패 카운트 초기화
        boLoginService.updatePwdFailCntReset(paramMap);

        HttpSession session = request.getSession();
        session.setAttribute("BoMbrInfo", sessionData);

        paramMap.put("mbrId", mbrId);
        paramMap.put("sesnId", session.getId());
        introService.insertSesnAccsLog(paramMap);//세션접속로그 로그인 기록

        mav.addObject("msg", "success");

        return mav;
    }

    /**
     * 로그아웃 처리
     *
     * @param request HttpServletRequest
     * @return ModelAndView
     */
    @RequestMapping("/common/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/sems";
    }

    @RequestMapping("/image")
    public void getImage(@RequestParam("imageUrl") String imageUrl, HttpServletResponse response) throws Exception {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(value = "/common/noPermission")
    public ModelAndView noPermission() {
        return new ModelAndView("backoffice/common/noPermission");
    }


    /**
     * 아이디 · 비밀번호 찾기 완료 화면
     *
     * @return ModelAndView
     */
//    @RequestMapping(value = "/common/findMe")
//    public ModelAndView findMe() {
//        return new ModelAndView("backoffice/common/findMe");
//    }

    /**
     * 사용자정보 조회
     *
     * @param paramMap
     * @return ModelAndView
     */
//    @RequestMapping(value = "/common/selectMyInfo")
//    public ModelAndView selectMyInfo(@RequestParam Map<String, Object> paramMap) {
//
//        ModelAndView mav = new ModelAndView("jsonView");
//        String msg = "";
//
//        SessionData sessionData = boLoginService.selectMyInfo(paramMap);// 사용자 정보 조회
//
//        if (sessionData != null) {
//            sessionData = boLoginService.newPwd(sessionData);//임시비밀번호 발급
//            msg = "success";
//            mav.addObject("msg", msg);
//            mav.addObject("myInfo", sessionData);
//        }
//
//        return mav;
//    }

    /**
     * 본인확인완료 화면
     *
     * @param paramMap
     * @return ModelAndView
     */
//    @RequestMapping(value = "/common/certificate")
//    public ModelAndView certificate(@RequestParam Map<String, Object> paramMap) {
//
//        ModelAndView mav = new ModelAndView("backoffice/common/certificate");
//        mav.addObject("myInfo", paramMap);
//
//        return mav;
//    }


}
