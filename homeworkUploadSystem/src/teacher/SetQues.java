package teacher;

import componment.*;
//修改 Exam
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class SetQues extends HttpServlet {

	private static final long serialVersionUID = -5623891599581325436L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String qId = req.getParameter("qId");
		Ques ques = Ques.getQues(tr.getDbName(), qId);
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		pw.println("題   數：" + qId + " <br>");
		pw.println("<FORM ACTION=SetQues METHOD=post name=form1> <br>");
		pw.println("分    類：<INPUT TYPE=text NAME=type1 value ='" + ques.getType1() + "' SIZE=20> <br>");
		pw.println("子分類：<INPUT TYPE=text NAME=type2 value ='" + ques.getType2() + "' SIZE=20> <br>");
		pw.println("答    案：<INPUT TYPE=text NAME=ans    value ='" + ques.getAns() + "' SIZE=20> <br>");
		pw.println("內容：<TEXTAREA NAME=content  ROWS=15 COLS=60>" + ques.getContent() + "</TEXTAREA> <br>");
		pw.println("<INPUT type = hidden name=qId value =" + qId + ">");
		pw.println(
				"<INPUT TYPE=submit onClick=\"return checkString(form1.ans.value, form1.content.value);\" VALUE=修改> <INPUT TYPE=reset VALUE=清除>");
		pw.println("</FORM>");
		pw.println("</body></html>");
		pw.close();

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
		String qId = req.getParameter("qId");
		String type1 = req.getParameter("type1");
		String type2 = req.getParameter("type2");
		String ans = req.getParameter("ans");
		String content = req.getParameter("content");
		Ques ques = new Ques(qId, type1, type2, content, ans); //

		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		if (ques.setQues(tr.getDbName())) {
			pw.println("OK");
			res.sendRedirect("ExamBoard");
			// pw.println("<br><br><input type=\"button\" value=\"回出考題\"
			// onclick=\"history.go( -2 );return true;\"> ");
		} else {
			pw.println("Fail");
		}
		pw.println("</body></html>");
		pw.close();
	}
}