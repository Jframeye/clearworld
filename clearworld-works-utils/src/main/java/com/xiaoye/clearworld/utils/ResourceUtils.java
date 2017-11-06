package com.xiaoye.clearworld.utils;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Des 国际化资源文件读取工具
 * @Author yehl
 * @Date ${date}
 */
public class ResourceUtils {

    /** **/
	private static Map<String, ResourceBundle> resourceBundleMap = new ConcurrentHashMap<>();

    private static ResourceBundle current;

    /**
     * 设置当前默认的资源文件
     * @param filePath 资源文件路径，不带后缀
     */
    public static void setCurrentResourceBundle(String filePath) {
        current = resource(filePath);
    }

    /**
     * 读取资源文件
     * @param filePath 资源文件路径，不带后缀
     * @return
     */
    public static ResourceBundle resource(String filePath) {
        ResourceBundle bundle = ResourceBundle.getBundle(filePath);
        resourceBundleMap.put(filePath, bundle);
        return bundle;
    }

    /**
     * 获取当前资源文件的属性值
     * @param key
     * @return
     */
    public static String getString(String key) {
        if(current == null) {
            throw new IllegalArgumentException("还未设置当前可使用的资源文件");
        }
        return current.getString(key);
    }

    /**
     * 获取当前资源文件属性值
     * @param key 属性名称
     * @return 默认值 0
     */
    public static int getIntValue(String key) {
        String value = getString(key);
        if(value != null && value.trim().length() != 0) {
            return Integer.valueOf(value);
        }
        return 0;
    }

    /**
     * 获取当前资源文件属性值
     * @param key 属性名称
     * @return 默认值 false
     */
    public static boolean getBooleanValue(String key) {
        String value = getString(key);
        if(value != null && value.trim().length() != 0) {
            return Boolean.valueOf(value);
        }
        return false;
    }

    /**
     * 获取资源文件属性值
     * @param key 属性名称
     * @param filePath 资源文件路径名称（不带后缀）
     * @return
     */
    public static String getString(String key, String filePath) {
        if(resourceBundleMap.containsKey(filePath)) {
            return resourceBundleMap.get(filePath).getString(key);
        }
        return resource(filePath).getString(key);
    }

    /**
     * 获取资源文件属性值
     * @param key 属性名称
     * @param filePath 资源文件路径名称（不带后缀）
     * @return 默认值 0
     */
    public static int getIntValue(String key, String filePath) {
        String value = getString(key, filePath);
        if(value != null && value.trim().length() != 0) {
            return Integer.valueOf(value);
        }
        return 0;
    }

    /**
     * 获取资源文件属性值
     * @param key 属性名称
     * @param filePath 资源文件路径名称（不带后缀）
     * @return 默认值 false
     */
    public static boolean getBooleanValue(String key, String filePath) {
        String value = getString(key, filePath);
        if(value != null && value.trim().length() != 0) {
            return Boolean.valueOf(value);
        }
        return false;
    }

    /**
     * 获取资源文件
     * @param filePath
     * @return
     */
    public static URL getResource(String filePath) {
        return ResourceUtils.class.getClassLoader().getResource(filePath);
    }

    public static void main(String[] args) {
        resource("clearworld");
        System.out.println(getString("application.title", "test/clearworld"));
    }
}
