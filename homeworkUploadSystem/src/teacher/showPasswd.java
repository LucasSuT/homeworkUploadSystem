package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class showPasswd extends HttpServlet {

	private static final long serialVersionUID = 3898009349245465547L;

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

		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>帳號</TD> <TD>姓名</TD> <TD>密碼</TD> </TR>");
		for (int i = 0; i < sts.size(); i++) {
			st = (Student) sts.get(i);
			pw.println("<TR>");
			pw.println("<TD>" + st.getName() + "</TD>");
			pw.println("<TD> " + st.getRealName() + "</TD>");
			pw.println("<TD> " + st.getPasswd() + "</TD>");
			pw.println("</TR>");
		}
		pw.println("</TABLE>");
		pw.println("</body></html>");
		pw.close();
	}
}
