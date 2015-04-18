package com.persistence.xml.analysisutil;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import org.dom4j.Element;

import com.persistence.exception.ErrorException;
import com.persistence.util.ReadXMLUtil;

/**
 * 将所有的po包下的进行加载的帮助类
 * 
 * @author mastery
 * 
 */
public class AllPoLoaderHelper {

	/**
	 * 根据一开始配置的xml文件,通过传入这个文件的rootElement,得到所配置的po包的包名,类得到Po包实际存在的物理路径,
	 * 得到一个file之后对这个file进行解析得到Po包下所有的Po类名,将之存在Set<String> entitys中
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
		System.out.println("文件路径（字符串）" + filePath);
		File file = null;
		try {
			file = new File(new URI(filePath));
			System.out.println("文件路径是（文件类型）：" + file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (file.exists()) {
			ClassScanHelper classHelper = new ClassScanHelper();
			Set<String> entitys = classHelper.getAllEntity(file, poSrcPath);
			if (entitys.size() == 0) {
				try {
					throw new ErrorException(ReadXMLUtil.SRC_PATH
						+ "文件配置的poSrcPath路径错误！");
				} catch (ErrorException e) {
					e.printStackTrace();
				}
			} else {
				return entitys;
			}
		} else {
			try {
				throw new ErrorException(ReadXMLUtil.SRC_PATH
					+ "文件配置的poSrcPath路径错误！");
			} catch (ErrorException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 替代字符串方法,将字符串中所有的'.'替换成'/'
	 * 
	 * @param poSrcPath
	 * @return
	 */
	private String replaceByPath(String poSrcPath) {
		return poSrcPath.replace(".", "/");
	}

}
