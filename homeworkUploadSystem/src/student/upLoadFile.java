package student;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;

import javax.servlet.http.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.servlet.*;
import com.oreilly.servlet.MultipartRequest;
import tool.*;
import componment.*;

public class upLoadFile extends HttpServlet {

	private static final long serialVersionUID = -7853523789509342250L;
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		 
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		int status = 0;
		TestThread testThread = null;
		HttpSession session = req.getSession(true);
		Student st = (Student) session.getAttribute("Student");
		if (st == null) {
			res.sendRedirect("http://www.cc.ntut.edu.tw/~jykuo/");
			return;
		}
		String hwId = (String) session.getAttribute("hwId");
		if (UserHomework.isExistUserOneHomework(st.getDbName(), st.getName(), hwId)) {
			res.setCharacterEncoding("UTF-8");
			req.setCharacterEncoding("UTF-8");
			PrintWriter pw = res.getWriter();
			MyUtil.printHtmlHead(pw);
			pw.println("<!DOCTYPE html>");
			pw.println("<meta charset='UTF-8'>");
			pw.println("<h2>作業已繳交過" + hwId + "</h2></center>");
			pw.println("<h2>若要重新繳交，請<a href=#  OnMouseDown=\"checkDel('delHw?title=" + hwId
					+ "')\">刪除</a>作業，再重新上傳！</h2></center>");
			pw.println("</center></body></html>");
			pw.close();
			return ;
		}
		String name = st.getName();
		String FileDescription = "";
		String saveDirectory = req.getServletContext().getRealPath("./") + "/WEB-INF/uploadHW/" + hwId + "/";
		String cLibHookPath = req.getServletContext().getRealPath("./") + "/WEB-INF/hook/";
		System.out.println("hwId="+hwId+"  studentId="+name+"  "+saveDirectory+"  "+cLibHookPath);

