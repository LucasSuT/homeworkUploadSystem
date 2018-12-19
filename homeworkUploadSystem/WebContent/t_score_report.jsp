<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="bean.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="componment.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>
<%
	Teacher tr = (Teacher) session.getAttribute("Teacher");
	Student st = (Student) session.getAttribute("Student");
	String dbName = "";
	if (st != null)
		dbName = st.getDbName();
	else if (tr != null)
		dbName = tr.getDbName();
	DBcon dbCon = new DBcon();

	ArrayList<String> homeworktitleList = dbCon.selectHomeworkIDList(dbName);
	ArrayList<String> studentIDList = dbCon.selectStudentIDList(dbName);
	System.out.println(homeworktitleList.size());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>作業檢查結果</title>
</head>
<body TEXT=#FFFFFF BGCOLOR=#000000 link=#00FFFF vlink=#CCFF33
	alink=#FFCCFF>
	<div></div>
	<div class="panel panel-default"
		style="width: 70%; margin: auto;">
		<!-- Default panel contents -->
		<!-- Table -->
		<a href="HomeworkScoreExcel">下載Excel</a><br />
		<table class="table" border="1">
			<tr>
				<td>學號</td>
				<%
					for (int n = 0; n < homeworktitleList.size(); n++) {
				%>
				<td><%=homeworktitleList.get(n)%></td>
				<%
					}
				%>
				<!-- <td>平均</td> -->
				<td>通過題數</td>
			</tr>
			<%
				for (int i = 0; i < studentIDList.size(); i++) {
			%>
			<tr>
				<td><%=studentIDList.get(i)%></td>

				<%
					int amount1 = 0;
						for (int n = 0; n < homeworktitleList.size(); n++) {
							int question_id = Integer.parseInt(homeworktitleList.get(n));
							ArrayList<TestResult> result = dbCon.getTestResult(dbName, question_id, studentIDList.get(i));
							int count = 0;
							int testDataAmount = dbCon.getTestDatas(dbName, question_id).size();
							for (int j = 0; j < testDataAmount; j++) {
								if (j >= result.size()) {
									break;
								} else if (result.get(j).isResult()) {
									count++;
								}
							}
							if (testDataAmount == 0) {
								out.print("<td>-</td>");
							} else if (count == testDataAmount) {
								out.print("<td>100</td>");
								amount1++;
							} else
								out.print("<td>0</td>");
						}
				%>
				<td><%=amount1%></td>
			</tr>
			<%
				}
			%>
		</table>
	</div>
</body>
</html>