package componment;

import java.util.*;
import ExamDB.*;

public class UserHomework {
	private String id;
	private String _time;
	private String title;
	private String fileDescription;
	private String fileName;
	private String systemName;
	private String saveDirectory;

	public UserHomework(String id, String t, String title, String f, String fn, String st, String sd) {
		this.id = id;
		this._time = t;
		this.title = title;
		this.fileDescription = f;
		this.fileName = fn;
		this.systemName = st;
		this.saveDirectory = sd;
	}

	public String getId() {
		return id;
	}

	public String getTime() {
		return _time;
	}

	public String getTitle() {
		return title;
	}

	public String getFileDesc() {
		return fileDescription;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getSaveDirectory() {
		return saveDirectory;
	}

	public static boolean isExistUserOneHomework(ArrayList<?> uhws, String title) {
		UserHomework uhw = null;
		for (int i = 0; i < uhws.size(); i++) {
			uhw = (UserHomework) uhws.get(i);
			if (uhw.getTitle() != null) {
				if (uhw.getTitle().equals(title)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isExistUserOneHomework(String dbName, String id, String title) {
		ArrayList<UserHomework> uhws = getUserHomework(dbName, id);
		UserHomework uhw = null;
		for (int i = 0; i < uhws.size(); i++) {
			uhw = (UserHomework) uhws.get(i);
			if (uhw.getTitle() != null) {
				if (uhw.getTitle().equals(title)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean delUserOneHomework(String dbName, String id, String title) {
		if (!isExistUserOneHomework(dbName, id, title)) {
			return false;
		}
		// systemname=rs.getString("systemname");
		// File delfile=new
		// File("d:\\resin\\doc\\Java\\upload\\"+title+"\\"+systemname);
		// delfile.delete(); //刪除存於系統中的檔案
		String sql = "DELETE FROM Homework WHERE title = ? AND id = ?;";
		DbProxy.delData(dbName, sql, title, id);
		String sql2 = "DELETE FROM test_result WHERE question_id = ? AND student_id = ?;";
		DbProxy.delData2(dbName, sql2, title, id);
		return true;
	}

	public static ArrayList<UserHomework> getUserHomework(String dbName, String id) {
		ArrayList<UserHomework> uhws = new ArrayList<UserHomework>();
		String[] param = new String[1];
		param[0] = id;
		String sql = "SELECT * FROM Homework WHERE id = ? order by title;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			uhws.add(new UserHomework(id, data[1], data[2], data[3], data[4], data[5], data[6]));
		}
		return uhws;
	}

	public boolean writeDB(String dbName) {
		String[] data = new String[7];
		String sql = "insert into Homework values(?, ?, ?, ?, ?, ?, ?);";
		data[0] = id;
		data[1] = _time;
		data[2] = title;
		data[3] = fileDescription;
		data[4] = fileName;
		data[5] = systemName;
		data[6] = saveDirectory;
		return DbProxy.addData(dbName, sql, data);
	}
}