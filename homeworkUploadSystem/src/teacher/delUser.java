package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class delUser extends HttpServlet {

	private static final long serialVersionUID = 7581427843259711390L;

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
		MyUtil.printTeacherHead(pw);
		pw.println("<form action=delUser method=post name=form1>");
		pw.println("<p><h2>Delete Usert</h2></p>");
		pw.println("<p>User Name:<input type=text name=id size=20></p>");
		pw.println("<input type=submit name=submit value=\"Ok\" onClick=\"return checkModify(form1.id.value);\">");
		pw.println("<input type=reset name=reset value=\" reset \">");
		pw.println("</form>");
		pw.println("</body></html>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		String id = req.getParameter("id");
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		if (Student.delUser(tr.getDbName(), id)) {
			pw.println("Delete <h3>" + id + "</h3>Success");
		} else {
			pw.println("<h3>  User " + id + " donnot exist ! </h3>");
		}
		pw.println("</body></html>");
		pw.close();
	}
}
