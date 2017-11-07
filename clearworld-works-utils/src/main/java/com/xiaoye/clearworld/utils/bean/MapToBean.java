package com.xiaoye.clearworld.utils.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xiaoye.clearworld.utils.DateTimeUtils;
import com.xiaoye.clearworld.utils.StringUtils;

/**
 *<p> Title: MapToBean </p>
 *<p> Description: Map To Bean 工具类 </p>
 *<p> Copyright: openlo.cn Copyright (C) 2017 </p>
 *
 * @author yehl
 * @version
 * @since 2017年3月19日
 */
public class MapToBean {

    /**
     * Map To Bean
     * @param clazz
     * @param dataMap
     * @return
     */
    public static <T> T toBean(Map<?, ?> dataMap, Class<T> clazz) {
        T bean = BeanTypeUtils.instantiate(clazz);
        toBean(dataMap, bean);
        return bean;
    }

    /**
     * Map To Bean
     * @param bean
     * @param dataMap
     */
    public static void toBean(Map<?, ?> dataMap, Object bean) {
        Class<?> clazz = bean.getClass();
        if (!BeanTypeUtils.isBean(clazz)) {
            String msg = String.format("%s is not a bean", new Object[] { clazz.getCanonicalName() });
			throw new RuntimeException(msg);
        }
        
        List<BeanPropertyMetaData> beanPropertyMetaDatas = BeanMetaDataRegistCenter.getBeanPropertyMetaDatas(clazz);
        if (beanPropertyMetaDatas.isEmpty()) {
            return;
        }
        for (BeanPropertyMetaData metaData : beanPropertyMetaDatas) {
            Object value = dataMap.get(metaData.getPropertyName()); // 获取属性对应的值
            if (value != null) {
                // 对值进行分析，判断是否是对象、集合、数组等
                Object finalValue = getBeanPropertyValue(metaData, value);
                metaData.setPropertyValue(bean, finalValue); // 设值
            }
        }
    }

