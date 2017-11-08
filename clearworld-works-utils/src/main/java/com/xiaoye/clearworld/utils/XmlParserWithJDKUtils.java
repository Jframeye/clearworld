/**
 * asdas
 */
package com.xiaoye.clearworld.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @Desc xml xpath 解析器<br>
 *       xpath 语法：
 * @Author yehl
 * @Date 2017年11月7日
 */
public class XmlParserWithJDKUtils {

	/** 文档 **/
	private static Document document;

	/**
	 * 构建xml文档
	 * @param xmlPath
	 * @return
	 */
	public static Document build(String xmlPath) {
		return XmlParserWithJDKUtils.build(xmlPath, false);
	}
	
	/**
	 * 构建xml文档
	 * @param xmlPath
	 * @param validation
	 * @return
	 */
	public static Document build(String xmlPath, boolean validation) {
		return XmlParserWithJDKUtils.build(xmlPath, validation, null);
	}

	/**
	 * 构建xml文档
	 * @param xmlPath
	 * @param validation
	 * @param resolver
	 * @return
	 */
	public static Document build(String xmlPath, boolean validation, EntityResolver resolver) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlPath);
		return XmlParserWithJDKUtils.build(inputStream);
	}

	/**
	 * 构建xml文档
	 * @param inputStream
	 * @return
	 */
	public static Document build(InputStream inputStream) {
		return XmlParserWithJDKUtils.build(inputStream, false);
	}

	/**
	 * 构建xml文档
	 * @param inputStream
	 * @param validation
	 * @return
	 */
	public static Document build(InputStream inputStream, boolean validation) {
		return XmlParserWithJDKUtils.build(inputStream, validation, null);
	}

	/**
	 * 构建xml文档
	 * @param inputStream
	 * @param validation
	 * @param resolver
	 * @return
	 */
	public static Document build(InputStream inputStream, boolean validation, EntityResolver resolver) {
		XmlParserWithJDKUtils.document = createDocument(new InputSource(inputStream), validation, resolver);
		return document;
	}

	/**
	 * 构建xml文档的Document
	 * @param inputSource
	 * @param validation
	 * @param resolver
	 * @return
	 */
	private static Document createDocument(InputSource inputSource, boolean validation, EntityResolver resolver) {
		try {
			XPathFactory xPathFactory = XPathFactory.newInstance();
			xpath = xPathFactory.newXPath();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validation);

			factory.setNamespaceAware(false);
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(false);
			factory.setCoalescing(false);
			factory.setExpandEntityReferences(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(resolver);
			builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void error(SAXParseException exception) throws SAXException {
					throw exception;
				}

				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}

				@Override
				public void warning(SAXParseException exception) throws SAXException {
				}
			});
			return builder.parse(inputSource);
		} catch (Exception e) {
			throw new RuntimeException("Error creating xml document instance.  Cause: " + e, e);
		}
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static String evalString(String expression) {
		return evalString(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static String evalString(Object root, String expression) {
		check();
		return (String) evaluate(expression, root, XPathConstants.STRING);
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Boolean evalBoolean(String expression) {
		return evalBoolean(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Boolean evalBoolean(Object root, String expression) {
		check();
		return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Short evalShort(String expression) {
		return evalShort(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Short evalShort(Object root, String expression) {
		check();
		return Short.valueOf(evalString(root, expression));
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Integer evalInteger(String expression) {
		return evalInteger(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Integer evalInteger(Object root, String expression) {
		check();
		return Integer.valueOf(evalString(root, expression));
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Long evalLong(String expression) {
		return evalLong(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Long evalLong(Object root, String expression) {
		check();
		return Long.valueOf(evalString(root, expression));
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Float evalFloat(String expression) {
		return evalFloat(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Float evalFloat(Object root, String expression) {
		check();
		return Float.valueOf(evalString(root, expression));
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Double evalDouble(String expression) {
		return evalDouble(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Double evalDouble(Object root, String expression) {
		check();
		return (Double) evaluate(expression, root, XPathConstants.NUMBER);
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static List<Node> evalNodes(String expression) {
		return evalNodes(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static List<Node> evalNodes(Object root, String expression) {
		check();
		List<Node> results = new ArrayList<Node>();
		NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			results.add(nodes.item(i));
		}
		return results;
	}

	/**
	 * 获取值
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Node evalNode(String expression) {
		return evalNode(document, expression);
	}

	/**
	 * 获取值
	 * @param root
	 * @param expression xpath 语法表达式<br>
	 * 节点的选取需要从根节点开始（root/node/node……）<br>
	 * 选取某些特定的节点（//node/node）
	 * @return
	 */
	public static Node evalNode(Object root, String expression) {
		check();
		return (Node) evaluate(expression, root, XPathConstants.NODE);
	}

	private static XPath xpath;
	private static Object evaluate(String expression, Object root, QName returnType) {
		try {
			return xpath.evaluate(expression, root, returnType);
		} catch (Exception e) {
			throw new RuntimeException("Error evaluating XPath.  Cause: " + e, e);
		}
	}

	private static void check() {
		if (document == null) {
			throw new RuntimeException("the xml document is null, please build xml by XmlXPathUtils.build method");
		}
	}

	/**
	 * 生成的文件路径
	 * @param xmlPath
	 */
	public static void writeToXml(String xmlPath) {
		try {
			document.normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// 编码
			DOMSource source = new DOMSource(document);
			PrintWriter pw = new PrintWriter(new FileOutputStream(xmlPath));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成的文件
	 * @param file
	 */
	public static void writeToXml(File file) {
		try {
			document.normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// 编码
			DOMSource source = new DOMSource(document);
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		build("env.notify.sms.mysql.xml");
		List<Node> nodes = evalNodes("//entry[@key='url']");
		System.out.println(nodes.size());
		for (Node node : nodes) {
			Element element = (Element) node;
			String value = element.getAttribute("value");
			element.setAttribute("value", value.replace("10.91.19.10", "100.100.100.100"));
			System.out.println(element.getAttribute("value"));
		}
		writeToXml("G:\\clearworld\\clearworld-works-utils\\src\\main\\resources\\env.notify.mysql.xml");
	}
}
