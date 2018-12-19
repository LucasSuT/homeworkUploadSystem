package student;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import componment.*;

public class HomeworkBoard extends HttpServlet {

	private static final long serialVersionUID = -6828757103170180760L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		Student st = (Student) session.getAttribute("Student");
		if (st == null && tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		User user = User.getUser(tr, st);
		ArrayList<?> hws;
		Homework hw;
		String hwType = req.getParameter("hwType");
		if (hwType == null) {
			hwType = "課後作業";
		}
					//
//		System.out.println(user.getDbName()+"  "+hwType);
					//
		hws = Homework.getAllHomeworks(user.getDbName(), hwType);
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		ArrayList<?> uhws = UserHomework.getUserHomework(user.getDbName(), user.getName());
		pw.println(" <a href=HomeworkBoard?hwType=" + "課後作業" + "> 課後作業 </a> ");
		pw.println(" <a href=HomeworkBoard?hwType=" + "隨堂練習" + "> 隨堂練習</a> ");
		pw.println(" <a href=HomeworkBoard?hwType=" + "實習練習" + "> 實習練習 </a>  請注意繳交期限");
		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>編號</TD>  <TD>種類</TD> <TD>題號</TD> <TD>繳交期限</TD> <TD>繳交</TD><TD>語言</TD><TD>備註</TD><TD>測試</TD></TR>");
				//
		//System.out.println(hws.size());
				//
		for (int i = 1; i <= hws.size(); i++) {
			hw = (Homework) hws.get(i - 1);
						//
			//System.out.println(hw.getType()+"   !");
						//
			pw.println("<TR>");
			pw.println("<TD>" + i + "</TD><TD>" + hw.getType() + "</TD>");
			
			pw.println("<TD> <a href=showHomework?hwId=" + hw.getId() + ">" + hw.getId() + "</a></TD>");
			if (hw.isExpired()) {
				pw.println("<TD>" + hw.getDeadline() + "</TD>");
				pw.println("<TD> 期限已過 </TD>");
			} else {
				pw.println("<TD>" + hw.getDeadline() + "</TD>");
				pw.println("<TD> <a href=upLoadHw?hwId=" + hw.getId() + ">  繳交 </a> </TD>");
			}
			pw.println("<TD>" + hw.getlanguage() + "</TD>");
			if (tr != null) {
				pw.println("<TD> <a href=SetHomework?hwId=" + hw.getId() + ">  編修 </a> </TD>");
				pw.println("<TD> <a href='TestDataList.jsp?questionID=" + hw.getId() + "'> 測試資料 </a> </TD>");
				pw.println("<TD> <a href='HW_result_report.jsp?HW_ID=" + hw.getId() + "'> 測試結果 </a> </TD>");
				pw.println("<TD> <a href=ExportStudentHW?title=" + hw.getId() + ">  匯出學生繳交作業 </a> </TD>");
				pw.println("<TD> <a href=#  OnMouseDown=\"checkDel('DelHomework?hwId=" + hw.getId()
						+ "')\"> 刪除 </a> </TD>");
			} else {
				if (UserHomework.isExistUserOneHomework(uhws, hw.getId())) {
					pw.println("<TD><font color=#FF0000>  已繳 </font></TD>");
					pw.println("<TD><a href='CheckResult.jsp?questionID=" + hw.getId() + "&studentID=" + st.getName()
							+ "' target='_blank'>查看結果</a></TD>");
					pw.println("<TD><a href='success.jsp?HW_ID=" + hw.getId() + "' target='_blank'>通過測試名單</a></TD>");
				} else {
					pw.println("<TD>  未繳 </TD><TD>-</TD>");
					pw.println("<TD><a href='success.jsp?HW_ID=" + hw.getId() + "' target='_blank'>通過測試名單</a></TD>");
				}
			}
			
			pw.println("</TR>");
		}
		pw.println("</TABLE>");
		
		String[] v = { "課後作業", "隨堂練習", "實習練習" };
		String t[]= {"C","Java","Python","C#"};
//		String t[]= {"C","Java","Python"};
//		String t[]= {"C","Java"};
		if (tr != null) {
			pw.println("<FORM ACTION=AddHomework METHOD=post name=form1> <br>");
			pw.println("作業類型：<select name= type>");
			pw.println("<option " + "selected" + " value=" + v[0] + " >" + v[0] + "</option>");
			pw.println("<option value=" + v[1] + " >" + v[1] + "</option>");
			pw.println("<option value=" + v[2] + " >" + v[2] + "</option>");
			pw.println("</select><br>");
			pw.println("程式語言：<select name= ltype>");
			pw.println("<option " + "selected" + " value=" + t[0] + " >" + t[0] + "</option>");
			for(int i=1;i<t.length;i++) {
				System.out.println(t[i]);
			pw.println("<option value=" + t[i] + " >" + t[i] + "</option>");
			//pw.println("<option value=" + t[2] + " >" + t[2] + "</option>");
			}
			pw.println("</select><br>");
			pw.println("相 似 度：<INPUT TYPE=text NAME=weights SIZE=20> %以上算抄襲<br>");
			pw.println("題        號：<INPUT TYPE=text NAME=hwId SIZE=40> <br>");
			pw.println("期        限：<INPUT TYPE=text NAME=deadline SIZE=40> <br>");					
			pw.println("內        容：<TEXTAREA NAME=content ROWS=6 COLS=40></TEXTAREA> <br>");
			pw.println(
					"<INPUT TYPE=submit onClick=\"return checkString(form1.deadline.value, form1.hwId.value);\" VALUE=增加> <INPUT TYPE=reset VALUE=清除>");
			pw.println("</FORM>");
		}
		pw.println("</body></html>");
		pw.close();
	}
}