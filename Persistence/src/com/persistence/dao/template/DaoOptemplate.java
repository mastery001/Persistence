package com.persistence.dao.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dom4j.Element;

import com.persistence.exception.ErrorException;
import com.persistence.util.DBUtil;

/**
 * 
 * Dao���������ݿ������ģ����
 * 
 * @author mastery
 * 
 */
public class DaoOptemplate {

	public static final DaoOptemplate INSTANCE = new DaoOptemplate();

	private DaoOptemplate() {
	}

	public static DaoOptemplate getInstance() {
		return INSTANCE;
	}

	/**
	 * ͨ������ĸ���SQL���Ͳ�������,��Ӧ�Ĳ���������ִ�����ݿ���²�����
	 * 
	 * @param sql
	 * @param objParams
	 * @param paramTypes
	 * @param flag
	 * @return
	 */
	public Boolean update(String sql , List<Object> objParams , List<String> paramTypes , boolean flag) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		Boolean updateFlag = false;
		
		try {
			conn = DBUtil.getConnection();
			pstmt = flag ? conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sql);
			if(null == objParams) {
			}else {
				for(int i = 0 ; i < objParams.size() ; i++) {
					SetPreparedStatement.setPreparedStatementByPropertiseType(pstmt , i+1 , paramTypes.get(i) , objParams.get(i));
				}
			}
			//���ò��Զ��ύ
			conn.setAutoCommit(false);
			
			int result = pstmt.executeUpdate();
			
			conn.commit();
			if(result > 0 ) {
				updateFlag = true;
			}
		}catch (SQLException e) {
			throw new ErrorException("���ݿ���·������ִ���!");
		}finally {
			try {
				pstmt.close();
				conn.close();
			}catch(SQLException e ){
				e.printStackTrace();
			}
		}
		return updateFlag;
	}
	
	/**
	 * 
	 * ͨ������Ĳ�ѯSQL������Ҫ��ѯʵ����ڵ�,��Ӧ�Ĳ���������ִ�����ݿ��ѯ����.
	 * @param sql
	 * @param entity
	 * @param entityClass
	 * @return
	 */
	public <T> List<T> query(String sql , Element entity , Class<T> entityClass ) {
		List<T> resultList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			resultList = DaoObjectMapper.getObjectByResultSet(rs,entity , entityClass);
			
			if(resultList.size() == 0 ) {
				throw new ErrorException("��ѯ��������Ϊ�գ�");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}
}
