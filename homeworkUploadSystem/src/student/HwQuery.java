package student;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class HwQuery extends HttpServlet {

	private static final long serialVersionUID = 3459255130327633083L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}

		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		ArrayList<?> uhws = UserHomework.getUserHomework(st.getDbName(), st.getName());
		ArrayList<?> hws = Homework.getAllHomeworks(st.getDbName());
		Homework hw;
		UserHomework uhw = null;
		boolean delHw = true;
		pw.println("<TABLE BORDER=1><TR>");
		pw.println("<TD>日期</TD> <TD>主題</TD> <TD>描述</TD> <TD>檔名</TD><TD>編修</TD></TR>");
		for (int i = 0; i < uhws.size(); i++) {
			uhw = (UserHomework) uhws.get(i);
			delHw = true;
			for (int j = 0; j < hws.size(); j++) {
				hw = (Homework) hws.get(j);
				if (hw.getId().equals(uhw.getTitle())) {
					if (hw.isExpired()) {
						delHw = false;
					}
					break;
				}
			}
			pw.println("<TR><TD>" + uhw.getTime() + "</TD>");
			pw.println("<TD>" + uhw.getTitle() + "</TD>");
			pw.println("<TD>" + uhw.getFileDesc().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</TD>");
			pw.println("<TD>" + uhw.getFileName() + "</TD>");
			if (!delHw) {
				pw.println("<TD> 處理中 </TD>");
			} else {
				pw.println("<TD> <a href=#  OnMouseDown=\"checkDel('delHw?title=" + uhw.getTitle()
						+ "')\">刪除 </a></TD> </TR>");
			}
		}
		pw.println("</TABLE>");
		pw.println("</body></html>");
		pw.close();
	}
}
