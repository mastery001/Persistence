package com.persistence.dao.template;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.persistence.util.ReadXMLUtil;

/**
 * 
 * 对象解析工具类
 * 
 * @author mastery
 * 
 */
public class DaoObjectMapper {

	private DaoObjectMapper() {
	}

	/**
	 * 
	 * 根据配置的po实体类的配置文件，通过传入的一个ResultSet结果集，通过classElement，Class<T>
	 * ,entityClass来解析获取一个对应的实体类对象
	 * @param rs
	 * @param classElement
	 * @param entityClass
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> getObjectByResultSet(ResultSet rs,
			Element classElement, Class<T> entityClass) throws SQLException {
		if (classElement == null) {
			classElement = ReadXMLUtil.getElement(entityClass);
		}

		List<T> entitys = new ArrayList<T>();

		while (rs.next()) {
			Map<String, Object> fieldsValue = new HashMap<String, Object>();
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				fieldsValue.put(rs.getMetaData().getColumnLabel(i + 1),
						rs.getObject(i + 1));
			}
			T entity = null;
			entity = setAllFieldsValue(classElement, entityClass, fieldsValue);
			entitys.add(entity);

		}
		return entitys;
	}

	/**
	 * 通过classElement节点，对象的Class类型，和对象字段的键值对，来解析成一个对应的实体类对象
	 * @param classElement
	 * @param entityClass
	 * @param fieldsValue
	 * @return
	 */
	private static <T> T setAllFieldsValue(Element classElement,
			Class<T> entityClass, Map<String, Object> fieldsValue) {
		if (null == classElement) {
			classElement = ReadXMLUtil.getElement(entityClass);
		}
		T entity = null;

		try {
			entity = entityClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		for (Iterator<String> i = fieldsValue.keySet().iterator(); i.hasNext();) {
			String column = i.next();

			Iterator<Element> classIterator = classElement.elementIterator();
			while (classIterator.hasNext()) {
				Element prop = classIterator.next();
				if (prop.attributeValue("column").equals(column)) {
					Field field = null;

					try {
						field = entityClass.getDeclaredField(prop
								.attributeValue("name"));
						field.setAccessible(true);

						if (prop.attribute("className") != null
								&& fieldsValue.get(prop
										.attributeValue("column")) != null) {
							Class<?> fatherClass = Class.forName(prop
									.attributeValue("className"));

							Object father = null;

							father = fatherClass.newInstance();

							Field id = fatherClass.getDeclaredField("id");
							id.setAccessible(true);
							if ("java.math.BigDecimal".equals(fieldsValue
									.get(prop.attributeValue("column"))
									.getClass().getName())) {
								BigDecimal decimal = (BigDecimal) fieldsValue
										.get(prop.attributeValue("column"));
								if (prop.attributeValue("type")
										.equals("Double")) {
									id.set(father, decimal.doubleValue());
								} else if (prop.attributeValue("type").equals(
										"Float")) {
									id.set(father, decimal.floatValue());
								}
							} else {
								id.set(father, fieldsValue.get(prop
										.attributeValue("column")));
							}

							field.set(entity, father);

						} else {
							if (fieldsValue.get(prop.attributeValue("column")) != null) {
								if ("java.math.BigDecimal".equals(fieldsValue
										.get(prop.attributeValue("column"))
										.getClass().getName())) {
									BigDecimal decimal = (BigDecimal) fieldsValue
											.get(prop.attributeValue("column"));
									if (prop.attributeValue("type").equals(
											"Double")) {
										field.set(entity, decimal.doubleValue());
									} else if (prop.attributeValue("type")
											.equals("Float")) {
										field.set(entity, decimal.floatValue());
									}

								} else {
									field.set(entity, fieldsValue.get(prop
											.attributeValue("column")));
								}
								System.out.println(fieldsValue.get(prop
										.attributeValue("column")));

							}
						}
						break;
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return entity;
	}
}
