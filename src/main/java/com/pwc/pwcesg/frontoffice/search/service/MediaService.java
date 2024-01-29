package com.pwc.pwcesg.frontoffice.search.service;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.frontoffice.search.dao.DocDAO;
import com.pwc.pwcesg.frontoffice.search.dao.MediaDAO;
import com.pwc.pwcesg.frontoffice.search.dao.NewsDAO;
import com.pwc.pwcesg.frontoffice.search.data.ParameterVO;
import com.pwc.pwcesg.frontoffice.search.data.RestResultVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Class Name : KsfService.java
 * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 서비스
 *
 * Modification Information
 *
 * 수정일                        수정자           수정내용
 * --------------------  -----------  ---------------------------------------
 * 2017년 12월  00일                       최초 작성
 *
 * @since 2017년
 * @version V1.0
 * @see (c) Copyright (C) by KONANTECH All right reserved
 */

/**
 * 게시판 검색하기 위한 서비스 인터페이스
 * 
 * @author seunghee.kim
 * @since 2016.06.30
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaService {
	
	private final MediaDAO mediaDAO;
	
	/**
	 * 게시판정보를 검색한다.
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public RestResultVO MediaSearch(ParameterVO paramVO) throws Exception {
		RestResultVO resultVO = mediaDAO.MediaSearch(paramVO);

		if (resultVO == null)
			throw processException("info.nodata.msg");
		
		return resultVO;
	}

	private Exception processException(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
