package kr.or.ddit.basic;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Z01_HotelManage {
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Z01_HotelManage hm = new Z01_HotelManage();
		hm.start();

	}

	void start() {
		System.out.println("***************************");
		System.out.println("호텔문을 열었습니다.");
		System.out.println("***************************");
		System.out.println();
		while (true) {
			System.out.println("***************************");
			System.out.println("어떤 업무를 하시겠습니까?");
			System.out.println("1. 체크인 2.체크아웃 3.객실상태 4.업무종료");
			System.out.println("***************************");
			System.out.print("메뉴선택=> ");
			int input = Integer.parseInt(sc.nextLine().trim());
			switch (input) {
			case 1:
				checkIn();
				break;
			case 2:
				checkOut();
				break;
			case 3:
				roomStatus();
				break;
			case 4:
				System.out.println("호텔 문닫습니다.");
				System.exit(0);
			default:
				System.out.println("잘못 눌렀습니다.");
			}

		}
	}

	private void roomStatus() {

		//		Set<Map.Entry<Integer, Hotel>> mapSet = map.entrySet();
		//
		//		Iterator<Map.Entry<Integer, Hotel>> entryIt = mapSet.iterator();
		//
		//		while (entryIt.hasNext()) {
		//			Map.Entry<Integer, Hotel> entry = entryIt.next();
		//
		//			System.out.println(entry.getKey() + " : " + entry.getValue());
		//		}

		try {
			// 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "hyungwook";
			String password = "java";

			conn = DriverManager.getConnection(url, userId, password);

			stmt = conn.createStatement();

			String sql = "select * from hotel";
			rs = stmt.executeQuery(sql);

			System.out.println("실행한 쿼리문 : " + sql);

			while (rs.next()) {
				System.out.println("=================================");
				System.out.println("방번호 : " + rs.getInt(1));
				System.out.println("예약자 : " + rs.getString(2));
				System.out.println("=================================");
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
			//			if (pstmt != null)
			//				try {
			//					conn.close();
			//				} catch (SQLException e2) {
			//				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e2) {
				}
		}

	}

	private void checkOut() {
		//		System.out.println("어느방을 체크하웃 하시겠습니까?");
		//		System.out.print("방번호 입력 >> ");
		//		int roomNum = Integer.parseInt(sc.nextLine().trim());
		//
		//		if (map.get(roomNum) != null) {
		//			map.remove(roomNum);
		//			System.out.println("체크아웃 되었습니다.");
		//		} else {
		//			System.out.println(roomNum + "방에는 체크인한 사람이 없습니다.");
		//		}

		System.out.println("어느방을 체크아웃 하시겠습니까?");
		System.out.print("방번호 입력 >>");
		int roomNum = Integer.parseInt(sc.nextLine().trim());

		String sql = "select roomNum from hotel where roomNum=" + roomNum;
		System.out.println("질의문 확인 : " + sql);

		try {
			// 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "hyungwook";
			String password = "java";

			conn = DriverManager.getConnection(url, userId, password);

			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
			String sql2 = "delete from hotel where roomNum = " + roomNum;
			System.out.println("쿼리문 확인 : " + sql2);

			//			int cnt = stmt.executeUpdate(sql2);
			//			System.out.println(cnt);

			if (rs.next()) {
				System.out.println(rs.getInt("roomNum") + "을 체크아웃 하였습니다.");
				int cnt = stmt.executeUpdate(sql2);
				//				System.out.println(cnt);
			} else {
				System.out.println(roomNum + "는 비어있습니다.");
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
			//			if (pstmt != null)
			//				try {
			//					conn.close();
			//				} catch (SQLException e2) {
			//				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e2) {
				}
		}

	}

	private void checkIn() {
		//		System.out.println("어느방에 체크인 하시겠습니까?");
		//		System.out.print("방번호 입력=> ");
		//		int roomNum = Integer.parseInt(sc.nextLine().trim());
		//		System.out.println();
		//		System.out.println("누구를 체크인 하시겠습니까?");
		//		System.out.print("이름 입력 => ");
		//		String guest = sc.nextLine();
		//
		//		if (map.get(roomNum) != null) {
		//			System.out.println(map.get(roomNum).getRoomNum() + "방에는 이미 사람이 있습니다.");
		//		} else {
		//			map.put(roomNum, new Hotel(roomNum, guest));
		//		}

		try {
			// 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "hyungwook";
			String password = "java";

			conn = DriverManager.getConnection(url, userId, password);

			stmt = conn.createStatement();

			System.out.println("어느방에 체크인 하시겠습니까?");
			System.out.print("방번호 입력 => ");
			int roomNum = Integer.parseInt(sc.nextLine().trim());

			String sql2 = "select roomNum from hotel where roomNum=" + roomNum;
			System.out.println("쿼리문 확인 : " + sql2);

			rs = stmt.executeQuery(sql2);
			while (rs.next()) {
				System.out.println(rs.getInt("roomNum") + "은 이미 예약되었습니다.");
			}

			System.out.println("누구를 체크인 하시겠습니까?");
			System.out.print("이름 입력 => ");
			String guest = sc.nextLine().trim();

			// PreparedStatement 객체를 이용한 자료 추가방법
			String sql = "insert into hotel (roomNum, guest)" + " values(?,?)";
			System.out.println("쿼리문 확인 : " + sql);

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, roomNum);
			pstmt.setString(2, guest);

			// 데이터를 세팅한 후 쿼리문을 실행
			int cnt = pstmt.executeUpdate();
			System.out.println("첫번째 반환값 : " + cnt);

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
			if (pstmt != null)
				try {
					conn.close();
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
