package com.xiaoye.clearworld.utils.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xiaoye.clearworld.utils.DateTimeUtils;

/**
 * <p>Title: BeanToMap</p>
 * <p>Description: Bean To Map</p>
 * <p>Copyright: openlo.cn Copyright (C) 2017</p>
 * @author yehl
 * @version
 * @since 2017年3月20日
 */
public class BeanToMap {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object toMap(Object value) {
        if (value == null) {
            return null;
        }
        
        Class<? extends Object> clazz = value.getClass();

        if (clazz.isPrimitive()) {
            return value;
        }
        if (clazz.isEnum()) {
            return ((Enum) value).name();
        }
        
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if ((componentType.isPrimitive()) || (componentType.getCanonicalName().startsWith("java")) || (componentType.getCanonicalName().startsWith("javax"))) {
                return value;
            }

            int length = Array.getLength(value);
            Object array = Array.newInstance(Object.class, length);
            for (int i = 0; i < length; i++) {
                Object object = Array.get(value, i);
                Array.set(array, i, toMap(object));
            }
            return array;
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            List<Object> list = new ArrayList<Object>(((Collection<?>) value).size());
            for (Object object : (Collection) value) {
                list.add(toMap(object));
            }
            return list;
        }
        
        if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map) value;
            Iterator<?> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry) iterator.next();
                Object v = entry.getValue();

                if (v != null) {
                    entry.setValue(toMap(v));
                }
            }
            Map<Object, Object> res = new HashMap(map.size());
            res.putAll(map);
            return res;
        }
        
        if ((clazz.getCanonicalName().startsWith("java")) || (clazz.getCanonicalName().startsWith("javax"))) {
            return value;
        }

        HashMap<Object, Object> datas = new HashMap(8);
        List<BeanPropertyMetaData> metaDataList = BeanMetaDataRegistCenter.getBeanPropertyMetaDatas(clazz);
        if (metaDataList.size() > 0) {
            for (BeanPropertyMetaData metaData : metaDataList) {
                Object object = metaData.getPropertyValue(value);
                if (object != null) {
                    datas.put(metaData.getPropertyName(), toMap(object));
                }
            }
        }
        return datas;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object toMap(Object value, String datePattern) {
        if (value == null) {
            return null;
        }
        
        Class<? extends Object> clazz = value.getClass();
    
        if (clazz.isPrimitive()) {
            return value;
        }
        if (clazz.isEnum()) {
            return ((Enum) value).name();
        }
        
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if ((componentType.isPrimitive()) || (componentType.getCanonicalName().startsWith("java")) || (componentType.getCanonicalName().startsWith("javax"))) {
                return value;
            }
    
            int length = Array.getLength(value);
            Object array = Array.newInstance(Object.class, length);
            for (int i = 0; i < length; i++) {
                Object object = Array.get(value, i);
                Array.set(array, i, toMap(object));
            }
            return array;
        }
    
        if (Collection.class.isAssignableFrom(clazz)) {
            List<Object> list = new ArrayList<Object>(((Collection<?>) value).size());
            for (Object object : (Collection) value) {
                list.add(toMap(object));
            }
            return list;
        }
        
        if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map) value;
            Iterator<?> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry) iterator.next();
                Object v = entry.getValue();
    
                if (v != null) {
                    entry.setValue(toMap(v));
                }
            }
            Map<Object, Object> res = new HashMap(map.size());
            res.putAll(map);
            return res;
        }
        
        if(isDate(clazz)) {
			return DateTimeUtils.formatDateToDate((Date) value, datePattern);
        }
        
        if ((clazz.getCanonicalName().startsWith("java")) || (clazz.getCanonicalName().startsWith("javax"))) {
            return value;
        }
    
        HashMap<Object, Object> datas = new HashMap(8);
        List<BeanPropertyMetaData> metaDataList = BeanMetaDataRegistCenter.getBeanPropertyMetaDatas(clazz);
        if (metaDataList.size() > 0) {
            for (BeanPropertyMetaData metaData : metaDataList) {
                Object object = metaData.getPropertyValue(value);
                if (object != null) {
                    datas.put(metaData.getPropertyName(), toMap(object));
                }
            }
        }
        return datas;
    }

    /**
     * 判断是否是时间类型
     * @param clazz
     * @return
     */
    private static boolean isDate(Class<? extends Object> clazz) {
        String name = clazz.getCanonicalName();
        return "java.util.Date".equals(name);
    }
}
