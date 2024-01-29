package com.pwc.pwcesg.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 공통 함수
 * @author N.J.Kim
 *
 */
class CommonUtil {
	
	private CommonUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
    /**
     * String 배열의 데이터 중에 특정문자가 있으면 true를 리턴
     * @param str
     * @param data
     * @return
     */
    public static boolean strCompare(String[] str, String data){
    	boolean bo = false;
    	
    	for(int i = 0; i < str.length; i++){
    		if(nvl(data,"").equals(nvl(str[i],""))){
    			bo = true;
    			break;
    		}
    	}
    	
    	return bo;
    }
	
    public static String nvl(String s, String s1) {
        if (s==null || "".equals(s)) {
            return s1;
        } else {
            return s; 
        }
    }
	
	/**
     * 오늘 날짜를 원하는 패턴으로 가져온다.
     * @param pm_sDatePattern
     * @return 원하는 패턴의 오늘날짜
     */
	public static String getToDayPattern(String pm_sDatePattern) {
        return getDateFormat(new Date().getTime(), pm_sDatePattern);
    }
	
	/**
     * 날짜의 모양을 원하는 패턴을 주면 그것으로 날짜 포멧으로 변환하여 
     * 문자열 값으로 리턴하는 함수이다.
     * @param pm_lDate      long 타입으로 날짜값이 들어온다.
     * @param pm_sPattern   String 타입으로 원하는 날짜 모양의 패턴이 들어온다.
     * @return  포맷팅된 날짜 문자열
     */
    public static String getDateFormat(long pm_lDate, String pm_sPattern) {
        SimpleDateFormat lm_oFormat = new SimpleDateFormat(pm_sPattern);
        return lm_oFormat.format(new Date(pm_lDate));
    }
}