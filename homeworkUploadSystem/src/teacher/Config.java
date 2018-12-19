package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class Config extends HttpServlet {

	private static final long serialVersionUID = 6793391474197073516L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		pw.println("<h3> 歡迎光臨線上課程管理系統-系統設定 </h3>");
		pw.println("<form action = Config method=POST name=FORM1>");
		pw.println("Course id (English)    : <input type = text name = id><br><br>");
		pw.println("Course name (Chinese)  : <input type = text name = name><br><br>");
		pw.println("Teacher id (English)   : <input type = text name = tid><br><br>");
		pw.println("Teacher passwd         : <input type = password name = passwd><br><br>");
		pw.println(
				"<br><br><input type = submit onClick=\"return checkString(FORM1.name.value, FORM1.passwd.value);\"  style=width:60 value =Ok>");
		pw.println("</form>");
		pw.println("<TABLE BORDER=1>");
		pw.println("</TABLE>");
		pw.println("</body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("BIG5");
		req.setCharacterEncoding("BIG5");
		PrintWriter pw = res.getWriter();
		req.getSession(true);
		// String Path ="d:\\resin\\doc\\";
		String Path = req.getRealPath("/") + "WEB-INF/setting/";
		String courseId = req.getParameter("id");
		String courseName = req.getParameter("name");
		String tid = req.getParameter("tid");
		String tPasswd = req.getParameter("passwd");
		Course.createCourse();
		System.out.println("1");
		Course.addCourse(Path, courseId, courseName, tid, tPasswd);
		System.out.println("2");
		ArrayList<?> courses = Course.getAllCourse();
		Course c = null;
		pw.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		pw.println("<head>");
		pw.println("<content=\"text/html; charset=Big5\">");
		pw.println("<title>Online Exam</title>");
		pw.println("</head>");
		pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000>");
		pw.println("<TABLE BORDER=1>");
		for (int i = 0; i < courses.size(); i++) {
			c = (Course) courses.get(i);
			pw.println("<TR><TD>" + c.getId() + "</TD><TD>" + c.getName() + "</TD>");
			pw.println("<TD>" + c.getTeacherId() + "</TD><TD>" + c.getTeacherName() + "</TD></TR>");
		}
		pw.println("</TABLE></body></html>");
		pw.close();
	}
}
