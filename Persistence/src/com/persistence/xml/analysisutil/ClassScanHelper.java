package com.persistence.xml.analysisutil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 对po包下所有的类进行解析得到其类名的帮助类
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
		// TODO 自动生成的方法存根
		return this.classSet;
	}

	/**
	 * 通过传入的路径来进行判断解析其路径下所有的文件，通过指定的过滤器来找到解析所需要的名字
	 * @param file
	 * @param poSrcPath
	 */
	public void scanFolderToGetClass(File file, String poSrcPath) {
		File[] files = file.listFiles(new ClassFileFilterHelper());
		for(File fileItem : files) {
			//判断文件是否是目录
			if(fileItem.isDirectory()) {
				this.scanFolderToGetClass(fileItem, poSrcPath + "." + fileItem.getName());
			}
			classSet.add(poSrcPath + "." + fileItem.getName().substring(0, fileItem.getName().lastIndexOf(".")));
		}
		
	}

}
