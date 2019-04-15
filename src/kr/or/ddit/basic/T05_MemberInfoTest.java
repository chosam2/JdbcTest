package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;
import kr.or.ddit.util.DBUtil2;

/*
	회원정보를 관리하는 프로그램을 작성하는데 
	아래의 메뉴를 모두 구현하시오. (CRUD기능 구현하기)
	(DB의 MYMEMBER테이블을 이용하여 작업한다.)
	
	* 자료 삭제는 회원ID를 입력 받아서 삭제한다.
	
	예시메뉴)
	----------------------
		== 작업 선택 ==
		1. 자료 입력			---> insert
		2. 자료 삭제			---> delete
		3. 자료 수정			---> update
		4. 전체 자료 출력	---> select
		5. 작업 끝.
	----------------------
	 
	   
// 회원관리 프로그램 테이블 생성 스크립트 
create table mymember(
    mem_id varchar2(8) not null,  -- 회원ID
    mem_name varchar2(100) not null, -- 이름
    mem_tel varchar2(50) not null, -- 전화번호
    mem_addr varchar2(128)    -- 주소
);

*/
public class T05_MemberInfoTest {

	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private Scanner scan = new Scanner(System.in);

	/**
	 * 메뉴를 출력하는 메서드
	 * View 
	 */
	public void displayMenu() {
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 작 업 선 택 ===");
		System.out.println("  1. 자료 입력");
		System.out.println("  2. 자료 삭제");
		System.out.println("  3. 자료 수정");
		System.out.println("  4. 전체 자료 출력");
		System.out.println("  5. 작업 끝.");
		System.out.println("----------------------");
		System.out.print("원하는 작업 선택 >> ");
	}

	/**
	 * 프로그램 시작메서드
	 * Controller 기능
	 */
	public void start() {
		int choice;
		do {
			displayMenu(); //메뉴 출력
			choice = scan.nextInt(); // 메뉴번호 입력받기
			switch (choice) {
			case 1: // 자료 입력
				insertMember();
				break;
			case 2: // 자료 삭제
				deleteMember();
				break;
			case 3: // 자료 수정
				updateMember();
				break;
			case 4: // 전체 자료 출력
				displayMemberAll();
				break;
			case 5: // 작업 끝
				System.out.println("작업을 마칩니다.");
				break;
			default:
				System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
			}
		} while (choice != 5);
	}

	/**
	 * 회원정보 삭제하기 위한 메서드
	 */
	private void deleteMember() {
		System.out.println();
		System.out.print("삭제할 회원 ID를 입력하세요 >> ");
		String memId = scan.next();

		try {
			conn = DBUtil2.getConnection();

			String sql = "delete from mymember where mem_id = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memId);

			int cnt = pstmt.executeUpdate();

			if (cnt > 0) {
				System.out.println(memId + "회원 정보 삭제 성공");
			} else {
				System.out.println(memId + "회원 정보 삭제 실패"); // 회원정보가 없다는 뜻
			}
		} catch (SQLException e) {
			System.out.println(memId + "회원 정보 삭제 실패"); // 회원정보가 없다는 뜻
			e.printStackTrace();
		} finally {
			disConnect();
		}

	}

	/**
	 * 회원정보를 수정하기 위한 메서드
	 * Service 기능 
	 * 나중에는 DAO로 뺄 예정
	 */
	private void updateMember() {

		boolean chk = false;
		String memId = ""; // 회원아이디 

		do {
			System.out.print("수정할 회원ID를 입력하세요 >> ");
			memId = scan.next();

			chk = chkMemberInfo(memId); // true가 리턴되면 이미 존재한다는 의미.
			if (chk == false) {
				System.out.println(memId + "회원은 없는 회원입니다.");
				System.out.println("수정할 자료가 없으니 다시 입력해 주세요.");
			}

		} while (chk == false);

		System.out.println("수정할 내용을 입력하세요.");

		System.out.print("새로운 회원 이름 >> ");
		String memName = scan.next();

		System.out.print("새로운 회원 전화번호 >> ");
		String memTel = scan.next();

		scan.nextLine(); // 버퍼 지우기
		System.out.print("새로운 회원 주소 >> ");
		String memAddr = scan.nextLine();

		try {
			conn = DBUtil.getConnection();
			String sql = "update mymember " + " set mem_name = ? " + " , mem_tel = ? " + " , mem_addr = ? " + " where mem_id = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memName); // 물음표의 순서에맞게 Index에 넣어주면됨.
			pstmt.setString(2, memTel);
			pstmt.setString(3, memAddr);
			pstmt.setString(4, memId);

			int cnt = pstmt.executeUpdate();

			if (cnt > 0) {
				System.out.println(memName + "회원의 정보를 수정했습니다.");
			} else {
				System.out.println(memName + "회원의 정보를 수정에 실패했습니다.");
			}

		} catch (SQLException e) {

		} finally {
			disConnect();
		}

	}

