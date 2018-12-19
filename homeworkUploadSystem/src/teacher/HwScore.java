package teacher;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.DBcon;
import bean.TestResult;
import componment.Homework;
import componment.MyUtil;
import componment.Student;
import componment.Teacher;

public class HwScore extends HttpServlet 
{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		HttpSession session = req.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		if (tr == null) 
		{
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		Student st = (Student) session.getAttribute("Student");
		String dbName = "";
		if (st != null)
			dbName = st.getDbName();
		else if (tr != null)
			dbName = tr.getDbName();
		DBcon dbCon = new DBcon();
		ArrayList<String> homeworktitleList = null;
		System.out.println(dbName);
		try 
		{
			homeworktitleList = dbCon.selectHomeworkIDList(dbName);
			System.out.println("hw個數"+homeworktitleList.size());
		}
		catch(Exception e) {}
		ArrayList<String> studentIDList=new ArrayList<String>();
		ArrayList<String> studentNameList=new ArrayList<String>();
		ArrayList<?> sts = Student.getAllStudent(tr.getDbName(),homeworktitleList.size());
		for(int i=0;i<sts.size();i++)
		{
			Student s=(Student)sts.get(i);
			studentIDList.add(s.getName());
			studentNameList.add(s.getRealName());
		}
		try {
			dbCon.getTestResult(dbName, sts,homeworktitleList.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int questionCount[]=new int [homeworktitleList.size()+1];
//		int questionCount[]=new int[1000];
		int questionCount[]=new int[999999];
		try {
			questionCount=dbCon.getTestDatas(dbName, questionCount,homeworktitleList.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for(int i=0;i<questionCount.length;i++)
//		{
//			System.out.println(questionCount[i]);
//		}
//		System.out.println(questionCount[1]+"  "+questionCount[39]);
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
				"<title>作業檢查結果</title>\r\n" + 
				"</head>\r\n" + 
				"<body TEXT=#FFFFFF BGCOLOR=#000000 link=#00FFFF vlink=#CCFF33\r\n" + 
				"	alink=#FFCCFF>\r\n" + 
				"	<div></div>\r\n" + 
				"	<div class=\"panel panel-default\"\r\n" + 
				"		style=\"width: 80%; margin: auto;\">\r\n" + 
				"		<!-- Default panel contents -->\r\n" + 
				"		<!-- Table -->\r\n" + 
				"		<a href=\"HomeworkScoreExcel\">下載Excel</a><br />\r\n" + 
				"		<table class=\"table\" border=\"1\">\r\n" + 
				"			<tr>\r\n" + 
				"				<td>學號</td>");
		pw.println("<td><font size=2>UseName</font></td>");
		for (int n = 0; n < homeworktitleList.size(); n++) 
		{
			pw.println("<td>"+homeworktitleList.get(n)+"</td>");
		}
		pw.println("<td>通過題數</td>\r\n" + 
				"			</tr>");
		System.out.println("student個數"+studentIDList.size());
		for (int i = 0; i < studentIDList.size(); i++) 
		{
			int amount1 = 0;
			pw.println("<tr>\r\n" + 
					"				<td>"+studentIDList.get(i)+"</td>"+
					"				<td>"+studentNameList.get(i)+"</td>");	
			for (int j = 0; j < homeworktitleList.size(); j++) 
			{
				Student s=(Student)sts.get(i);
				//System.out.println(s.getStudentPass(j+1));
//				if( s.getStudentPass(j+1)==questionCount[j+1] && questionCount[j+1]!=0)
				if( s.getStudentPass(Integer.parseInt(homeworktitleList.get(j)))==questionCount[Integer.parseInt(homeworktitleList.get(j))] && questionCount[Integer.parseInt(homeworktitleList.get(j))]!=0)
				{
					amount1++;
					pw.println("<td>100</td>");
				}
				else if(questionCount[Integer.parseInt(homeworktitleList.get(j))]==0)
				{
					pw.println("<td>-</td>");
				}
				else pw.println("<td>0</td>");
			}
			pw.println("<td>" + amount1 + "</td>\r\n" + 
					"			</tr>");
//			int amount1 = 0;
//			for (int n = 0; n < homeworktitleList.size(); n++) 
//			{
//				int question_id = Integer.parseInt(homeworktitleList.get(n));
//				ArrayList<TestResult> result = null;
//				try 
//				{
//					result = dbCon.getTestResult(dbName, question_id, studentIDList.get(i));
//				} 
//				catch (Exception e) 
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				int count = 0;
//				int testDataAmount = 0;
//				try 
//				{
//					testDataAmount = dbCon.getTestDatas(dbName, question_id).size();
//				} 
//				catch (Exception e) 
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				for (int j = 0; j < testDataAmount; j++) 
//				{
//					if (j >= result.size()) 
//					{
//						break;
//					} 
//					else if (result.get(j).isResult()) 
//					{
//						count++;
//					}
//				}
//				if (testDataAmount == 0) 
//				{
//					pw.println("<td>-</td>");
//				} 
//				else if (count == testDataAmount) 
//				{
//					pw.println("<td>100</td>");
//					amount1++;
//				} 
//				else
//					pw.println("<td>0</td>");
//			}
//			pw.println("<td>" + amount1 + "</td>\r\n" + 
//					"			</tr>");
		}
		pw.println("</table>\r\n" + 
				"	</div>\r\n" + 
				"</body>\r\n" + 
				"</html>");
	}
}

