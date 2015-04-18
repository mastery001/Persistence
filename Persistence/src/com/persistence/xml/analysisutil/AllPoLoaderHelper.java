package com.persistence.xml.analysisutil;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import org.dom4j.Element;

import com.persistence.exception.ErrorException;
import com.persistence.util.ReadXMLUtil;

/**
 * �����е�po���µĽ��м��صİ�����
 * 
 * @author mastery
 * 
 */
public class AllPoLoaderHelper {

	/**
	 * ����һ��ʼ���õ�xml�ļ�,ͨ����������ļ���rootElement,�õ������õ�po���İ���,��õ�Po��ʵ�ʴ��ڵ�����·��,
	 * �õ�һ��file֮������file���н����õ�Po�������е�Po����,��֮����Set<String> entitys��
	 * 
	 * @param rootElement
	 * @return
	 */
	public Set<String> loadAllPoConfig(Element rootElement) {
		String poSrcPath = ReadXMLUtil.getPoPathByXmlConfig(rootElement);

		URL url = AllPoLoaderHelper.class.getClassLoader().getResource(
				ReadXMLUtil.SRC_PATH);
		String filePath = url.toString();
		filePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/"
				+ replaceByPath(poSrcPath);
		System.out.println("�ļ�·�����ַ�����" + filePath);
		File file = null;
		try {
			file = new File(new URI(filePath));
			System.out.println("�ļ�·���ǣ��ļ����ͣ���" + file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (file.exists()) {
			ClassScanHelper classHelper = new ClassScanHelper();
			Set<String> entitys = classHelper.getAllEntity(file, poSrcPath);
			if (entitys.size() == 0) {
				try {
					throw new ErrorException(ReadXMLUtil.SRC_PATH
						+ "�ļ����õ�poSrcPath·������");
				} catch (ErrorException e) {
					e.printStackTrace();
				}
			} else {
				return entitys;
			}
		} else {
			try {
				throw new ErrorException(ReadXMLUtil.SRC_PATH
					+ "�ļ����õ�poSrcPath·������");
			} catch (ErrorException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ����ַ�������,���ַ��������е�'.'�滻��'/'
	 * 
	 * @param poSrcPath
	 * @return
	 */
	private String replaceByPath(String poSrcPath) {
		return poSrcPath.replace(".", "/");
	}

}
