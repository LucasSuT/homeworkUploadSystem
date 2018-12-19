package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class upLoadHw extends HttpServlet {

	private static final long serialVersionUID = 581155036630424356L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String hwId = req.getParameter("hwId");
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
		Homework hw = Homework.getHomework(st.getDbName(), hwId);
		if (hw == null) {
			pw.println("<h2>請依正常程序繳交</h2>");
			pw.println("<br><input type=\"button\" value=\"上一頁\" onclick=\"history.go( -1 );return true;\"> ");

		} else if (hwId == null) {
			pw.println("<h2>請依正常程序繳交</h2>");
			pw.println("<br><input type=\"button\" value=\"上一頁\" onclick=\"history.go( -1 );return true;\"> ");
		} else if (hw.isExpired()) {
			pw.println("<h2>作業繳交期限已過</h2>");
			pw.println("<br><input type=\"button\" value=\"上一頁\" onclick=\"history.go( -1 );return true;\"> ");
		} else if (UserHomework.isExistUserOneHomework(st.getDbName(), st.getName(), hwId)) {
			pw.println("<h2>作業已繳交過" + hwId + "</h2></center>");
			pw.println("<h2>若要重新繳交，請<a href=#  OnMouseDown=\"checkDel('delHw?title=" + hwId
					+ "')\">刪除</a>作業，再重新上傳！</h2></center>");
		} else {
			session.setAttribute("hwId", hwId);
			pw.println("<p> 作業上傳：檔名請使用英文，上傳檔案大小請在2M以下</p>");
			pw.println("<form name=Form1 enctype=\"multipart/form-data\" method=post action=upLoadFile>");
			pw.println("上傳檔案: <input type=file name=hwFile size=20 maxlength=20>");
			pw.println("<br><br>檔案敘述: <input type=text name=FileDesc size=30 maxlength=50></p><p>");
			pw.println("<input type=submit value=upload>");
			pw.println("<input type=reset value=reset></p>");
			pw.println("</form></center>");
		}

		pw.println("</center></body></html>");
		pw.close();
	}
}