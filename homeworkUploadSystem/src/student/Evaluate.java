package student;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class Evaluate extends HttpServlet {

	private static final long serialVersionUID = -7755744634040773598L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		Evaluator eval = new Evaluator(st.getDbName(), st.getName());
		String[][] name = eval.getSender();
		String[] score = eval.getScoreS();
		String[] commentS = eval.getCommentS();
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		pw.println("<h3> 互相評分: 目前分數 " + eval.getScoreR() + "</h3>");
		pw.println("<h3> 目前評語: " + eval.getCommentR() + "</h3>");
		pw.println("<form name=Form1 method=post action=Evaluate>");
		for (int k = 0; k < score.length; k++) {
			try {
				int x = Integer.parseInt(score[k]);
				if ((x > 100) || (x < 0)) {
					score[k] = "0";
				}
			} catch (Exception e) {
				score[k] = "0";
			}
		}
		for (int k = 0; k < name.length; k++) {
			pw.println(name[k][1] + ", 分數: <select name=" + name[k][0] + ">");
			for (int j = 0; j < 100; j++) {
				if (j == Integer.parseInt(score[k])) {
					pw.println(" <option " + "selected" + " value=" + j + " >" + j + "</option>");
				} else {
					pw.println("<option value=" + j + " >" + j + "</option>");
				}
			}
			pw.println("</select><br>");
			pw.println("評語：<input type=text name=comment" + name[k][0] + " value='" + commentS[k] + "' SIZE=80> <br>");
		}
		pw.println("<br><br><input type=submit value=Ok>");
		pw.println("</form></center></body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		HttpSession session = req.getSession(true);

		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		MyUtil.printHtmlHead(pw);
		Evaluator eval = new Evaluator(st.getDbName(), st.getName());
		String[][] sender = eval.getSender();
		String[] score = new String[sender.length];
		String[] comment = new String[sender.length];
		String temp = "";
		for (int i = 0; i < sender.length; i++) {
			score[i] = req.getParameter(sender[i][0]);
			if (score[i] == null) {
				score[i] = "0";
			}
			temp = req.getParameter("comment" + sender[i][0]);
			if (temp != null) {
				comment[i] = temp;
			}
			pw.println(sender[i][1] + "-" + score[i] + "<br>");
			pw.println("-:" + comment[i] + "<br>");
		}
		eval.setScoreS(score, comment);
		pw.println("<h3> 成績評分完成</h3>");
		pw.println("</body></html>");
		pw.close();
	}
}
