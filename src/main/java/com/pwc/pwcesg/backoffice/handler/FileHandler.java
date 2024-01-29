package com.pwc.pwcesg.backoffice.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class FileHandler {

	@Value("${fileInfo.contents.uploadPath}")
	String uploadPath;
	@Value("${fileInfo.contents.uploadPath2}")
	String uploadPath2;

	/**
	 * 첨부 파일 저장
	 *
	 * @param multipartFile
	 * @param atacFileUid
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> parseFileInfo(MultipartFile multipartFile, String atacFileUid, String gbn) {
		// 반환할 파일 리스트
		Map<String, Object> fileMap = new HashMap<String, Object>();

		// 전달되어 온 파일이 존재할 경우
		if (!multipartFile.isEmpty()) {
			// 파일명을 업로드 한 날짜로 변환하여 저장
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String current_date = now.format(dateTimeFormatter);

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File(uploadPath).getAbsolutePath() + File.separator + File.separator;
			log.info("absolutePath=>{}", absolutePath);

			// 파일을 저장할 세부 경로 지정
			String path = uploadPath + gbn + "/";
			String path2 = uploadPath2 + gbn + "/";
			File file = new File(path);

			log.info("path=>{}", path);
			log.info("path2=>{}", path2);

			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful) {
					log.error("file: was not successful");
				}
			}

			// 파일의 확장자 추출
			String originalFileExtension;
			String contentType = multipartFile.getContentType();
			String atacTpCd;

			// 확장자
			String fileExt = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()).toLowerCase();

			// 컨텐츠타입에 따른 설정값 할당
			if (ObjectUtils.isEmpty(contentType)) {
				return null;
			} else {    // 확장자가 jpeg, png인 파일들만 받아서 처리
				if (contentType.contains("image/jpeg")) {
					originalFileExtension = ".jpg";
					atacTpCd = "30";
					if (gbn.equals("thumbnail")) {
						atacTpCd = "40";
					}
				} else {
					if (contentType.contains("image/png")) {
						originalFileExtension = ".png";
						atacTpCd = "30";
						if (gbn.equals("thumbnail")) {
							atacTpCd = "40";
						}
					} else {
						if (contentType.contains("application/pdf") || contentType.contains("application/epub+zip") || contentType.contains("application/zip")) {
							originalFileExtension = ".pdf";
							if (contentType.contains("application/epub+zip")) {
								originalFileExtension = ".epub";
							}
							if (contentType.contains("application/zip")) {
								originalFileExtension = ".zip";
							}
							atacTpCd = "10";
						} else {
							if (contentType.contains("video/mp4")) {
								originalFileExtension = ".mp4";
								atacTpCd = "20";
							} else {    // 다른 확장자일 경우 처리 x
								return null;
							}
						}
					}
				}
			}

			// 파일명 중복 피하고자 나노초까지 얻어와 지정
			String nanoTime = System.nanoTime() + "";
			String newFileName = nanoTime + "-" + multipartFile.getOriginalFilename();

			fileMap.put("atacFileUid", atacFileUid);
			fileMap.put("atacDtlUid", "");
			fileMap.put("atacTpCd", atacTpCd);
			fileMap.put("filePrcStCd", "30");
			fileMap.put("dpYn", "Y");
			fileMap.put("useYn", "Y");
			fileMap.put("orgnlFileNm", multipartFile.getOriginalFilename());
			fileMap.put("orgnlExtn", fileExt);
			fileMap.put("orgnlFileSz", multipartFile.getSize());
			fileMap.put("orgnlFileFpath", path + multipartFile.getOriginalFilename());
			fileMap.put("cnvnFileNm", newFileName);
			fileMap.put("cnvnExtn", fileExt);
			fileMap.put("cnvnFileSz", multipartFile.getSize());
			fileMap.put("cnvnFileFpath", path2 + newFileName);
			fileMap.put("tumnlFpath", "");
			if (originalFileExtension.equals(".pdf")) {
				fileMap.put("linkUrl", "https://samilesg.blob.core.windows.net" + uploadPath2 + "docs/" + newFileName);
			}
			if (originalFileExtension.equals(".epub") || originalFileExtension.equals(".zip")) {
				fileMap.put("linkUrl", "https://samilesg.blob.core.windows.net" + uploadPath2 + "epub/" + nanoTime);
			}
			if (originalFileExtension.equals(".mp4")) {
				fileMap.put("linkUrl", "https://samilesg.blob.core.windows.net" + uploadPath2 + "movie/" + newFileName);
			}

			// 업로드 한 파일 데이터를 지정한 파일에 저장
			file = new File(path + newFileName);
			try {
				multipartFile.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 파일 권한 설정(쓰기, 읽기)
			file.setWritable(true);
			file.setReadable(true);

			// epub일 경우 압축 해제
			if (originalFileExtension.equals(".epub") || originalFileExtension.equals(".zip")) {
				try {
					log.info("EPUB 압축해제 : " + path + newFileName);
					ZipFile zipFile = new ZipFile(path + newFileName);
					log.info("EPUB 압축해제(zipFile) : {}", zipFile);
					Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
					while (entries.hasMoreElements()) {
						ZipArchiveEntry entry = entries.nextElement();
						log.info("EPUB 압축해제2 : " + path + nanoTime);
						File entryDestination = new File(path + nanoTime, entry.getName());
						if (entry.isDirectory()) {
							entryDestination.mkdirs();
						} else {
							entryDestination.getParentFile().mkdirs();
							InputStream in = zipFile.getInputStream(entry);
							OutputStream out = new FileOutputStream(entryDestination);
							IOUtils.copy(in, out);
							IOUtils.closeQuietly(in);
							IOUtils.closeQuietly(out);
						}
					}
				} catch (Exception e) {
				}
			}
		}

		return fileMap;
	}
	public Map<String, Object> parseFileInfo2(MultipartFile multipartFile, String atacFileUid, String gbn) {
		// 반환할 파일 리스트
		Map<String, Object> fileMap = new HashMap<String, Object>();

		// 전달되어 온 파일이 존재할 경우
		if (!multipartFile.isEmpty()) {
			// 파일명을 업로드 한 날짜로 변환하여 저장
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String current_date = now.format(dateTimeFormatter);

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File(uploadPath).getAbsolutePath() + File.separator + File.separator;
			log.info("absolutePath=>{}", absolutePath);

			// 파일을 저장할 세부 경로 지정
			String path = uploadPath + gbn + "/";
			String path2 = uploadPath2 + gbn + "/";
			File file = new File(path);

			log.info("path=>{}", path);
			log.info("path2=>{}", path2);

			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful) {
					log.error("file: was not successful");
				}
			}

			// 파일의 확장자 추출
			String originalFileExtension;
			String contentType = multipartFile.getContentType();
			String atacTpCd;

			// 확장자
			String fileExt = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()).toLowerCase();

			// 컨텐츠타입에 따른 설정값 할당
//			if (ObjectUtils.isEmpty(contentType)) {
//				return null;
//			} else {    // 확장자가 jpeg, png인 파일들만 받아서 처리
//				if (contentType.contains("image/jpeg")) {
//					originalFileExtension = ".jpg";
//					atacTpCd = "30";
//					if (gbn.equals("thumbnail")) {
//						atacTpCd = "40";
//					}
//				} else {
//					if (contentType.contains("image/png")) {
//						originalFileExtension = ".png";
//						atacTpCd = "30";
//						if (gbn.equals("thumbnail")) {
//							atacTpCd = "40";
//						}
//					} else {
//						if (contentType.contains("application/pdf") || contentType.contains("application/epub+zip") || contentType.contains("application/zip")) {
//							originalFileExtension = ".pdf";
//							if (contentType.contains("application/epub+zip")) {
//								originalFileExtension = ".epub";
//							}
//							if (contentType.contains("application/zip")) {
//								originalFileExtension = ".zip";
//							}
//							atacTpCd = "10";
//						} else {
//							if (contentType.contains("video/mp4")) {
//								originalFileExtension = ".mp4";
//								atacTpCd = "20";
//							} else {    // 다른 확장자일 경우 처리 x
//								return null;
//							}
//						}
//					}
//				}
//			}

			// 파일명 중복 피하고자 나노초까지 얻어와 지정
			String nanoTime = System.nanoTime() + "";
			String newFileName = nanoTime + "-" + multipartFile.getOriginalFilename();

			fileMap.put("linkUrl", "https://samilesg.blob.core.windows.net" + uploadPath2 + gbn + "/" + newFileName);

			// 업로드 한 파일 데이터를 지정한 파일에 저장
			file = new File(path + newFileName);
			try {
				multipartFile.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 파일 권한 설정(쓰기, 읽기)
			file.setWritable(true);
			file.setReadable(true);
		}

		return fileMap;
	}
	public Map<String, Object> parseEpubPdfFileInfo(MultipartFile multipartFile, String atacFileUid) {
		// 반환할 파일 리스트
		Map<String, Object> fileMap = new HashMap<String, Object>();

		// 전달되어 온 파일이 존재할 경우
		if (!multipartFile.isEmpty()) {
			// 파일명을 업로드 한 날짜로 변환하여 저장
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String current_date = now.format(dateTimeFormatter);

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File(uploadPath).getAbsolutePath() + File.separator + File.separator;
			log.info("absolutePath=>{}", absolutePath);

			// 파일을 저장할 세부 경로 지정
			String path = uploadPath + "epub/";
			String path2 = uploadPath2 + "epub/";
			File file = new File(path);

			log.info("path=>{}", path);
			log.info("path2=>{}", path2);

			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful) {
					log.error("file: was not successful");
				}
			}

			// 파일의 확장자 추출
			String originalFileExtension;
			String contentType = multipartFile.getContentType();
			String atacTpCd;

			// 확장자
			String fileExt = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()).toLowerCase();

			// 파일명 중복 피하고자 나노초까지 얻어와 지정
			String nanoTime = System.nanoTime() + "";
			String newFileName = nanoTime + "-" + multipartFile.getOriginalFilename();

			fileMap.put("linkUrl", "https://samilesg.blob.core.windows.net" + uploadPath2 + "epub/" + newFileName);
			fileMap.put("cnvnFileFpath", newFileName);

			// 업로드 한 파일 데이터를 지정한 파일에 저장
			file = new File(path + newFileName);
			try {
				multipartFile.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 파일 권한 설정(쓰기, 읽기)
			file.setWritable(true);
			file.setReadable(true);
		}

		return fileMap;
	}

	/**
	 * 파일 목록 저장
	 *
	 * @param multipartFiles
	 * @param atacFileUid
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseFileInfo(List<MultipartFile> multipartFiles, String atacFileUid, String gbn) {
		// 반환할 파일 리스트
		List<Map<String, Object>> fileList = new ArrayList<>();

		// 전달되어 온 파일이 존재할 경우
		if (!CollectionUtils.isEmpty(multipartFiles)) {
			// 파일명을 업로드 한 날짜로 변환하여 저장
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String current_date = now.format(dateTimeFormatter);

			// 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
			// 경로 구분자 File.separator 사용
			String absolutePath = new File(uploadPath).getAbsolutePath() + File.separator + File.separator;

			// 파일을 저장할 세부 경로 지정
			// String path = uploadPath + gbn + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/"));
			String path = uploadPath + gbn + "/";
			String path2 = uploadPath2 + gbn + "/";
			File file = new File(path);

			// 디렉터리가 존재하지 않을 경우
			if (!file.exists()) {
				boolean wasSuccessful = file.mkdirs();

				// 디렉터리 생성에 실패했을 경우
				if (!wasSuccessful) {
					log.error("file: was not successful");
				}
			}
			// 다중 파일 처리
			for (MultipartFile multipartFile : multipartFiles) {
				// 파일의 확장자 추출
				String originalFileExtension;
				String contentType = multipartFile.getContentType();
				String atacTpCd;

				// 확장자
				String fileExt = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()).toLowerCase();

				// 나중에 확장자 체크하자!
				// https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
				// 확장자명이 존재하지 않을 경우 처리 x
				if (ObjectUtils.isEmpty(contentType)) {
					break;
				} else {    // 확장자가 jpeg, png인 파일들만 받아서 처리
					if (contentType.contains("image/jpeg")) {
						originalFileExtension = ".jpg";
						atacTpCd = "30";
					} else {
						if (contentType.contains("image/png")) {
							originalFileExtension = ".png";
							atacTpCd = "30";
						} else {
							if (contentType.contains("application/pdf") || contentType.contains("application/epub+zip") || contentType.contains("application/zip")) {
								originalFileExtension = ".pdf";
								if (contentType.contains("application/epub+zip")) {
									originalFileExtension = ".epub";
								}
								if (contentType.contains("application/zip")) {
									originalFileExtension = ".zip";
								}
								atacTpCd = "10";
							} else {
								if (contentType.contains("video/mp4")) {
									originalFileExtension = ".mp4";
									atacTpCd = "20";
								} else {    // 다른 확장자일 경우 처리 x
									break;
								}
							}
						}
					}
				}

				// 파일명 중복 피하고자 나노초까지 얻어와 지정
				//				String newFileName = System.nanoTime() + originalFileExtension;
				String nanoTime = System.nanoTime() + "";
				String newFileName = nanoTime + "-" + multipartFile.getOriginalFilename();
				Map<String, Object> uploadData = new HashMap<String, Object>();

				uploadData.put("atacFileUid", atacFileUid);
				uploadData.put("atacDtlUid", "");
				uploadData.put("atacTpCd", atacTpCd);
				uploadData.put("filePrcStCd", "30");
				uploadData.put("dpYn", "N");
				uploadData.put("useYn", "Y");
				uploadData.put("orgnlFileNm", multipartFile.getOriginalFilename());
				uploadData.put("orgnlExtn", fileExt);
				uploadData.put("orgnlFileSz", multipartFile.getSize());
				uploadData.put("orgnlFileFpath", path + multipartFile.getOriginalFilename());
				uploadData.put("cnvnFileNm", newFileName);
				uploadData.put("cnvnExtn", fileExt);
				uploadData.put("cnvnFileSz", multipartFile.getSize());
				uploadData.put("cnvnFileFpath", path2 + newFileName);
				uploadData.put("tumnlFpath", "");
				if (originalFileExtension.equals(".pdf")) {
					uploadData.put("linkUrl", "https://samilesg.blob.core.windows.net/esg/docs/" + newFileName);
				}
				if (originalFileExtension.equals(".epub")) {
					uploadData.put("linkUrl", "https://samilesg.blob.core.windows.net/esg/epub/" + nanoTime);
				}

				// 생성 후 리스트에 추가
				fileList.add(uploadData);

				// 업로드 한 파일 데이터를 지정한 파일에 저장
				file = new File(path + newFileName);
				try {
					multipartFile.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 파일 권한 설정(쓰기, 읽기)
				file.setWritable(true);
				file.setReadable(true);

				// epub일 경우 압축 해제
				if (originalFileExtension.equals(".epub") || originalFileExtension.equals(".zip")) {
					try {
						log.info("EPUB 압축해제 : " + path + newFileName);
						ZipFile zipFile = new ZipFile(path + newFileName);
						log.info("EPUB 압축해제(zipFile) : {}", zipFile);
						Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
						while (entries.hasMoreElements()) {
							ZipArchiveEntry entry = entries.nextElement();
							log.info("EPUB 압축해제2 : " + path + nanoTime);
							File entryDestination = new File(path + nanoTime, entry.getName());
							if (entry.isDirectory()) {
								entryDestination.mkdirs();
							} else {
								entryDestination.getParentFile().mkdirs();
								InputStream in = zipFile.getInputStream(entry);
								OutputStream out = new FileOutputStream(entryDestination);
								IOUtils.copy(in, out);
								IOUtils.closeQuietly(in);
								IOUtils.closeQuietly(out);
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}

		return fileList;
	}

	/**
	 * 파일 삭제
	 *
	 * @param fileNm
	 */
	public void deleteFile(String fileNm) {
		String tarFn = uploadPath + fileNm.replace("/esg/", "");
		File file = new File(tarFn);
		if (file.exists()) {
			if (file.delete()) {
				log.info("[deleted file]{}", fileNm);
			} else {
				log.info("[delet fail]{}", fileNm);
			}
		} else {
			log.info("[file not found]{}", fileNm);
		}
	}

}
