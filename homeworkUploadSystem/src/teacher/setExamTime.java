package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class setExamTime extends HttpServlet {

	private static final long serialVersionUID = 2689699165202560204L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		Exam exam = new Exam(tr.getDbName());

		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		int index[] = { 5, 12, 31, 24, 60 };
		int startIndex[] = { 2017, 1, 1, 0, 0 };
		String msg[] = { "年", "月", "日", "時", "分" };
		String optName[][] = {
				{ "startExamYear", "startExamMonth", "startExamDay", "startExamHour", "startExamMinute" },
				{ "endExamYear", "endExamMonth", "endExamDay", "endExamHour", "endExamMinute" } };
		String titleMsg[] = { "<br>開始開放考試時間：", "<br>結束開放考試時間：" };
		String quesMsg[] = { "<br>設定本次考試題數：" };
		String quesName[] = { "quesCount" };
		int[] quesNum = new int[2];
		quesNum[0] = exam.getTotalQuesCount();
		quesNum[1] = exam.getQuesCount();
		MyUtil.printTeacherHead(pw);
		pw.println("<h3> 設定考試時間與題數</h3>");
		pw.println("<h3> 資料庫總題數 " + quesNum[0] + "</h3>");
		pw.println("<h3> 目前設定考試開始時間 " + exam.getLegalStartTime() + "</h3>");
		pw.println("<h3> 目前設定考試結束時間 " + exam.getLegalEndTime() + "</h3>");
		pw.println("<h3> 目前設定本次考試題數 " + quesNum[1] + "</h3>");
		pw.println("<h3><font color=#FF0000> 考試題數請設定為5的倍數。</font></h3>");
		DateBean dateBean = new DateBean(); // ---- 初始化日期 --------
		int toDay[] = new int[index.length];
		toDay[0] = dateBean.getYearI();
		toDay[1] = dateBean.getMonthI();
		toDay[2] = dateBean.getDayI();
		toDay[3] = dateBean.getHourI();
		toDay[4] = dateBean.getMinuteI();
		pw.println("<form name=Form1 method=post action=SetExamTime>");
		pw.print(quesMsg[0]);
		pw.println("<select name=" + quesName[0] + ">");
		for (int j = 1; j < quesNum[0]; j++) {
			if (j == quesNum[1]) {
				pw.println("<option " + "selected" + " value=" + j + " >" + j + "</option>");
			} else {
				pw.println("<option value=" + j + " >" + j + "</option>");
			}
		}
		pw.println("</select>");
		for (int k = 0; k < titleMsg.length; k++) {
			pw.print(titleMsg[k]);
			for (int j = 0; j < index.length; j++) {
				int v = 0;
				pw.println("<select name=" + optName[k][j] + ">");
				for (int i = 0; i < index[j]; i++) {
					v = i + startIndex[j];
					if (v == toDay[j]) {
						pw.println("<option " + "selected" + " value=" + v + " >" + v + "</option>");
					} else {
						pw.println("<option value=" + v + " >" + v + "</option>");
					}
				}
				pw.println("</select>");
				pw.println(msg[j]);
			}
		}
		pw.println("<INPUT type = hidden name=totalQuesCount value =" + quesNum[0] + ">");
		pw.println("<br><br><input type=submit value=Ok>");
		pw.println("</form></center></body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		String optName[][] = {
				{ "startExamYear", "startExamMonth", "startExamDay", "startExamHour", "startExamMinute" },
				{ "endExamYear", "endExamMonth", "endExamDay", "endExamHour", "endExamMinute" } };
		String optValue[][] = new String[optName.length][optName[0].length];
		String quesName[] = { "totalQuesCount", "quesCount" };
		String quesNum[] = new String[quesName.length];
		String space[] = { "/", "/", " ", ":", ":" };
		HttpSession session = req.getSession(true);
		String[] timeString = { "", "" };
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}

		res.setCharacterEncoding("BIG5");
		req.setCharacterEncoding("BIG5");
		MyUtil.printTeacherHead(pw);
		for (int i = 0; i < quesName.length; i++) {
			quesNum[i] = req.getParameter(quesName[i]);
		}
		// if (Integer.parseInt(quesNum[1])>Integer.parseInt(quesNum[0])) {
		// quesNum[1] = ""+(Integer.parseInt(quesNum[0])-1);
		// }
		for (int i = 0; i < optName.length; i++) {
			for (int j = 0; j < optName[0].length; j++) {
				optValue[i][j] = req.getParameter(optName[i][j]);
				if (Integer.parseInt(optValue[i][j]) < 10) {
					optValue[i][j] = "0" + optValue[i][j];
				}
				timeString[i] = timeString[i] + optValue[i][j];
				if (j < space.length) {
					timeString[i] = timeString[i] + space[j];
				}
			}
			timeString[i] = timeString[i] + "00";
		}
		pw.println("<h3> 設定考試開始時間: " + timeString[0] + "</h3>");
		pw.println("<h3> 設定考試結束時間: " + timeString[1] + "</h3>");
		pw.println("<h3> 設定題庫總題數 " + quesNum[0] + "</h3>");
		pw.println("<h3> 設定本次考試題數 " + quesNum[1] + "</h3>");
		pw.println("<h3><font color=#FF0000> 請至『重新考試』選單，清除上次考試資料，重新考試。</font></h3>");
		Exam.setTime(tr.getDbName(), timeString, quesNum);
		pw.println("</body></html>");
		pw.close();
	}
}
