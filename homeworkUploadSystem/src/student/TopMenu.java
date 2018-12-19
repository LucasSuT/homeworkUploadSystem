package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class TopMenu extends HttpServlet {

	private static final long serialVersionUID = 41801838252835332L;

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

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		session.getAttribute("CourseName");
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println(st.getCourse() + ":" + st.getName() + "_" + st.getRealName());
		pw.println(
				"<A HREF=InitExam TARGET=DownMenu OnmouseOver=\"window.status='';return true;\"OnMouseOut=\"window.status=' ';\">線上考試</A> ");
		pw.println("<A HREF=HwQuery TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">觀看已交作業</A> ");
		pw.println("<A HREF=showScore TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">觀看成績</A> ");
		pw.println("<A HREF=HomeworkBoard TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">作業繳交</A> ");
		pw.println("<A HREF=MessageBoard TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">留言版</A> ");
		pw.println("<A HREF=changePasswd TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">更改密碼</A> ");
//		pw.println("<A HREF=Evaluate TARGET=DownMenu OnmouseOver=\"window.status='';return true;\">評分</A> "); //2018/2/27 take out
		pw.println("<A HREF=Logout TARGET=_parent OnmouseOver=\"window.status='';return true;\">登出</A>");
		pw.println("</body></html>");
		pw.close();
	}
}
