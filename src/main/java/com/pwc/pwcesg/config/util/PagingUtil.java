package com.pwc.pwcesg.config.util;

import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PagingUtil {
	
	private PagingUtil() {
		throw new IllegalStateException("PagingUtil class");
	}
	
    /**
     * 페이지 번호 추출
     * @param DTO
     * @return MAP
     */
    public static Map<String, Object> pageInfoDTO (PageInfo<?>  pageInfo) {

        Map<String, Object> rtnMap = new HashMap<>();

        log.info("PagingUtil pageInfo : " + pageInfo);

        rtnMap.put("totalCount", pageInfo.getTotal());
        rtnMap.put("totalPage", pageInfo.getPages());
        rtnMap.put("pageNumber", pageInfo.getPageNum());
        //rtnMap.put("pageSize", pageInfo.getPageSize());
        rtnMap.put("size", pageInfo.getSize());
        rtnMap.put("startRow", pageInfo.getStartRow());
        rtnMap.put("endRow", pageInfo.getEndRow());
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("pages", pageInfo.getPages());
        rtnMap.put("prePage", pageInfo.getPrePage());
        rtnMap.put("nextPage", pageInfo.getNextPage());
        //pageData.put("isFirstPage", "");
        //pageData.put("isLastPage", "");
        //pageData.put("hasPreviousPage", "");
        //pageData.put("hasNextPage", "");
        rtnMap.put("navigatePages", pageInfo.getNavigatePages());
        rtnMap.put("navigateFirstPage", pageInfo.getNavigateFirstPage());
        rtnMap.put("navigateLastPage", pageInfo.getNavigateLastPage());
        rtnMap.put("navigatepageNums", pageInfo.getNavigatepageNums());

        log.info("PagingUtil pageData : " + rtnMap);

        return rtnMap;
    }

}
