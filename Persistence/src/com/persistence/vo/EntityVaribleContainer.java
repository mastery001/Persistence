package com.persistence.vo;

import java.lang.annotation.Annotation;


/**
 * 实体变量容器类,将实体变量与注解信息,和其类的信息,类型绑定在一起
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
