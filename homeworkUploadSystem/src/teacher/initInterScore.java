package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class initInterScore extends HttpServlet {

	private static final long serialVersionUID = 3302582245311610887L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String dbName = tr.getDbName();
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);

		Evaluator.initEvaluation(dbName);
		pw.println("Active Inter Scoring");
		pw.close();
	}
}
