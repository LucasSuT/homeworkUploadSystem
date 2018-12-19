package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

import componment.*;

public class delHw extends HttpServlet {

	private static final long serialVersionUID = 3223129380490809046L;

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
		String title = req.getParameter("title");
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		if (!UserHomework.delUserOneHomework(st.getDbName(), st.getName(), title)) {
			pw.println("無此作業可刪 請重新輸入");
		} else {
			pw.println("delete success");
		}
		pw.println("</body></html>");
		pw.close();
	}
}
