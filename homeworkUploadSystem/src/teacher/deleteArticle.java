package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class deleteArticle extends HttpServlet {

	private static final long serialVersionUID = 8597871215054656295L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String _time = req.getParameter("time");
		Article.delArticle(tr.getDbName(), _time);
		res.sendRedirect("MessageBoard");
	}
}
