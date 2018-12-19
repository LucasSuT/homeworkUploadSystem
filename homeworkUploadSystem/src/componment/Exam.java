package componment;

import ExamDB.*;
import java.util.*;

public class Exam {
	private int totalQuesCount = 1;
	private int quesCount = 1;
	private String legalStartTime = "";
	private String legalEndTime = "";

	public Exam(String dbName) {
		// --從資料庫 QuesNum 取得所有題數 totalQuesNum, 本次考試題數 quesCount
		String sql = "SELECT * FROM QuesNum WHERE ID = '1';";
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		if (result.size() > 0) {
			String[] data = (String[]) result.get(0);
			totalQuesCount = Integer.parseInt(data[1]);
			quesCount = Integer.parseInt(data[2]);
			legalStartTime = data[3];
			legalEndTime = data[4];
		}
		sql = "SELECT * FROM Exam;";
		result = DbProxy.selectData(dbName, sql);
		if (result.size() > 0) {
			totalQuesCount = result.size();
		}
	}

	public int getTotalQuesCount() {
		return totalQuesCount;
	}

	public int getQuesCount() {
		return quesCount;
	}

	public String getLegalStartTime() {
		return legalStartTime;
	}

	public String getLegalEndTime() {
		return legalEndTime;
	}

	public static void setTime(String dbName, String[] timeString, String[] quesNum) {
		String sql = "UPDATE QuesNum SET startTime = ?, endTime = ?, AllQues = ?, quesNum = ? WHERE ID = '1';";
		String[] data = new String[4];
		data[0] = timeString[0];
		data[1] = timeString[1];
		data[2] = quesNum[0];
		data[3] = quesNum[1];
		DbProxy.setData(dbName, sql, data);
	}

	// --- 檢查是否在合法的考試時間 ---------------------------
	public boolean isLegalTime(String startTime) {
		if ((startTime.compareTo(legalStartTime) >= 0) && (startTime.compareTo(legalEndTime) <= 0)) {
			return true;
		}
		return false;
	}

	// --------- 亂數產生題號數 ------------------------------------
	public static String genQues(int allQuesNum, int quesNumber) {
		String result = "";
		boolean chcQus[] = new boolean[allQuesNum];
		Random Rnd = new Random(); // 建立產生亂數的Random物件
		int i = 1;
		while (i <= quesNumber) { // i 為計算已答題數的變數
			// 取得值為 1 至allQuesNum的亂數
			int chcNum = Rnd.nextInt(allQuesNum - 1) + 1;
			// 判斷代表某記錄的陣列元素值是否為true, 不是則代表該記錄尚未被選取
			if (!chcQus[chcNum - 1]) {
				chcQus[chcNum - 1] = true; // 設定代表該記錄的陣列元素之值為true
				if (chcNum > 99) {
					result = result + chcNum;
				} else if (chcNum > 9) {
					result = result + "0" + chcNum;
				} else {
					result = result + "00" + chcNum;
				}
				i++;
			}
		}
		return result;
	}
}