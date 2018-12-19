package ExamDB;

import java.sql.*;
import java.util.*;

public class DbProxy {
	private static String dbClass = "com.mysql.jdbc.Driver";
	private static String dbSource = "jdbc:mysql://127.0.0.1:3306";
	private static String db = "jdbc:mysql://127.0.0.1:3306/";
//	private static String user = "root";
//	private static String passwd = "tn00917892";
	private static String user = "root";
	private static String passwd = "islab1221";
//	private static String user = "root";
//	private static String passwd = "0000";

	public static Connection getConnection(String dbName) throws Exception {
		Class.forName(dbClass);
		Connection con = DriverManager.getConnection(dbSource + "/" + dbName, user, passwd);
		return con;
	}

	public static boolean createDB(String dbName) {
		String sql = "create database " + dbName;
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbSource, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	public static boolean creatTable(String dbName, String table, String[] colName, String[] type) {
		Connection con = null;
		Statement stmt = null;
		String sql = "create table " + table + " ( ";
		for (int i = 0; i < colName.length; i++) {
			sql = sql + colName[i] + " " + type[i];
			if (i < (colName.length - 1))
				sql = sql + ",";
		}
		sql = sql + ")";
		System.out.println(sql);
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	public static String getData(String dbName, String strSQL, String clumn) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(strSQL);
			rs.absolute(1); // 設定記錄指標的位置
			result = rs.getString(clumn);
			stmt.close(); // 關閉Statement物件
			con.close(); // 關閉Connection物件
		} catch (Exception e) {
			System.out.println(e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// 20170603
	public static String setData(String dbName, String sql, String[] data) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < data.length; i++) {
				stmt.setString(i + 1, data[i]);
			}
			stmt.executeUpdate();
			stmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "False";
		}
	}

	// 20170601
	public static String setExamScore(String dbName, String sql, int quesNum, int rightNum, String id) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, quesNum);
			stmt.setInt(2, rightNum);
			stmt.setString(3, id);
			stmt.executeUpdate();
			stmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "False";
		}
	}

	public static String setData(String dbName, String sql) {
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "False";
		}
	}

	public static String setAllData(String dbName, String[] strSQL) {
		Connection con = null;
		Statement stmt = null;
		String s = "";
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < strSQL.length; i++) {
				stmt.executeUpdate(strSQL[i]);
				s = strSQL[i];
			}
			stmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println(s + "=error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "False";
		}
	}

	public static boolean addData(String dbName, String sql, String[] data) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < data.length; i++) {
				if(data[i].length()!=data[i].getBytes().length)pstmt.setNString(i+1, data[i]);
				else pstmt.setString(i + 1, data[i]);
			}
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println("error from addData! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	public static String delData(String dbName, String sql, String cluName) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cluName);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "Fail";
		}
	}

	// 20170603
	public static String delData(String dbName, String sql, String title, String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			String sql2 = "DELETE FROM test_result WHERE question_id = ? AND student_id = ?;";
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql2);
			int titleint=Integer.parseInt(title);
			pstmt.setInt(1, titleint );
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "Fail";
		}
	}
	public static String delData2(String dbName, String sql, String title, String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(title) );
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return "OK";
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return "Fail";
		}
	}

	public static boolean delAllData(String dbName, String sql) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;
		}
	}

	// 20170603
	public static ArrayList<String[]> selectData(String dbName, String sql, String[] param) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String[] data = null;
		ArrayList<String[]> result = new ArrayList<String[]>();
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.prepareStatement(sql);
			for (int i = 0; i < param.length; i++) {
//				System.out.println(param[i].getBytes().length);
				if(param[i].getBytes().length!=param[i].length())stmt.setNString(i + 1, param[i]);
				else stmt.setString(i + 1, param[i]);
			}
//			System.out.println("!!!!"+stmt);
			rs = stmt.executeQuery();
			int count = rs.getMetaData().getColumnCount();
//			System.out.println(count);
			while (rs.next()) {
				data = new String[count];
				for (int i = 1; i <= count; i++) {
					data[i - 1] = rs.getString(i);
//					System.out.print(data[i-1]+"  ");
				}
//				System.out.print("\n");
				result.add(data);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("error! " + e.toString());
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static ArrayList<String[]> selectData(String dbName, String sql) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String[] data = null;
		ArrayList<String[]> result = new ArrayList<String[]>();
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(db + dbName, user, passwd);
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			int count = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				data = new String[count];
				for (int i = 1; i <= count; i++) {
					data[i - 1] = rs.getString(i);
				}
				result.add(data);
			}
			rs.close();
			stmt.close(); // ����Statement����
			con.close(); // ����Connection����
		} catch (Exception e) {
			try {
				if (con != null) {
					con.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			System.out.println("error from selectData! " + e.toString());
		}
		return result;
	}
}