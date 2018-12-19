package componment;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class showArticle extends HttpServlet {

	private static final long serialVersionUID = -5004175571049171676L;

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
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<html><head>");
		pw.println("<content=\"text/html; charset=UTF-8\">");
		pw.println("<title>Online Exam</title>");
		pw.println("<script language='JavaScript'>");
		pw.println(
				"function checkString(s1,s2) {  if ((s1==\"\")||(s2==\"\")) { alert(\"請填完整\");  return false;  } }  ");
		pw.println("function look(){if(event.altKey) alert(\"禁止按Alt鍵!\"); } document.onkeydown=look; ");
		pw.println("function scroll(){window.status=\"hello\"; setTimeout(\"scroll()\",100);  }   ");
		pw.println("</script></head>");
		pw.println(
				"<BODY onLoad=\"look(); scroll(); return true;\" oncontextmenu=window.event.returnValue=false TEXT=#FFFFFF BGCOLOR=#000000>");
		String _time = req.getParameter("time");
		ArrayList<?> arts = Article.getAllArticle(user.getDbName());
		Article art = null;
		for (int k = 0; k < arts.size(); k++) {
			art = (Article) arts.get(k);
			if (_time.equals(art.getTime())) {
				pw.println("<pre>" + art.getBody().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</pre>");
				break;
			}
		}
		pw.println(
				"<br><br><br><input type=\"button\" value=\"上一頁\" onclick=\"history.go( -1 );return true;\"> <br><br><br>");
		pw.println("<FORM ACTION=AddMsgBoard METHOD=post name =form1> <br>");
		pw.println("主    題：<INPUT TYPE=text NAME=title SIZE=40 value =Re:"
				+ art.getTitle().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + " > <br>");
		pw.println("內    容：<TEXTAREA NAME=body ROWS=6 COLS=40>" + art.getBody() + "</TEXTAREA> <br>");
		pw.println(
				"<INPUT TYPE=submit VALUE=回覆 onClick=\"return checkString(form1.title.value, form1.body.value);\"> <INPUT TYPE=reset VALUE=清除>");
		pw.println("</FORM>");
		pw.println("</body></html>");
		pw.close();
	}
}
