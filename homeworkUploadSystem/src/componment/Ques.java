package componment;

import ExamDB.*;
import java.util.*;

public class Ques {
	private String qid;
	private String type1;
	private String type2;
	private String content;
	private String ans;

	public Ques(String i, String t1, String t2, String c, String a) {
		this.qid = i;
		this.type1 = t1;
		this.type2 = t2;
		this.content = c;
		this.ans = a;
	}

	public String getQid() {
		return qid;
	}

	public String getType1() {
		return type1;
	}

	public String getType2() {
		return type2;
	}

	public String getContent() {
		return content;
	}

	private String addBr(String s) {
		int i = 0, width = 80;
		String temp = "";
		if (s.indexOf("<br>") < 0) {
			for (i = 0; i < (s.length() / width); i++) {
				temp = temp + s.substring(i * width, (i + 1) * width) + "<br>";
			}
			temp = temp + s.substring(i * width);
			System.out.println("x" + temp);
			return temp;
		}
		return s;
	}

	public String getTagContent() {
		String temp = "";
		temp = content.replaceAll(" ", " &nbsp; ");
		temp = temp.replaceAll("\n", "<br>");
		return temp;
	}

	public String getAns() {
		return ans;
	}

	public static boolean isExistQues(String dbName, String qid) {
		ArrayList<Ques> allQues = getAllQues(dbName);
		Ques ques = null;
		for (int i = 0; i < allQues.size(); i++) {
			ques = (Ques) allQues.get(i);
			if (ques.getQid() != null) {
				if (ques.getQid().equals(qid)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean delQues(String dbName, String qid) {
		if (!isExistQues(dbName, qid)) {
			return false;
		}
		String sql = "delete from ExamDB where QID = ?;";
		DbProxy.delData(dbName, sql, qid);
		return true;
	}

	public static ArrayList<?> getTypes(String dbName, String col) {
		String sql = "SELECT " + col + ", COUNT(*) AS NUM FROM ExamDB GROUP BY ?;";
		String[] param = new String[1];
		param[0] = col;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		return result;
	}

	public static Ques getQues(String dbName, String qId) {
		Ques ques = null;
		String sql = "SELECT * FROM ExamDB WHERE (QID = ?);";
		String[] param = new String[1];
		param[0] = qId;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			String[] data = (String[]) result.get(0);
			ques = new Ques(data[0], data[1], data[2], data[3], data[4]);
		}
		return ques;
	}

	public static ArrayList<Ques> getAllQues(String dbName) {
		ArrayList<Ques> allQues = new ArrayList<Ques>();
		Ques ques = null;
		;
		String sql = "SELECT * FROM ExamDB order by QID;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			ques = new Ques(data[0], data[1], data[2], data[3], data[4]);
			allQues.add(ques);
		}
		return allQues;
	}

	public static ArrayList<Ques> getSomeQues(String dbName, String col, String[] type) {
		ArrayList<Ques> someQues = new ArrayList<Ques>();
		Ques ques = null;
		String[] data = null;
		String sql = "", condition = "";
		for (int i = 0; i < type.length; i++) {
			if ((i + 1) < type.length) {
				condition = condition + " (" + col + "='" + type[i] + "') OR ";
			} else {
				condition = condition + "(" + col + "='" + type[i] + "')";
			}
		}
		sql = "SELECT * FROM ExamDB WHERE ? order by QID;";
		String[] param = new String[1];
		param[0] = condition;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			ques = new Ques(data[0], data[1], data[2], data[3], data[4]);
			someQues.add(ques);
		}
		return someQues;
	}

	public boolean setQues(String dbName) {
		String sql = "UPDATE ExamDB SET Qus = ?, Type1 = ?, Type2 = ?, Ans = ? WHERE QID = ?;";
		String[] data = new String[5];
		data[0] = content;
		data[1] = type1;
		data[2] = type2;
		data[3] = ans;
		data[4] = qid;
		String result = DbProxy.setData(dbName, sql, data);
		if (result.equals("OK")) {
			return true;
		}
		return false;
	}

	public boolean addQues(String dbName) {
		String sql = "insert into ExamDB values (?, ?, ?, ?, ?);";
		String[] data = new String[5];
		data[0] = qid;
		data[1] = type1;
		data[2] = type2;
		data[3] = content;
		data[4] = ans;
		boolean result = DbProxy.addData(dbName, sql, data);
		if (result) {
			return true;
		}
		return false;
	}

	public static boolean tranExam(String dbName, ArrayList<?> allQues) {
		boolean result = true;
		String sql = "";
		String[] data = new String[3];
		Ques ques = null;
		sql = "delete from Exam;";
		result = DbProxy.delAllData(dbName, sql);
		sql = "insert into Exam values (?, ?, ?);";
		for (int i = 0; i < allQues.size(); i++) {
			ques = (Ques) allQues.get(i);
			data[0] = "" + (i + 1);
			data[1] = ques.getContent();
			data[2] = ques.getAns();
			result = DbProxy.addData(dbName, sql, data);
		}
		if (result) {
			return true;
		}
		return false;
	}

	public static String getNextId(String dbName) {
		ArrayList<Ques> allQues = getAllQues(dbName);
		Ques ques = null;
		int i = 0;
		for (i = 0; i < allQues.size(); i++) {
			ques = (Ques) allQues.get(i);
			if ((i + 1) < Integer.parseInt(ques.getQid())) {
				return (i + 1) + "";
			}
		}
		return ((i + 1) + "");
	}
}