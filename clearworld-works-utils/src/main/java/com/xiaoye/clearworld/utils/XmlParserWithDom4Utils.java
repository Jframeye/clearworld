/**
 * asdas
 */
package com.xiaoye.clearworld.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @Desc xml xpath 解析器<br>
 *       xpath 语法：
 * @Author yehl
 * @Date 2017年11月7日
 */
public class XmlParserWithDom4Utils {

	/** 文档 **/
	private static Document document;

	/**
	 * 构建xml文档
	 * @param xmlPath
	 * @return
	 */
	public static Document build(String xmlPath) {
		return XmlParserWithDom4Utils.build(xmlPath, null);
	}

	/**
	 * 构建xml文档
	 * @param xmlPath
	 * @param resolver
	 * @return
	 */
	public static Document build(String xmlPath, EntityResolver resolver) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath);
		return XmlParserWithDom4Utils.build(inputStream, resolver);
	}

	/**
	 * 构建xml文档
	 * @param inputStream
	 * @return
	 */
	public static Document build(InputStream inputStream) {
		return XmlParserWithDom4Utils.build(inputStream, null);
	}

	/**
	 * 构建xml文档
	 * @param inputStream
	 * @param resolver
	 * @return
	 */
	public static Document build(InputStream inputStream, EntityResolver resolver) {
		XmlParserWithDom4Utils.document = createDocument(new InputSource(inputStream), resolver);
		return document;
	}

	/**
	 * 构建xml文档的Document
	 * @param inputSource
	 * @param resolver
	 * @return
	 */
	private static Document createDocument(InputSource inputSource, EntityResolver resolver) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputSource);
			document.setEntityResolver(resolver);
			return document;
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static List<Node> evalNodes(String expression) {
		return evalNodes(document.getRootElement(), expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Node> evalNodes(Element element, String expression) {
		check();
		return element.selectNodes(expression);
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Node evalNode(String expression) {
		return evalNode(document.getRootElement(), expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Node evalNode(Element element, String expression) {
		check();
		return element.selectSingleNode(expression);
	}

	private static void check() {
		if (document == null) {
			throw new RuntimeException("the xml document is null, please build xml by XmlXPathUtils.build method");
		}
	}

	/**
	 * xml 数据转为实体Bean
	 * @param xmlPath
	 * @param xpath
	 * @param useAttribute
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T convertXmlToBean(String xmlPath, String xpath, boolean useAttribute, Class<T> clazz) throws Exception {
		build(xmlPath);
		Map<Object, Object> result = convertXmlToMap(xmlPath, xpath, useAttribute);
		return BeanUtils.mapToBean(result, clazz);
	}

	/**
	 * xml 数据转为实体Bean
	 * @param xmlPath
	 * @param xpath
	 * @param useAttribute
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> convertXmlToBeanList(String xmlPath, String xpath, boolean useAttribute, Class<T> clazz) throws Exception {
		build(xmlPath);
		List<T> results = new ArrayList<T>();
		List<Node> nodes = evalNodes(xpath);
		for (Node node : nodes) {
			results.add(BeanUtils.mapToBean(convertXmlToMap((Element) node, useAttribute), clazz));
		}
		return results;
	}

	/**
	 * xml 数据转为Map
	 * @param xmlPath
	 * @param xpath
	 * @param useAttribute
	 * @return
	 * @throws Exception
	 */
	public static Map<Object, Object> convertXmlToMap(String xmlPath, String xpath, boolean useAttribute) throws Exception {
		build(xmlPath);
		Element element = (Element) evalNode(xpath);
		return convertXmlToMap(element, useAttribute);
	}

	/**
	 * xml 数据转为Map
	 * @param element
	 * @param useAttribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Object, Object> convertXmlToMap(Element element, boolean useAttribute) {
		Map<Object, Object> result = new HashMap<>();
		if (useAttribute) {
			Iterator<Attribute> iterator = element.attributeIterator();
			while (iterator.hasNext()) {
				Attribute attribute = iterator.next();
				String fieldName = attribute.getName();
				String fieldValue = attribute.getStringValue();
				result.put(fieldName, fieldValue);
			}
		} else {
			Iterator<Element> iterator = element.elementIterator(); // 获取子节点
			while (iterator.hasNext()) {
				Element child = iterator.next();
				String fieldName = child.getName();
				if (child.elements().size() > 0) { // 如果子节点还有子节点
					result.put(fieldName, convertXmlToMap(child, useAttribute));
				} else {
					String fieldValue = child.getStringValue();
					result.put(fieldName, fieldValue);
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
	}
}
