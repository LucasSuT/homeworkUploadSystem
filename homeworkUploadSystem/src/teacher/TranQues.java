package teacher;

import componment.*;
//轉移考試題目
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class TranQues extends HttpServlet {

	private static final long serialVersionUID = 1964200293587053319L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// doPost(req, res); 編輯刪除
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);

		String qid = req.getParameter("qid");
		ArrayList<?> allQues = (ArrayList<?>) session.getAttribute("EditQues");
		Ques ques = null;
		for (int i = 0; i < allQues.size(); i++) {
			ques = (Ques) allQues.get(i);
			if (ques.getQid().equals(qid)) {
				allQues.remove(i);
				break;
			}
		}
		session.setAttribute("EditQues", allQues);
		res.sendRedirect("EditQues?quesCount=" + 1);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// ~~~~~~~~轉檔
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		ArrayList<?> allQues = (ArrayList<?>) session.getAttribute("EditQues");
		Ques.tranExam(tr.getDbName(), allQues);
		pw.println("OK");
		res.sendRedirect("SetExamTime");
		pw.println("</html>");
		pw.close();
	}
}