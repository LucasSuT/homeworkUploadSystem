package teacher;

import componment.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class setScore extends HttpServlet {

	private static final long serialVersionUID = -7332372631651386727L;

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
		pw.println("<form action=setScore method=post name=form1>");
		pw.println("<h2>設定最近一次考試成績至學生個人資料檔</h2>");
		pw.println("考試名稱:<input type=text name=examName size=20>");
		pw.println("<br><br>設定至學生個人資料檔中的第幾");
		pw.println("<select name= index >");
		for (int i = 1; i <= 10; i++) {
			pw.println("<option " + " value=" + i + " >" + i + "</option>");
		}
		pw.println("</select>");
		pw.println("次成績。<br><font color=#FF0000> 請先至『顯示所有成績』選單觀看學生成績空欄</font><br><br>");
		pw.println(
				"<input type=submit name=submit value=\"Ok\" onClick=\"return checkModify(form1.examName.value);\">");
		pw.println("<input type=reset name=reset value=\" reset \">");
		pw.println("</form>");
		pw.println("</body></html>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");

		String indexString = req.getParameter("index");
		String examName = req.getParameter("examName");
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		int index = 0, score = 0;
		if (indexString != null) {
			index = Integer.parseInt(indexString);
		}
		ArrayList<?> allExamScore = ExamScore.getAllExamScores(tr.getDbName());

		ExamScore es = null;
		pw.println("index=" + index + ", title=" + examName);
		for (int i = 0; i < allExamScore.size(); i++) {
			es = (ExamScore) allExamScore.get(i);
			score = es.getRightNum();
			score = (score * 102) / (es.getQuesNum());
			pw.println(" ==" + es.getId() + "=" + score);
			Student.setScoreDB(tr.getDbName(), es.getId(), index, score + "");
		}
		Student.setScoreDB(tr.getDbName(), "0", index, examName);
		pw.println("</body></html>");
		pw.close();
	}
}
