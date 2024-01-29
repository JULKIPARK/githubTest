package com.pwc.pwcesg.backoffice.main.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.pwc.pwcesg.backoffice.main.mapper.BoMainMapper;

import lombok.RequiredArgsConstructor;


/**
 * 메인 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class BoMainService {
	
    private final BoMainMapper boMainMapper;

	
	/**
	 * 오늘의 할 일
	 * @return
	 */
    public Map<String, Object> selectToDo(){
        return boMainMapper.selectToDo();
    }
	
	/**
	 * 회원 현황
	 * @return
	 */
    public List<Map<String, Object>> selectMbrState(){
        return boMainMapper.selectMbrState();
    }
	
	/**
	 * 회원 현황 집계
	 * @return
	 */
    public List<Map<String, Object>> selectMbrStateSum(){
        return boMainMapper.selectMbrStateSum();
    }
	
	/**
	 * 온라인 문의 현황
	 * @return
	 */
    public List<Map<String, Object>> selectAskCtCnt(){
        return boMainMapper.selectAskCtCnt();
    }
	
	/**
	 * 회원 현황 집계
	 * @return
	 */
    public List<Map<String, Object>> selectUnconfirmedAskList(){
        return boMainMapper.selectUnconfirmedAskList();
    }
}
