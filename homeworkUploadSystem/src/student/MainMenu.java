package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class MainMenu extends HttpServlet {

	private static final long serialVersionUID = -1811807434664588432L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<FRAMESET rows=\"10%,*\" border=0>");
		pw.println("<FRAME name = TopMenu SRC= TopMenu>");
		pw.println("<FRAME name = DownMenu SRC= DownMenu>");
		pw.println("</FRAMESET>");
		pw.println("</html>");
		pw.close();
	}
}