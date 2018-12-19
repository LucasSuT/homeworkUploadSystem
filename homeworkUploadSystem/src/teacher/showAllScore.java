package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class showAllScore extends HttpServlet {

	private static final long serialVersionUID = -3166091494769362758L;

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
		tr.getDbName();
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);

		ArrayList<?> sts = Student.getAllStudent(tr.getDbName());
		Student st = null;
		pw.println("<a href=EditAllScore>編修</a>");
		pw.println("<TABLE BORDER=1>");
		for (int i = 0; i < sts.size(); i++) {
			st = (Student) sts.get(i);
			pw.println("<TR>");
			pw.println("<TD>" + st.getName() + "</TD>");
			pw.println("<TD> " + st.getRealName() + "</TD>");
			for (int j = 0; j < st.getScoreSize(); j++) {
				pw.println("<TD> " + st.getScore(j) + "</TD>");
			}
			if (i == 0) {
				pw.println("<TD>編修</TD>");
			} else {

			}
			pw.println("</TR>");
		}
		pw.println("</TABLE></body></html>");
		pw.close();
	}
}
