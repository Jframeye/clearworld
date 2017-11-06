package com.xiaoye.clearworld.utils;

import java.util.UUID;

public class StringUtils {
	
	/** 编码类型：utf-8 */
	public static final String UTF = "UTF-8";
	
	/** 编码类型：GBK */
	public static final String GBK = "GBK";

	/**
	 * 判断字符数组是否为空
	 * @param str
	 * @return
	 */
    public static boolean isEmpty(String[] array) {
    	return (array == null || array.length == 0);
    }

    /**
     * 判断字符串是否为空
     * <pre>
     * StringUtils.isEmpty(null)          = true
     * StringUtils.isEmpty("")            = true
     * StringUtils.isEmpty("     ")       = true
     * StringUtils.isEmpty("abc")         = false
     * StringUtils.isEmpty("    abc    ") = false
     * </pre>
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }
    
    /**
     * 判断字符串是否不为空
     * <pre>
     * StringUtils.isNotEmpty(null)          = false
     * StringUtils.isNotEmpty("")            = false
     * StringUtils.isNotEmpty("     ")       = false
     * StringUtils.isNotEmpty("abc")         = true
     * StringUtils.isNotEmpty("    abc    ") = true
     * </pre>
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
	public static String firstToLowerCase(String str){
	    if(isEmpty(str)) throw new IllegalArgumentException("the argument can not be null or ''");
		return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toLowerCase());
	}
	
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String firstToUpperCase(String str){
	    if(isEmpty(str)) throw new IllegalArgumentException("the argument can not be null or ''");
		return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
	}

    /**
	 * 描述: 获取{@link length} 长度的随机数字<br>
	 * @param length
	 * @return
	 */
	public static String randomNumber(int length) {
		return String.valueOf(Math.random()).substring(2, length + 2);
	}
	
	/**
	 * 描述: 32位长度的UUID <br>
	 * @return
	 */
	public static String UUID32() {
		return UUID36().replaceAll("-", "");
	}
	
	/**
	 * 描述: 36位长度的UUID <br>
	 * @return
	 */
	public static String UUID36() {
		return UUID.randomUUID().toString().toUpperCase();
	}

    /**
     * 判断字符串数组存在空值
     * @param strs
     * @return if has empty value return true; else return false.
     */
    public static boolean hasEmptyString(String...strs) {
        if(strs == null) return true;
        for (String string : strs) {
            if(isEmpty(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串数组不存在空值
     * @param strs
     * @return if has empty value return false; else return true.
     */
    public static boolean hasNoEmptyString(String...strs) {
        return !hasEmptyString(strs);
    }

    /**
     * 获取驼峰字符串
     * @param str
     * @return
     */
    public static String getHumpString(String str) {
        if(isEmpty(str)) throw new IllegalArgumentException("the argument can not be null or ''");
        if(str.indexOf("_") == -1) {
            return str;
        }
        str = str.replace("__", "_");
        if(str.startsWith("_")) {
            str = str.substring(1);
        }
        while(str.indexOf("_") != -1) {
            int index = str.indexOf("_");
            str = str.substring(0, index) + firstToUpperCase(str.substring(index + 1));
        }
        return str;
    }

    /**
     * 获取驼峰字符串，并且首字母大写
     * @param str
     * @return
     */
    public static String getHumpStringAndFirstUpperCase(String str) {
        if(isEmpty(str)) throw new IllegalArgumentException("the argument can not be null or ''");
        if(str.indexOf("_") == -1) {
            return str;
        }
        str = str.replace("__", "_");
        if(str.startsWith("_")) {
            str = firstToUpperCase(str.substring(1));
        } else {
            str = firstToUpperCase(str);
        }
        while(str.indexOf("_") != -1) {
            int index = str.indexOf("_");
            str = str.substring(0, index) + firstToUpperCase(str.substring(index + 1));
        }
        return str;
    }
}
