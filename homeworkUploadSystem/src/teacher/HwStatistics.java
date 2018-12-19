package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class HwStatistics extends HttpServlet {

	private static final long serialVersionUID = 3216128071067326377L;

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

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		boolean[][] checkList = new boolean[100][100];
		int count = 0;
		String temp = "";
		//												c
		ArrayList<?> sts = Student.getAllStudent(tr.getDbName());//c.login all new student();
		ArrayList<String> hws = new ArrayList<String>();
		Student st = null;
		MyUtil.printTeacherHead(pw);
		Homework.hwStatistic(tr.getDbName(), sts, hws, checkList);
		pw.println("<TABLE BORDER=1 style=\"border-collapse: collapse\">");
		pw.println("<TR> <TD><font size=1>帳號</font></TD>");
		pw.println("<TD><font size=2>UsName</font></TD>");
		for (int i = 0; i < hws.size(); i++) {
			temp = (String) hws.get(i);
			pw.println("<TD><font size=1>" + temp + "</font></TD>");
		}
		pw.println("<TD><font size=1> total </font></TD></TR>");
		for (int i = 0; i < sts.size(); i++) {
			st = (Student) sts.get(i);
			pw.println("<TR>");
			pw.println("<TD><font size=2>" + st.getName() + "</font></TD>");
			pw.println("<TD><font size=2>" + st.getRealName() + "</font></TD>");
			count = 0;
			for (int j = 0; j < hws.size(); j++) {
				if (checkList[i][j]) {
					pw.println("<TD><font color=#FFFFFF size=1>1</font></TD>");
					count++;
				} else {
					pw.println("<TD><font color=#FF0000 size=1>0</font></TD>");
				}
			}
			pw.println("<TD><font color=#FFFFFF size=1>" + count + "</font></TD>");
			pw.println("</TR>");
		}
		pw.println("</TABLE>");
		pw.println("</body></html>");
		pw.close();
	}
}
