package teacher;

import componment.*;
//修改 homework
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class SetHomework extends HttpServlet {

	private static final long serialVersionUID = -7807514106154471389L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		String hwId = req.getParameter("hwId");
		Homework hw = Homework.getHomework(tr.getDbName(), hwId);
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		String dline = hw.getDeadline();
		pw.println("作業類型：" + hw.getType() + " <br>");
		pw.println("題         號：" + hwId + " <br>");
		pw.println("<FORM ACTION=SetHomework METHOD=post name=form1> <br>");
		pw.println("相 似 度：<INPUT TYPE=text NAME=weights value ='" + hw.getWeights() + "' SIZE=20> %<br>");
		pw.println("期        限：<INPUT TYPE=text NAME=deadline value ='" + dline + "' SIZE=40> <br>");
		pw.println("內        容：<TEXTAREA NAME=content  ROWS=6 COLS=40>" + hw.getNoBRContent() + "</TEXTAREA> <br>");
		pw.println("<INPUT type = hidden name=hwId value =" + hwId + ">");
		pw.println(
				"<INPUT TYPE=submit onClick=\"return checkString(form1.deadline.value, form1.hwId.value);\" VALUE=修改> <INPUT TYPE=reset VALUE=清除>");
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
		String hwId = req.getParameter("hwId");
		String content = req.getParameter("content");
		int weights = Integer.valueOf(req.getParameter("weights"));
		// 2017-05-18 Fix " and ' can't insert bug.
		content = content.replaceAll("\'", "&#39;");
		content = content.replaceAll("\"", "&quot;");
		String deadline = req.getParameter("deadline");
		String srh = "\n";
		int index = 0;
		while ((index = content.indexOf(srh)) != -1) { // 處理公佈欄的換行字元
			content = content.substring(0, index) + " <BR> " + content.substring(index + srh.length());
		}
		Homework hw = new Homework("", hwId, content, deadline, false, weights);
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		if (hw.setHomework(tr.getDbName())) {
			pw.println("OK");
			pw.println("<br><br><input type=\"button\" value=\"回作業\" onclick=\"history.go( -2 );return true;\"> ");
		} else {
			pw.println("Fail");
		}
		pw.println("</body></html>");
		pw.close();
	}
}