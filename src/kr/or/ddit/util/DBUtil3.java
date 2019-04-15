package kr.or.ddit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBUtil3 {

	static ResourceBundle bundle; // ResourceBundle객체변수 선언

	static {
		bundle = ResourceBundle.getBundle("db"); // 객체 생성

		try {
			Class.forName(bundle.getString("driver"));

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}
	}

	/**
	 * 커넥션 객체를 요청하는 메서드
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(
					bundle.getString("url"), 
					bundle.getString("user"), 
					bundle.getString("pass"));
		} catch (SQLException e) {
			System.out.println("DB 연결 실패!!");
			e.printStackTrace();
			return null;
		}

	}
}
