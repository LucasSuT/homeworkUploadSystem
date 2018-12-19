package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class DelHomework extends HttpServlet {

	private static final long serialVersionUID = -4563221292598740807L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String id = req.getParameter("hwId");
		Homework.delHomework(tr.getDbName(), id);
		res.sendRedirect("HomeworkBoard");
	}
}