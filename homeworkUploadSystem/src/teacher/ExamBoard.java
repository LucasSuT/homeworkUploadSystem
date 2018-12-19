package teacher;

import componment.*;
//��ܦҸ��D��
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class ExamBoard extends HttpServlet {

	private static final long serialVersionUID = 5139292072985321231L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
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
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		ArrayList<?> allQues = Ques.getAllQues(tr.getDbName());
		Ques ques = null;

		String quesCountString = req.getParameter("quesCount");
		int quesCount = 0;
		if (quesCountString != null) {
			quesCount = Integer.parseInt(quesCountString);
			quesCount = (quesCount - 1) * 10 + 1;
		}
		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>題數</TD> <TD>分類</TD> <TD>子分類</TD><TD>內容</TD>");
		pw.println("<TD>答案</TD><TD> 編修</TD><TD> 刪除 </TD></TR>");
		for (int k = quesCount; (k < allQues.size()) && (k <= (quesCount + 9)); k++) {
			ques = (Ques) allQues.get(k);
			pw.println("<TD>" + ques.getQid() + "</TD>");
			pw.println("<TD> " + ques.getType1() + "</a></TD>");
			pw.println("<TD> " + ques.getType2() + "</a></TD>");
			pw.println("<TD> " + ques.getTagContent() + "</a></TD>");
			pw.println("<TD> " + ques.getAns() + "</a></TD>");
			pw.println("<TD> <a href=SetQues?qId=" + ques.getQid() + ">  編修 </a> </TD>");
			pw.println("<TD> <a href=#  OnMouseDown=\"checkDel('DelQues?qid=" + ques.getQid()
					+ "')\">   刪除 </a> </TD></TR>");
		}
		pw.println("</TABLE>");
		pw.println("(" + (quesCount / 10 + 1) + ")");
		for (int j = 0; j <= ((allQues.size() - 1) / 10); j++) {
			pw.println(" <a href=ExamBoard?quesCount=" + (j + 1) + "> " + (j + 1) + " </a>| ");
		}
		pw.println("<FORM ACTION=AddQues METHOD=post name =form1> <br>");
		pw.println("分    類：<INPUT TYPE=text NAME=type1 SIZE=40> <br>");
		pw.println("子分類：<INPUT TYPE=text NAME=type2 SIZE=40> <br>");
		pw.println("答    案：<INPUT TYPE=text NAME=ans SIZE=40> <br>");
		pw.println("內    容：<TEXTAREA NAME=content ROWS=6 COLS=40></TEXTAREA> <br>");
		pw.println(
				"<INPUT TYPE=submit VALUE=新增 onClick=\"return checkString(form1.content.value, form1.ans.value);\"> <INPUT TYPE=reset VALUE=清除>");
		pw.println("</FORM>");

		pw.println("<form name=Form2 enctype=\"multipart/form-data\" method=post action=AddQuesFile>");
		pw.println("<h2>Add from text file</h2>");
		pw.println("<h2>Text file format: </h2>");
		pw.println("<h3>type1~type2 </h3>");
		pw.println("<h3>answer(1,2,3,4)~question text</h3>");
		pw.println("<h3>answer(1,2,3,4)~question text</h3>");
		pw.println("<h3>. . . </h3></p>");
		pw.println("Upload file: <input type=file name=nameFile size=20 maxlength=20><br><br>");
		pw.println("<input type=submit name=submit value=\"Ok\" >");
		pw.println(
				"<h3>1~Which one is correct? (1) computer is a machine (2) you are a human (3) It is hot (4) She is hot</h3>");
		pw.println("</form>");
		pw.println("</body></html>");
		pw.close();
	}
}