package com.pwc.pwcesg.common.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pwc.pwcesg.common.mapper.CommonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

	@Value("${fileInfo.contents.uploadPath}")
	String uploadPath;

	private final CommonMapper commonMapper;

	/**
	 * 공통 코드 목록 조회
	 *
	 * @param grpCd
	 * @return
	 */
	public List<Map<String, Object>> selectCodeList(String grpCd) {
		return commonMapper.selectCodeList(grpCd);
	}











	/**
	 * 하위 공통 코드 목록 조회
	 *
	 * @param grpCd
	 * @return
	 */
	public List<Map<String, Object>> selectUnderCodeList(Map<String, Object> paramMap) {
		log.info("[paramMap]", paramMap);
		return commonMapper.selectUnderCodeList(paramMap);
	}

	/**
	 * 파일 목록 조회
	 *
	 * @param param Map<String, String>
	 * @return List<Map < String, Object>>
	 */
	public List<Map<String, Object>> selectFileList(Map<String, Object> param) {
		return commonMapper.selectFileList(param);
	}

	/**
	 * 파일 목록 삭제
	 *
	 * @param param: Map<String, Object>
	 * @return String
	 */
	public String deleteFileList(Map<String, Object> param) {

		//삭제 파일정보 세팅
		List<Map<String, Object>> delfileList = new ArrayList<Map<String, Object>>();

		// 첨부파일 삭제 처리 결과 리턴(default 설정 : fileId, 파일이 없는 경우 빈값, 오류인 경우 error)
		String result = String.valueOf(param.get("fileId"));

		List<Map<String, Object>> deleteFileList = commonMapper.selectFileList(param);

		if (!deleteFileList.isEmpty()) {

			// 서버 Root 경로
			//String rootPath = System.getProperty("user.dir");
			String rootPath = "";
			// 첨부파일 업로드 경로 DB 정보
			String sysFilePath = "";
			// 첨부파일 업로드 서버 최종 경로
			String realPath = "";
			String sysFileNm = "";
			String fileExt = "";
			Integer iRtn = 0;

			for (int i = 0; i < delfileList.size(); i++) {

				Map<String, Object> delFileParam = new HashMap<String, Object>();
				delFileParam = delfileList.get(i);

				sysFilePath = String.valueOf(delFileParam.get("filePath"));
				realPath = rootPath + sysFilePath;
				sysFileNm = String.valueOf(delFileParam.get("fileNm"));
				fileExt = String.valueOf(delFileParam.get("fileXtnsNm"));

				//파일 상세정보 DB 삭제
				iRtn = commonMapper.deleteFileDetail(delFileParam);

				if (iRtn > 0) {
					// 업로드된 파일 삭제
					File deleteFile = new File(realPath, sysFileNm + "." + fileExt);
					if (deleteFile.exists()) {
						deleteFile.delete();
					}
				}
			}

			//파일 건수 체크시 상세번호 제외
			param.remove("fileNo");

			//파일 상세정보 건수 체크
			List<Map<String, Object>> filetList = new ArrayList<Map<String, Object>>();
			filetList = commonMapper.selectFileList(param);

			if (filetList.isEmpty()) {
				result = "";
				iRtn = commonMapper.deleteFileGroup(param);
				log.debug("첨부파일 그룹 삭제 성공");
			}

		} else {
			result = "";
		}

		log.debug("첨부파일 삭제 결과 : " + result);
		return result;
	}

	/**
	 * 파일 다운로드
	 *
	 * @param param:   Map<String, Object>
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	public void fileDownload(Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> fileDataList = commonMapper.selectFileList(param);
		File file = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream out = null;
		ServletOutputStream so = null;
		BufferedOutputStream bos = null;
		FileInputStream fin = null;
		FileOutputStream fout = null;

		//압축 파일 임시 다운로드 경로
		String tmpFilePath = "";
		// 서버 Root 경로
		//String rootPath = System.getProperty("user.dir");
		String rootPath = "";

		try {
			if (!fileDataList.isEmpty()) {
				String userAgent = request.getHeader("User-Agent");
				String filePath = "";
				String fileName = "";
				String fileExt = "";
				String usrFileNm = "";
				String fileFullPath = "";

				//단일 첨부파일 다운로드
				if (fileDataList.size() == 1) {
					Map<String, Object> fileData = new HashMap<String, Object>();
					fileData = fileDataList.get(0);

					filePath = String.valueOf(fileData.get("filePath"));
					fileName = String.valueOf(fileData.get("fileNm"));
					fileExt = String.valueOf(fileData.get("fileXtnsNm"));
					usrFileNm = String.valueOf(fileData.get("ortxFlnm")) + "." + fileExt;
					fileFullPath = rootPath + filePath + "/" + fileName + "." + fileExt;

					file = new File(fileFullPath.replaceAll("/", Matcher.quoteReplacement(File.separator)));
					//
					//파일이 없으면 예외 발생
					if (!file.isFile()) {
						throw new IOException();
					}
					// 특정 브라우저에서 한글 깨짐 방지
					if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
						usrFileNm = URLEncoder.encode(usrFileNm, "UTF-8").replaceAll("\\+", "%20");
						response.setHeader("Content-Disposition", "attachment; filename=" + usrFileNm + ";");
					} else {
						usrFileNm = new String(usrFileNm.getBytes("UTF-8"), "ISO-8859-1");
						response.setHeader("Content-Disposition", "attachment; filename=\"" + usrFileNm + "\"");
					}

					response.setContentType("application/octet-stream");
					response.setHeader("Content-Transfer-Encoding", "binary");
					response.setHeader("Content-Length", "" + (file.length()));
					response.setHeader("Pragma", "no-cache;");
					response.setHeader("Expires", "-1;");
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					out = response.getOutputStream();

					int input = 0;
					byte[] data = new byte[1024];

					while ((input = fis.read(data)) != -1) {
						out.write(data, 0, input);
					}
				}
			}

		} catch (IOException e) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.println("<script type='text/javascript'>alert('파일 정보가 없습니다.'); self.close();</script>");
				pw.flush();
			} catch (IOException iie) {
				log.error("파일 다운로드 오류 발생");
			}
		} finally {
			if ("".equals(tmpFilePath)) {
				// tmpFilePath에 해당되는 파일의 File 객체를 생성
				File fileFolder = new File(tmpFilePath.replaceAll("/", Matcher.quoteReplacement(File.separator)));

				//임시 폴더 및 파일 삭제
				if (fileFolder.exists()) {

					File[] folder_list = fileFolder.listFiles(); //파일리스트 얻어오기
					for (int j = 0; j < folder_list.length; j++) {
						folder_list[j].delete(); //파일 삭제
						log.info("파일이 삭제되었습니다. : " + folder_list[j].delete());
					}

					if (folder_list.length == 0 && fileFolder.isDirectory()) {
						fileFolder.delete(); //대상폴더 삭제
						log.info("폴더가 삭제되었습니다.");
					}
				}
			}

			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (so != null) {
				try {
					so.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					log.debug("파일 다운로드 오류 발생");
				}
			}
		}
	}

	//	================================================================================================
	//	DELETED ========================================================================================
	//	================================================================================================
}