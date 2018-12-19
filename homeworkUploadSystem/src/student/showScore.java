package student;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class showScore extends HttpServlet {

	private static final long serialVersionUID = -211745004680815924L;

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
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		ArrayList<?> sts = Student.getAllStudent(st.getDbName());
		Student stTemp = null;
		pw.println("<TABLE BORDER=1>");
		for (int i = 0; i < sts.size(); i++) {
			stTemp = (Student) sts.get(i);
			if ((stTemp.getName().equals("0")) || (stTemp.getName().equals(st.getName()))) {
				pw.println("<TR><TD>" + stTemp.getName() + "</TD>");
				pw.println("<TD> " + stTemp.getRealName() + "</TD>");
				for (int j = 0; j < stTemp.getScoreSize(); j++) {
					pw.println("<TD> " + stTemp.getScore(j) + "</TD>");
				}
				pw.println("</TR>");
			}
		}
		pw.println("</TABLE></body></html>");
		pw.close();
	}
}
