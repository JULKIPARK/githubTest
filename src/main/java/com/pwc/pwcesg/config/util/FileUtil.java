package com.pwc.pwcesg.config.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FileUtil {
	
	/**
	* 파일 업로드
	* @param param: Map<String, Object>
						uploadPath: String			업로드 경로
	* 					uploadFiles: List<MultipartFile>
	* 					saveFileName: List<String>	파일명(첨부파일채번_첨부상세채번.확장자)
	* 													첨부파일채번	ATAC_FILE_UID
	* 													첨부상세채번	ATAC_DTL_UID
	* @return fileUploadList: List<Map<String, Object>>
						fileUploadParam.put("filePath", sysFilePath);//파일경로
						fileUploadParam.put("fileName", fileName);//파일명
						fileUploadParam.put("fileMg", fileSize);//파일Size
	*/
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> fileUpload(Map<String, Object> param) {
		List<Map<String, Object>> fileUploadList = new ArrayList<>();
		// 첨부파일 업로드 경로
		String sysFilePath = (String)param.get("uploadPath")+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/"));//업로드 경로
		List<String> fileNames = (List<String>)param.get("saveFileName");//파일명
		List<MultipartFile> mfList = (List<MultipartFile>)param.get("uploadFiles");// 업로드 할 파일 리스트 얻어오기

		// realPath에 해당되는 파일의 File 객체를 생성
		File fileFolder = new File(sysFilePath);
		//업로드 폴더 확인
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}

		// 첨부파일 업로드
		for(int i=0; i < mfList.size(); i++) {
			//파일정보 세팅
			MultipartFile multipartFile = mfList.get(i);//파일
			String fileName = sysFilePath+fileNames.get(i);//파일명
			
			if(!multipartFile.isEmpty()) {
				long fileSize = multipartFile.getSize();
				Path savePath = Paths.get(fileName);

				try {
					FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(savePath.toFile()));
					
					Map<String, Object> fileUploadParam = new HashMap<>();
					fileUploadParam.put("filePath", sysFilePath);//파일경로
					fileUploadParam.put("fileName", fileName);//파일명
					fileUploadParam.put("fileMg", fileSize);//파일Size
					fileUploadList.add(fileUploadParam);
				} catch (IOException e) {
					for(int j=0; j < fileUploadList.size(); j++) {
						File deleteFile = new File(sysFilePath, fileName);
						log.info("deleteFile={}"+deleteFile);
						// 오류 발생시 업로드된 파일 삭제
						if(deleteFile.exists()) {
							boolean result = deleteFile.delete();
							log.info("deleteFile{}", result);
						}
					}
				}
			}
		}
		
		return fileUploadList;
	}

	/**
	* 파일 목록 삭제
	* @param param: List<String>: 파일명 목록(파일명(/upload/2023/08/1_1.png))
	* @return String
	*/
	public void deleteFileList(List<String> fileList) {

		for(String fileNm: fileList) {

			// 업로드된 파일 삭제
			File deleteFile = new File(fileNm);
			if(deleteFile.exists()) {
				boolean result = deleteFile.delete();
				log.info("deleteFile{}", result);
			}
		}
	}

	/**
	* 파일 삭제
	* @param param: String: 파일명(/upload/2023/08/1_1.png)
	* @return String
	*/
	public void deleteFile(String fileNm) {
		// 업로드된 파일 삭제
		File deleteFile = new File(fileNm);
		if(deleteFile.exists()) {
			boolean result = deleteFile.delete();
			log.info("deleteFile{}", result);
		}
	}
}