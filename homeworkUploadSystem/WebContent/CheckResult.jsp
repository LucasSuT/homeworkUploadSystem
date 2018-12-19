<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="bean.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="componment.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>
<%
	Teacher tr = (Teacher) session.getAttribute("Teacher");
	Student st = (Student) session.getAttribute("Student");
	String dbName = "";
	ArrayList<TestResult> result = new ArrayList<TestResult>();
	if (st == null && tr == null) {
		return;
	} else {
		if (st != null)
			dbName = st.getDbName();
		else if (tr != null)
			dbName = tr.getDbName();

		DBcon dbCon = new DBcon();

		int questionID = -1;
		String studentID = "";
		try {
			questionID = Integer.parseInt(request.getParameter("questionID"));
			studentID = request.getParameter("studentID").toString();
		} catch (Exception e) {
		}

		if (questionID == -1)
			return;

		result = dbCon.getTestResult(dbName, questionID, studentID);
	}
%>
<html>
<head>
<meta content="text/html; charset=Big5" />
<title>TestResult</title>
</head>
<BODY TEXT=#FFFFFF BGCOLOR=#000000 link=#00FFFF vlink=#CCFF33
	alink=#FFCCFF>
	<table class="table" border="1">
		<tr>
			<td>測試編號</td>
			<td>測試結果</td>
		</tr>
		<%
			int true_count = 0;
			for (int i = 0; i < result.size(); i++) {
				if (result.get(i).isResult())
					true_count++;
		%>
		<tr>
			<td><%=result.get(i).getTest_data_id()%></td>
			<%
				if (result.get(i).isResult()) {
			%>
			<td style="background-color: #008800"><div><%=result.get(i).getDescription()%></div></td>
			<%
				} else {
			%>
			<td style="background-color: #880000"><div><%=result.get(i).getDescription()%></div></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
	<%
		if (result.size() != 0) {
			int percent = 100 * true_count / result.size();
	%>
	測試通過率:
	<div class="progress">
		<div class="progress-bar progress-bar-striped active"
			role="progressbar" aria-valuenow="<%=percent%>" aria-valuemin="0"
			aria-valuemax="100" style="width: <%=percent%>%;">
			<%=percent%>%
		</div>
		<%
			} else {
		%>
		<div class="alert alert-danger" role="alert">未經過任何測試</div>
		<%
			}
		%>
	</div>
</body>
</html>