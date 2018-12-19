package student;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class InitExam extends HttpServlet {

	private static final long serialVersionUID = -5668969763237759676L;
	String dbName = "", startTime = "", quesString = "", quesNum = "", name = "";
	UserExam userExam;
	Exam exam;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter pw;
		String result = "";
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		// ----- 顯示目前題目與選項 ----------
		dbName = st.getDbName();
		name = st.getName();
		result = initQues(name);
		session.setAttribute("userExam", userExam);
		// userExam = new UserExam();
		if (result.equals("Not this time")) {
			session.setAttribute("OverMessage", "Not this time");
			res.sendRedirect("Over");
			return;
		} else if (result.equals("Exam Over")) {
			session.setAttribute("OverMessage", "Exam Over");
			res.sendRedirect("Over");
			return;
		}
		// ----- ��ܥثe�D�ػP�ﶵ ----------
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		pw = res.getWriter();
		pw.println("<html><head>");
		pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		pw.println("<title>Online Exam</title>");
		pw.println("<script language='JavaScript'>");
		pw.println("function look(){if(event.altKey) alert(\"禁止按Alt鍵!\");} document.onkeydown=look; ");
		pw.println("function scroll(){window.status=\"hello\"; setTimeout(\"scroll()\",0);  }   ");
		pw.println("</script></head>");
		pw.println(
				"<BODY onLoad=\"look(); scroll(); return true;\" oncontextmenu=window.event.returnValue=false TEXT=#FFFFFF BGCOLOR=#000000>");
		pw.println(userExam.getUserCurrent() + "嗨！ " + name + "答過答案： " + userExam.getUserAnswer() + ", 答過"
				+ userExam.getUserAnswerLength() + " 題, 答對 " + userExam.getUserRight() + " 題, 本次共："
				+ userExam.getQuesCount() + ", 題");
		pw.println("<br>開放起始時間: " + exam.getLegalStartTime() + ", <br>開放結束時間: " + exam.getLegalEndTime()
				+ "<br><font color=#FF0000>考試結束時間: " + userExam.getMaxEndTime() + " <br> 現在時間: " + startTime
				+ "</Font>");
		pw.println("<FORM action=Examing method=post name=FORM1>");
		pw.println("<INPUT type=submit value=Start name=submit>");
		pw.println("</form>");
		pw.println("</body></html>");
		pw.close();
	}

	// ---從資料庫 QuesNum 取得所有題數 totalQuesNum，考試總題數 quesCount
	// ---亂數產生題號 quesNum,
	// ---第一次考試初始化 userAnswerString, userCurrent, userRight，存進資料庫
	// ---再次進來考試，取得所有題號 quesNumber，使用者答過的答案 userAnswer
	// ---答過題數 userCurrent, 答對題數 userRight----
	private String initQues(String name) {
		int quesCount = 0, quesSet = 0;
		String maxEndTime = "", result = "Hi";
		// -----開始考試時間 ------------------
		DateBean dateBean = new DateBean();
		startTime = dateBean.getDateTime();
		exam = new Exam(dbName);
		// --- 檢查是否在合法的考試時間 ---------------------------
		if (!exam.isLegalTime(startTime)) {
			return "Not this time";
		}
		maxEndTime = exam.getLegalEndTime();
		quesSet = 5;
		quesCount = exam.getQuesCount();
		quesNum = Exam.genQues(exam.getTotalQuesCount(), quesCount);
		userExam = new UserExam(name, maxEndTime, quesCount, quesNum, quesSet);
		userExam.init(dbName, startTime);
		if (userExam.isExamOver()) {
			result = "Exam Over";
		}
		return result;
	}

}
