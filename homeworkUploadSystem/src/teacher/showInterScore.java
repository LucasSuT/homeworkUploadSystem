package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class showInterScore extends HttpServlet {

	private static final long serialVersionUID = 1354012602407405660L;

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

		Evaluator eval = null;
		int[] score;
		String[][] sender;
		ArrayList<?> sts = Student.getAllStudent(tr.getDbName());
		Student st = null;
		pw.println("<TABLE BORDER=1>");
		for (int i = 0; i < sts.size(); i++) {
			if (i == 0) {
				st = (Student) sts.get(1);
				pw.println("<TR>");
				pw.println("<TD>" + "        " + "</TD>");
				pw.println("<TD><Font size =1>" + "姓名" + "</TD>");
				eval = new Evaluator(st.getDbName(), st.getName());
				sender = eval.getSender();
				for (int j = 0; j < sender.length; j++) {
					pw.println("<TD><Font size =1> " + sender[j][1] + "</TD>");
				}
				pw.println("<TD><Font size =1> " + "平均" + "</TD>");
				pw.println("</TR>");
			} else {
				st = (Student) sts.get(i);
				pw.println("<TR>");
				pw.println("<TD><Font size =1>" + st.getName() + "</TD>");
				pw.println("<TD><Font size =1> " + st.getRealName() + "</TD>");
				eval = new Evaluator(st.getDbName(), st.getName());
				score = eval.getAllScoreR();
				for (int j = 0; j < score.length; j++) {
					pw.println("<TD><Font size =1> " + score[j] + "</TD>");
				}
				pw.println("<TD><Font size =1> " + eval.getScoreR() + "</TD>");
				pw.println("</TR>");
			}
		}
		pw.println("</TABLE></body></html>");
		pw.close();
	}
}
