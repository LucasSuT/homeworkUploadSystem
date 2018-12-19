package tool;

import java.io.File;
import java.util.ArrayList;

import bean.DBcon;
import bean.TestData;

public class TestThread extends java.lang.Thread {
	String fileName = "";
	int questionID = 0;
	String student_id = "";
	String inputPath = "";
	String outputPath = "";
	String db_name = "";
	String clib_hook_path = "";
	String questionID_String="";
	String oldfileName ="";
	String packageName[]=new String [1];

	public TestThread(String iPath, String oPath, String fn, int id, String sid, String dbName) {
		this(iPath, oPath, fn, id, sid, dbName, "./","");
	}

	public TestThread(String iPath, String oPath, String fn, int id, String sid, String dbName, String cLibHookPath, String question_ID) {
		fileName = fn;
		questionID = id;
		student_id = sid;
		inputPath = iPath;
		outputPath = oPath;
		db_name = dbName;
		clib_hook_path = cLibHookPath;
		questionID_String=question_ID;
		if (!(new File(oPath)).isDirectory()) {
			(new File(oPath)).mkdir();
		}
	}
	public TestThread(String iPath, String oPath, String fn, int id, String sid, String dbName, String cLibHookPath, String question_ID, String ofn) {
		fileName = fn;
		questionID = id;
		student_id = sid;
		inputPath = iPath;
		outputPath = oPath;
		db_name = dbName;
		clib_hook_path = cLibHookPath;
		questionID_String=question_ID;
		oldfileName=ofn;
		if (!(new File(oPath)).isDirectory()) {
			(new File(oPath)).mkdir();
		}
		System.out.println("oldFileName="+oldfileName);
	}

	public void run() {
		try {
			ArrayList<String> error = new ArrayList<String>();
			C_Tool c_Tool = new C_Tool(inputPath, outputPath);
			DBcon dbCon = new DBcon();
			dbCon.clearCheckResult(db_name, questionID, student_id);
			String language=dbCon.getLanguage(db_name, questionID_String);
			if (c_Tool.checkSourceCodeContext(inputPath + fileName) == 1) {
				dbCon.insertCheckResult(db_name, questionID, student_id, -1, false, "請勿使用system函數");
			} else if (c_Tool.checkSourceCodeContext(inputPath + fileName) == 2) {
				dbCon.insertCheckResult(db_name, questionID, student_id, -1, false, "fopen第二個參數只可以是\"rb\"或\"r\"");
			} else if (!c_Tool.complierFile(fileName,language,oldfileName,error,packageName)) {
//				dbCon.insertCheckResult(db_name, questionID, student_id, -1, false, "編譯發生錯誤");
				dbCon.insertCheckResult(db_name, questionID, student_id, -1, false, error.get(0));
			} else {
				ArrayList<TestData> testDatas = dbCon.getTestDatas(db_name, questionID);
				for (int i = 0; i < testDatas.size(); i++) {
					try {
						if (c_Tool.checkResult(fileName, testDatas.get(i).getInput_data(),
								testDatas.get(i).getTrue_result(), clib_hook_path,oldfileName,packageName)) {
							dbCon.insertCheckResult(db_name, questionID, student_id, testDatas.get(i).getId(), true,
									"通過測試");
						} else {
							dbCon.insertCheckResult(db_name, questionID, student_id, testDatas.get(i).getId(), false,
									"測試失敗");
						}
					} catch (Exception e) {
						dbCon.insertCheckResult(db_name, questionID, student_id, testDatas.get(i).getId(), false,
								"執行超過時間(8秒)");
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
