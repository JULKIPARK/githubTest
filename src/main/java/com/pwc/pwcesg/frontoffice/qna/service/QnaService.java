package com.pwc.pwcesg.frontoffice.qna.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pwc.pwcesg.frontoffice.qna.mapper.QnaMapper;

import lombok.RequiredArgsConstructor;


/**
 * 문의·요청 Service
 * @author N.J.Kim
 *
 */
@RequiredArgsConstructor
@Service
public class QnaService {
	
    private final QnaMapper qnaMapper;

    /**
     * 문의·요청 목록 조회
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> selectQnaList(Map<String, Object> paramMap) {
        return qnaMapper.selectQnaList(paramMap);
    }
    
    /**
     * 문의·요청 상세 조회
     * @param paramMap
     * @return ModelAndView
     */
    public Map<String, Object> selectQna(String askUid) {
        return qnaMapper.selectQna(askUid);
    }

    /**
     * 문의·요청 등록
     * @param paramMap
     * @return
     */
    public int insertQna(Map<String, Object> paramMap) {
        return qnaMapper.insertQna(paramMap);
    }

    /**
     * 문의·요청 수정
     * @param paramMap
     * @return
     */
    public int updateQna(Map<String, Object> paramMap) {
        return qnaMapper.updateQna(paramMap);
    }
}
