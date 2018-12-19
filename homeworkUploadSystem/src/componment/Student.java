package componment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import ExamDB.*;
import java.util.*;

public class Student extends User {
	private static final int scoreSize = 10;
	private String[] score = new String[scoreSize];
	private int [] studentPass;

	public Student(String d, String n, String r, String p, String c, String cp, String[] sc) {
		super(d, n, r, p, c, cp);
		for (int i = 0; i < score.length; i++) {
			score[i] = sc[i];
		}
	}
	public Student(String d, String n, String r, String p, String c, String cp, String[] sc,int hwSize) {
		super(d, n, r, p, c, cp);
		for (int i = 0; i < score.length; i++) {
			score[i] = sc[i];
		}
		studentPass=new int[999999];
	}

	public int getScoreSize() {
		return score.length;
	}
	
	public void countUp(String question_id,boolean result)
	{
		if(result==true)
		{
			studentPass[Integer.parseInt(question_id)]++;
		}
	}
	
	public int getStudentPass(int question_id)
	{
		return studentPass[question_id];
	}

	public String getScore(int i) {
		if (i < score.length) {
			return score[i];
		}
		return "0";
	}

	public static boolean setScoreDB(String dbName, String id, int index, String score) {
		String[] data = new String[2];
		data[0] = score;
		data[1] = id;
		String sql = "UPDATE Login SET score" + index + " = ? WHERE id= ?;";
		String result = DbProxy.setData(dbName, sql, data);
		if (result.equals("OK")) {
			return true;
		}
		return false;
	}

	public static boolean setAllScoreDB(String dbName, String[][] sData) {
		int index = sData[0].length - 1;
		int stNo = sData.length;
		String[] data = new String[2];
		String sql;
		String result;
		for (int i = 1; i <= index; i++) {
			for (int j = 0; j < stNo; j++) {
				data[0] = sData[j][i];
				data[1] = sData[j][0];
				sql = "UPDATE Login SET score" + i + " = ? WHERE id= ?;";
				result = DbProxy.setData(dbName, sql, data);
				if (!result.equals("OK")) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean setPasswd(String p) {
		try {
			Connection con = DbProxy.getConnection(dbName);
			String sql = "UPDATE Login SET pass= ? WHERE id= ? ";
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, p);
			pstmt.setString(2, name);
			pstmt.execute();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static ArrayList<Student> getAllStudent(String dbName) {
		ArrayList<Student> students = new ArrayList<Student>();
		Student st;
		String id = "", name = "", passwd = "";
		String[] _score = new String[scoreSize];
		String sql = "SELECT * FROM Login ORDER BY Login.id";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			id = data[0];
			name = data[1];
			passwd = data[2];
			for (int j = 1; j <= 10; j++) {
				_score[j - 1] = data[2 + j];
			}
			st = new Student(dbName, id, name, passwd, " ", " ", _score);
			students.add(st);
		}
		return students;
	}
	
	public static ArrayList<Student> getAllStudent(String dbName,int hwSize) {
		ArrayList<Student> students = new ArrayList<Student>();
		Student st;
		String id = "", name = "", passwd = "";
		String[] _score = new String[scoreSize];
		String sql = "SELECT * FROM Login ORDER BY Login.id";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			id = data[0];
			name = data[1];
			passwd = data[2];
			for (int j = 1; j <= 10; j++) {
				_score[j - 1] = data[2 + j];
			}
			st = new Student(dbName, id, name, passwd, " ", " ", _score,hwSize);
			students.add(st);
		}
		return students;
	}

	public static boolean addUser(String dbName, String id, String realName, String pass) {
		String[] param = new String[1];
		param[0] = id;
		String sql = "SELECT * FROM Login WHERE ID = ?;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			return false;
		}
		sql = "insert into Login values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		data = new String[13];
		data[0] = id;
		data[1] = realName;
		data[2] = pass;
		for (int i = 0; i < scoreSize; i++) {
			data[i + 3] = "0";
		}
		DbProxy.addData(dbName, sql, data);
		// Evaluator.addEvaluation(dbName, id);
		return true;
	}

	public static boolean delUser(String dbName, String id) {
		boolean delFlag = false;
		String[] data = new String[1];
		data[0] = id;
		String sql = "SELECT * FROM Login WHERE ID = ?;";
		ArrayList<?> result = DbProxy.selectData(dbName, sql, data);
		if (result.size() > 0) {
			sql = "delete from Login where id = ?;";
			DbProxy.delData(dbName, sql, id);
			delFlag = true;
		}
		return delFlag;
	}
}