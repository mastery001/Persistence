package com.persistence.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mastery
 *
 */
public class DBUtil {
	private static Connection conn = null;
	
	private DBUtil() {
	}
	/**
	 * 加载数据库的驱动
	 */
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		String url = "jdbc:mysql:///log4j";
		try {
			conn = DriverManager.getConnection(url , "root" , "root" );
			conn.setAutoCommit(false);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void close() {
		try {
			if(conn != null) {
				conn.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
