package componment;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class AddMsgBoard extends HttpServlet {

	private static final long serialVersionUID = 8570210415716823006L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		if ((tr == null) && (st == null)) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		User user = User.getUser(tr, st);
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		DateBean dateBean = new DateBean();
		String title = req.getParameter("title");
		String body = req.getParameter("body");
		String _time = dateBean.getDateTime();
		_time = _time.substring(0, 10) + "_" + _time.substring(11, 19);
		/*
		 * String srh="\n"; while( (index=body.indexOf(srh))!= -1) { //
		 * 處理公佈欄的換行字元
		 * body=body.substring(0,index)+" <BR> "+body.substring(index+srh.length
		 * ()); }
		 */
		Article art = new Article(_time, title, body, user.getName());
		art.writeDB(user.getDbName());
		pw.println("Add " + title + " Success");
		res.sendRedirect("MessageBoard");
		pw.println("</body></html>");
		pw.close();
	}
}