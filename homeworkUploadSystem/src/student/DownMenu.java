package student;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class DownMenu extends HttpServlet {

	private static final long serialVersionUID = 6238040408109659969L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String message = "";
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<h3>" + st.getName() + ", " + st.getRealName() + " 您好！</h3>");
		message = Article.getNewsArticle(st.getDbName(), st.getCourse());
		pw.print("<h3> <font color=#FF0000> 最新公告：  " + message + " </font></h3>");
		pw.println("</body></html>");
		pw.close();
	}
}
