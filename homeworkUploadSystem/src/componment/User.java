package componment;

import java.util.ArrayList;
import ExamDB.*;

public class User {

	protected String dbName;
	protected String name;
	protected String realName;
	protected String passwd;
	protected String course;
	protected String coursePath;

	public User(String d, String n, String r, String p, String c, String cp) {
		dbName = d;
		name = n;
		realName = r;
		passwd = p;
		course = c;
		coursePath = cp;
	}

	public static User getUser(User u1, User u2) {
		if (u1 != null) {
			return u1;
		}
		return u2;
	}

	public static String checkLogin(String dbName, String table, String name, String Passwd) {
		String r = "查無此人";
		String account;
		String passwd = "";
		String[] param = new String[1];
		param[0] = name;
		String sql = "SELECT * FROM " + table + " WHERE id = ?;";
		String[] data = null;
		ArrayList<?> result = null;
		if (name.indexOf("or") != -1 || name.indexOf("'") != -1 || table.indexOf(";") != -1) {
			r = "非法入侵";
		} else {
			//
			//System.out.println(dbName+"   "+table);
			//
			result = DbProxy.selectData(dbName, sql, param);
			if (result.size() > 0) {
				data = (String[]) result.get(0);
				passwd = data[2];
				account=data[0];
				if(!account.equals(name))r="查無此人";
				else if (!passwd.equals(Passwd)) {
					r = "密碼錯誤";
				} else {
					r = data[1];
				}
			} else {
				r = "查無此人";
			}
		}
		return r;
	}

	public String getName() {
		return name;
	}

	public String getRealName() {
		return realName;
	}

	public String getPasswd() {
		return passwd;
	}

	public boolean setPasswd(String p) {
		return true;
	}

	public String getCourse() {
		return course;
	}

	public String getCoursePath() {
		return coursePath;
	}

	public String getDbName() {
		return dbName;
	}
}
