package kr.or.ddit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * JDBC 드라이버를 로딩하고 Connection객체를 생성하는 메서드로 구성된 클래스
 */

//String url = "jdbc:oracle:thin:@localhost:1521/xe";
//String userId = "pc18";
//String password = "java";
//
//conn = DriverManager.getConnection(url, userId, password);
//
//stmt = conn.createStatement();

public class DBUtil {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!");
			e.printStackTrace();
		}
	}
	
	/**
	 * 커넥션 객체를 요청하는 메서드
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", 
					"pc18", 
					"java");
		} catch (SQLException e) {
			System.out.println("DB 연결 실패!!");
			e.printStackTrace();
			return null;
		}

	}
}
