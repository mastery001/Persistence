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

	// dom4j�ж�ȡxml�ļ�����
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}

	public static Element getRootElement() {
		return rootElement;
	}
	
	/**
	 *  ���ݿ�ʼ���õ�Դxml�ļ�������rootElement�ڵ�.
	 * @param srcPath
	 * @return
	 * @throws Exception
	 */
	public static Element getRootElementByPath(String srcPath)  throws Exception {
		Element rootElement = null;

		rootElement = getDocument(srcPath).getRootElement();
		if (getPoPathByXmlConfig(rootElement) != null) {
			if (rootElement.elements("entity").size() != 0) {
				throw new ErrorException("���Ѿ����ú���entity����Ҫ�����������ˣ�");
			}
			// ����po���µ�ʵ��������XML�ڵ�Ĺ�����
			Po2XmlUtil xmlUtil = new Po2XmlUtil();
			rootElement = xmlUtil.createXmlByPos(new AllPoLoaderHelper()
					.loadAllPoConfig(rootElement));
			entitysElement = rootElement;

			createXmlByEntity(entitysElement);
		}
		return rootElement;
	}

	
	/**
	 * ����·��������xml�ĵ����ĵ�����(document)
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
	 * ���ݴ����Class����ƥ��xml�ļ��е�Element�ڵ㣬�����򷵻����Class���Element�����򷵻�Ϊ��
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
	 * �ж�һ��Class���Ƿ������ɵ�xml�ļ��д��ڣ������򷵻�true�����ڷ���false
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
	 * ����������õ�xml�ļ��У��õ�po�����ڵİ���
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
