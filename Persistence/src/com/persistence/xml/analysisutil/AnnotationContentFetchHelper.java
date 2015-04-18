package com.persistence.xml.analysisutil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.persistence.po.Student;

/**
 * 获取这个注解的详细信息的工具类
 * 
 * @author mastery
 * 
 */
public class AnnotationContentFetchHelper {

	/**
	 * 获取注解内部的详细信息，插入到Map集合当中
	 * @param annotation
	 * @return
	 */
	public Map<String, String> getAnnotationContent(Annotation annotation) {
		Map<String, String> map = null;
		String annotationToString = annotation.toString();
		annotationToString = annotationToString.substring(
				annotationToString.indexOf("(") + 1,
				annotationToString.lastIndexOf(")"));
		System.out.println(annotationToString);
		if(annotationToString == null || "".equals(annotationToString)) {
			System.out.println("不存在信息");
		}else {
			map = new HashMap<String , String>();
			String messages[] = annotationToString.split(",");
			if(messages.length == 1) {
				String[] message0 = messages[0].split("=");
				map.put(message0[0], message0[1]);
			}else {
				for(String message : messages) {
					String[] message0 = message.split("=");
					map.put(message0[0], message0[1]);
				}
			}
		}
		return map;
	}

	@Test
	public void test() {
		//Map<String , String> map = getAnnotationContent(Student.class.getAnnotations()[0]);
		
		Map<String , String> map = getAnnotationContent(Student.class.getDeclaredFields()[0].getAnnotations()[0]);
		System.out.println(map.size());
	}
}
