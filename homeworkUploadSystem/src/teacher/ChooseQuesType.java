package teacher;

import componment.*;
//選擇考試題目
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class ChooseQuesType extends HttpServlet {

	private static final long serialVersionUID = 4269313011783979342L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
		ArrayList<?> choose = Ques.getTypes(tr.getDbName(), "Type1");
		String[] data = null;
		pw.println("<FORM ACTION=ChooseQuesType METHOD=post name =form1> <br>");
		pw.println("<TABLE BORDER=1>");
		pw.println("<TR> <TD>選擇</TD> <TD>分類</TD> <TD>題數</TD></TR>");
		for (int i = 0; i < choose.size(); i++) {
			data = (String[]) choose.get(i);
			pw.println("<TR><TD> <input type=checkbox name=C" + i + " value=" + data[0] + "></TD>");
			pw.println("<TD> " + data[0] + "</TD>");
			pw.println("<TD> " + data[1] + " </TD>");
		}
		pw.println("<INPUT type = hidden name=chooseCount value =" + choose.size() + ">");
		pw.println("</TABLE>");
		pw.println("<INPUT TYPE=submit VALUE=Edit>");
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
		String chooseCountString = req.getParameter("chooseCount");
		int chooseNo = Integer.parseInt(chooseCountString);
		int No = 0;
		String[] s = new String[chooseNo];
		String[] type = null;
		for (int i = 0; i < chooseNo; i++) {
			s[i] = req.getParameter("C" + i);
			if (s[i] != null) {
				No++;
			}
		}
		type = new String[No];
		for (int i = 0, k = 0; i < chooseNo; i++) {
			if (s[i] != null) {
				type[k] = s[i];
				// pw.println(type[k]+"<br>");
				k++;
			}
		}
		ArrayList<?> allQues = Ques.getSomeQues(tr.getDbName(), "Type1", type);
		session.setAttribute("EditQues", allQues);
		int quesCount = 1;
		res.sendRedirect("EditQues?quesCount=" + quesCount);
	}
}