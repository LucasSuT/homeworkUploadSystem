package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class Logout extends HttpServlet {

	private static final long serialVersionUID = -7980834449864822837L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		session.setAttribute("Student", null);
		session.setAttribute("Teacher", null);
		if (st == null) {
//			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			session.invalidate();
			res.sendRedirect("TLogin");
			return;
		}
		User user = User.getUser(tr, st);

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		MyUtil.printHtmlHead(pw);
		pw.println("<h3>Welcome Online Exam</h3>");
		pw.println("<h3>Hello! " + user.getName() + ":" + user.getRealName() + ". 您已順利登出 ! </h3>");
		pw.println(
				"<A HREF=Login OnmouseOver=\"window.status='';return true;\"OnMouseOut=\"window.status=' ';\">重新登入</A><br>");
		pw.println("<A HREF=http://www.cc.ntut.edu.tw/~jykuo/>回到首頁</A>");
		pw.println("</body></html>");
		pw.close();
	}
}
