package com.xiaoye.clearworld.utils;

import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @Desc JAXB XML 工具类
 * @Author yehl
 * @Date 2017年11月2日
 */
public class JAXBXmlUtils {

	/**
	 * 将xml文件内容转化为JAXB Bean实体类
	 * @param xmlPath
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertXmlToBean(String xmlPath, Class<T> clazz) throws Exception {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(JAXBXmlUtils.class.getClassLoader().getResourceAsStream(xmlPath));
		} catch (JAXBException exception) {
			throw exception;
		}
	}

	/**
	 * 将JAXB Bean实体转为xml写入文件
	 * @param xmlOutPath
	 * @param bean
	 * @throws Exception
	 */
	public static <T> void convertBeanToXml(String xmlOutPath, T bean) throws Exception {
		try {
			JAXBContext context = JAXBContext.newInstance(bean.getClass());
			Marshaller marshaller = context.createMarshaller();

			// output pretty printed
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			marshaller.marshal(bean, new FileOutputStream(JAXBXmlUtils.class.getClassLoader().getResource(xmlOutPath).getPath()));
			// marshaller.marshal(bean, System.out); // 打印
		} catch (Exception exception) {
			throw exception;
		}
	}
}
