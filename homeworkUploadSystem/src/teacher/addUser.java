package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class addUser extends HttpServlet {

	private static final long serialVersionUID = -8512082244882942176L;

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
		pw.println("<form action=addUser method=post name=form1>");
		// pw.println("<form name=Form1 enctype=\"multipart/form-data\"
		// method=post action=addUser>");
		pw.println("<p><h2>Add User</h2></p>");
		pw.println("<INPUT type=radio name=addType value=0 checked> 個人新增");
		pw.println("<p>User Name:<input type=text name=id size=20></p>");
		pw.println("<p>Passwd:<input type=text name=pass size=20></p>");
		pw.println("<p>realName:<input type=text name=realName size=20></p>");
		// pw.println("<INPUT type=radio name=addType value=1> 從文字檔" );
		// pw.println("上傳檔案: <input type=file name=nameFile size=20
		// maxlength=20><br><br>");
		pw.println("<input type=submit name=submit value=\"Ok\" >");
		// pw.println("<input type=submit name=submit value=\"Ok\"
		// onClick=\"return checkModify(form1.id.value);\">");
		pw.println("<input type=reset name=reset value=\"reset\">");
		pw.println("</form>");
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
		// String saveDirectory= req.getRealPath("/")+"/WEB-INF/temp/";
		// int maxPostSize=2*1024*1024;
		// File directoryFile = new File(saveDirectory);
		// if (!directoryFile.isDirectory()) {directoryFile.mkdir();}
		// MultipartRequest multi =new
		// MultipartRequest(req,saveDirectory,maxPostSize);

		String addType = req.getParameter("addType");
		String id = req.getParameter("id");
		String pass = req.getParameter("pass");
		String realName = req.getParameter("realName");
		// System.out.println(realName);
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		if ((id == null) || (pass == null) || (realName == null)) {
			pw.println("<h3>  Username null  </h3>");
		} else if ((id.length() <= 0) && (addType.equals("0"))) {
			pw.println("<h3>  Username empty  </h3>");
		} else if (addType.equals("0")) {
			if (Student.addUser(tr.getDbName(), id, realName, pass)) {
				pw.println("Add User <h3>" + id + "</h3> Success");
			} else {
				pw.println("<h3>  User " + id + " already exist ! </h3>");
			}
		}
		pw.println("</body></html>");
		pw.close();
	}
}
