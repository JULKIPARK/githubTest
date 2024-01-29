package com.pwc.pwcesg.config.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StringUtil {

    /**
     * <pre>
     * 입력된 값이 null이면, "" 값으로 대체하고, null이 아니면 입력값을 리턴한다..
     *
     * &#64;param str 문자열
     * &#64;return String 체크된 문자열
     * </pre>
     */
    public static String nvl(String str) {
        if (str == null || str.length() == 0 || str.equals("null"))
            return "";
        return str;
    }

    /**
     * <pre>
     * 입력된 값이 널이면, "" 값으로 대체하고, 널이 아니면 입력값을 trim()후 리턴한다.
     *
     * &#64;param str 문자열
     * &#64;return String 체크된 문자열
     * </pre>
     */
    public static String nvlt(String str) {
        if (str == null)
            return "";
        return str.trim();
    }

    /**
     * <pre>
     * 입력된 값이 널이면, replace 값으로 대체한다.
     *
     * &#64;param str 입력
     * &#64;param replace 대체 값
     * &#64;return String 문자
     * </pre>
     */
    public static String nvl(String str, String replace) {
        if (isEmpty(str)) {
            return replace;
        } else {
            return str;
        }
    }

    /**
     * <pre>
     * 문자열이 널(null)이거나 공백문자열("")인지 검사한다.
     * &#64;param str 검사 문자열
     * &#64;return boolean
     * </pre>
     */
    public static boolean isNullOrBlank(String str) {
        if (str == null || str.length() == 0 || str.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * <pre>
     * 문자열을 일정길이 만큼만 보여주고,
     * 그 길이에 초과되는 문자열일 경우 특정문자를 덧붙여 보여준다.
     *
     * &#64;param input String 변환할 문자열
     * &#64;param limit int 문자열의 제한 길이
     * &#64;param postfix String 덧붙일 문자열
     * &#64;return String the translated string.
     * </pre>
     */
    public static String fixLength(String input, int limit, String postfix) {
        String buffer = "";
        char[] charArray = input.toCharArray();
        if (limit >= charArray.length) {
            return input;
        }

        for (int j = 0; j < limit; j++) {
            buffer += charArray[j];

        }
        buffer += postfix;
        return buffer;
    }

    /**
     * <pre>
     * 문자열에서 특정 문자열을 치환한다.
     *
     * &#64;param source String 변환할 문자열
     * &#64;param keyStr String 치환 대상 문자열
     * &#64;param toStr String 치환될 문자열
     * &#64;return String the translated string.
     *
     * <b>Example)</b>
     * 123456-7890123라는 문자열 str을 1234567890123 형식으로 바꾸고 싶다면,
     * replaceStr( str, "-", "") 로 호출한다.
     * </pre>
     */
    public static String replaceStr(String source, String keyStr, String toStr) {
        if (source == null) {
            return null;
        }
        int startIndex = 0;
        int curIndex = 0;
        StringBuilder result = new StringBuilder();

        while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
            result.append(source.substring(startIndex, curIndex)).append(toStr);
            startIndex = curIndex + keyStr.length();
        }

        if (startIndex <= source.length()) {
            result.append(source.substring(startIndex, source.length()));

        }
        return result.toString();
    }

    /**
     * <pre>
     * 문자열에서 특정 자리수를 치환한다.
     *
     * &#64;param source
     * &#64;param start int 치환할 시작 인덱스
     * &#64;param end int 치환할 끝 인덱스
     * &#64;param toStr String 치환될 문자열
     * &#64;return String the translated string.
     *
     * <b>Example)</b>
     * StringUtil.replaceStr("abcde", 3, 4, "55"); 의 결과는 "abc55e" 이다.
     * </pre>
     */
    public static String replaceStr(String source, int start, int end, String toStr) {
        if (source == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(source);

        int len = source.length();
        if (start > len || end < start) {
            return result.toString();
        }

        result.replace(start, end, toStr);
        return result.toString();
    }

    /**
     * <pre>
     * 문자열에서 특정 문자열을 치환한다.
     * 문자열 배열의 차례대로 치환하되
     * 더 이상 배열 값이 없으면 space 1칸으로 치환한다.
     *
     * &#64;param source String 변환할 문자열
     * &#64;param keyStr String 치환 대상 문자열
     * &#64;param toStr String[] 치환될 문자열 배열
     * &#64;return String the translated string.
     * </pre>
     */
    public static String replaceStr(String source, String keyStr, String[] toStr) {
        if (source == null) {
            return null;
        }
        int startIndex = 0;
        int curIndex = 0;
        int i = 0;
        StringBuilder result = new StringBuilder();
        String specialString = null;

        while ((curIndex = source.indexOf(keyStr, startIndex)) >= 0) {
            if (i < toStr.length) {
                specialString = toStr[i++];
            } else {
                specialString = " ";
            }
            result.append(source.substring(startIndex, curIndex)).append(specialString);
            startIndex = curIndex + keyStr.length();
        }

        if (startIndex <= source.length()) {
            result.append(source.substring(startIndex, source.length()));

        }
        return result.toString();
    }

    /**
     * <pre>
     * 문자열을 특정 형식으로 출력한다.
     * 단, source가 null이라면 빈 문자열(empty string)을 리턴한다.
     * 형식: #은 문자열을 나타낸다. # 외의 문자는 그대로 출력된다.
     *
     * &#64;param source String 변환할 문자열
     * &#64;param format String 형식
     * &#64;return String the translated string.
     *
     * <b>Example)</b>
     * 1234567890123라는 문자열 str을 123456-7890123 형식으로 바꾸고 싶다면,
     * printStr( str, "######-#######") 로 호출한다.
     * </pre>
     */
    public static String formatStr(String source, String format) {
        if (source == null) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        char[] f = format.toCharArray();
        char[] s = source.toCharArray();

        int len = f.length;
        int h = 0;
        for (int i = 0; i < len; i++) {
            if (h >= s.length) {
                break;
            }
            if (f[i] == '#') {
                buf.append(s[h++]);
            } else {
                buf.append(f[i]);
            }
        }
        return buf.toString();
    }

    /**
     * LPAD
     *
     * @param str    입력 및 반환
     * @param addStr add할 문자
     * @param len    전체 길이
     * @return
     */
    public static String getLpad(String str, String addStr, int len) {

        if (addStr == null || addStr.length() == 0)
            return str;

        if (str == null)
            str = "";

        if (len < str.length())
            return str;

        int tmpLen = len - str.length();

        for (int i = 0; i < tmpLen; i++)
            str = addStr + str;

        return str;
    }

    /**
     * RPAD
     *
     * @param str    입력 및 반환
     * @param addStr add할 문자
     * @param len    전체 길이
     * @return
     */
    public static String getRpad(String str, String addStr, int len) {

        if (addStr == null || addStr.length() == 0)
            return str;

        if (str == null)
            str = "";

        if (len < str.length())
            return str;

        int tmpLen = len - str.length();

        for (int i = 0; i < tmpLen; i++)
            str = str + addStr;

        return str;
    }

    /**
     * 문자열에서 숫자만 반환
     *
     * @param input
     * @return
     */
    public static String getDigit(String input) {

        if (input == null || input.trim().length() == 0) {

            return "";
        }

        StringBuffer sb = new StringBuffer();
        int length = input.length();

        for (int i = 0; i < length; i++) {

            char curChar = input.charAt(i);
            if (Character.isDigit(curChar))
                sb.append(curChar);
        }
        return sb.toString();
    }

    /**
     * <pre>
     * 소수점 이하 자리수의 길이를 맞춰 문자열로 리턴한다.
     *
     * &#64;param aSrc double 형 데이터
     * &#64;param aPrecisionSize 소수점 이하 자리수
     * &#64;return String 맞춤 문자열
     *
     * <b>Example)</b>
     * StringUtil.fixPrecision(5000, 5) 의 결과는 5000.00000
     * StringUtil.fixPrecision(500.123456, 2) 의 결과는 500.12
     * </pre>
     */
    public static String fixPrecision2(double aSrc, int aPrecisionSize) {
        String result = "";

        try {
            double d = 0;

            result = new DecimalFormat("#################.############").format(d);
        } catch (NullPointerException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        return result;
    }

    /**
     * <pre>
     * 소수점 이하 자리수의 길이를 맞춰 문자열로 리턴한다.
     *
     * &#64;param aSrc 입력 문자열
     * &#64;param aPrecisionSize 소수점 이하 자리수
     * &#64;return String 맞춤 문자열
     *
     * <b>Example)</b>
     * StringUtil.fixPrecision("500.2",5) 의 결과는 500.20000
     * StringUtil.fixPrecision("500.2",0) 의 결과는 500
     * StringUtil.fixPrecision(".02",0) 의 결과는 빈 문자열(empty String)
     * </pre>
     */
    public static String fixPrecision(String aSrc, int aPrecisionSize) {
        if (aSrc == null) {
            return null;
        }
        String result = aSrc;
        int index = aSrc.lastIndexOf(".");
        if (index >= 0) {
            int len = aSrc.length();
            if (aPrecisionSize > (len - index)) {
                for (int i = (len - index); i <= aPrecisionSize; i++) {
                    result += '0';
                }
            } else {
                result = result.substring(0, index);
            }
        } else {
            if (aPrecisionSize > 0) {
                result += '.';
                for (int i = 0; i < aPrecisionSize; i++) {
                    result += '0';
                }
            }
        }
        return result;
    }

    /**
     * <pre>
     * 소수점 자리수를 포맷에 맞게 표현하여 반환한다.
     *
     * &#64;param aSrc 입력 수
     * &#64;param aFormat 포맷
     * &#64;return String 맞춤 문자열
     *
     * <b>Example)</b>
     * StringUtil.fixPrecision(500.2,"#####.000")의 결과는 500.200
     * </pre>
     */
    public static String fixPrecision(double aSrc, String aFormat) {
        if (aFormat == null || aFormat.equals("")) {
            return null;
        }
        DecimalFormat newFormat = new DecimalFormat(aFormat);
        return newFormat.format(aSrc);
    }

    /**
     * <pre>
     * 문자열을 특정 길이에 맞춰 구성한다.
     * filler는 " "이며, 뒤에 붙는다.
     * 문자열이 size보다 짧으면 filler로 채우고, 길면 오른쪽부터 size자리까지만 자른다.
     *
     * &#64;param src 원본 문자열
     * &#64;param size 맞춤 길이
     * &#64;return String 맞춤 문자열
     * </pre>
     */
    public static String rpad(String src, int size) {
        return rpad(src, size, " ");
    }

    /**
     * <pre>
     * 문자열을 특정 길이에 맞춰 구성한다.
     * filler는 뒤에 붙는다.
     * 문자열이 size보다 짧으면 filler로 채우고, 길면 오른쪽부터 size자리까지만 자른다.
     *
     * &#64;param src 원본 문자열
     * &#64;param size 맞춤 길이
     * &#64;param filler 빈자리에 채울 문자
     * &#64;return String 맞춤 문자열
     * </pre>
     */
    public static String rpad(String src, int size, String filler) {
        /*
         * if (src == null) src = ""; int len = src.length(); if (len > size) return
         * src.substring(0, size); for (int i = len; i < size; i++) src += filler;
         * return src; </pre>
         */
        StringBuilder sb = new StringBuilder();
        if (src == null) {
            for (int i = 0; i < size; i++) {
                sb.append(filler);
            }
        } else {
            int len = src.length();
            if (len > size) {
                sb.append(src.substring(0, size));
            } else {
                sb.append(src);

            }
            for (int i = len; i < size; i++) {
                sb.append(filler);
            }
        }
        return sb.toString();
    }

    /**
     * <pre>
     * 구분자 있는 문자열에 대해 java.util.List 형태로 변환하여 반환한다.
     *
     * &#64;param sourceString 구분자 포함 문자열
     * &#64;param delim 구분자
     * &#64;return java.util.List
     * </pre>
     */
    public static List<String> stringToList(String sourceString, String delim) {
        List<String> destinationList = new ArrayList<String>();

        if (sourceString != null) {
            int index = -1;
            int oldIndex = -1;

            while (true) {
                oldIndex = index + 1;
                index = sourceString.indexOf(delim, oldIndex);
                if (index != -1) {
                    destinationList.add(sourceString.substring(oldIndex, index));
                } else {
                    destinationList.add(sourceString.substring(oldIndex, sourceString.length()));
                    break;
                }
            }
        }
        return destinationList;
    }

    /**
     * <pre>
     * java.util.List에 대해 구분자 있는 문자열의 형태로 변환하여 반환한다.
     *
     * &#64;param lst List
     * &#64;param delim 구분자
     * &#64;return String 구분자 있는 문자열
     * </pre>
     */
    @SuppressWarnings("rawtypes")
    public static String listToString(List lst, String delim) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < lst.size(); i++) {
            if (i != 0) {
                buf.append(delim);
                buf.append(lst.get(i));
            } else {
                buf.append(lst.get(i));
            }
        }

        return buf.toString();
    }

    /**
     * <pre>
     * Checks if a String is empty ("") or null.
     * NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().
     *
     * &#64;param str  the String to check, may be null
     * &#64;return boolean if the String is empty or null
     *
     * <b>Example)</b>
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     * </pre>
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <pre>
     * Checks if a String is not empty ("") and not null.
     *
     * &#64;param str  the String to check, may be null
     * &#64;return boolean if the String is not empty and not null
     *
     * <b>Example)</b>
     * StringUtil.isNotEmpty(null)      = false
     * StringUtil.isNotEmpty("")        = false
     * StringUtil.isNotEmpty(" ")       = true
     * StringUtil.isNotEmpty("bob")     = true
     * StringUtil.isNotEmpty("  bob  ") = true
     * </pre>
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * <pre>
     * Checks if a String is whitespace, empty ("") or null.
     *
     * &#64;param str  the String to check, may be null
     * &#64;return boolean if the String is null, empty or whitespace
     *
     * <b>Example)</b>
     * StringUtil.isBlank(null)      = true
     * StringUtil.isBlank("")        = true
     * StringUtil.isBlank(" ")       = true
     * StringUtil.isBlank("bob")     = false
     * StringUtil.isBlank("  bob  ") = false
     * </pre>
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <pre>
     * Checks if a String is not empty (""), not null and not whitespace only.
     *
     * &#64;param str  the String to check, may be null
     * &#64;return boolean if the String is not empty and not null and not whitespace
     *
     * <b>Example)</b>
     * StringUtil.isNotBlank(null)      = false
     * StringUtil.isNotBlank("")        = false
     * StringUtil.isNotBlank(" ")       = false
     * StringUtil.isNotBlank("bob")     = true
     * StringUtil.isNotBlank("  bob  ") = true
     * </pre>
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * <pre>
     * OR 조건으로 값이 일치하는지 비교한다.
     *
     * &#64;param  var 비교할 변수
     * &#64;param  val 비교할 값
     * &#64;return boolean 일치하는값이 있으면 true를 리턴한다.
     *
     * <b>Example)</b>
     *     StringUtil.equalsOr("CF", "CF|CG")
     * </pre>
     */
    private static Map<String, String[]> splitedArrs = new ConcurrentHashMap<String, String[]>();

    public static boolean equalsOr(String var, String val) {
        if (var == null || "".equals(var))
            return false;
        if (val == null || "".equals(val))
            return false;

        boolean rtnVal = false;
        String[] valArr = splitedArrs.get(val);
        if (valArr == null) {
            valArr = val.split("\\|");
            splitedArrs.put(val, valArr);
        }
        for (String _val : valArr) {
            if (_val.equals(var)) {
                rtnVal = true;
                break;
            }
        }

        return rtnVal;
    }

    /**
     * <pre>
     * 입력된 값이 널이면, "" 값으로 대체하고, 널이 아니면 입력값을 trim()후 리턴한다.
     *
     * &#64;param str 문자열
     * &#64;return String 체크된 문자열
     * </pre>
     */
    public static String trimAll(String str) {
        str = nvl(str);
        return str.replaceAll("\\p{Z}", "");
    }

    /**
     * 임시 업로드 파일 ms단위까지 suffix Strng 생성
     *
     * @param
     * @return String
     * @throws Exception
     */
    public static String getMsString() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String tempFileSuffix = dateFormatter.format(cal.getTime());
        return tempFileSuffix;
    }

}