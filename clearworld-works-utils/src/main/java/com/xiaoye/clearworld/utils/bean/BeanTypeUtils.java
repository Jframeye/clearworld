package com.xiaoye.clearworld.utils.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class BeanTypeUtils {

	private static final Map<Class<?>, Class<?>> TYPES_MAP = new HashMap<>();

    static {
        /** 八种基本的数据类型 */
        TYPES_MAP.put(Integer.TYPE, Integer.class);
        TYPES_MAP.put(Double.TYPE, Double.class);
        TYPES_MAP.put(Boolean.TYPE, Boolean.class);
        TYPES_MAP.put(Long.TYPE, Long.class);
        TYPES_MAP.put(Character.TYPE, Character.class);
        TYPES_MAP.put(Float.TYPE, Float.class);
        TYPES_MAP.put(Short.TYPE, Short.class);
        TYPES_MAP.put(Byte.TYPE, Byte.class);

        /** 集合类型 */
        TYPES_MAP.put(Collection.class, ArrayList.class);
        TYPES_MAP.put(List.class, ArrayList.class);
        TYPES_MAP.put(LinkedList.class, LinkedList.class);
        TYPES_MAP.put(ArrayList.class, ArrayList.class);
        TYPES_MAP.put(Set.class, HashSet.class);
        TYPES_MAP.put(HashSet.class, HashSet.class);
        TYPES_MAP.put(LinkedHashSet.class, LinkedHashSet.class);
        TYPES_MAP.put(Map.class, HashMap.class);
        TYPES_MAP.put(HashMap.class, HashMap.class);
        TYPES_MAP.put(Hashtable.class, Hashtable.class);
        TYPES_MAP.put(Properties.class, Properties.class);
        TYPES_MAP.put(LinkedHashMap.class, LinkedHashMap.class);
    }

    private static Class<?> getMappedClass(Class<?> clazz) {
        return (Class<?>) TYPES_MAP.get(clazz);
    }

    /**
     * 获取相应的Class
     * @param clazz
     * @return
     */
    public static Class<?> getBindClass(Class<?> clazz) {
        Class<?> tempClass = getMappedClass(clazz);
        if (tempClass == null) {
            tempClass = clazz;
        }
        return tempClass;
    }

    /**
     * 实例化对象
     * @param clazz
     * @return
     */
    public static <T> T instantiate(Class<T> clazz) {
        if (clazz.isInterface()) {
            String msg = String.format("%s is a an interface, can not be instantiated.", new Object[] { clazz.getCanonicalName() });
            throw new IllegalArgumentException(msg);
        }
        
        try {
            return clazz.newInstance();
        }
        catch (Exception exception) {
            String msg = String.format("%s  instantiate fail, cause: %s.", new Object[] { clazz.getCanonicalName(), exception.getMessage() });
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 是否是普通实体类
     * @param clazz
     * @return
     */
    public static boolean isBean(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return false;
        }
        if (clazz.isEnum()) {
            return false;
        }
        if (clazz.isArray()) {
            return false;
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return false;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return false;
        }
        if ((clazz.getCanonicalName().startsWith("java")) || (clazz.getCanonicalName().startsWith("javax"))) {
            return false;
        }
        return true;
    }

    /** 空类型 */
    public static final int MapToBean_NullType = 0;
    
    /** 空类型 */
    public static final int MapToBean_DefautType = 1;

    /** 时间类型 */
    public static final int MapToBean_DateType = 2;
    
    /** 枚举类型 */
    public static final int MapToBean_EnumType = 3;
    
    /** 字符串类型 */
    public static final int MapToBean_StringType = 4;
    
    /** 数据类型：包括AtomicInteger, AtomicLong, BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short */
    public static final int MapToBean_NumberType = 5;
    
    public static final int MapToBean_BooleanType = 6;

    public static final int MapToBean_CharacterType = 8;
    
    /**
     * 获取类型标志
     * @param clazz
     * @return
     */
    public static int getBindType(Class<?> clazz) {
        if (clazz == null) {
            return MapToBean_NullType;
        }
        else if (clazz.equals(String.class)) {
            return MapToBean_StringType;
        }
        else if (Number.class.isAssignableFrom(clazz)) {
            return MapToBean_NumberType;
        }
        else if (Boolean.class.equals(clazz)) {
            return MapToBean_BooleanType;
        }
        else if (Character.class.equals(clazz)) {
            return MapToBean_CharacterType;
        }
        else if ((Date.class.isAssignableFrom(clazz)) || (Calendar.class.equals(clazz))) {
            return MapToBean_DateType;
        }
        else if (clazz.isEnum()) {
            return MapToBean_EnumType;
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
            return MapToBean_DefautType;
        }
        else if (Map.class.isAssignableFrom(clazz)) {
            return MapToBean_DefautType;
        }
        else if (clazz.isArray()) {
            return MapToBean_DefautType;
        }
        else if ((clazz.getCanonicalName().startsWith("java")) || (clazz.getCanonicalName().startsWith("javax"))) {
            return MapToBean_NullType;
        } else {
            return MapToBean_NullType;
        }
    }
}