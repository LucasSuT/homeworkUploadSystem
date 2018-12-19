package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class EditAllScore extends HttpServlet {

	private static final long serialVersionUID = 5370143248077021962L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// doPost(req, res);
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		tr.getDbName();
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		ArrayList<?> sts = Student.getAllStudent(tr.getDbName());
		Student st = null;
		String no = "";
		pw.println("<FORM ACTION=EditAllScore METHOD=post name =form1> <br>");
		pw.println("<TABLE BORDER=1>");
		for (int i = 0; i < sts.size(); i++) {
			st = (Student) sts.get(i);
			pw.println("<TR>");
			pw.println("<TD>" + st.getName() + "</TD>");
			pw.println("<TD> " + st.getRealName() + "</TD>");
			for (int j = 0; j < st.getScoreSize(); j++) {
				if (j < 10)
					no = st.getName() + "0" + j;
				else
					no = st.getName() + j;
				pw.println("<TD><input type=text name=" + no + " size=3 value=" + st.getScore(j) + "></TD>");
			}
			pw.println("</TR>");
		}
		pw.println("</TABLE>");
		pw.println("<INPUT TYPE=submit VALUE=存檔>");
		pw.println("</FORM>");
		pw.println("</body></html>");
		pw.close();
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
		String[][] sData = null;
		String no = "", temp = "";
		MyUtil.printTeacherHead(pw);
		ArrayList<?> sts = Student.getAllStudent(tr.getDbName());
		Student st = null;
		sData = new String[sts.size()][11];
		for (int i = 0; i < sts.size(); i++) {
			st = (Student) sts.get(i);
			sData[i][0] = st.getName();
			for (int j = 0; j < 10; j++) {
				no = st.getName() + "0" + j;
				temp = req.getParameter(no);
				if (sData[i][0].equals("0") && (temp != null)) {
					sData[i][j + 1] = temp;
				} else
					sData[i][j + 1] = temp;
			}
		}
		Student.setAllScoreDB(tr.getDbName(), sData);
		res.sendRedirect("EditAllScore");
	}
}
