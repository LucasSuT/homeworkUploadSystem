package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class TMainMenu extends HttpServlet {

	private static final long serialVersionUID = 3053543219796501790L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		// -------------------如果沒有登入則導向登入網頁 --------------
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<FRAMESET rows=\"10%,*\" border=0>");
		pw.println("<FRAME name = TopMenu SRC= TTopMenu>");
		pw.println("<FRAME name = DownMenu SRC= showAllScore>");
		pw.println("</FRAMESET>");
		pw.println("</html>");
		pw.close();
	}
}