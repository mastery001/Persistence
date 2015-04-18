package com.persistence.xml.analysisutil;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤器帮助类，对一个文件夹下所有的文件进行指定的过滤
 * @author mastery
 *
 */
public class ClassFileFilterHelper implements FileFilter {

	/* （非 Javadoc）
	 * 
	 * 过滤方法，只返回后缀为.class的文件
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File pathName) {
		boolean result = true;
		if(pathName.isFile()) {
			String fileName = pathName.getName();
			String filterName = fileName.substring(fileName.lastIndexOf(".") , fileName.length());
			
			if(!filterName.equals(".class")) {
				result = false;
			}
		}
		return result;
	}

}
