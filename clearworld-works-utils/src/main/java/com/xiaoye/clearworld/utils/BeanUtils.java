package com.xiaoye.clearworld.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.xiaoye.clearworld.utils.bean.BeanToMap;
import com.xiaoye.clearworld.utils.bean.MapToBean;

/**
 *<p> Title: BeanToMapUtils </p>
 *<p> Description: 实体转集合类 </p>
 *<p> Copyright: openlo.cn Copyright (C) 2017 </p>
 *
 * @author yehl
 * @version
 * @since 2017年3月17日
 */
public class BeanUtils {

    /**
     * Bean To Map
     * @param object
     * @return 如果对象为空，则返回空Map
     */
    @SuppressWarnings("unchecked")
    public static Map<? extends Object, ? extends Object> beanToMap(Object object) {
        return (Map<? extends Object, ? extends Object>) BeanToMap.toMap(object);
    }

    /**
     * Bean To Map
     * @param object
     * @param pattern 时间转换格式
     * @return 如果对象为空，则返回空Map
     */
    @SuppressWarnings("unchecked")
    public static Map<? extends Object, ? extends Object> beanToMap(Object object, String pattern) {
        return (Map<? extends Object, ? extends Object>) BeanToMap.toMap(object, pattern);
    }
    
    /**
     * Map To Bean
     * @param dataMap
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<? extends Object, ? extends Object> dataMap, Class<T> clazz) {
        return MapToBean.toBean(dataMap, clazz);
    }
    
    /**
     * 将List<T>转换为List<Map<Object, Object>>
     * @param list
     * @return
     */
    public static <T> List<Map<? extends Object, ? extends Object>> beanListToMapList(List<T> beanList) {
		List<Map<? extends Object, ? extends Object>> list = new ArrayList<>();
        if (beanList != null && beanList.size() > 0) {
            Map<? extends Object, ? extends Object> map = null;
            T bean = null;
            for (int i = 0,size = beanList.size(); i < size; i++) {
                bean = beanList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }
    
    /**
	 * 将List<Map<String,Object>>转换为List<T>
	 * 
	 * @param dataMapList
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> mapListToBeanList(List<Map<? extends Object, ? extends Object>> dataMapList,
			Class<T> clazz) {
        try {
			List<T> list = new ArrayList<>();
			if (dataMapList != null && dataMapList.size() > 0) {
                Map<? extends Object, ? extends Object> map = null;
				for (int i = 0, size = dataMapList.size(); i < size; i++) {
					map = dataMapList.get(i);
                    T bean = clazz.newInstance();
                    mapToBean(map, clazz);
                    list.add(bean);
                }
            }
            return list;
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Properties To Bean
     * @param dataMap
     * @param clazz
     * @return
     */
    public static <T> T propertiesToBean(Properties properties, Class<T> clazz) {
        Map<? extends Object, ? extends Object> dataMap = propertiesToMap(properties);
        return mapToBean(dataMap, clazz);
    }

    /**
     * Map To Bean
     * @param dataMap
     * @param clazz
     * @return
     */
    public static Map<? extends Object, ? extends Object> propertiesToMap(Properties properties) {
        if(properties == null) {
            throw new IllegalArgumentException("properties to map, the properties can not be null");
        }
		Map<Object, Object> map = new HashMap<>();
        Set<Entry<Object, Object>> propertySet = properties.entrySet();
        for (Entry<Object, Object> entry : propertySet) {
            Map.Entry<Object, Object> mapEntry = entry;
            map.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return map;
    }

    /**
     * 对象拷贝
     * @param generatorInfo
     * @param clazz
     * @return
     */
    public static <T> T beanToBean(Object source, Class<T> clazz) {
        Map<? extends Object, ? extends Object> dataMap = beanToMap(source);
        return mapToBean(dataMap, clazz);
    }

    /**
     * JSON to Map
     * 
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObject) {
        if(jsonObject == null) return null;
        
		Map<String, Object> map = new HashMap<>();
        Set<Entry<String, Object>> set = jsonObject.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * JSON to Bean
     * 
     * @param jsonObject
     * @return
     */
    public static <T> T jsonToBean(JSONObject jsonObject, Class<T> clazz) {
        Map<String, Object> dataMap = jsonToMap(jsonObject);
        return mapToBean(dataMap, clazz);
    }

    /**
     * Map to JSON
     * 
     * @param jsonObject
     * @return
     */
    public static JSONObject mapToJson(Map<String, Object> map) {
        if(map == null) return null;
        
        JSONObject jsonObject = new JSONObject();
        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject;
    }
}
