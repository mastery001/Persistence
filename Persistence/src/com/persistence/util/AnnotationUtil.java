package com.persistence.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.persistence.annotation.ForeignKeysAnnotation;
import com.persistence.annotation.PrimaryKeysAnnotation;
import com.persistence.annotation.TableAnnotation;
import com.persistence.annotation.VaribleAnnotation;
import com.persistence.po.Score;
import com.persistence.po.Student;
import com.persistence.vo.EntityVaribleContainer;

/**
 * ע�⹤���࣬��ע����Ϣ���д���
 * 
 * @author mastery
 * 
 */
public class AnnotationUtil {

	/**
	 * 
	 * ���ݴ��ݹ�����ʵ����Class����õ����ϵı���ע��
	 * 
	 * @param entity
	 * @return
	 */
	public String getAnnotationTableName(Class<?> entity) {
		String tableName = null;
		if (entity.isAnnotationPresent(TableAnnotation.class)) {
			TableAnnotation tableAnnotation = entity
					.getAnnotation(TableAnnotation.class);
			tableName = tableAnnotation.table();
		}
		return tableName;
	}

	/**
	 * ͨ�������ʵ�����Class����,ͨ����ȡ��������ϵ�ע������ȡ�����ϵ�����ע��
	 * 
	 * @param entity
	 * @return
	 */
	public String getAnnotationPrimaryNameByMethod(Class<?> entity) {
		String primaryName = null;
		Method[] methods = entity.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(PrimaryKeysAnnotation.class)) {
				PrimaryKeysAnnotation primaryKeysAnnotation = method
						.getAnnotation(PrimaryKeysAnnotation.class);
				primaryName = primaryKeysAnnotation.column();
				break;
			}
		}
		return primaryName;
	}

	/**
	 * ͨ�������ʵ�����Class����,ͨ����ȡ��������ϵ�ע������ȡ�����ϵ�����ע��
	 * 
	 * @param entity
	 * @return
	 */
	public String getAnnotationPrimaryNameByField(Class<?> entity) {
		String primaryName = "";
		Field[] Fields = entity.getDeclaredFields();
		for (Field Field : Fields) {
			if (Field.isAnnotationPresent(PrimaryKeysAnnotation.class)) {
				PrimaryKeysAnnotation primaryKeysAnnotation = Field
						.getAnnotation(PrimaryKeysAnnotation.class);
				primaryName += primaryKeysAnnotation.column() + ",";
			}
		}
		return primaryName.substring(0, primaryName.lastIndexOf(","));
	}

	/**
	 * ͨ�������������������������,����ȡ�������������ı��ע��
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Annotation getTableAnnotation(String className)
			throws ClassNotFoundException {
		return Class.forName(className).getAnnotation(TableAnnotation.class);
	}
	
	/**
	 * 
	 * ͨ�������ʵ���������������,ȡ���������������ϵ�����ע����Ϣ,������,ע��,��������,Class���Ͱ���һ��,��װ��һ��������
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public List<EntityVaribleContainer> getEntityVaribleContainerByPrimary(String className) throws ClassNotFoundException{
		List<EntityVaribleContainer> entityVaribleContainerList = new ArrayList<EntityVaribleContainer>();
		Field[] fields = Class.forName(className).getDeclaredFields();
		for(Field field : fields ) {
			Annotation[] annotations = field.getAnnotations();
			for(Annotation annotation : annotations) {
				if(annotation instanceof PrimaryKeysAnnotation) {
					EntityVaribleContainer entityVaribleContainer = new EntityVaribleContainer();
					entityVaribleContainer.setAnnotation(annotation);
					entityVaribleContainer.setVaribleName(field.getName());
					String fieldType = field.getType().getSimpleName().toLowerCase();
					if(!fieldType.equals("int")) {
						String a = fieldType.substring(0,1).toUpperCase();
						String b = fieldType.substring(1,fieldType.length());
						fieldType = a + b;
					}else {
						fieldType = "Integer";
					}
					entityVaribleContainer.setVaribleType(fieldType);
					entityVaribleContainerList.add(entityVaribleContainer);
				}
			}
		}
		return entityVaribleContainerList;
	}
	
	/**
	 * ͨ�������ʵ���������������,ȡ���������������ϵ����ע����Ϣ,������,ע��,��������,Class���Ͱ���һ��,��װ��һ��ʵ���������������,
	 * ��󷵻�һ��List<EntityVaribleContainer> container����
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public List<EntityVaribleContainer> getEntityVaribleContainerByForeign(String className) throws ClassNotFoundException {
		List<EntityVaribleContainer> container = new ArrayList<EntityVaribleContainer>(4);
		Field[] fields = Class.forName(className).getDeclaredFields();
		for(Field field : fields ) {
			Annotation[] annotations = field.getAnnotations();
			for(Annotation annotation : annotations) {
				if(annotation instanceof ForeignKeysAnnotation) {
					EntityVaribleContainer entityVaribleContainer = new EntityVaribleContainer();
					entityVaribleContainer.setAnnotation(annotation);
					entityVaribleContainer.setVaribleName(field.getName());
					entityVaribleContainer.setVaribleRefClass(field.getType().getName());
				}
			}
		}
		return container;
	}
	
	/**
	 *  ͨ�������ʵ���������������,ȡ���������������ϵı���ע����Ϣ,������,ע��,��������,Class���Ͱ���һ��,��װ��һ��ʵ���������������,
	 * ��󷵻�һ��List<EntityVaribleContainer> entityVaribleContainer
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public List<EntityVaribleContainer> getOnVaribleAnnotation(String className) throws ClassNotFoundException {
		List<EntityVaribleContainer> entityVaribleContainer = new ArrayList<EntityVaribleContainer>();
		Field[] fields = Class.forName(className).getDeclaredFields();
		
		for(Field field : fields) {
			boolean isPrimary = false;
			
			Annotation varibleAnnotation = null;
			
			Annotation[] fieldsAnnotations = field.getAnnotations();
			for(Annotation annotation : fieldsAnnotations) {
				if(annotation instanceof PrimaryKeysAnnotation || annotation instanceof ForeignKeysAnnotation) {
					isPrimary = true;
					break;
				}else if(annotation instanceof VaribleAnnotation) {
					varibleAnnotation = annotation;
					break;
				}
			}
			
			if(!isPrimary) {
				EntityVaribleContainer container = new EntityVaribleContainer();
				container.setAnnotation(varibleAnnotation);
				container.setVaribleName(field.getName());
				String fieldType = field.getType().getSimpleName().toLowerCase();
				if(!fieldType.equals("String")) {
					String a = fieldType.substring(0,1).toUpperCase();
					String b = fieldType.substring(1);
					fieldType = a + b;
				}else {
					fieldType = "Integer";
				}
				container.setVaribleType(fieldType);
				entityVaribleContainer.add(container);
			}
		}
		return entityVaribleContainer;
	}
	
	/**
	 * ����һ��ʵ�������,ͨ������õ���������ֶζ�Ӧ��ֵ�ü�ֵ��.ע�����field.setAccessible(true);�������Ҫ����,
	 * Ĭ������ֶο��Բ��ܱ�����
	 * @param object
	 * @return
	 */
	public Map<String , Object> getAllFieldsValue(Object object) {
		Map<String , Object> properties = new HashMap<String , Object>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(int i = 0 ; i < fields.length ; i ++) {
			try {
				fields[i].setAccessible(true);
				properties.put(fields[i].getName(), fields[i].get(object));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	public Map<String ,Object> getBeanInfo(Object bean) {
		Map<String , Object> beanMap = new HashMap<String , Object>();
	/*	Method[] methods = bean.getClass().getMethods();
		for(Method method : methods) {
			String methodName = method.getName();
			if(methodName.indexOf("get") == 0 ) {
				try {
					String fieldName = methodName.substring(3).toLowerCase();
					Object retVal = method.invoke(bean);
					if(retVal != null) {
						beanMap.put(fieldName, retVal);
					}
				}catch(Exception e) {
					
				}
			}
		}*/
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertys = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor property : propertys) {
				Method readMethod = property.getReadMethod();
				String fieldName = property.getName().toLowerCase();
				Object retVal = readMethod.invoke(bean);
				if(retVal != null) {
					beanMap.put(fieldName, retVal);
				}
			}
		}catch(Exception e ) {
			e.printStackTrace();
		}
		return beanMap;
	}
	
	
	/**
	 * ͨ�������ʵ�����Class����,�õ�ʵ�������ֶζ�Ӧ��PropertyDescriptior����(java��ʡ),�����ֶε�����,
	 * ������getter��setter����
	 * 
	 * @param entityClass
	 * @return Map<String,PropertyDescriptor> beanMap
	 * @throws Exception
	 */
	public Map<String, PropertyDescriptor> getBeanInfo(Class<?> entityClass)
			throws Exception {
		Map<String, PropertyDescriptor> beanMap = new HashMap<String, PropertyDescriptor>();

		BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);

		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

		for (PropertyDescriptor pd : pds) {

			String fieldName = pd.getName().toLowerCase();

			beanMap.put(fieldName, pd);
		}
		return beanMap;
	}
	
	@Test
	public void test() throws Throwable{
		System.out.println(getAnnotationTableName(Student.class));
		System.out.println(getAnnotationPrimaryNameByMethod(Student.class));
		System.out.println(getAnnotationPrimaryNameByField(Score.class));
		System.out.println(getTableAnnotation(Student.class.getName()));
		System.out.println(getEntityVaribleContainerByPrimary(Student.class.getName()));
		System.out.println(getEntityVaribleContainerByForeign(Score.class.getName()));
		System.out.println(getOnVaribleAnnotation(Student.class.getName()));
	}
}
