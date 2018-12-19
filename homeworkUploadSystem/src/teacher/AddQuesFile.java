package teacher;

import componment.*;
//�q�ɮ׼W�[�Ҹ��D��
import com.oreilly.servlet.MultipartRequest;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class AddQuesFile extends HttpServlet {

	private static final long serialVersionUID = 8684307388913579382L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		PrintWriter pw = res.getWriter();
		boolean success = true;
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String saveDirectory = tr.getCoursePath() + "\\";
		int maxPostSize = 2 * 1024 * 1024;
		File directoryFile = new File(saveDirectory);
		if (!directoryFile.isDirectory()) {
			directoryFile.mkdir();
		}
		MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize);
		String FileName = multi.getFilesystemName("nameFile");

		MyUtil.printTeacherHead(pw);
		String qid = "", type1 = "test", type2 = "test";
		Ques ques = null;
		String[][] temp = LoadFile.getData(saveDirectory + FileName, "~", 2);
		System.out.println("--" + saveDirectory + FileName);
		System.out.println(temp[0][0]);
		type1 = temp[0][0];
		type2 = temp[0][1];
		for (int i = 1; i < temp.length; i++) {
			qid = Ques.getNextId(tr.getDbName());
			// qid, type1, type2, content, ans
			ques = new Ques(qid, type1, type2, temp[i][1], temp[i][0]);
			if (ques.addQues(tr.getDbName())) {
				pw.println("OK");
			} else {
				pw.println("<h3>  Failure ! </h3>");
				success = false;
			}
		}
		pw.println("</body></html>");
		if (success) {
			res.sendRedirect("ExamBoard");
		}
		pw.close();
	}
}