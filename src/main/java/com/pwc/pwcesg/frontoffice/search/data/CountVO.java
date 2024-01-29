package com.pwc.pwcesg.frontoffice.search.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Class Name : SearchVO.java
 * Description : 검색엔진 통해 검색하기위한 파라미터 VO
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
@Getter
@Setter
public class CountVO {
	public int totalCount; //전체 값
	public int webCount;
	public int docCount;
	public int newsCount;		
	public int mediaCount;
	
	public String[] recommandResult;

	public int guideCount;
	public int legalCount;
	public int reportCount;
	public int samilCount;
	
	
}

