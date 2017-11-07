package com.xiaoye.clearworld.utils.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 *<p> Title: BeanPropertyMetaData </p>
 *<p> Description: 实体类属性metaData </p>
 *<p> Copyright: openlo.cn Copyright (C) 2017 </p>
 *
 * @author yehl
 * @version
 * @since 2017年3月19日
 */
public class BeanPropertyMetaData {
    
    /** 实体类 */
    private Class<?> beanClass;
    
    /** PropertyDescriptor描述Java Bean中通过一对存储器方法（getter / setter）导出的一个属性 */
    private PropertyDescriptor propertyDescriptor;
    
    /** 属性名称 */
    private String propertyName;
    
    /** 属性类型 */
    private Class<?> propertyType;
    
    /** 数组或集合元素类型【即泛型】 */
    private Class<?> componentInstanceClass;
    
    private int bindType;
    
    private Class<?> bindClass;
    
    private boolean isByte = false;
    
    private boolean isArray = false;
    
    private boolean isCollection = false;
    
    private boolean isMap = false;
    
    public BeanPropertyMetaData(PropertyDescriptor propertyDescriptor, Class<?> beanClass) {
        this.propertyDescriptor = propertyDescriptor;
        this.beanClass = beanClass;
        
        this.propertyName = this.propertyDescriptor.getName();
        this.propertyType = this.propertyDescriptor.getPropertyType();
        
        if (propertyType.isArray()) { // 属性是数组
            isArray = true;
            Class<?> componentClass = propertyType.getComponentType(); // 获取数组类型
            if ((componentClass.equals(Byte.class)) || (componentClass.equals(Byte.TYPE))) {
                isByte = true;
            }
            componentInstanceClass = BeanTypeUtils.getBindClass(componentClass);
        }
        else if (Collection.class.isAssignableFrom(propertyType)) { // 属性是Collection集合 isAssignableFrom 判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口
            isCollection = true;
            Type genericType = getGenericType(this.propertyDescriptor);
            Class<?> genericClass = getGenericClass(genericType, 0, propertyName);
            componentInstanceClass = BeanTypeUtils.getBindClass(genericClass);
            bindClass = BeanTypeUtils.getBindClass(propertyType);
        }
        else if (Map.class.isAssignableFrom(propertyType)) {
            isMap = true;
            Type genericType = getGenericType(this.propertyDescriptor);
            Class<?> genericClass = getGenericClass(genericType, 1, propertyName);
            componentInstanceClass = BeanTypeUtils.getBindClass(genericClass);
            bindClass = BeanTypeUtils.getBindClass(propertyType);
        }
        else {
            bindClass = BeanTypeUtils.getBindClass(propertyType);
            bindType = BeanTypeUtils.getBindType(bindClass);
        }
    }

    /**
     * 获取属性的Type对象【即集合的泛型类】
     * @param descriptor
     * @return
     */
    private Type getGenericType(PropertyDescriptor descriptor) {
        if (descriptor.getReadMethod() != null) {
            return descriptor.getReadMethod().getGenericReturnType();
        }
        if (descriptor.getWriteMethod() != null) {
            return descriptor.getWriteMethod().getGenericParameterTypes()[0];
        }
        String msg = String.format("Property<%s> does not contain ReadMethod nor WriteMethod", new Object[] { descriptor.getName() });
        throw new IllegalArgumentException(msg);
    }

    /**
     * 获取属性的Type对象的Class【即集合的泛型类的Class】
     * @param type
     * @param index
     * @param propertyName
     * @return
     */
    private Class<?> getGenericClass(Type type, int index, String propertyName) {
        if ((type instanceof ParameterizedType)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypes = parameterizedType.getActualTypeArguments(); // 表示此类型实际类型参数的 Type 对象的数组
            if (actualTypes.length > index) {
                Type actualType = actualTypes[index];
                if ((actualType instanceof Class)) {
                    Class<?> actualTypeClass = (Class<?>) actualType;
                    return actualTypeClass;
                }

                if ((actualType instanceof ParameterizedType)) {
                    ParameterizedType nextParameterizedType = (ParameterizedType) actualType;
                    if (checkNextGenericClassIsNotBean(nextParameterizedType)) {
                        String msg = String.format("The next actual type<%s> at index<%d> is a ParameterizedType. And the final type is a bean. The related property is <%s>", new Object[] { nextParameterizedType.getActualTypeArguments().toString(), Integer.valueOf(index), propertyName });
                        throw new IllegalArgumentException(msg);
                    }
                    return (Class<?>) nextParameterizedType.getRawType();
                }
                String msg = String.format("The nested type<%s> at index<%d> is not a class nor ParameterizedType. The related property is <%s>", new Object[] { actualType.toString(), Integer.valueOf(index), propertyName });
                throw new IllegalArgumentException(msg);
            }
            String msg = String.format("The parameterized type<%s> does not contain the actural type at index<%d>. The related property is <%s>", new Object[] { parameterizedType.getActualTypeArguments().toString(), Integer.valueOf(index), propertyName });
            throw new IllegalArgumentException(msg);
        }

        String msg = String.format("The type<%s> is not ParameterizedType. The related property is <%s>", new Object[] { type.toString(), propertyName });
        throw new IllegalArgumentException(msg);
    }

    /**
     * 递归检验泛型是否是实体
     * @param nextParameterizedType
     * @return
     */
    private boolean checkNextGenericClassIsNotBean(ParameterizedType nextParameterizedType) {
        Type[] nextActualTypes = nextParameterizedType.getActualTypeArguments();
        Type nextActualType = nextActualTypes[(nextActualTypes.length - 1)];
        if ((nextActualType instanceof Class)) {
            Class<?> clazz = (Class<?>) nextActualType;
            return BeanTypeUtils.isBean(clazz);
        }

        if ((nextActualType instanceof ParameterizedType)) {
            return checkNextGenericClassIsNotBean((ParameterizedType) nextActualType);
        }
        return true;
    }

    /**
     * 设置属性值
     * @param target
     * @param value
     */
    public void setPropertyValue(Object target, Object value) {
        Method writer = propertyDescriptor.getWriteMethod();
        if (writer == null) {
            String msg = String.format("The property<%s> does not have write method.", new Object[] { propertyName });
            throw new IllegalArgumentException(msg);
        }
        try {
            writer.invoke(target, new Object[] { value });
        }
        catch (Exception exception) {
            String msg = String.format("The property<%s>  write method invoke fail, cause: %s.",
                new Object[] { propertyName, exception.getMessage() });
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 获取属性值
     * @param target
     * @return
     */
    public Object getPropertyValue(Object target) {
        Method read = propertyDescriptor.getReadMethod();
        if (read == null) {
            String msg = String.format("The property<%s> does not have read method.", new Object[] { propertyName });
            throw new IllegalArgumentException(msg);
        }
        try {
            return read.invoke(target, new Object[0]);
        }
        catch (Exception exception) {
            String msg = String.format("The property<%s>  read method invoke fail, cause: %s.", new Object[] { propertyName, exception.getMessage() });
            throw new IllegalArgumentException(msg);
        }
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isArray() {
        return isArray;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isMap() {
        return isMap;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public boolean isByte() {
        return isByte;
    }

    public Class<?> getComponentInstanceClass() {
        return componentInstanceClass;
    }

    public int getBindType() {
        return bindType;
    }

    public Class<?> getBindClass() {
        return bindClass;
    }

   public Class<?> getPropertyType() {
       return propertyType;
   }
}