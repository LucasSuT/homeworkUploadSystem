package servlet;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.*;

import bean.DBcon;
import componment.Teacher;

/**
 * Servlet implementation class TestDataAjax
 */
public class TestDataAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestDataAjax() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("GET");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		DBcon dbCon = new DBcon();

		try {
			String m = request.getParameter("m");
			if (m.equals("addTestData")) {
				int questionID = Integer.parseInt(request.getParameter("questionID"));
				String input_data = request.getParameter("input_data");
				String true_result = request.getParameter("true_result");
				System.out.println(input_data);
				System.out.println(true_result);
				dbCon.insertTestData(tr.getCourse(), questionID, input_data, true_result);
			} else if (m.equals("deleteTestData")) {
				int testDataID = Integer.parseInt(request.getParameter("testDataID"));
				dbCon.deleteTestData(tr.getCourse(), testDataID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
