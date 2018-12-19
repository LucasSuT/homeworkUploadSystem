package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class TLogin extends HttpServlet {

	private static final long serialVersionUID = 7876740627289838611L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		ArrayList<?> courses = Course.getAllCourse();
		String[] optName = new String[courses.size()];
		Course c = null;
		for (int i = 0; i < courses.size(); i++) {
			c = (Course) courses.get(i);
			optName[i] = c.getName();
		}

		pw.println("<h3> 歡迎光臨線上課程管理系統 </h3>");
		pw.println("<form action = TLogin method=POST name=FORM1>");
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
		pw.println("</body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		HttpSession session = req.getSession(true);
		String userName = req.getParameter("name");
		String userPasswd = req.getParameter("passwd");
		String i_course = req.getParameter("rdoCourse");
		String dbName = "";
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
		String coursePath = "d:\\resin\\doc\\" + opt[i - 1] + "\\";
		String realName = Teacher.checkLogin("Course", "Course", opt[i - 1], userName, userPasswd);
		ArrayList<String> managerIp = Teacher.getManagerIp("Course", "manager_ip");
		dbName = opt[i - 1];
		if (managerIp.contains(req.getRemoteAddr().toString())) {
			if ((!realName.equals("密碼錯誤")) && (!realName.equals("查無此人"))) {
				//         						c      sysop     sysop    jykuo1449     c      
				Teacher teacher = new Teacher(dbName, userName, realName, userPasswd, course, coursePath);
				session.setAttribute("Teacher", teacher);
				res.sendRedirect("TMainMenu");
			}
			res.setLocale(new Locale(new String("zh"), new String("TW")));
			res.setContentType("text/html");
			pw.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
			pw.println("<head>");
			pw.println("<content=\"text/html; charset=Big5\">");
			pw.println("<title>Online Exam</title>");
			pw.println("<h3>" + realName + "</h3>");
			pw.println("<h3><A href = Login>請重新登入</A><h3>");
			pw.println("</head>");
			pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000>");
			pw.println("</body>");
			pw.close();
		} else {
			res.setLocale(new Locale(new String("zh"), new String("TW")));
			res.setContentType("text/html");
			pw.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
			pw.println("<head>");
			pw.println("<content=\"text/html; charset=Big5\">");
			pw.println("<title>Online Exam</title>");
			pw.println("<h3>" + req.getRemoteAddr().toString() + "</h3>");
			pw.println("<h3>偵測到非法IP,您的IP已被追蹤<h3>");
			pw.println("</head>");
			pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000>");
			pw.println("</body>");
		}

	}
}
