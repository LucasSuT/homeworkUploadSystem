package bean;

import java.sql.*;
import java.util.ArrayList;

import ExamDB.DbProxy;
import componment.Student;

public class DBcon {

	public ArrayList<TestData> getTestDatas(String dbName, int questionID) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		ArrayList<TestData> testDatas = new ArrayList<TestData>();
		String sql = "select * from test_data WHERE question_id = ?;";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, questionID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TestData td = new TestData();
				td.setId(rs.getInt("id"));
				td.setQuestionID(rs.getInt("question_id"));
				td.setInput_data(rs.getString("input_data"));
				td.setTrue_result(rs.getString("true_result"));
				testDatas.add(td);
			}
			pstmt.close();
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			con.close();
		}
		return testDatas;
	}
	
	public int[] getTestDatas(String dbName,int[] questionCount,int hwSize) throws Exception{
		Connection con=null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			con = DbProxy.getConnection(dbName);
			String sql = "SELECT * FROM test_data order by question_id;";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				int question_id=rs.getInt(2);
				if(question_id<=999999)
				questionCount[question_id]++;
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		//return studentIDList;
		return questionCount;
	}
	
	public String getLanguage(String dbName, String questionID) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "select language from message WHERE id = ?;";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String language="";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, questionID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				language=rs.getString(1);
			}
			pstmt.close();
			rs.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			con.close();
		}
		return language;
	}

	public void insertCheckResult(String dbName, int questionID, String student_ID, int test_data_id, boolean result,
			String description) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "INSERT INTO test_result (question_id, student_id, test_data_id, result, description) VALUES (?,?,?,?,?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, questionID);
			pstmt.setString(2, student_ID);
			pstmt.setInt(3, test_data_id);
			pstmt.setBoolean(4, result);
			pstmt.setString(5, description);
			pstmt.execute();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
	}

	public void clearCheckResult(String dbName, int questionID, String student_ID) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "delete from test_result WHERE question_id = ? AND student_id = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, questionID);
			pstmt.setString(2, student_ID);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
	}

	public void getTestResult(String dbName,ArrayList<?> sts,int hwSize) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "select * from test_result order by student_id;";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) 
			{
				String question_id=rs.getString(2);
				String student_id=rs.getString(3);
				boolean result=rs.getBoolean(5);
				if(Integer.parseInt(question_id)<999999)
				{
					for(int i=0;i<sts.size();i++)
					{
						Student s=(Student) sts.get(i);
						if(s.getName().toLowerCase().equals(student_id.toLowerCase()))
						{
							s.countUp(question_id, result);
						}
					}
				}
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		//return studentIDList;
	}

	
	public ArrayList<TestResult> getTestResult(String dbName, int questionID, String student_ID) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		ArrayList<TestResult> result = new ArrayList<TestResult>();
		String sql = "select * from test_result WHERE question_id = ? AND student_id = ?;";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, questionID);
			pstmt.setString(2, student_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TestResult tr = new TestResult();
				tr.setId(rs.getInt("id"));
				tr.setQuestionID(rs.getInt("question_id"));
				tr.setTest_data_id(rs.getInt("test_data_id"));
				tr.setResult(rs.getBoolean("result"));
				tr.setDescription(rs.getString("description"));
				result.add(tr);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		return result;
	}

	public void insertTestData(String dbName, int questionID, String input_data, String true_result) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "INSERT INTO test_data (question_id, input_data, true_result) VALUES (?, ?, ?);";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, questionID);
			pstmt.setString(2, input_data);
			pstmt.setString(3, true_result);
			pstmt.execute();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
	}

	public void deleteTestData(String dbName, int testDataID) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "delete from test_data WHERE id = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, testDataID);
			pstmt.execute();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
	}

	public void updateCheckResult(String dbName, int testResultID, boolean result, String description)
			throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "update test_result SET result = ?, description =? WHERE id = ?;";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, result);
			pstmt.setString(2, description);
			pstmt.setInt(3, testResultID);
			pstmt.execute();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
	}

	public ArrayList<String> selectStudentIDList(String dbName) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "select * from login order by id;";
		ArrayList<String> studentIDList = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				studentIDList.add(rs.getString("id"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		return studentIDList;
	}

	// ---20150313
	public ArrayList<String> selectStudentNameList(String dbName) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "select * from login order by id;";
		ArrayList<String> studentNAMEList = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				studentNAMEList.add(rs.getString("name"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		return studentNAMEList;
	}

	public ArrayList<String> selectHomeworkIDList(String dbName) throws Exception {
		Connection con = DbProxy.getConnection(dbName);
		String sql = "select id from message where type = 'hw' order by id;";
		ArrayList<String> howeworkTitleList = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				howeworkTitleList.add(rs.getString("id"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		return howeworkTitleList;
	}
}
