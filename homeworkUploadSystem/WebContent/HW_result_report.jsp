<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" import="componment.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="bean.*"%>
<%@ page contentType="text/html; charset=UTF-8" import="java.util.*"%>
<%
	Teacher tr = (Teacher) session.getAttribute("Teacher");
	Student st = (Student) session.getAttribute("Student");
	String dbName = "";
	if (st == null && tr == null) {
		return;
	}
	if (st != null)
		dbName = st.getDbName();
	else if (tr != null)
		dbName = tr.getDbName();
	DBcon dbCon = new DBcon();

	ArrayList<String> studentIDList = dbCon.selectStudentIDList(dbName);
	ArrayList<String> studentNAMEList = dbCon.selectStudentNameList(dbName);

	int HWid = -1;
	try {
		HWid = Integer.parseInt(request.getParameter("HW_ID"));
	} catch (Exception e) {
	}

	if (HWid == -1) {
		out.println("參數錯誤");
		return;
	}
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
		style="width: 70%; margin-right: auto; margin-left: auto; margin-top: 20px;">
		<!-- Default panel contents -->

		<!-- Table -->
		<table class="table" border="1">
			<tr>
				<td>學號</td>
				<!-- <td>平均</td> -->
			</tr>
			<%
				int testDataAmount = dbCon.getTestDatas(dbName, HWid).size();
				for (int i = 0; i < studentIDList.size(); i++) {
			%>
			<tr>
				<%
					ArrayList<TestResult> result = dbCon.getTestResult(dbName, HWid, studentIDList.get(i));
				%>
				<td><%=studentIDList.get(i)%></td>
				<td><%=studentNAMEList.get(i)%></td>
				<%
					for (int j = 0; j < testDataAmount; j++) {
				%>
				<%
					if (j >= result.size()) {
				%>
				<td style="background-color: #880000"><div>-</div></td>
				<%
					} else if (result.get(j).isResult()) {
				%>
				<td style="background-color: #008800"><div>通過測試</div></td>
				<%
					} else {
				%>
				<td style="background-color: #880000"><div><%=result.get(j).getDescription()%></div></td>
				<%
					}
				%>
				<%
					}
				%>
			</tr>
			<%
				}
			%>
		</table>
	</div>
</body>
</html>