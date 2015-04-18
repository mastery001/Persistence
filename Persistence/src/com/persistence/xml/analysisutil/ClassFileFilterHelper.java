package com.persistence.xml.analysisutil;

import java.io.File;
import java.io.FileFilter;

/**
 * �ļ������������࣬��һ���ļ��������е��ļ�����ָ���Ĺ���
 * @author mastery
 *
 */
public class ClassFileFilterHelper implements FileFilter {

	/* ���� Javadoc��
	 * 
	 * ���˷�����ֻ���غ�׺Ϊ.class���ļ�
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
