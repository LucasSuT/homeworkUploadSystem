package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.*;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.*;

import bean.DBcon;
import bean.TestResult;
import componment.Teacher;

/**
 * Servlet implementation class QuestionAjax
 */
public class HomeworkScoreExcel extends HttpServlet {

	private static final long serialVersionUID = -574188880742093842L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		try {
			String dbName = tr.getDbName();
			DBcon dbCon = new DBcon();
			ArrayList<String> homeworktitleList = dbCon.selectHomeworkIDList(dbName);
			ArrayList<String> studentIDList = dbCon.selectStudentIDList(dbName);

			String tempDirectory = request.getRealPath("/") + "WEB-INF\\temp\\";
			String tempFile = (new Date().getTime()) + ".xls";
			FileOutputStream fileOut = new FileOutputStream(tempDirectory + tempFile); // 新建一輸出檔案流
			HSSFWorkbook workBook = new HSSFWorkbook(); // 在Excel中建一工作表
			HSSFSheet sheet = workBook.createSheet("輔導活動");
			int nowRow = 0;
			HSSFRow rowForTitle = sheet.createRow(nowRow++);
			int nowCell = 0;
			rowForTitle.createCell(nowCell++).setCellValue("學號");
			for (int i = 0; i < homeworktitleList.size(); i++) {
				rowForTitle.createCell(nowCell++).setCellValue(homeworktitleList.get(i));
			}
			rowForTitle.createCell(nowCell++).setCellValue("通過題數");

			for (int i = 0; i < studentIDList.size(); i++) {
				nowCell = 0;
				rowForTitle = sheet.createRow(nowRow++);
				rowForTitle.createCell(nowCell++).setCellValue(studentIDList.get(i));
				int amount = 0;
				for (int j = 0; j < homeworktitleList.size(); j++) {
					int question_id = Integer.parseInt(homeworktitleList.get(j));
					ArrayList<TestResult> result = dbCon.getTestResult(dbName, question_id, studentIDList.get(i));
					int count = 0;
					int testDataAmount = dbCon.getTestDatas(dbName, question_id).size();
					for (int k = 0; k < testDataAmount; k++) {
						if (k >= result.size()) {
							break;
						} else if (result.get(k).isResult()) {
							count++;
						}
					}
					String score = "0";
					if (testDataAmount == 0) {
						score = "-";
					} else if (count == testDataAmount) {
						score = "100";
						amount++;
					}
					rowForTitle.createCell(nowCell++).setCellValue(score);
				}
				rowForTitle.createCell(nowCell++).setCellValue(amount);
			}

			workBook.write(fileOut); // 把相應的Excel存檔
			fileOut.flush();
			fileOut.close(); // 操作結束，關閉檔
			workBook.close();
			downloadFile(response, tempDirectory + tempFile, "homeworkScore.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	// 讓網頁下載檔案 filePath=>檔案位置 downloadFileName=>下載時地的檔案名
	static void downloadFile(HttpServletResponse response, String filePath, String downloadFileName) throws Exception {
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		BufferedOutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/force-download");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFileName + "\"");
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	}
}
