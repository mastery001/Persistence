package com.persistence.dao.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.persistence.exception.ErrorException;

public class SetPreparedStatement {

	private SetPreparedStatement() {
	}

	/**
	 * 声明8大基本数据类型
	 */
	private final static int ShortType = 0;

	private final static int IntegerType = 1;

	private final static int LongType = 2;

	private final static int FloatType = 3;

	private final static int DoubleType = 4;

	private final static int BooleanType = 5;

	private final static int StringType = 6;

	private final static int DateType = 7;

	/**
	 * 
	 * setPreparedStatement具体的实现方法，通过传入的P热怕热的Statement对象，索引，参数类型，参数值，来进行配置验证赋值
	 * 
	 * @param pstmt
	 * @param index
	 * @param type
	 * @param parameter
	 */
	public static void setPreparedStatementByPropertiseType(
			PreparedStatement pstmt, int index, String type, Object parameter)
			throws SQLException, ErrorException {
		int changeTypeBeIntValue = changePropertiesTypeToInt(type);
		switch (changeTypeBeIntValue) {
		case IntegerType:
			pstmt.setInt(index, (Integer) parameter);
			break;
		case LongType:
			pstmt.setLong(index, (Long) parameter);
			break;
		case StringType:
			pstmt.setString(index, String.valueOf(parameter));
			break;
		case ShortType:
			pstmt.setShort(index, (Short) parameter);
			break;
		case DateType:
			Date time = (Date) parameter;
			pstmt.setDate(index, new java.sql.Date(time.getTime()));
			break;
		case BooleanType:
			pstmt.setBoolean(index, Boolean.parseBoolean((String) parameter));
			break;
		case FloatType:
			pstmt.setFloat(index, (Float) parameter);
			break;
		case DoubleType:
			pstmt.setDouble(index, (Double) parameter);
			break;
		default:
			throw new ErrorException("配置的数据类型出现错误！！！！");
		}
	}

	private static int changePropertiesTypeToInt(String type) {
		int result = -1;
		if (type.equals("Integer")) {
			result = IntegerType;
		} else if (type.equals("Long")) {
			result = LongType;
		} else if (type.equals("String")) {
			result = StringType;
		} else if (type.equals("Short")) {
			result = ShortType;
		} else if (type.equals("Date")) {
			result = DateType;
		} else if (type.equals("Boolean")) {
			result = BooleanType;
		} else if (type.equals("Float")) {
			result = FloatType;
		} else if (type.equals("Double")) {
			result = DoubleType;
		}

		return result;
	}

}
