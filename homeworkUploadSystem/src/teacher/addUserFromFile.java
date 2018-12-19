package teacher;

import componment.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import com.oreilly.servlet.MultipartRequest;

public class addUserFromFile extends HttpServlet {

	private static final long serialVersionUID = -7194830234593297898L;

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
		// pw.println("<form action=addUser method=post name=form1>");
		pw.println("<form name=Form1 enctype=\"multipart/form-data\" method=post action=addUserFromFile>");
		pw.println("<p><h2>Add User</h2></p>");
		// pw.println("<INPUT type=radio name=addType value=0 checked> 個人" );
		// pw.println("<p>User Name:<input type=text name=id size=20></p>");
		// pw.println("<p>Passwd:<input type=text name=pass size=20></p>");
		// pw.println("<p>realName:<input type=text name=realName
		// size=20></p>");
		pw.println("<INPUT type=radio name=addType value=1 checked> 從文字檔");
		pw.println("上傳檔案: <input type=file name=nameFile size=20 maxlength=20><br><br>");
		pw.println("<input type=submit name=submit value=\"Ok\" >");
		// pw.println("<input type=submit name=submit value=\"Ok\"
		// onClick=\"return checkModify(form1.id.value);\">");
		pw.println("<input type=reset name=reset value=\"reset\">");
		pw.println("</form>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("BIG5");
		req.setCharacterEncoding("BIG5");
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String saveDirectory = req.getRealPath("/") + "/WEB-INF/temp/";
		int maxPostSize = 2 * 1024 * 1024;
		File directoryFile = new File(saveDirectory);
		if (!directoryFile.isDirectory()) {
			directoryFile.mkdir();
		}
		MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize);
		multi.getParameter("addType");
		// String id=multi.getParameter("id");
		// String pass=multi.getParameter("pass");
		// String realName=multi.getParameter("realName");
		// System.out.println(realName);
		PrintWriter pw = res.getWriter();
		MyUtil.printTeacherHead(pw);
		String FileName = multi.getFilesystemName("nameFile");
		// String newFileName = FileName;
		// File newFile = new File(saveDirectory + FileName);
		// if ((FileName != null)&&(newFile.length()>0)) {
		// File abstractFile=new File(saveDirectory + newFileName);
		// boolean done = newFile.renameTo(abstractFile);
		// }
		String[][] temp = LoadFile.getData(saveDirectory + FileName);
		for (int i = 0; i < temp.length; i++) {
			if (Student.addUser(tr.getDbName(), temp[i][0], temp[i][1], "0")) {
				pw.println("Add User " + temp[i][0] + " Success");
			} else {
				pw.println("<h3>  Users " + temp[i][0] + " already exist ! </h3>");
			}
		}
		pw.println("Add User File Success: " + saveDirectory + FileName);
		pw.println("</body></html>");
		pw.close();
	}
}
