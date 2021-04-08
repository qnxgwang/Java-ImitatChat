package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {
	
	/**
	 * Jdbc�������ݿ��װ
	 */
	private static JdbcUtil jdbcUtil =null;
	
	private JdbcUtil() {
		
	}
	
	/**
	 * ��̬������ֱ��ͨ����������
	 * @return
	 */
	 public static synchronized JdbcUtil getJdbcUtil() {
		if(null==jdbcUtil) {			
			jdbcUtil = new JdbcUtil();
		}
		return jdbcUtil;
	}
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ���ݿ�����
	 */
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(
				PropertiesUtil.getPropertiesUtil().getValue("url_qq"),
				PropertiesUtil.getPropertiesUtil().getValue("username"),
				PropertiesUtil.getPropertiesUtil().getValue("pwd")
				);
	}
	
	/**
	 * �ر����ݿ�����
	 */	
	public void closeConnection(ResultSet resultSet,Statement statement,Connection connection) {
		try {
			if(null!=resultSet) {
				resultSet.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		    }
		try {
			if(null!=statement) {
				statement.close();
			}
		}catch(SQLException e){
				e.printStackTrace();
		    }
		try {
			if(null!=connection) {
				connection.close();
			}
		}catch(SQLException e) {
				e.printStackTrace();
			}	
	}
}