	private void insertMember() {

		boolean chk = false;
		String memId = "";

		do {
			System.out.println();
			System.out.println("추가할 회원 정보를 입력하세요.");
			System.out.print("회원 ID >> ");
			memId = scan.next();

			chk = chkMemberInfo(memId);

			if (chk) {
				System.out.println("회원ID가 " + memId + "인 회원이 이미 존재합니다.");
				System.out.println("다시 입력해주세요.");
			}

		} while (chk);

		System.out.print("회원 이름 >> ");
		String memName = scan.next();

		System.out.print("회원 전화번호 >> ");
		String memTel = scan.next();

		scan.nextLine(); // 버퍼 지우기
		System.out.print("회원 주소 >> ");
		String memAddr = scan.nextLine();

		try {
			conn = DBUtil.getConnection();

			String sql = "insert into mymember (mem_id, mem_name, mem_tel, mem_addr)" + " values (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memId);
			pstmt.setString(2, memName);
			pstmt.setString(3, memTel);
			pstmt.setString(4, memAddr);

			int cnt = pstmt.executeUpdate(); // insert기때문에 executeQuery()가 아니라 Update()임.

			if (cnt > 0) { // cnt > 0 의미는 데이터가 잘 전달되었다는 의미.
				System.out.println(memId + "회원 추가 작업 성공!!");
			} else {
				System.out.println(memId + "회원 추가 작업 실패!!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}

	}

	/**
	 * 회원아이디를 이용하여 해당 회원정보가 존재하는지 체크하는 메서드
	 * @param memId
	 * @return
	 */
	private boolean chkMemberInfo(String memId) {

		boolean isExist = false;

		try {
			conn = DBUtil.getConnection();

			String sql = "select count(*) as cnt from mymember where mem_id = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, memId);

			rs = pstmt.executeQuery(); // select니깐 던져줌.

			int cnt = 0;

			if (rs.next()) { // 한건밖에 안나올테니깐 if로 해도 무방함. 여러건이면 while(rs.next())
				//				rs.getInt(cnt);
				cnt = rs.getInt(1);
			}

			if (cnt > 0) { // 0 이상이면 true로
				isExist = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return isExist;
	}

	private void displayMemberAll() {
		System.out.println();
		System.out.println("--------------------------------------------------------------------");
		System.out.println(" ID		이름		전화번호				주소");
		System.out.println("--------------------------------------------------------------------");

		try {
			conn = DBUtil.getConnection();

			stmt = conn.createStatement();

			String sql = "select * from mymember";

			rs = stmt.executeQuery(sql); // select 쿼리이기때문에 executeQuery로 던짐

			while (rs.next()) {
				String memId = rs.getString("mem_id");
				String memName = rs.getString("mem_name");
				String memTel = rs.getString("mem_tel");
				String memAddr = rs.getString("mem_addr");

				System.out.println(memId + "		" + memName + "		" + memTel + "		" + memAddr);
			}

			System.out.println("--------------------------------------------------------------------");
			System.out.println("출력작업 끝...");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}

	}

	/**
	 *  연결 끊을 때 finally에 들어갈 예외처리.
	 */
	private void disConnect() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException ee) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException ee) {
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException ee) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ee) {
			}

	}

	public static void main(String[] args) {
		T05_MemberInfoTest memObj = new T05_MemberInfoTest();
		memObj.start();
	}

}
