package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class showHomework extends HttpServlet {

	private static final long serialVersionUID = 5779368787840125364L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		User user = User.getUser(tr, st);
		if (st == null && tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		String hwId = req.getParameter("hwId");
//		System.out.println(user.getDbName()+"  "+hwId);
		Homework hw = Homework.getHomework(user.getDbName(), hwId);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println(hw.getContent() + "<br>");
		if (!hw.isExpired()) {
			pw.println("<a href=upLoadHw?hwId=" + hwId + ">  繳交作業 </a>");
		}
		pw.println("<br><br><input type=\"button\" value=\"上一頁\" onclick=\"history.go( -1 );return true;\"> ");
		pw.println("</body></html>");
		pw.close();
	}
}
