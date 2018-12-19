package componment;

import java.util.*;
import ExamDB.*;

public class ExamScore {
	private String id;
	private int quesNum;
	private int rightNum;

	public ExamScore(String id, int q, int r) {
		this.id = id;
		this.quesNum = q;
		this.rightNum = r;
	}

	public String getId() {
		return id;
	}

	public int getQuesNum() {
		return quesNum;
	}

	public int getRightNum() {
		return rightNum;
	}

	public static ExamScore getOneExamScore(String dbName, String id) {
		ExamScore es = null;
		String r = "", q = "";
		int rn = 1, qn = 1;
		String sql = "SELECT * FROM Ques WHERE ID = ?;";
		String[] data = null;
		String[] param = new String[1];
		param[0] = id;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			data = (String[]) result.get(0);
			q = data[1];
			if (q != null)
				qn = Integer.parseInt(q);
			r = data[3];
			if (r != null)
				rn = Integer.parseInt(r);
			es = new ExamScore(id, qn, rn);
		}
		return es;
	}

	public static void addExamScore(String dbName, String id) {
	}

	public boolean setExamScore(String classForName, String dbName) {
		String sql = "UPDATE Ques SET quesNum = ?, rightNum = ? WHERE ID = ?;";
		String result = DbProxy.setExamScore(dbName, sql, quesNum, rightNum, id);
		if (result.equals("OK")) {
			return true;
		}
		return false;
	}

	public static boolean delExamScore(String dbName, String id) {
		String sql = "delete from Ques where id = ?;";
		DbProxy.delData(dbName, sql, id);
		return true;
	}

	public static ArrayList<ExamScore> getAllExamScores(String dbName) {
		ArrayList<ExamScore> allES = new ArrayList<ExamScore>();
		ExamScore es = null;
		String id = "", r = "", q = "";
		int rn = 1, qn = 1;
		String sql = "SELECT * FROM Ques;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			id = data[0];
			q = data[1];
			if (q != null)
				qn = Integer.parseInt(q);
			r = data[3];
			if (r != null)
				rn = Integer.parseInt(r);
			es = new ExamScore(id, qn, rn);
			allES.add(es);
		}
		return allES;
	}
}