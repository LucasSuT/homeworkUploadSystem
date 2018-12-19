package student;

import java.io.*;
import ExamDB.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 5331490486489936572L;
	String man = "1";

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String sql = "";
		String[] optName = null;
		Course c = null;
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		sql = "SELECT time FROM people WHERE id ='1';";
		man = DbProxy.getData("Course", sql, "time");
		ArrayList<?> courses = Course.getAllCourse();
		optName = new String[courses.size()];
		for (int i = 0; i < courses.size(); i++) {
			c = (Course) courses.get(i);
			optName[i] = c.getName();
		}
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<h3> 歡迎光臨線上課程系統 T</h3>");
		pw.println("<form action = Login method=POST name=FORM1>");
		pw.println("name    : <input type = text name = name><br><br>");
		pw.println("passwd: <input type = password name = passwd><br><br>");
		pw.println("<INPUT type=radio name=rdoCourse" + " value=" + 1 + " checked>");
		pw.println("<FONT SIZE = 4>" + optName[0] + "</FONT>");
		for (int j = 2; j <= optName.length; j++) {
			pw.println("<INPUT type=radio name=rdoCourse value=" + j + ">");
			pw.println("<FONT SIZE = 4>" + optName[j - 1] + "</FONT>");
		}
		pw.println(
				"<br><br><input type = submit onClick=\"return checkString(FORM1.name.value, FORM1.passwd.value);\"  style=width:60 value =Ok>");
		pw.println("</form>");
		pw.println("您是第 " + man + " 個上線者");
		pw.println("</body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String sql = "SELECT time FROM people WHERE id ='1';";
		man = DbProxy.getData("Course", sql, "time");
		PrintWriter pw = res.getWriter();
		String strSQL = "";
		DateBean dateBean = new DateBean();
		String loginTime = dateBean.getDateTime();
		HttpSession session = req.getSession(true);
		String userName = req.getParameter("name");
		String userPasswd = req.getParameter("passwd");
		String i_course = req.getParameter("rdoCourse");
		int i = Integer.parseInt(i_course);
		ArrayList<?> courses = Course.getAllCourse();
		String[] opt = new String[courses.size()];
		String[] optName = new String[courses.size()];
		Course c = null;
		for (int k = 0; k < courses.size(); k++) {
			c = (Course) courses.get(k);
			opt[k] = c.getId();
			optName[k] = c.getName();
		}
		String course = opt[i - 1];
		String[] score = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
		String dbName = opt[i - 1];
		String coursePath = "d:\\resin\\doc\\" + opt[i - 1] + "\\";
		//
		//System.out.println(dbName);
		//
		String realName = User.checkLogin(dbName, "Login", userName, userPasswd); //dbName=c
		//String realName = User.checkLogin(c, "Login", userName, userPasswd);
		//c C sysop jykuo1449
		Student student = null;

		if (realName.equals("密碼錯誤") || realName.equals("查無此人") || realName.equals("非法入侵")) {
			session.setAttribute("Student", student);
			session.setAttribute("Teacher", null);
			MyUtil.printHtmlHead(pw);
			if (realName.equals("非法入侵")) {
				pw.println("<h3> 非法入侵 你已被追蹤 </h3>");
			} else {
				pw.println("<h3>" + realName + "</h3>");
			}
			pw.println("<h3><A href = Login>請重新登入</A><h3>");
			pw.println("</head>");
			pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000>");
			pw.println("</body>");
		} else {
			student = new Student(dbName, userName, realName, userPasswd, course, coursePath, score);
			session.setAttribute("Student", student);
			session.setAttribute("Teacher", null);
			i = (Integer.parseInt(man) + 1);
			strSQL = "UPDATE people SET time = ? WHERE id = '1'";
			String[] param = new String[1];
			param[0] = Integer.toString(i);
			DbProxy.setData("Course", strSQL, param);
			param = new String[2];
			param[0] = userName;
			param[1] = loginTime;
			strSQL = "insert into people values(?, ?)";
			DbProxy.setData("Course", strSQL, param);
			res.sendRedirect("MainMenu");
		}
		pw.close();
		test;
		test;
	}
}
