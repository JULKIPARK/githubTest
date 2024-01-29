package com.pwc.pwcesg.frontoffice.diagnosis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.frontoffice.diagnosis.service.DiagnosisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.pwc.pwcesg.common.service.CommonService;
import com.pwc.pwcesg.config.SessionData;
import com.pwc.pwcesg.config.util.SHA256Util;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RequestMapping(value = "/frontoffice")
public class DiagnosisController {

    @Value("${memberInfo.saltKey}")
    private String saltKey; // 암호화된 비밀번호

    /**
     * 공통 서비스
     */
    private final CommonService commonService;

    private final DiagnosisService diagnosisService;

    /**
     * 자가진단 화면
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/selfDiagnosisView")
	public ModelAndView selfDiagnosisView(){
		ModelAndView mav = new ModelAndView("frontoffice/diagnosis/selfDiagnosisView");
		mav.addObject("code1", commonService.selectCodeList("INDSTY_LCLS_CD"));

		return mav;
	}
    /**
     * 자가진단 결과
     * @param paramMap
     * @return ModelAndView
     */
	@RequestMapping(value = "/diagnosis/selfDiagnosisResult")
	public ModelAndView selfDiagnosisResult(HttpServletRequest request,@RequestParam(value="actTgtResultUid" ) String actTgtResultUid){

		ModelAndView mav = new ModelAndView("frontoffice/diagnosis/selfDiagnosisResult");

		mav.addObject("actTgtResultUid", actTgtResultUid);
		return mav;
	}
	@RequestMapping(value = "/diagnosis/selfDiagnosisResultJson")
	public ModelAndView selfDiagnosisResultJson(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap){

		ModelAndView mav = new ModelAndView("jsonView");

		//자가진단정보
        Map<String, Object> info = diagnosisService.selectDiagnosisResult(paramMap);
		mav.addObject("info", info);

		//자가진단정보 답변
        List<Map<String, Object>> list = diagnosisService.selectDiagnosisResultAw(paramMap);
		mav.addObject("list", list);

		//자가진단정보 합산
		paramMap.put("reultType","ETC1");
        List<Map<String, Object>> sum2 = diagnosisService.selectDiagnosisResultSum(paramMap);
		mav.addObject("sum1", sum2);
		paramMap.put("reultType","");
		paramMap.put("reultType","ETC2");
        List<Map<String, Object>> sum1 = diagnosisService.selectDiagnosisResultSum(paramMap);
		mav.addObject("sum2", sum1);

        List<Map<String, Object>> sub = diagnosisService.selectDiagnosisResultSubSum(paramMap);
		mav.addObject("sub", sub);

		//자가진단정보 chart
        List<Map<String, Object>> chart1 = diagnosisService.selectDiagnosisResultChart1(paramMap);
		mav.addObject("chart1", chart1);
        List<Map<String, Object>> chart2 = diagnosisService.selectDiagnosisResultChart2(paramMap);
		mav.addObject("chart2", chart2);

		return mav;
	}
	@RequestMapping(value = "/diagnosis/selfDiagnosisSave")
	public ModelAndView selfDiagnosisSave(
			HttpServletRequest request
			, @RequestParam Map<String, Object> paramMap
		){

		ModelAndView mav = new ModelAndView("jsonView");
		String msg = "";

        HttpSession session = request.getSession(false);
		SessionData sessionData = (SessionData)session.getAttribute("FoMbrInfo");

		//세션체크
		if(sessionData == null) {
			msg="로그인후 이용해주세요.";
			mav.addObject("msg", msg);

	        return mav;
		}

        paramMap.put("mbrUid", sessionData.getMbrUid());//회원채번
		paramMap.put("mbrId", sessionData.getMbrId());//회원아이디

		List<Map<String, Object>> gCd = commonService.selectCodeList("CSRD_ACT");

		diagnosisService.insertSave(paramMap);

		List<Map<String, Object>> paramMapList = new ArrayList<Map<String, Object>>();
		int inAw = 1;
		for(int inI = 0 ; inI < gCd.size() ; inI++)
		{
			String Cd = gCd.get(inI).get("cd").toString();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("actTgtResultAwCd",Cd);
			param.put("actTgtResultUid",paramMap.get("actTgtResultUid"));

			System.out.println(">>>>>>>>>>>>>>" + Cd);
			System.out.println(">>>>>>>>>>>>>>" + paramMap.get(Cd));



			String[] awOption = request.getParameterValues(Cd +"o");
			if(awOption!=null) {
				param.put("awOption", String.join(",", awOption));
			}

			if(paramMap.get(Cd) != null)
			{
				param.put("awVal", paramMap.get(Cd));
		        String awVal = paramMap.get(Cd).toString();

		        String[] arrayParam = awVal.split(",");

		        float  val = 0;
		        if(arrayParam!=null) {
			        for (int i = 0; i < arrayParam.length; i++) {
			            val += Float.parseFloat(arrayParam[i]);
			            System.out.println(">>>>>>>>>>>>>>" + Cd + ">>>>>>>>" + arrayParam[i]);
			        }
		        }
		        if(val >= 0.9)
		        	val = 1;

				param.put("awSum",val);
			}
			else
			{
				param.put("awSum",-1);
			}


			if(param.get("awOption") == null)
				param.put("awOption","");


			paramMapList.add(param);
			inAw++;
		}

		diagnosisService.insertAwSave(paramMapList);


		msg="success";
		mav.addObject("msg", msg);
		mav.addObject("actTgtResultUid", paramMap.get("actTgtResultUid"));

        return mav;

	}
}