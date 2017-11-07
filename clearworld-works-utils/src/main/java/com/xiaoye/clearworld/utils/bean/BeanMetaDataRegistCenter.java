package com.xiaoye.clearworld.utils.bean;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class BeanMetaDataRegistCenter {
    
    /** 存储 bean -- bean的属性 metaData */
	private static Map<Class<?>, List<BeanPropertyMetaData>> beanPropertyMetaDataMaps = new ConcurrentHashMap<>();

    /** 存储 classLoader -- bean List 集合 */
	private static Map<ClassLoader, List<Class<?>>> beanClassMaps = new ConcurrentHashMap<>();
    
    /** 锁 */
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 获取 实体类属性的metaData
     * @param clazz
     * @return
     */
    public static List<BeanPropertyMetaData> getBeanPropertyMetaDatas(Class<?> clazz) {
        List<BeanPropertyMetaData> metaDataList = beanPropertyMetaDataMaps.get(clazz);
        if (metaDataList == null) {
            try {
                lock.lock();
                
				metaDataList = new ArrayList<>();
                PropertyDescriptor[] descriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                for (PropertyDescriptor descriptor : descriptors) {
                    if (!descriptor.getName().equals("class")) {
                        metaDataList.add(new BeanPropertyMetaData(descriptor, clazz));
                    }
                }
                if (metaDataList.size() > 0) {
                    beanPropertyMetaDataMaps.put(clazz, metaDataList);
                    ClassLoader classLoader = clazz.getClassLoader();
                    List<Class<?>> clazzes = beanClassMaps.get(classLoader);
                    if (clazzes == null) {
						clazzes = new ArrayList<>();
                        beanClassMaps.put(classLoader, clazzes);
                    }
                    clazzes.add(clazz);
                }
            }
            catch (Exception e) {
                String message = String.format("BeanMetaDataRegistCenter's method getBeanPropertyMetaDatas error, the error is s%", e.getMessage());
                throw new IllegalArgumentException(message);
            }
            finally {
                lock.unlock();
            }
        }
        return metaDataList;
    }

    /**
     * 根据 classLoader 删除
     * @param classLoader
     */
    public static void clearByClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        try {
            lock.lock();
            List<Class<?>> clazzes = beanClassMaps.get(classLoader);
            if (clazzes != null) {
                for (Class<?> clazz : clazzes) {
                    beanPropertyMetaDataMaps.remove(clazz);
                }
            }
            beanClassMaps.remove(classLoader);
        }
        catch (Exception e) {
            String message = String.format("BeanMetaDataRegistCenter's method clearByClassLoader error, the error is s%", e.getMessage());
            throw new IllegalArgumentException(message);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 清空所有
     */
    public static void clearAll() {
        try {
            lock.lock();
            beanPropertyMetaDataMaps.clear();
            beanClassMaps.clear();
        }
        catch (Exception e) {
            String message = String.format("BeanMetaDataRegistCenter's method clearAll error, the error is s%", e.getMessage());
            throw new IllegalArgumentException(message);
        }
        finally {
            lock.unlock();
        }
    }
}