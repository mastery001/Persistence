package com.persistence.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import com.persistence.exception.ErrorException;
import com.persistence.xml.analysisutil.AllPoLoaderHelper;
import com.persistence.xml.analysisutil.Po2XmlUtil;

public class ReadXMLUtil {

	// dom4j中读取xml文件的类
	private static SAXReader saxReader = null;

	private static Element rootElement = null;
	private static Element entitysElement = null;
	public final static String SRC_PATH = "entitysTable.xml";
	public final static String DEC_PATH = "entitys.xml";

	static {
		saxReader = new SAXReader();
		InputStream is = ReadXMLUtil.class.getClassLoader()
				.getResourceAsStream(DEC_PATH);
		try {
			int size = is.available();
			if (size == 0) {
				rootElement = getRootElementByPath(SRC_PATH);
			} else {
				rootElement = getDocument(DEC_PATH).getRootElement();
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public static Element getRootElement() {
		return rootElement;
	}
	
	/**
	 *  根据开始配置的源xml文件来生成rootElement节点.
	 * @param srcPath
	 * @return
	 * @throws Exception
	 */
	public static Element getRootElementByPath(String srcPath)  throws Exception {
		Element rootElement = null;

		rootElement = getDocument(srcPath).getRootElement();
		if (getPoPathByXmlConfig(rootElement) != null) {
			if (rootElement.elements("entity").size() != 0) {
				throw new ErrorException("你已经配置好了entity不需要再生成配置了！");
			}
			// 根据po包下的实体类生成XML节点的工具类
			Po2XmlUtil xmlUtil = new Po2XmlUtil();
			rootElement = xmlUtil.createXmlByPos(new AllPoLoaderHelper()
					.loadAllPoConfig(rootElement));
			entitysElement = rootElement;

			createXmlByEntity(entitysElement);
		}
		return rootElement;
	}

	
	/**
	 * 根据路径来返回xml文档的文档对象(document)
	 * @param decPath
	 * @return
	 */
	public static Document getDocument(String decPath){
		InputStream is = ReadXMLUtil.class.getClassLoader()
				.getResourceAsStream(decPath);
		Document doc = null;
		try {
			doc = saxReader.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}
	
	/**
	 * 根据传入的Class类来匹配xml文件中的Element节点，存在则返回这个Class类的Element，否则返回为空
	 * @param entityClass
	 * @return
	 */
	public static Element getElement(Class<?> entityClass) {
		Element classElement = null;
		
		Iterator<Element> elementIterator = getRootElement().elementIterator();
		while(elementIterator.hasNext()) {
			Element childrenElement = elementIterator.next();
			if(entityClass.getName().equals(childrenElement.attributeValue("className"))) {
				classElement = childrenElement;
				break;
			}
		}
		return classElement;
	}
	
	/**
	 * 判断一个Class类是否在生成的xml文件中存在，存在则返回true，否在返回false
	 * @param t
	 * @return
	 */
	public static Boolean getExistOfRootElement(Class<?> t) {
		Boolean result = false;
		
		Iterator<Element> elementIterator = getRootElement().elementIterator();
		while(elementIterator.hasNext()) {
			Element childrenElement = elementIterator.next();
			if(t.getName().equals(childrenElement.attributeValue("className"))) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private static void createXmlByEntity(Element entitysElement) throws Exception{
		String urlPath = ReadXMLUtil.class.getClassLoader().getResource(DEC_PATH).toString();
		urlPath = urlPath.replace("bin", "src");
		System.out.println(urlPath);
		File file = new File(new URI(urlPath));
		Writer writer = new FileWriter(file);
		OutputFormat outputFotmat = OutputFormat.createCompactFormat();
		XMLWriter xmlWriter = new XMLWriter(writer , outputFotmat);
		
		Document document = DocumentFactory.getInstance().createDocument();
		
		document.add(entitysElement);
		
		xmlWriter.write(document);
		
		xmlWriter.close();
	}

	/**
	 * 根据最初配置的xml文件中，拿到po类所在的包名
	 * @param rootElement
	 * @return
	 */
	public static String getPoPathByXmlConfig(Element rootElement) {
		String result = null;
		Element element = rootElement.element("usePoSrcPath");
		if(element == null) {
			
		}else {
			Attribute attribute = element.attribute("path");
			String text = attribute.getText();
			if(text == null || text.equals("")) {
				
			}else {
				result = text;
			}
		}
 		return result;
	}
	
	@Test
	public void test() {
		System.out.println(ReadXMLUtil.getRootElement());
	}
}
