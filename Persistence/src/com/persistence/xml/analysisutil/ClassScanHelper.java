package com.persistence.xml.analysisutil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * ��po�������е�����н����õ��������İ�����
 * @author mastery
 *
 */
public class ClassScanHelper {

	private Set<String> classSet = new HashSet<String>();
	
	public Set<String> getAllEntity(File file, String poSrcPath) {
		Set<String> result = new HashSet<String>();
		
		this.scanFolderToGetClass(file , poSrcPath);
		
		for(String classItem : getClassSet() ) {
			result.add(classItem);
		}
		return result;
	}

	public Set<String> getClassSet() {
		// TODO �Զ����ɵķ������
		return this.classSet;
	}

	/**
	 * ͨ�������·���������жϽ�����·�������е��ļ���ͨ��ָ���Ĺ��������ҵ���������Ҫ������
	 * @param file
	 * @param poSrcPath
	 */
	public void scanFolderToGetClass(File file, String poSrcPath) {
		File[] files = file.listFiles(new ClassFileFilterHelper());
		for(File fileItem : files) {
			//�ж��ļ��Ƿ���Ŀ¼
			if(fileItem.isDirectory()) {
				this.scanFolderToGetClass(fileItem, poSrcPath + "." + fileItem.getName());
			}
			classSet.add(poSrcPath + "." + fileItem.getName().substring(0, fileItem.getName().lastIndexOf(".")));
		}
		
	}

}
