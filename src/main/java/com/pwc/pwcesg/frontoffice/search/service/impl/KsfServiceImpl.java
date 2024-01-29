package com.pwc.pwcesg.frontoffice.search.service.impl;//package com.pwc.pwcesg.frontoffice.search.service.impl;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.pwc.pwcesg.frontoffice.search.dao.KsfModule; 
//import com.pwc.pwcesg.frontoffice.search.service.KsfService;
//
///**
// * Class Name : KsfServiceImpl.java
// * Description : 코난 검색엔진의 KSF 모듈을 이용한 검색을 위한 컨트롤러
// *
// * Modification Information
// *
// * 수정일                        수정자           수정내용
// * --------------------  -----------  ---------------------------------------
// * 2017년 12월  00일                       최초 작성
// *
// * @since 2017년
// * @version V1.0
// * @see (c) Copyright (C) by KONANTECH All right reserved
// */
//
////@Service("ksfService") //ksfService는 이미 빈 등록이 되어 있어서 주석 처리
//@Service
//public class KsfServiceImpl implements KsfService {
//	
//	/** REST 모듈 */
//	@Resource(name = "ksfModule")
//	private KsfModule ksfModule;
//	
//	
//	@Override
//	public List<Map<String, String>> getPopularKwd(int doaminNo, int maxResult)
//			throws Exception {
//		return ksfModule.getPopularKwd(doaminNo, maxResult);
//	}
//
//	@Override
//	public String getRecommendKwd(int doaminNo, int maxResult, String kwd) {
//		return null;
//	}
//
//	@Override
//	public String getAutocomplete(String seed, String mode, int maxResultCount, int domainNo) {
//		return ksfModule.getAutocomplete(seed, mode,maxResultCount ,domainNo);
//	}
//	
//	@Override
//	public String getSpellChek(String term){
//		return ksfModule.getSpellChek(term);
//	}
//
//	@Override
//	public Boolean getcensoredKwd(String kwd, int domainNo) {
//		return ksfModule.getcensoredKwd(kwd, domainNo);
//	}
//}
