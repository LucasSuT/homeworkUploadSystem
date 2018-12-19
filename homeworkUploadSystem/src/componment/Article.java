package componment;

import java.util.*;
import ExamDB.*;

public class Article {
	private String _time;
	private String title;
	private String body;
	private String name;

	public Article(String t, String title, String body, String name) {
		this._time = t;
		this.title = title;
		this.body = body;
		this.name = name;
	}

	public String getTime() {
		return _time;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getName() {
		return name;
	}

	public static boolean isExistArticle(String dbName, String _time) {
		ArrayList<Article> arts = getAllArticle(dbName);
		Article art = null;
		for (int i = 0; i < arts.size(); i++) {
			art = (Article) arts.get(i);
			if (art.getTime() != null) {
				if (art.getTime().equals(_time)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean delArticle(String dbName, String _time) {
		if (!isExistArticle(dbName, _time)) {
			return false;
		}
		String sql = "delete from Message where type = 'article' and id= ?;";
		DbProxy.delData(dbName, sql, _time);
		return true;
	}

	public static ArrayList<Article> getAllArticle(String dbName) {
		ArrayList<Article> arts = new ArrayList<Article>();
		Article art = null;
		;
		String _time = "", title = "", body = "", name = "";
		String sql = "SELECT * FROM Message WHERE type='article' order by id;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			_time = data[1];
			title = data[2];
			name = data[3];
			body = data[4];
			art = new Article(_time, title, body, name);
			arts.add(art);
		}
		return arts;
	}

	// modify
	public static String getOneArticle(String dbName, String title) {
		new ArrayList<Object>();
		String sql = "SELECT * FROM Message WHERE type='article' order by id;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			if (title.equals(data[2])) {
				return data[4];
			}
		}
		return " ";
	}

	public static String getNewsArticle(String dbName, String course) {
		new ArrayList<Object>();
		String[] data = null;
		String[] param = new String[1];
		ArrayList<?> result = null;
		String teacherId = " ";
		String sql = "SELECT teacherId FROM Course where id = ?;";
		param[0] = course;
		result = DbProxy.selectData("Course", sql, param);
		if (result.size() > 0) {
			data = (String[]) result.get(0);
			teacherId = data[0];
		}
		sql = "SELECT * FROM Message WHERE type = 'article' and title = 'news' and memo = ? order by id;";
		param[0] = teacherId;
		result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			data = (String[]) result.get(0);
			return data[4];
		}
		return " ";
	}

	public boolean writeDB(String dbName) {
		String sql = "insert into Message values (?, ?, ?, ?, ?,?,?);";
		String[] data = new String[7];
		data[0] = "article";
		data[1] = _time;
		data[2] = title;
		data[3] = name;
		data[4] = body;
		data[5] = "0";
		data[6] = "0";
		DbProxy.addData(dbName, sql, data);
		return true;
	}
}