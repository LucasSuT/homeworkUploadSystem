package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.*;

import ExamDB.DbProxy;

import javax.servlet.*;

import componment.Teacher;

/**
 * Servlet implementation class QuestionAjax
 */
public class ExportStudentHW extends HttpServlet {

	private static final long serialVersionUID = -919719974720732937L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExportStudentHW() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Teacher tr = (Teacher) session.getAttribute("Teacher");
		try {
			export(request, response, 0, tr.getDbName(), "000");
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

	public static ArrayList<String> selectStudentSystemHomeworkNameList(String dbName, String title, int check)
			throws Exception {

		// check == 1 表示學生上傳作業要進行抄襲偵測
		// check == 0 表示老師下載作業(刪去有抄襲所以沒上傳成功的學生的作業)
		// check == 2 表示老師下載作業(所有上傳成功的學生的作業)

		Connection con = DbProxy.getConnection(dbName);
		String sql = "select systemname from homework where title = ?;";
		ArrayList<String> howeworkTitleList = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				howeworkTitleList.add(rs.getString("systemname"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			con.close();
		}
		return howeworkTitleList;
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
		System.out.println(filePath);
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
	}

	public static String export(HttpServletRequest request, HttpServletResponse response, int check, String DbName,
			String hwId) throws Exception {
		// check == 1 表示學生上傳作業要進行抄襲偵測
		// check == 0 表示老師下載作業(刪去有抄襲所以沒上傳成功的學生的作業)
		// check == 2 表示老師下載作業(所有上傳成功的學生的作業)
		String result = "";
		try {
			String title = "000";
			if (check == 1) {
				title = hwId;
			} else {
				title = request.getParameter("title");
			}
			String tempDirectory = request.getRealPath("/") + "WEB-INF/temp/";
			String zipName = (new Date().getTime()) + ".zip";
			if (!new File(tempDirectory).isDirectory())
				new File(tempDirectory).mkdirs();
			String saveDirectory = request.getRealPath("/") + "WEB-INF/uploadHW/" + title + "/";
//			System.out.println("\n\n!!!!"+tempDirectory+"  "+zipName+"  "+saveDirectory);
			ArrayList<String> nameList = selectStudentSystemHomeworkNameList(DbName, title, check);
			if (nameList.size() == 0) {
				response.setCharacterEncoding("BIG5");
				PrintWriter pw = response.getWriter();
				pw.println("尚未有人上傳作業。");
			}

			// 開起壓縮後輸出的檔案
			ZipOutputStream zOut = new ZipOutputStream(
					new BufferedOutputStream(new FileOutputStream(tempDirectory + zipName)));
			// 設定壓縮的程度0~9
			zOut.setLevel(0);

			// 在壓縮檔內建立一個項目(表示一個壓縮的檔案或目錄，可以目錄結構的方式表示，
			// 解壓縮後可以設定的目錄結構放置檔案)
			for (int i = 0; i < nameList.size(); i++) {
				File f = new File(saveDirectory + nameList.get(i));
				if (f.exists()) {
					FileInputStream fis = new FileInputStream(f);
					zOut.putNextEntry(new ZipEntry(f.getName()));

					// 以byte的方式讀取檔案並寫入壓縮檔
					int byteNo;
					byte[] b = new byte[64];
					while ((byteNo = fis.read(b)) > 0) {
						zOut.write(b, 0, byteNo);// 將檔案寫入壓縮檔
					}
					fis.close();
				}
			}
			zOut.close();
			if (check == 0) {
				downloadFile(response, tempDirectory + zipName, title + ".zip");
			}
			if (new File(tempDirectory + zipName).exists())
				result = tempDirectory + zipName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
