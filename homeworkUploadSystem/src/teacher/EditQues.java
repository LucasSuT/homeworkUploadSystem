package teacher;

import componment.*;
//�s��Ҹ��D��
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class EditQues extends HttpServlet {

	private static final long serialVersionUID = -410760762134956718L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// doPost(req, res);
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

		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>題數</TD> <TD>分類</TD> <TD>子分類</TD><TD>內容</TD>");
		pw.println("<TD>答案</TD><TD> 刪除 </TD></TR>");
		Ques ques = null;
		String quesCountString = req.getParameter("quesCount");
		int quesCount = 1;
		if (quesCountString != null) {
			quesCount = Integer.parseInt(quesCountString);
			quesCount = (quesCount - 1) * 10;
		}
		for (int k = quesCount; (k < allQues.size()) && (k <= (quesCount + 9)); k++) {
			ques = (Ques) allQues.get(k);
			pw.println("<TD>" + ques.getQid() + "</TD>");
			pw.println("<TD> " + ques.getType1() + "</a></TD>");
			pw.println("<TD> " + ques.getType2() + "</a></TD>");
			pw.println("<TD> " + ques.getTagContent() + "</a></TD>");
			pw.println("<TD> " + ques.getAns() + "</a></TD>");
			pw.println("<TD> <a href=#  OnMouseDown=\"checkDel('TranQues?qid=" + ques.getQid()
					+ "')\">  刪除 </a> </TD></TR>");
		}
		pw.println("</TABLE>");
		pw.println("(" + (quesCount / 10 + 1) + ")");
		for (int j = 0; j <= ((allQues.size() - 1) / 10); j++) {
			pw.println(" <a href=EditQues?quesCount=" + (j + 1) + "> " + (j + 1) + " |</a> ");
		}
		pw.println("<FORM ACTION=TranQues METHOD=post name =form1> <br>");
		pw.println("<INPUT TYPE=submit VALUE=轉檔>");
		pw.println("</FORM>");
		pw.println("</body></html>");
		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	}
}