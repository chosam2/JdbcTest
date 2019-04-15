package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil;

/*
	기능 구현하기 ==> 전체 목록 출력, 새글작성, 수정, 삭제, 검색 
	
------------------------------------------------------------

게시판 테이블 구조 및 시퀀스

create table jdbc_board(
    board_no number not null,  -- 번호(자동증가)
    board_title varchar2(100) not null, -- 제목
    board_writer varchar2(50) not null, -- 작성자
    board_date date not null,   -- 작성날짜
    board_content clob,     -- 내용
    constraint pk_jdbc_board primary key (board_no)
);
create sequence board_seq
    start with 1   -- 시작번호
    increment by 1; -- 증가값
    
    
      
----------------------------------------------------------

*/
public class Z02_BoardTest2 {

	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private Scanner scan = new Scanner(System.in);

	/**
	 * 메뉴를 출력하는 메서드
	 */
	public void displayMenu() {
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 작 업 선 택 ===");
		System.out.println("  1. 게시글 작성");
		System.out.println("  2. 게시글 삭제");
		System.out.println("  3. 게시글 수정");
		System.out.println("  4. 게시글 출력");
		System.out.println("  5. 종료!");
		System.out.println("----------------------");
		System.out.print("원하는 작업 선택 >> ");
	}

	/**
	 * 프로그램 시작메서드
	 */
	public void start() {
		int choice;
		do {
			displayMenu(); //메뉴 출력
			choice = scan.nextInt(); // 메뉴번호 입력받기
			switch (choice) {
			case 1: // 게시글 작성
				writerBoard();
				break;
			case 2: // 게시글 삭제
				deleteBoard();
				break;
			case 3: // 게시글 수정
				modifyBoard();
				break;
			case 4: // 게시글 전체조회
				showBoardList();
				break;
			case 5: // 종료
				System.out.println("작업을 마칩니다.");
				break;
			default:
				System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
			}
		} while (choice != 5);
	}

	private void showBoardList() {
		System.out.println();
		System.out.println("-----------------------------------------------------------------");
		System.out.println("       게시판 번호\t제목\t작성자\t\t날짜\t\t내용\t");
		System.out.println("-----------------------------------------------------------------");

		try {
			conn = DBUtil.getConnection();

			stmt = conn.createStatement();

			String sql = "select * from jdbc_board";

			//			System.out.println("질의문 확인 : " + sql);

			rs = stmt.executeQuery(sql); // select문이기 때문에 ResultSet으로 반환

			while (rs.next()) {
				int board_no = rs.getInt("board_no");
				String board_title = rs.getString("board_title");
				String board_writer = rs.getString("board_writer");
				//				String board_date = rs.getString("board_date");
				java.sql.Date board_date = rs.getDate("board_date");
				String board_content = rs.getString("board_content");

				System.out.println("\t" + board_no + "     " + board_title + "\t" + board_writer + "\t" + board_date + "\t" + board_content + "\t");
			}
			System.out.println("-----------------------------------------------------------------");
			System.out.println("출력 끝!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	private void deleteBoard() {
		System.out.println();
		System.out.println("삭제할 게시글의 번호 입력하세요.");
		int board_no = Integer.parseInt(scan.next());

		try {
			conn = DBUtil.getConnection();

			String sql = "delete from jdbc_board where board_no = ?";

			System.out.println("질의문 확인 : " + sql);

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, board_no);

			int cnt = pstmt.executeUpdate();

			if (cnt > 0) {
				System.out.println(board_no + "번 게시글이 삭제되었습니다.");
			} else {
				System.out.println(board_no + "번 게시글이 없습니다.(삭제실패!)");
			}

		} catch (SQLException e) {
			System.out.println(board_no + "번 게시글이 없습니다.(삭제실패!)");
			e.printStackTrace();
		} finally {
			disConnect();
		}

	}

	/**
	 * 게시글 수정
	 */
	private void modifyBoard() {
		System.out.println("수정할 게시판 번호를 입력하세요.");
		scan.nextLine();
		int board_no = Integer.parseInt(scan.nextLine());

		try {
			conn = DBUtil.getConnection();

			String sql = "update jdbc_board set board_title = ? " + ", board_content = ? " + "where board_no = ?";

			System.out.println("쿼리문 확인 : " + sql);
			System.out.println("-----------------------------------------------------------------");
			System.out.print("변경할 제목을 입력하세요 >> ");
			String board_title = scan.nextLine();
			System.out.print("변경할 내용 입력하세요 >> ");
			String board_content = scan.nextLine();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, board_title);
			pstmt.setString(2, board_content);
			pstmt.setInt(3, board_no);

			int cnt = pstmt.executeUpdate();

			if (cnt > 0) {
				System.out.println(board_no + "번 글이 업데이트가 되었습니다.");
			} else {
				System.out.println(board_no + "번 글의 업데이트가 실패했습니다 ㅠㅠ");
			}

		} catch (SQLException e) {
			System.out.println(board_no + "번 글의 업데이트가 실패했습니다 ㅠㅠ");
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	/**
	 * 게시판 글쓰기
	 */
	private void writerBoard() {

		System.out.print("제목을 입력하세요 >> ");
		String board_title = scan.next();

		scan.nextLine();
		System.out.print("작성자를 입력하세요 >> ");
		String board_writer = scan.nextLine().trim();

		System.out.print("내용을 입력하세요 >> ");
		String board_content = scan.nextLine();

		try {
			conn = DBUtil.getConnection();

			String sql = "insert into jdbc_board values " + "(board_seq.nextval, ?, ?, sysdate, ?)";

			System.out.println("질의문 확인 : " + sql);

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, board_title);
			pstmt.setString(2, board_writer);
			pstmt.setString(3, board_content);

			int cnt = pstmt.executeUpdate();

			if (cnt > 0) {
				System.out.println("게시물이 잘 작성되었습니다.");
			} else {
				System.out.println("게시글 작성에 실패했습니다.");
			}
		} catch (SQLException e) {
			System.out.println("게시글 작성에 실패했습니다.");
			e.printStackTrace();
		} finally {
			disConnect();
		}

	}

//	/**
//	 * 회원아이디를 이용하여 해당 회원정보가 존재하는지 체크하는 메서드
//	 * @param writer
//	 * @return
//	 */
//	private boolean chkMemberInfo(String writer) {
//
//		boolean isExist = false;
//
//		try {
//			conn = DBUtil.getConnection();
//
//			String sql = "select count(*) as cnt from jdbc_board where writer= ?";
//
//			pstmt = conn.prepareStatement(sql);
//
//			pstmt.setString(1, writer);
//
//			rs = pstmt.executeQuery(); // select니깐 던져줌.
//
//			int cnt = 0;
//
//			if (rs.next()) { // 한건밖에 안나올테니깐 if로 해도 무방함. 여러건이면 while(rs.next())
//				//				rs.getInt(cnt);
//				cnt = rs.getInt(1);
//			}
//
//			if (cnt > 0) { // 0 이상이면 true로
//				isExist = true;
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			disConnect();
//		}
//		return isExist;
//	}

	/**
	 *  연결 끊을 떄 finally에 들어갈 예외처리.
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
		Z02_BoardTest2 board = new Z02_BoardTest2();
		board.start();
	}

}
