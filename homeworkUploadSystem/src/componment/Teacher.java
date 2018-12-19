package componment;

import java.util.*;
import ExamDB.*;

public class Teacher extends User {
	public Teacher(String d, String n, String r, String p, String c, String cp) {
		super(d, n, r, p, c, cp);
	}

	public boolean setPasswd(String p) {
		String[] data = new String[3];
		data[0] = p;
		data[1] = course;
		data[2] = name;
		String sql = "UPDATE course SET teacherPass = ? WHERE id = ? and teacherId = ?;";
		String result = DbProxy.setData("Course", sql, data);
		if (result.equals("OK")) {
			passwd = p;
			return true;
		}
		return false;
	}

	public static ArrayList<String> getManagerIp(String dbName, String table) {
		ArrayList<String> r = new ArrayList<String>();
		String sql = "SELECT * FROM manager_ip;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				data = (String[]) result.get(i);
				r.add(data[1]);
			}
		}
		return r;
	}

	public static String checkLogin(String dbName, String table, String id, String tid, String tPasswd) {
		String r = "查無此人";
		String[] param = new String[1];
		param[0] = id;
		String sql = "SELECT * FROM Course WHERE id = ?;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			data = (String[]) result.get(0);
			if (!data[3].equals(tPasswd)) {
				r = "密碼錯誤";
			} else if (!data[2].equals(tid)) {
				r = "查無此人";
			} else {
				r = data[2];
			}
		} else {
			r = "查無此課程";
		}
		return r;
	}
}