package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class DelExam extends HttpServlet {

	private static final long serialVersionUID = -5891537961258058407L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		pw.println("<p><h3>清除上次考試資料，重新考試</h3></p>");
		pw.println("<h3><font color=#FF0000> 請先至『考試成績設定』選單，將上次考試成績轉至個人資料檔。</font></h3>");
		pw.println("<form action=DelExam method=post name=form1>");
		pw.println("<INPUT type=radio name=delType value=0 checked> 個人");
		pw.println("<INPUT type=radio name=delType value=1> 全部");
		pw.println("<p>User Name:<input type=text name=id size=20></p>");
		pw.println(
				"<input type=submit name=submit value=\"OK\" onClick=\"return checkClearExam(form1.id.value,form1.delType);\">");
		pw.println("<input type=reset name=reset value=\" Reset \">");
		pw.println("</form>");
		pw.println("</body></html>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		res.setCharacterEncoding("BIG5");
		req.setCharacterEncoding("BIG5");
		String id = req.getParameter("id");
		String delType = req.getParameter("delType");
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		if (delType.equals("0")) {
			if (UserExam.delOneUserExam(tr.getDbName(), id)) {
				pw.println("Clear <h3>" + id + "</h3> Exam Data Success");
			} else {
				pw.println("<h3>  User " + id + " donnot exist ! </h3>");
			}
		} else {
			if (UserExam.delAllUserExam(tr.getDbName())) {
				pw.println("Clear All Exam Data Success");
			} else {
				pw.println("Clear All Exam Data Fail");
			}
		}
		pw.println("</body></html>");
		pw.close();
	}
}