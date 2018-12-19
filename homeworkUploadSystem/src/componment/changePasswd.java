package componment;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class changePasswd extends HttpServlet {

	private static final long serialVersionUID = -4275064312021997268L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		if ((tr == null) && (st == null)) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<form action=changePasswd method=post name=form1>");
		pw.println("<p><h2>更改密碼</h2></p>");
		pw.println("<p>Passwd:<input type=text name=pass size=20></p>");
		pw.println("<input type=submit name=submit value=\"sumit\" onClick=\"return checkModify(form1.pass.value);\">");
		pw.println("<input type=reset name=reset value=\"reset\">");
		pw.println("</form>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String pass = req.getParameter("pass");
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		if ((tr == null) && (st == null)) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		User user = User.getUser(tr, st);
		boolean changed = user.setPasswd(pass);
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		if (changed) {
			pw.println("Change Password <h3>" + user.getName() + ":" + user.getPasswd() + "</h3> Success");
		} else {
			pw.println(" <H3>資料錯誤 請重新輸入 /h3>");
		}
		pw.println("</body></html>");
		pw.close();
	}
}
