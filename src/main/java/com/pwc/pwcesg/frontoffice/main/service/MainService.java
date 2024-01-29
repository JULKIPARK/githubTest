package com.pwc.pwcesg.frontoffice.main.service;

import com.pwc.pwcesg.frontoffice.main.mapper.MainMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class MainService {

    private final MainMapper mainMapper;

    public List<Map<String, Object>> selectMainList(Map<String, Object> paramMap) {
        return mainMapper.selectMainList(paramMap);
    }


    /**
     *  삼일 인사이트 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, Object>> selectPwcInsightsList(Map<String, String> paramMap){
		List<Map<String, Object>> dataList = mainMapper.selectPwcInsightsList(paramMap);
        return dataList;
	}

	/**
     *  최신 동향 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectTrendList(Map<String, String> paramMap){
        return mainMapper.selectTrendList(paramMap);
	}


	/**
     *  ESG 자료모음 목록
	 * @param paramMap : 토픽채번
	 * @return
	 */
	public List<Map<String, String>> selectBbsList(Map<String, String> paramMap){
        return mainMapper.selectBbsList(paramMap);
	}

}