		int maxPostSize = 2 * 1024 * 1024;
		res.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		MyUtil.printHtmlHead(pw);
		// ------- 從 input type = file 讀取檔案，上傳到 server's saveDirectory
		// ---------------
		if (hwId != null) {
			File directoryFile = new File(saveDirectory);
			if (!directoryFile.isDirectory()) {
				directoryFile.mkdirs();
			}
			MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize);
			if (multi.getParameter("FileDesc").length() > 0) {
				FileDescription = multi.getParameter("FileDesc");
			} else {
				FileDescription = "無描述";
			}
			// pw.println(hwId+"-hh- " + saveDirectory);
			// Enumeration filesname = multi.getFileNames(); // 取得 input type =
			// file 的參數name
			// String fname = (String)filesname.nextElement(); // 此為 hwFile
			// (upLoadHw.java)
			// File f = multi.getFile(fname);
			// String FileName = multi.getFilesystemName(fname); //取得input
			// type=file的 filename
			String fname = "hwFile";
			String FileName = multi.getFilesystemName(fname);
			String ContentType = multi.getContentType(fname);
			String newFileName = name + "_" + hwId + "_" + FileName.replaceAll("\\s+", "");
			File newFile = new File(saveDirectory + FileName);
			DateBean dateBean = new DateBean();
			String dateTime = dateBean.getDateTime();
			if ((FileName != null) && (newFile.length() > 0)) {
				File abstractFile = new File(saveDirectory + newFileName);
				boolean done = newFile.renameTo(abstractFile);
				// ------更名失敗，再更名
				int i = 2;
				while (!done) {
					newFileName = name + "_" + hwId + "_0" + i + "_" + FileName.replaceAll("\\s+", "");
					abstractFile = new File(saveDirectory + newFileName);
					done = newFile.renameTo(abstractFile);
					i++;
				}
				// 將使用者id,檔名,檔案描述,上傳日期,上傳路徑等..存到資料庫中
				String saveURLDir = "<A HREF=http://140.124.3.101:8082/Java/" + hwId + "/" + newFileName + ">look</A>";
//				System.out.println("\n\n"+name+" "+dateTime+" "+hwId+" "+FileDescription+"\n"+FileName+" "+newFileName+" "+saveURLDir);
				UserHomework uhw = new UserHomework(name, dateTime, hwId, FileDescription, FileName, newFileName,
						saveURLDir);
				if (uhw.writeDB(st.getDbName())) {
					pw.println("<font color=red>" + name + "所上傳" + hwId + "檔案:</font><br>");
					pw.println("檔案名稱: " + FileName + "<br> 檔案型態: " + ContentType + "<br>");
					pw.println("檔案敘述: " + FileDescription + "<br><br>");

					// 執行測試(執行緒)
					String tempDir = req.getServletContext().getRealPath("./") + "/WEB-INF/temp/";					
					
					if (!new File(tempDir).isDirectory())
						(new File(tempDir)).mkdir();
					//tempDir = "D:/C_complie/output/";
//					TestThread testThread = new TestThread(
//						saveDirectory,
//						tempDir + hwId + "/",
//						newFileName,
//						Integer.parseInt(hwId),
//						name,
//						st.getDbName(),
//						cLibHookPath,
//						hwId
//					);
					testThread = new TestThread(
							saveDirectory,
							tempDir + hwId + "/",
							newFileName,
							Integer.parseInt(hwId),
							name,
							st.getDbName(),
							cLibHookPath,
							hwId,
							FileName
						);
					testThread.start();
					
//					URL u = new URL("http://localhost:8090/copyTest/test2");
//					HttpURLConnection c = (HttpURLConnection) u.openConnection();
//				    c.setRequestMethod("POST");
//				    c.connect();
//				    int status2 = c.getResponseCode();
//				    System.out.println(status2);
//				    String inputLine;
//				    try(InputStreamReader stream = new InputStreamReader(c.getInputStream());
//				            BufferedReader in = new BufferedReader(stream)) {
//				            while ((inputLine = in.readLine()) != null) {
//				                System.out.println(inputLine);
//				            }
//				        }
//				    System.out.println(req.getAttribute("yourname"));
				    
					/*  開始進行抄襲檢查  */
					
					try {
						System.out.println("dbNam="+st.getDbName());
						Homework hw = Homework.getHomework(st.getDbName(), hwId);
						int weights = hw.getWeights();						
//						
//						//壓縮.c作業成zip檔
						String zipFile = servlet.ExportStudentHW.export(req, res, 1, st.getDbName(), hwId);
//						System.out.println("zipDirectory= "+zipFile);
						
//						本機測試端
//						URL oracle = new URL("http://localhost:8090/copyTest/HttpDetect");
						//server端
						URL oracle = new URL("http://localhost:8080/copyTest/HttpDetect");
						
//						URL oracle = new URL("https://140.124.184.228/copyTest/HttpDetect");
//						trustAllHosts();
//						HttpsURLConnection connection = (HttpsURLConnection)oracle.openConnection();
//						connection.setHostnameVerifier(DO_NOT_VERIFY);
						
//						System.out.println(oracle.getFile());
						
						HttpURLConnection connection = null;
						connection = (HttpURLConnection) oracle.openConnection();
						connection.setRequestMethod("POST");
						connection.setDoOutput(true);
						connection.setDoInput(true);
						
						String parameters = "zipFile="+zipFile+"&weights="+weights;
						DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
						wr.writeBytes(parameters);
						wr.flush();
						wr.close();
						
						status = connection.getResponseCode();
						if(status == 200) {
							System.out.println("Response = "+status);
							pw.println("通過 " + status);
						}else if(status == 303){							
							System.out.println("Response = "+status);
							pw.println("抄襲 " + status);
						}else {
							System.out.println("Response = "+status);
							pw.println("系統發生例外 " + status);
						}
						File path = new File(zipFile); //刪除zip檔
						path.delete(); //刪除zip檔e
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					/*  若不通過則刪掉上傳的檔案  */
				} else {
					pw.println("您沒有上傳檔案 請重新操作");
				}
			}
		} else {
			pw.println("您沒有上傳檔案 請重新操作");
		}
		if(status==303)pw.println("<script>alert('作業抄襲，已記錄並刪除作業')</script>");
		pw.println("</center></body></html>");
		pw.close();
		if(status == 303)
		{
			try {
				testThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UserHomework.delUserOneHomework(st.getDbName(), st.getName(), hwId);
			System.out.println("---------------delete homework because cheating-------------");
		}
	}

	private static void trustAllHosts() {
		 
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	 
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}
		} };
	 
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}