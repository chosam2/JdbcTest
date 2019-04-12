package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class T04_JdbcTest {
	/*
		LPROD 테이블에 새로운 데이터를 추가하기
		
		lprod_gu와 lprod_nm은 직접 입력받아 처리하고
		lprod_id는 현재의 lprod_id들 중 제일 큰 값보다 1 증가된 값으로 한다.
		(번외 : lprod_gu도 중복되는지 검사한다.)
		
		
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "pc18";
			String password = "java";

			conn = DriverManager.getConnection(url, userId, password);

			stmt = conn.createStatement();

			//	String sql = "select max(lprod_id) + 1 from lprod";
			String sql = "select max(lprod_id) from lprod";
			// String sql select max(lprod_id) as maxNum from lprod;
			System.out.println("쿼리문 확인 : " + sql);
			rs = stmt.executeQuery(sql); // 질의문에대한 결과

			int num = 0;

			if (rs.next()) {
				// num = rs.getInt(1);	// 컬럼번호로 
				 num = rs.getInt("(max)lprod_id"); // 별명 없을때
				// num = rs.getInt("maxNum");	// 별명사용
			}

			int count = 0;
			String sql2 = "select count(*) cnt from lprod " 
					+ " where lprod_gu = ?";

//			pstmt
			
			do {
				System.out.println("");
			}while(count > 0);

		} catch (Exception e) {

		}

	}
}
