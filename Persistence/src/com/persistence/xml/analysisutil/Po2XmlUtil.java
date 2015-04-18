package com.persistence.xml.analysisutil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.persistence.annotation.PrimaryKeysAnnotation;
import com.persistence.exception.ErrorException;
import com.persistence.util.AnnotationUtil;
import com.persistence.vo.EntityVaribleContainer;

/**
 * 根据po包下的实体类生成xml节点的工具类
 * 
 * @author mastery
 * 
 */
public class Po2XmlUtil {

	/**
	 * 根据po包下的所有的实体类，生成相对应的xml节点<entity></entity>最后返回一个总的实体类节点rootElement<
	 * entitys><entity> </entity></entitys>
	 * 
	 * @param entitys
	 * @return
	 */
	public Element createXmlByPos(Set<String> entitys)
			throws ClassNotFoundException, ErrorException {
		DocumentFactory documentFactory = new DocumentFactory();
		Element rootElement = documentFactory.createElement("entitys");

		for (String entityName : entitys) {
			Element entity = this.createXmlByPo(documentFactory, entityName);
			rootElement.add(entity);
		}
		return rootElement;
	}

	public Element createXmlByPo(DocumentFactory documentFactory,
			String entityName) throws ClassNotFoundException, ErrorException {
		Class<?> entity = null;
		entity = Class.forName(entityName);
		Field[] fields = entity.getDeclaredFields();
		boolean flag = false;
		for (Field field : fields) {
			Annotation primaryAnnotation = field
					.getAnnotation(PrimaryKeysAnnotation.class);
			if (primaryAnnotation != null) {
				flag = true;
			}
		}
		if (flag == false) {
			throw new ErrorException(entityName + "没有配置主键注解，请重新配置");
		}
		return this
				.createElementIfPrimaryKeyExists(documentFactory, entityName);
	}

	public Element createElementIfPrimaryKeyExists(
			DocumentFactory documentFactory, String entityName)
			throws ClassNotFoundException {
		Element entity = documentFactory.createElement("entity");
		AnnotationUtil annotationUtil = new AnnotationUtil();
		Annotation tableAnnotation = annotationUtil
				.getTableAnnotation(entityName);
		AnnotationContentFetchHelper fetchHelper = new AnnotationContentFetchHelper();
		Map<String, String> entityConfigValue = fetchHelper
				.getAnnotationContent(tableAnnotation);
		if (entityConfigValue.get("table") == null) {
			return null;
		}
		entity.add(documentFactory.createAttribute(entity, "className",
				entityName));
		entity.add(documentFactory.createAttribute(entity, "table",
				entityConfigValue.get("table")));

		this.createElementsForPrimary(documentFactory, fetchHelper,
				annotationUtil, entity, entityName);

		this.createElementsForForeign(documentFactory, fetchHelper,
				annotationUtil, entity, entityName);

		this.createElementsForVarible(documentFactory, fetchHelper,
				annotationUtil, entity, entityName);
		return entity;
	}

	public void createElementsForVarible(DocumentFactory documentFactory,
			AnnotationContentFetchHelper fetchHelper,
			AnnotationUtil annotationUtil, Element entity, String entityName)
			throws ClassNotFoundException {
		List<EntityVaribleContainer> containers = annotationUtil
				.getOnVaribleAnnotation(entityName);

		for (EntityVaribleContainer container : containers) {
			Element idElement = documentFactory.createElement("property");

			this.addAttributeForElement(documentFactory, idElement, container,
					fetchHelper);
			entity.add(idElement);
		}
	}


	public void createElementsForForeign(DocumentFactory documentFactory,
			AnnotationContentFetchHelper fetchHelper,
			AnnotationUtil annotationUtil, Element entity, String entityName) throws ClassNotFoundException {
		List<EntityVaribleContainer> containers = annotationUtil
				.getOnVaribleAnnotation(entityName);

		for (EntityVaribleContainer container : containers) {
			Element idElement = documentFactory.createElement("property");

			this.addAttributeForElement(documentFactory, idElement, container,
					fetchHelper);
			entity.add(idElement);
		}

	}

	public void createElementsForPrimary(DocumentFactory documentFactory,
			AnnotationContentFetchHelper fetchHelper,
			AnnotationUtil annotationUtil, Element entity, String entityName)  throws ClassNotFoundException  {
		List<EntityVaribleContainer> containers = annotationUtil
				.getOnVaribleAnnotation(entityName);

		for (EntityVaribleContainer container : containers) {
			Element idElement = documentFactory.createElement("property");

			this.addAttributeForElement(documentFactory, idElement, container,
					fetchHelper);
			entity.add(idElement);
		}

	}
	

	public void addAttributeForElement(DocumentFactory documentFactory,
			Element idElement, EntityVaribleContainer container,
			AnnotationContentFetchHelper fetchHelper) {
		idElement.add(documentFactory.createAttribute(idElement, "name", container.getVaribleName()));
		
		if(container.getVaribleType() != null) {
			idElement.add(documentFactory.createAttribute(idElement, "type", container.getVaribleType()));
		}
		
		if(container.getVaribleRefClass() != null){
			idElement.add(documentFactory.createAttribute(idElement, "className", container.getVaribleRefClass()));
		}
		
		if(container.getAnnotation() == null) {
			idElement.add(documentFactory.createAttribute(idElement, "column", container.getVaribleName()));
		}else {
			Map<String , String > configValues = fetchHelper.getAnnotationContent(container.getAnnotation());
			System.out.println("注解的详细信息为：" + configValues);
			Set<String> keys = configValues.keySet();
			Iterator<String> iterator = keys.iterator();
			while(iterator.hasNext()) {
				String configName = iterator.next();
				String configValue = configValues.get(configName);
				idElement.add(documentFactory.createAttribute(idElement, configName, configValue));
			}
			if(configValues.get("column").equals("")) {
				idElement.add(documentFactory.createAttribute(idElement, "column", container.getVaribleName()));
			}
		}
	}

}
