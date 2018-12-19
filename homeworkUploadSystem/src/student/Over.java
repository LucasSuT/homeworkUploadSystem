package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class Over extends HttpServlet {

	private static final long serialVersionUID = 3650257831455040884L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		UserExam userExam;
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String OverMessage = (String) session.getAttribute("OverMessage");
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<h3>Welcome Online Exam</h3>");
		pw.println("<h3>Hello! " + st.getName() + "</h3>");
		if (OverMessage.equals("Not this time")) {
			pw.println("<h3>" + OverMessage + "</h3>");
			pw.println("<h3>" + "請在規定時間考試！" + "</h3>");
		} else if (OverMessage.equals("Over Time")) {
			userExam = (UserExam) session.getAttribute("userExam");
			int quesNumber = userExam.getQuesCount();
			int userRight = userExam.getUserRight();
			;
			String userAnswer = userExam.getUserAnswer();
			pw.println("<h3>" + OverMessage + "</h3>");
			pw.println("<h3>" + "超過考試時間！" + "</h3>");
			pw.println("Your answer is:" + userAnswer + "<br>");
			pw.println(quesNumber + " 題中, 你答對 " + userRight + " 題, 共 " + ((userRight * 101) / quesNumber) + "分");
		} else {
			userExam = (UserExam) session.getAttribute("userExam");
			int quesNumber = userExam.getQuesCount();
			int userRight = userExam.getUserRight();
			;
			String userAnswer = userExam.getUserAnswer();
			pw.println("<h3>You have already done ! </h3>");
			pw.println("<h3>" + "您已經考過試了！" + "</h3>");
			pw.println("Your answer is:" + userAnswer + "<br>");
			pw.println(quesNumber + " 題中, 你答對 " + userRight + " 題, 共 " + ((userRight * 101) / quesNumber) + "分");
		}
		pw.println("</body></html>");
		pw.close();
	}
}
