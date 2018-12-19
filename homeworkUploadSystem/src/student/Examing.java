package student;

/*  1. copy servlet.jar into same directory
 *  2. write a servlet program
 *  3. compiler the program, javac -classpath .;servlet.jar BankServlet.java
 *  4. install web application server, e.g. resin, Tomcat, WebLogic, WebSphere, ...
 *  4. copy BankServlet.class to  resin\doc\WEB-INF\classes
 *  5. http://127.0.0/servlet/BankServlet
 */
import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class Examing extends HttpServlet {

	private static final long serialVersionUID = 4994733344586488714L;
	final int quesSet = 5;
	UserExam userExam;
	String dbName = "", nowTime = "", name = "", s = "";

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter pw;
		int userAnswer = 0;
		boolean doWrite = true;
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		// -----現在時間 --
		DateBean dateBean = new DateBean();
		nowTime = dateBean.getDateTime();
		userExam = (UserExam) session.getAttribute("userExam");
		// --------- 檢查是否超過考試時間 -------------------------
		if (userExam.isTimeOver(nowTime)) {
			session.setAttribute("OverMessage", "Over Time");
			res.sendRedirect("Over");
			return;
		} else if (userExam.isExamOver()) {
			session.setAttribute("OverMessage", "Exam Over");
			res.sendRedirect("Over");
			return;
		}
		// - 答過題, 取得使用者答案 userAnswer, 電腦答案 compAnswer,作比對
		if (req.getParameter("submit1") != null) {
			// --- 檢查是否重複答題，隱藏欄位 checkDo為目前網頁題號，
			// ---與 userCurrent不符表示回去舊網頁重考，doWrite=false
			s = req.getParameter("checkDo");
			if (Integer.parseInt(s) != userExam.getUserCurrent()) {
				doWrite = false;
			} else if (!userExam.isExamOver()) {
				doWrite = true;
				for (int i = 1; i <= quesSet; i++) {
					if (userExam.isExamOver()) {
						break;
					}
					s = req.getParameter("rdoQ" + i);
					userAnswer = Integer.parseInt(s);
					userExam.addScore(userAnswer, i - 1);
				} // end for
			} // end if
		}
		dbName = st.getDbName();
		// 將使用者答案 userAnswer, 答過題數 userCurrent, 答對題數 userRight 存入資料庫
		if (doWrite) {
			userExam.writeDB(dbName, nowTime);
		}
		// -- 從 session 物件取得 QuesNumString (亂數產生的題目號數字串) ，依此
		// -- 從資料庫中 Exam 取得目前題目 quesString, 答案 compAnswer，存入 session 物件
		userExam.getQues(dbName);
		if (userExam.isExamOver()) {
			session.setAttribute("OverMessage", "Exam Over");
			res.sendRedirect("Over");
			return;
		}
		// ----- 顯示目前題目與選項 ----------
		String[] quesString = userExam.getQuesString();
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		pw = res.getWriter();
		int thisQuesCount = 0; // 本次網頁出現考題 (1~5)
		int newQuesSet = quesSet;
		int userCurrent = userExam.getUserCurrent();
		if ((userCurrent % quesSet) != 0) {
			newQuesSet = userCurrent % quesSet;
		}
		for (int i = 1; i <= newQuesSet; i++) {
			if ((userCurrent - newQuesSet + i) > userExam.getQuesCount()) {
				break;
			}
			thisQuesCount++;
		}
		pw.println("<html><head>");
		pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=Big5\">");
		pw.println("<title>Online Exam</title>");
		pw.println("<script language=javascript>");
		pw.println("function check() {                     ");
		pw.println("   var f = document.FORM1;        ");
		for (int i = 1; i <= thisQuesCount; i++) {
			pw.println("   if (f.rdoQ" + i + "[4].checked) {       ");
			pw.println("         alert(\"第" + i + "題放棄\");       ");
			pw.println("         return false;                       ");
			pw.println("   }                                            ");
		}
		pw.println("   return true;     }                      ");
		pw.println(
				"function look(){if(event.altKey) alert(\"禁止按Alt鍵!\");if(event.ctrlKey) alert(\"禁止按Ctrl鍵!\");} document.onkeydown=look; ");
		pw.println("function scroll(){window.status=\"hello\"; setTimeout(\"scroll()\",0);  }   ");
		pw.println("</script></head>");
		// pw.println("<BODY onLoad=\"look(); scroll(); return true;\"
		// oncontextmenu=window.event.returnValue=false TEXT=#FFFFFF
		// BGCOLOR=#000000>");
		pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000>");
		pw.println(userExam.getUserCurrent() + " " + name + " 答過答案: " + userExam.getUserAnswer() + ", 本次共 "
				+ userExam.getQuesCount() + " 題, 答過 " + userExam.getUserAnswerLength() + " 題, 對 "
				+ userExam.getUserRight() + " 題");
		pw.println("<br><font color=#FF0000>考試結束時間: " + userExam.getMaxEndTime() + ", 現在時間: " + nowTime + "</Font>");
		pw.println("<FORM action=Examing method=post name=FORM1>");

		for (int i = 1; i <= newQuesSet; i++) {
			if ((userCurrent - newQuesSet + i) > userExam.getQuesCount()) {
				break;
			}
			pw.println("(" + (userCurrent - newQuesSet + i) + ")" + quesString[i - 1] + "<br>");
			String[] opt = { "a", "b", "c", "d" };
			int j = 0;
			String n = "rdoQ" + i;
			for (j = 1; j <= opt.length; j++) {
				pw.println("<INPUT type=radio name=" + n + " value=" + j + ">");
				pw.println("<FONT SIZE = 4>" + opt[j - 1] + "</FONT><br>");
			}
			pw.println("<INPUT type=radio name=" + n + " value=" + j + " checked>");
			pw.println("<FONT SIZE = 4>" + "give up" + "</FONT> <br><br>");
		}
		pw.println("<INPUT type = hidden name=checkDo value =" + userExam.getUserCurrent() + ">");
		pw.println("<br><INPUT type=submit value=Ok name=submit1 onClick=\"return check();\" style=width:70>");
		pw.println("</form>");
		pw.println("</body></html>");
		pw.close();
	}
}