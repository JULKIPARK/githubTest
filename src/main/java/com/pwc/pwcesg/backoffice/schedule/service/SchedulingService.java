package com.pwc.pwcesg.backoffice.schedule.service;

import com.pwc.pwcesg.backoffice.schedule.mapper.SchedulingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SchedulingService {

	@Autowired
	private SchedulingMapper schedulingMapper;

	/**
	 * RSS 기준정보 목록 조회
	 *
	 * @return
	 */
	public List<Map<String, Object>> selectRssBaseInfoList() {
		return schedulingMapper.selectRssBaseInfoList();
	}

	/**
	 * 신규 뉴스 등록
	 *
	 * @param newsList
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class})
	public void registNews(List<Map<String, String>> newsList, Map<String, Object> rssBaseInfo) {
		int limit = 100;
		int repeat = newsList.size() % limit != 0 ? newsList.size() / limit + 1 : newsList.size() / limit;

		for (int id = 0; id < repeat; id++) {
			if (id != (repeat - 1)) {
				schedulingMapper.registNews(new ArrayList<>(newsList.subList(id * limit, (id + 1) * limit)));
			} else {
				schedulingMapper.registNews(new ArrayList<>(newsList.subList(id * limit, newsList.size())));
			}
		}

		schedulingMapper.updateBaseInfo(rssBaseInfo);//RSS 최종작성일 수정
	}

	/**
	 * RSS 최종작성일 수정
	 *
	 * @return
	 */
	public int updateBaseInfo(Map<String, Object> paramMap) {
		return schedulingMapper.updateBaseInfo(paramMap);
	}

//	/**
//	 * 일자별집계
//	 * @return
//	 */
//    public void insertBtDteGrpAggr(){
//    	schedulingMapper.insertBtDteGrpAggr();
//    }
}
