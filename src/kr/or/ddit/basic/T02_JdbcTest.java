package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class T02_JdbcTest {

	/*
		문제1) 사용자로부터 lprod_id값을 입력받아 값보다 lprod_id가 큰 자료들을 출력하시오.
		
		문제2) lprod_id값을 2개 입력 받아서 두 값 중 작은 값부터 큰 값 사이의 자료를 출력하시오.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "pc18";
			String password = "java";

			conn = DriverManager.getConnection(url, userId, password);

			stmt = conn.createStatement();

			//			String sql = "select * from lprod";
			//			rs = stmt.executeQuery(sql);

						System.out.println("찾을 아이디를 입력하세요.");
						int inputId = sc.nextInt();
			
						String sql = "select * from lprod where lprod_id > " + inputId;
						System.out.println("실행한 쿼리문 : " + sql);
						System.out.println(" === 쿼리문 실행 결과 ===");
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							System.out.println("lprod_id : " + rs.getInt("lprod_id"));
							System.out.println("lprod_gu : " + rs.getString("lprod_gu"));
							System.out.println("lprod_nm : " + rs.getString("lprod_nm"));
							System.out.println("---------------------------------------");
			
						}
						System.out.println("문제1 끝");

						while (rs.next()) {
							if (rs.getInt("lprod_id") > inputId) {
								System.out.println("lprod_id : " + rs.getInt("lprod_id"));
								System.out.println("--------------------------------------");
							}
						}

			System.out.println("문제2번");
			System.out.println("lprod_id값 2개 입력");
			int num1 = sc.nextInt();
			int num2 = sc.nextInt();

			sc.close();

			int max, min;

			//			if (num1 > num2) {
			//				max = num1;
			//				min = num2;
			//			} else {
			//				max = num2;
			//				min = num1;
			//			}

			max = Math.max(num1, num2);
			min = Math.min(num1, num2);
			String sql2 = "select * from lprod" + " where lprod_id >=" + min + " and lprod_id <=" + max;
			System.out.println("만들어진 sql문" + sql2);
			rs = stmt.executeQuery(sql2);
			while (rs.next()) {
				System.out.println("lprod_id : " + rs.getInt("lprod_id"));
				System.out.println("lprod_gu : " + rs.getString("lprod_gu"));
				System.out.println("lprod_nm : " + rs.getString("lprod_nm"));
				System.out.println("---------------------------------------");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e2) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e2) {
				}
		}

	}
}
