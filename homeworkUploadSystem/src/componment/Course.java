package componment;

import java.util.*;
import ExamDB.*;
import java.util.Scanner;
import java.io.*;

public class Course {
	private String id;
	private String name;
	private String teacherId;
	private String teacherName;

	public Course(String id, String name, String tid, String tName) {
		this.id = id;
		this.name = name;
		this.teacherId = tid;
		this.teacherName = tName;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	private static boolean createCourseTable(String dir, String dbName, String courseId) {
		boolean result = true;
		String tableName = "";
		String[] colName = null;
		String[] type = null;
		String sql = "";
		try {
			File directoryFile = new File(dir + courseId);
			/*if (!directoryFile.isDirectory()) {
				directoryFile.mkdir();
			}*/
			Scanner scan = new Scanner(new File(dir + "course.txt"));
			while (scan.hasNext()) {
				tableName = scan.nextLine();
				colName = scan.nextLine().split(",");
				type = scan.nextLine().split(",");
				result = DbProxy.creatTable(dbName, tableName, colName, type);
			}
			scan.close();
			Student.addUser(dbName, "0", "姓名", "0");
			sql = "insert into QuesNum values (?,?,?,?,?)";
			String[] data = { "1", "6", "5", "2005/06/23 19:16:00", "2005/06/23 19:40:00" };
			result = DbProxy.addData(dbName, sql, data);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return result;
	}

	public static boolean addCourse(String dir, String id, String name, String tid, String tName) {
		boolean result = true;
		String sql = "insert into Course values (?,?,?,?);";
		String[] data = new String[4];
		data[0] = id;
		data[1] = name;
		data[2] = tid;
		data[3] = tName;
		result = DbProxy.addData("Course", sql, data);
		result = DbProxy.createDB(id); 
		result = createCourseTable(dir, id, id);
		return result;
	}

	public static boolean createCourse() {
		String dbName = "Course";
		boolean result = true;
		String[] colName = { "id", "name", "teacherId", "teacherPass" };
		String[] type = { "varchar(50) UNIQUE", "varchar(50)", "varchar(50)", "varchar(50)" };
		DbProxy.createDB(dbName);
		result = DbProxy.creatTable(dbName, "Course", colName, type);
		String[] colName1 = { "id", "time" };
		String[] type1 = { "varchar(10)", "varchar(20)" };
		result = DbProxy.creatTable(dbName, "people", colName1, type1);
		String sql = "insert into people values (?,?);";
		String[] data = new String[2];
		data[0] = "1";
		data[1] = "1";
		result = DbProxy.addData(dbName, sql, data);
		return result;
	}

	public static Course getCourse(String id) {
		String dbName = "Course";
		String[] param = new String[1];
		String sql = "SELECT * FROM Course WHERE id = ?;";
		String[] data = null;
		Course course = null;
		param[0] = id;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		if (result.size() > 0) {
			data = (String[]) result.get(0);
			course = new Course(data[0], data[1], data[2], data[3]);
		}
		return course;
	}

	public static boolean delCourse(String id) {
		String dbName = "Course";
		String sql = "delete from Course where id = ?;";
		DbProxy.delData(dbName, sql, id);
		return true;
	}

	public static ArrayList<Course> getAllCourse() {
		String dbName = "Course";
		ArrayList<Course> courses = new ArrayList<Course>();
		Course courseTemp = null;
		String sql = "SELECT * FROM Course ORDER BY id;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			courseTemp = new Course(data[0], data[1], data[2], data[3]);
			courses.add(courseTemp);
		}
		return courses;
	}
}