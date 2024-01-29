package com.pwc.pwcesg.common.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.common.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommonController implements ErrorController {

	@Value("${fileInfo.editor.uploadPath}")
	String editorUploadPath;

	private final CommonService commonService;

	/**
	 * 공통 코드 목록 조회
	 *
	 * @param grpCd
	 * @return ModelAndView(JSON type - List < Map < String, Object > >)
	 */
	@RequestMapping("/common/selectCodeList")
	public ModelAndView selectCodeList(@RequestParam(required = true) String grpCd) {
		log.info("/common/selectCodeList Params: [grpCd]{}", grpCd);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", commonService.selectCodeList(grpCd));

		log.info("mav ModelAndView:{}", mav);

		return mav;
	}











	/**
	 * 오류를 처리합니다.
	 **/
	@RequestMapping("/error")
	public ModelAndView handleNoHandlerFoundException(HttpServletResponse response, HttpServletRequest request) {
		String rtnUrl = "";
		String status = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
		HttpSession session = request.getSession(false);
		boolean isBackOffice = false;
		if (session != null) {
			isBackOffice = (boolean) session.getAttribute("isBackOffice");
		}

		if (isBackOffice) {
			rtnUrl = "backoffice";
		} else {
			rtnUrl = "frontoffice";
		}

		if (status != null) {
			int statusCode = Integer.parseInt(status);

			if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.FORBIDDEN.value()) {
				rtnUrl += "/common/errors/" + statusCode;
			} else {
				rtnUrl += "/common/errors/500";
			}
		}

		log.info("[ERROR-Url]{}" + rtnUrl);

		return new ModelAndView(rtnUrl);
	}

	/**
	 * 하위 공통 코드 목록 조회
	 *
	 * @param Map<String, Object>
	 * @return ModelAndView(JSON type - List < Map < String, Object > >)
	 */
	@RequestMapping("/common/selectUnderCodeList")
	public ModelAndView selectUnderCodeList(@RequestParam Map<String, Object> paramMap) {
		log.info("/common/selectCodeList Params: [grpCd]{}", paramMap);

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("list", commonService.selectUnderCodeList(paramMap));

		log.info("mav ModelAndView:{}", mav);

		return mav;
	}

	/**
	 * 첨부파일 목록 조회
	 *
	 * @param Map<String, Object>
	 * @return ModelAndView(JSON type - List < Map < String, Object > >)
	 */
	@RequestMapping("/common/selectFileList")
	public ModelAndView selectFileList(@RequestParam Map<String, Object> param) {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Map<String, Object>> fileList = new ArrayList<>();

		fileList = commonService.selectFileList(param);
		mav.addObject("fileList", fileList);
		return mav;
	}

	/**
	 * 파일 다운로드
	 *
	 * @param Map<String, Object>
	 * @return ModelAndView(JSON type - List < Map < String, Object > >)
	 */
	@RequestMapping(value = "/common/fileDownload")
	public void fileDownload(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) {
		commonService.fileDownload(param, request, response);
	}

	@ResponseBody
	@RequestMapping("/common/fms/ckeditor5Upload.do")
	public void fileUpload(MultipartHttpServletRequest multiRequest, HttpServletRequest request, HttpServletResponse response) {
		try {
			final String real_save_path = editorUploadPath;

			// 폴더가 없을 경우 생성
			File saveFolder = new File(real_save_path);
			//업로드 폴더 확인
			if (!saveFolder.exists()) {
				saveFolder.mkdirs();
			}

			final Map<String, MultipartFile> files = multiRequest.getFileMap();
			MultipartFile fileload = (MultipartFile) files.get("upload");

			//filename 취득
			String fileName = fileload.getOriginalFilename();

			int index = fileName.lastIndexOf(".");
			String ext = fileName.substring(index + 1);
			Random ran = new Random(System.currentTimeMillis());
			fileName = System.currentTimeMillis() + "_" + (int) (ran.nextDouble() * 10000) + "." + ext;

			//폴더 경로 설정
			String newfilename = real_save_path + File.separator + fileName;
			fileload.transferTo(new File(newfilename));

			JSONObject outData = new JSONObject();
			outData.put("uploaded", true);
			outData.put("url", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + editorUploadPath + fileName);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(outData.toString());
		} catch (IOException e) {
			log.error("/common/fms/ckeditor5Upload.do: 파일 업로드 오류 발생");
		}
	}
    
    /*
    @RequestMapping("/common/fms/getImageForContents.do")
	public void getImageForContents(SessionVO sessionVO, ModelMap model, @RequestParam Map<String, Object> commandMap, HttpServletResponse response) throws Exception {
	    String fileNm = (String)commandMap.get("fileNm");
	    String fileStr = MainGlobals.FILE_STORE_PATH + "contents/";

	    File tmpDir = new File(fileStr);
	    if(!tmpDir.exists()) {
	        tmpDir.mkdirs();
	    }

	    FileInputStream fis = null;
	    BufferedInputStream in = null;
	    ByteArrayOutputStream bStream = null;

	    try {

	        fis = new FileInputStream(new File(fileStr, fileNm));
	        in = new BufferedInputStream(fis);
	        bStream = new ByteArrayOutputStream();

	        int imgByte;
	        while ((imgByte = in.read()) != -1) {
	            bStream.write(imgByte);
	        }

	        String type = "";
	        String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1).toLowerCase();

	        if ("jpg".equals(ext)) {
	            type = "image/jpeg";
	        } else {
	            type = "image/" + ext;
	        }

	        response.setHeader("Content-Type", type);
	        response.setContentLength(bStream.size());

	        bStream.writeTo(response.getOutputStream());

	        response.getOutputStream().flush();
	        response.getOutputStream().close();

	    } finally {
	        EgovResourceCloseHelper.close(bStream, in, fis);
	    }
	}
	*/

	//	================================================================================================
	//	DELETED ========================================================================================
	//	================================================================================================

}