package teacher;

import componment.*;

//�W�[�@�~
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class AddHomework extends HttpServlet {

	private static final long serialVersionUID = 6026985782730539599L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		String type = req.getParameter("type");
		String hwId = req.getParameter("hwId");
		String content = req.getParameter("content");
		String deadline = req.getParameter("deadline");
		String weights = req.getParameter("weights");
		String language = req.getParameter("ltype");
		System.out.println(language);
		int index = 0;
		String srh = "\n";
		while ((index = content.indexOf(srh)) != -1) {
			content = content.substring(0, index) + " <BR> " + content.substring(index + srh.length());
		}
		Homework.addHomework(tr.getDbName(), hwId, type, content, deadline, weights,language);
		pw.println("OK");
		pw.println("</body></html>");
		res.sendRedirect("HomeworkBoard");
		pw.close();
	}
}