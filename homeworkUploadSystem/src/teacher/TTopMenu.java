package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class TTopMenu extends HttpServlet {

	private static final long serialVersionUID = -6909984688693040117L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		// -------------------如果沒有登入則導向登入網頁 --------------
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<html><head>");
		pw.println("<content=\"text/html; charset=Big5\">");
		pw.println("<title>Online Exam</title></head>");
		pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#333300  link=#00FFFF vlink=#CCFF33 alink=#FFCCFF > <font size=2>");
		pw.println(tr.getCourse() + ":" + tr.getName() + "_" + tr.getRealName());
		pw.println("<A HREF=addUser TARGET=DownMenu>[加入帳號</A> ");
		pw.println("<A HREF=addUserFromFile TARGET=DownMenu>加入帳號(從文字檔)</A> ");
		pw.println("<A HREF=delUser TARGET=DownMenu>刪除帳號</A> ");
		pw.println("<A HREF=showPasswd TARGET=DownMenu>顯示帳號密碼]</A> ");
		pw.println("<A HREF=HomeworkBoard TARGET=DownMenu>[出作業</A> ");
//		pw.println("<A HREF='t_score_report.jsp' TARGET=DownMenu>作業通過統計</A> "); //2018/9/20 take out
		pw.println("<A HREF=HwScore TARGET=DownMenu>作業通過統計</A> ");
//		pw.println("<A HREF=HwStatistics TARGET=DownMenu>作業繳交統計]</A> "); //2018/9/20 take out
		pw.println("<A HREF=ExamBoard TARGET=DownMenu>[編修考題資料庫</A> ");
		pw.println("<A HREF=ChooseQuesType TARGET=DownMenu>出考題</A> ");
		pw.println("<A HREF=SetExamTime TARGET=DownMenu>設定考試時間</A> ");
		pw.println("<A HREF=DelExam TARGET=DownMenu>重新考試</A> ");
		pw.println("<A HREF=setScore TARGET=DownMenu>考試成績設定</A> ");
		pw.println("<A HREF=showAllScore TARGET=DownMenu>顯示所有成績]</A> ");
		pw.println("<A HREF=MessageBoard TARGET=DownMenu>留言版</A> ");
		pw.println("<A HREF=changePasswd TARGET=DownMenu>更改密碼</A> ");
//		pw.println("<A HREF=initInterScore TARGET=DownMenu>啟動互評成績</A> "); //2018/2/27 take out
//		pw.println("<A HREF=showInterScore TARGET=DownMenu>顯示互評成績</A> "); //2018/2/27 take out
//		pw.println("<A HREF=TestDaemon TARGET=DownMenu>測試Daemon</A> "); //2018/2/27 take out
		pw.println("<A HREF=Logout TARGET=_parent>登出</A>");
		pw.println("</body></html>");
		pw.close();
	}
}
