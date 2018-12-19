package componment;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class MessageBoard extends HttpServlet {

	private static final long serialVersionUID = 2394339513071955444L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		if ((tr == null) && (st == null)) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		User user = User.getUser(tr, st);
		int i = 0;
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		ArrayList<?> arts = Article.getAllArticle(user.getDbName());
		Article art = null;

		String articleCountString = req.getParameter("articleCount");
		int articleCount = 1;
		if (articleCountString != null) {
			articleCount = Integer.parseInt(articleCountString);
			articleCount = (articleCount - 1) * 10 + 1;
		}
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>篇數</TD> <TD>日期</TD> <TD>主題</TD>");
		if (user instanceof Teacher) {
			pw.println("<TD> 刪除 </TD>");
		}
		pw.println(" <TD>作者</TD></TR>");
		i = 1;
		for (int k = 0; k < arts.size(); k++) {
			art = (Article) arts.get(k);
			if ((i >= articleCount) && (i <= (articleCount + 9))) {
				pw.println("<TR><TD>" + i + "</TD>");
				pw.println("<TD>" + art.getTime() + "</TD>");
				pw.println("<TD> <a href=showArticle?time=" + art.getTime() + ">"
						+ art.getTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</a></TD>");
				if (tr != null) {
					pw.println("<TD> <a href=#  OnMouseDown=\"checkDel('deleteArticle?time=" + art.getTime()
							+ "')\">  刪除 </a> </TD>");
				}
				pw.println("<TD>" + art.getName() + "</TD></TR>");
			}
			i++;
		}
		pw.println("</TABLE>");
		for (int j = 0; j <= ((i - 2) / 10); j++) {
			pw.println(" <a href=MessageBoard?articleCount=" + (j + 1) + "> 第" + (j + 1) + "頁 </a> ");
		}
		pw.println("<FORM ACTION=AddMsgBoard METHOD=post name =form1> <br>");
		pw.println("主    題：<INPUT TYPE=text NAME=title SIZE=40> <br>");
		pw.println("內    容：<TEXTAREA NAME=body ROWS=6 COLS=40></TEXTAREA> <br>");
		pw.println(
				"<INPUT TYPE=submit VALUE=新增 onClick=\"return checkString(form1.title.value, form1.body.value);\"> <INPUT TYPE=reset VALUE=清除>");
		pw.println("</FORM>");
		pw.println("</body></html>");
		pw.close();
	}
}