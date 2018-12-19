package teacher;

import componment.*;
//�W�[�Ҹ��D��
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class AddQues extends HttpServlet {

	private static final long serialVersionUID = -1965576262694045426L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		String type1 = req.getParameter("type1");
		String type2 = req.getParameter("type2");
		String ans = req.getParameter("ans");
		String content = req.getParameter("content");
		String qid = Ques.getNextId(tr.getDbName());
		Ques ques = new Ques(qid, type1, type2, content, ans);
		if (ques.addQues(tr.getDbName())) {
			pw.println("OK");
			res.sendRedirect("ExamBoard");
		} else {
			pw.println("false");
		}
		pw.println("</body></html>");
		pw.close();
	}
}