    /**
     * 获取属性值
     * @param bpmd
     * @param value
     * @param convertOrNot
     * @param datePattern
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getBeanPropertyValue(BeanPropertyMetaData metaData, Object value) {
        if (metaData.isArray()) { // 属性是数组
            if (!metaData.isByte()) { // 非Byte、Byte数组
                if (Collection.class.isAssignableFrom(value.getClass())) { // 数组元素是集合类型
                    value = ((Collection<?>) value).toArray(); // 将集合转为数组
                }
                int length = Array.getLength(value);
                List<Object> list = new ArrayList<Object>(length);
                Class<?> componentClass = metaData.getComponentInstanceClass(); // 获取元素类型
                for (int i = 0; i < length; i++) {
                    Object tempValue = Array.get(value, i);
                    if (BeanTypeUtils.isBean(componentClass)) { // 元素是实体类
                        if ((tempValue instanceof Map)) {
                            list.add(toBean((Map<?, ?>) tempValue, componentClass));
                        } else {
                            list.add(tempValue);
                        }
                    }
                    else if (componentClass.isArray()) { // 元素是数组
                        list.add(getArrayBeanPropertyValue(componentClass, tempValue, metaData.getPropertyName()));
                    }
                    else if (Collection.class.isAssignableFrom(componentClass)) { // 元素是集合
                        Collection<?> collection = (Collection<?>) tempValue;
                        List<Object> tempAttr = new ArrayList<Object>(collection.size());
                        for (Object cValue : collection) {
                            tempAttr.add(getCollectionBeanPropertyValue(BeanTypeUtils.getBindType(componentClass), componentClass, cValue, metaData.getPropertyName()));
                        }
                        list.add(tempAttr);
                    }
                    else if (Map.class.isAssignableFrom(componentClass)) {
                        if ((tempValue instanceof Map)) {
                            if (!componentClass.equals(tempValue.getClass())) {
                                Map<?, ?> tempValueMap = (Map<?, ?>) BeanTypeUtils.instantiate(componentClass);
                                tempValueMap.putAll((Map) tempValue);
                                tempValue = tempValueMap;
                            }
                        } else {
                            String msg = String.format("Target class<%s> is a map, but the class of value<%s> is not a map. So the value<%s> can not be set to property<%s>.", new Object[] { componentClass.getCanonicalName(), tempValue.getClass().getCanonicalName(), tempValue.toString(), metaData.getPropertyName() });
							throw new RuntimeException(msg);
                        }
                        list.add(tempValue);
                    }
                    else {
                        list.add(getCollectionBeanPropertyValue(BeanTypeUtils.getBindType(componentClass), componentClass, tempValue, metaData.getPropertyName()));
                    }
                }
                return forArray(componentClass, list);
            }

            Object datas = null;

            if ((value instanceof String)) {
                datas = value;
            }
            else {
                Class<?> clazz = value.getClass();
                if ((clazz.isArray()) && ((clazz.getComponentType().equals(Byte.TYPE)) || (clazz.getComponentType().equals(Byte.class)))) {
                    datas = value;
                }
                else {
                    String msg = String.format("The property<%s> is byte array,but value class is <%s>.", new Object[] { metaData.getPropertyName(), value.getClass().getCanonicalName() });
					throw new RuntimeException(msg);
                }
            }
            return datas;
        }

        if (metaData.isCollection()) { // 集合
            Collection<?> collection = (Collection<?>) value;
            List<Object> list = new ArrayList<Object>(collection.size());
            for (Object cValue : collection) {
                list.add(getCollectionBeanPropertyValue(BeanTypeUtils.getBindType(metaData.getComponentInstanceClass()), metaData.getComponentInstanceClass(), cValue, metaData.getPropertyName()));
            }
            return forCollection(list, metaData.getBindClass());
        }
        
        if (metaData.isMap()) { // Map 集合
            if ((value instanceof Map)) {
                Class<?> valueActualType = metaData.getComponentInstanceClass();
                Map<Object, Object> temp = (Map<Object, Object>) BeanTypeUtils.instantiate(metaData.getBindClass());
                Map<?, ?> mapValue = (Map<?, ?>) value;
                for (Object key : mapValue.keySet()) {
                    Object tempValue = mapValue.get(key);
                    if ((BeanTypeUtils.isBean(valueActualType)) && ((tempValue instanceof Map))) {
                        tempValue = toBean((Map<?, ?>) tempValue, valueActualType);
                    }
                    else if (valueActualType.isArray()) {
                        tempValue = getArrayBeanPropertyValue(valueActualType, tempValue, metaData.getPropertyName());
                    }
                    else {
                        if (Collection.class.isAssignableFrom(valueActualType)) {
                            return forCollection((Collection<?>) tempValue, valueActualType);
                        }
                        if (Map.class.isAssignableFrom(valueActualType)) {
                            if ((tempValue instanceof Map)) {
                                if (!valueActualType.equals(tempValue.getClass())) {
                                    Map<?, ?> tempValueMap = (Map<?, ?>) BeanTypeUtils.instantiate(valueActualType);
                                    tempValueMap.putAll((Map) tempValue);
                                    tempValue = tempValueMap;
                                }
                            }
                            else {
                                String msg = String.format("Target class<%s> is a map, but the class of value<%s> is not a map. So the value<%s> can not be set to property<%s>.", new Object[] { valueActualType.getCanonicalName(), tempValue.getClass().getCanonicalName(), tempValue.toString(), metaData.getPropertyName() });
								throw new RuntimeException(msg);
                            }
                        }
                    }
                    temp.put(key, tempValue);
                }
                return temp;
            }

            String msg = String.format("Target class<%s> is a map, but the class of value<%s> is not a map. So the value<%s> can not be set to property<%s>.", new Object[] { metaData.getBindClass().getCanonicalName(), value.getClass().getCanonicalName(), value.toString(), metaData.getPropertyName() });
			throw new RuntimeException(msg);
        }
        return getCollectionBeanPropertyValue(metaData.getBindType(), metaData.getBindClass(), value, metaData.getPropertyName());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getArrayBeanPropertyValue(Class<?> arrayClass, Object arrayValue, String propertyName) {
        Class<?> componentType = arrayClass.getComponentType(); // 获取数组元素类型
        int length = Array.getLength(arrayValue);
        List<Object> list = new ArrayList<Object>(length);
        for (int j = 0; j < length; j++) {
            Object value = Array.get(arrayValue, j);
            if (BeanTypeUtils.isBean(componentType)) {
                if ((value instanceof Map)) {
                    list.add(toBean((Map<?, ?>) value, componentType));
                } else {
                    list.add(value);
                }
            }
            else if (componentType.isArray()) {
                list.add(getArrayBeanPropertyValue(componentType, value, propertyName));
            }
            else if (Collection.class.isAssignableFrom(componentType)) {
                Collection<?> collection = (Collection<?>) value;
                List<Object> nextList = new ArrayList<Object>(collection.size());
                for (Object object : collection) {
                    nextList.add(getCollectionBeanPropertyValue(BeanTypeUtils.getBindType(componentType), componentType, object, propertyName));
                }
                list.add(nextList);
            }
            else if (Map.class.isAssignableFrom(componentType)) {
                if ((value instanceof Map)) {
                    if (!componentType.equals(value.getClass())) {
                        Map<?, ?> tempValueMap = (Map<?, ?>) BeanTypeUtils.instantiate(componentType);
                        tempValueMap.putAll((Map) value);
                        value = tempValueMap;
                    }
                }
                else {
                    String msg = String.format("Target class<%s> is a map, but the class of value<%s> is not a map. So the value<%s> can not be set to property<%s>.", new Object[] { componentType.getCanonicalName(), value.getClass().getCanonicalName(), value.toString(), propertyName });
					throw new RuntimeException(msg);
                }
                list.add(value);
            }
            else {
                list.add(getSimpleTypePropertyValue(BeanTypeUtils.getBindType(componentType), componentType, value, propertyName));
            }
        }
        return forArray(componentType, list);
    }

    private static Object getCollectionBeanPropertyValue(int bindType, Class<?> bindClass, Object value, String propertyName) {
        if (bindType == BeanTypeUtils.MapToBean_DefautType) {
            if ((value instanceof Map)) {
                Map<?, ?> dataMap = (Map<?, ?>) value;
                return toBean(dataMap, bindClass);
            }
            return value;
        }
        return getSimpleTypePropertyValue(bindType, bindClass, value, propertyName);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object getSimpleTypePropertyValue(int bindType, Class<?> bindClass, Object value, String propertyName) {
        if (value == null) {
            return null;
        }
        
        if (bindType == BeanTypeUtils.MapToBean_NullType) {
            if (bindClass == null) {
                return value;
            }
            if (bindClass.isAssignableFrom(value.getClass())) {
                return value;
            }
            String msg = String.format("The value class<%s> can not be assigned to target class<%s>, so the property<%s> can not be set by value<%s>.", new Object[] { value.getClass().getCanonicalName(), bindClass, propertyName, value.toString() });
			throw new RuntimeException(msg);
        }
        
        if (bindType == BeanTypeUtils.MapToBean_DateType) {
            return parseDate(value);
        }

        if (bindType == BeanTypeUtils.MapToBean_EnumType) {
            return Enum.valueOf((Class)bindClass, value.toString());
        }

        if (bindType == BeanTypeUtils.MapToBean_StringType) {
            return value.toString();
        }

        if (bindType == BeanTypeUtils.MapToBean_NumberType) {
            String str = value.toString();
            if(StringUtils.isEmpty(str)) {
                return null;
            }
            try {
                return bindClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { str });
            }
            catch (Exception exception) {
                String msg = String.format("Invoke the with String parameter constructor fail property %s, cause: %s", new Object[] { propertyName, exception.getMessage() });
				throw new RuntimeException(msg);
            }
        }

        if (bindType == BeanTypeUtils.MapToBean_BooleanType) {
            String str = value.toString();
            if(StringUtils.isEmpty(str)) {
                return null;
            }
            try {
                return bindClass.getConstructor(new Class[] { String.class }).newInstance(new Object[] { str });
            }
            catch (Exception exception) {
                String msg = String.format("Invoke the with String parameter constructor fail, cause: %s", new Object[] { exception.getMessage() });
				throw new RuntimeException(msg);
            }
        }
        
        if (bindType == BeanTypeUtils.MapToBean_CharacterType) {
            String str = value.toString();
            if (str.length() != 1) {
                String msg = String.format("not be a char,length=%d", new Object[] { Integer.valueOf(str.length()) });
				throw new RuntimeException(msg);
            }
            return new Character(str.charAt(0));
        }
        return value;
    }

    private static Date parseDate(Object date) {
        if ((date instanceof Date)) {
			return DateTimeUtils.formatDateToDate((Date) date);
        }
        if ((date instanceof Number)) {
			return DateTimeUtils.formatDateToDate(new Date(((Number) date).longValue()));
        }

        
        String dateStr = date.toString().trim();
        if(StringUtils.isEmpty(dateStr)) {
            return null;
        }
        int length = dateStr.length();
		if (length == DateTimeUtils.yyyy_mm_dd.length()) {
			return DateTimeUtils.formatStringToDate(dateStr, DateTimeUtils.yyyy_mm_dd);
		} else if (length == DateTimeUtils.yyyy_mm_dd_hh_mm_ss.length()) {
			return DateTimeUtils.formatStringToDate(dateStr, DateTimeUtils.yyyy_mm_dd_hh_mm_ss);
		} else if (length == DateTimeUtils.yyyymmdd.length()) {
			return DateTimeUtils.formatStringToDate(dateStr, DateTimeUtils.yyyymmdd);
        } else {
			return DateTimeUtils.formatStringToDate(dateStr);
        }
    }

    private static Object forCollection(Collection<?> arr, Class<?> instanceClass) {
        if (instanceClass.equals(arr.getClass())) {
            return arr;
        }

        @SuppressWarnings("unchecked")
        Collection<Object> temp = (Collection<Object>) BeanTypeUtils.instantiate(instanceClass);
        temp.addAll(arr);
        return temp;
    }

    private static Object forArray(Class<?> instanceClass, List<Object> list) {
        Object temp = Array.newInstance(instanceClass, list.size()); // 初始化数组
        for (int i = 0; i < list.size(); i++) {
            Array.set(temp, i, list.get(i));
        }
        return temp;
    }
}
