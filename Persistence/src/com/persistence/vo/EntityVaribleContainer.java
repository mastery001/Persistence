package com.persistence.vo;

import java.lang.annotation.Annotation;


/**
 * ʵ�����������,��ʵ�������ע����Ϣ,���������Ϣ,���Ͱ���һ��
 * @author mastery
 *
 */
public class EntityVaribleContainer {
	private String varibleName;
	private String varibleType;
	private String varibleRefClass;
	private Annotation annotation;
	public String getVaribleName() {
		return varibleName;
	}
	public void setVaribleName(String varibleName) {
		this.varibleName = varibleName;
	}
	public String getVaribleType() {
		return varibleType;
	}
	public void setVaribleType(String varibleType) {
		this.varibleType = varibleType;
	}
	public String getVaribleRefClass() {
		return varibleRefClass;
	}
	public void setVaribleRefClass(String varibleRefClass) {
		this.varibleRefClass = varibleRefClass;
	}
	public Annotation getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
	
}
