package componment;

import ExamDB.*;
import java.util.*;

public class Evaluator {
	protected String dbName;
	protected String id;
	private String[][] sender;
	private int[] scoreR;
	private String[] commentR;
	private String[] scoreS;
	private String[] commentS;
	private static boolean initEval = false;

	public Evaluator(String d, String i) {
		dbName = d;
		id = i;
		getDBSender(d);
		getDBScoreR(d);
		getDBScoreS(d);
	}

	public boolean getDBSender(String dbName) {
		String sql = "SELECT id, name FROM Login WHERE id<>'0' ORDER BY id;";
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		sender = new String[result.size()][result.size()];
		scoreR = new int[result.size()];
		scoreS = new String[result.size()];
		commentR = new String[result.size()];
		commentS = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			sender[i] = (String[]) result.get(i);
		}
		return true;
	}

	public boolean getDBScoreR(String dbName) {
		String sql = "SELECT * FROM Evaluation WHERE idr= ? ORDER BY ids;";
		String[] param = new String[1];
		param[0] = id;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		String[] data = null;
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			scoreR[i] = Integer.parseInt(data[2]);
			commentR[i] = data[3];
		}
		return true;
	}

	public boolean getDBScoreS(String dbName) {
		String sql = "SELECT * FROM Evaluation WHERE ids = ? ORDER BY idr;";
		String[] param = new String[1];
		param[0] = id;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
		String[] data = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			scoreS[i] = data[2];
			if (data[3] == null)
				data[3] = "N";
			else if (data[3].length() < 1)
				data[3] = "N";
			commentS[i] = data[3];
		}
		return true;
	}

	public String[][] getSender() {
		return sender;
	}

	public int getScoreR() {
		int average = 0, count = 0;
		for (int i = 0; i < scoreR.length; i++) {
			if (scoreR[i] > 0) {
				count++;
				average = average + scoreR[i];
			}
		}
		if (count > 0) {
			average = average / count;
		}
		return average;
	}

	public String getCommentR() {
		String result = "";
		for (int i = 0; i < commentR.length; i++) {
			if (commentR[i] == null)
				commentR[i] = "N";
			result = result + commentR[i] + ", \n";
		}
		return result;
	}

	public String[] getCommentS() {
		return commentS;
	}

	public int[] getAllScoreR() {
		return scoreR;
	}

	public String[] getScoreS() {
		return scoreS;
	}

	public int getScoreSize() {
		return scoreR.length;
	}

	public boolean setScoreS(String[] s, String[] c) {
		boolean result;
		for (int i = 0; i < s.length; i++) {
			scoreS[i] = s[i];
			commentS[i] = c[i];
			if (sender[i][0].equals(id)) {
				scoreS[i] = "0";
				commentS[i] = "Nothing";
			}
		}
		result = setDBScoreS();
		return result;
	}

	public boolean setDBScoreS() {
		boolean ok = true;
		String sql, result;
		for (int i = 0; i < scoreS.length; i++) {
			sql = "UPDATE Evaluation SET score = ? ,comment = ? WHERE ids = ? AND idr = ?;";
			String[] data = new String[4];
			data[0] = scoreS[i];
			data[1] = commentS[i];
			data[2] = id;
			data[3] = sender[i][0];
			result = DbProxy.setData(dbName, sql, data);
			if (!result.equals("OK")) {
				ok = false;
			}
		}
		return ok;
	}

	public static boolean initEvaluation(String dbName) {
		String[] data = new String[4];
		String sql = "SELECT id, name FROM Login WHERE id<>'0' ORDER BY id;";
		if (initEval)
			return true;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		String[][] sender1 = new String[result.size()][result.size()];
		for (int i = 0; i < result.size(); i++) {
			sender1[i] = (String[]) result.get(i);
		}
		sql = "insert into Evaluation values (?, ?, ?, ?);";
		for (int i = 0; i < sender1.length; i++) {
			for (int j = 0; j < sender1.length; j++) {
				data[0] = sender1[i][0];
				data[1] = sender1[j][0];
				data[2] = "0";
				data[3] = "Nothing";
				DbProxy.addData(dbName, sql, data);
			}
		}
		initEval = true;
		return true;
	}

	public static boolean addEvaluation(String dbName, String sid) {
		String[] data = new String[4];
		String[] param = new String[2];
		String sql = "SELECT id, name FROM Login WHERE id<>'0' ORDER BY id;";
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		String[][] sender1 = new String[result.size()][result.size()];
		for (int i = 0; i < result.size(); i++) {
			sender1[i] = (String[]) result.get(i);
		}
		sql = "insert into Evaluation values (?, ?, ?, ?);";
		data[0] = sid;
		for (int i = 0; i < sender1.length; i++) {
			data[1] = sender1[i][0];
			data[2] = "0";
			data[3] = "Nothing";
			DbProxy.addData(dbName, sql, data);
		}
		data[1] = sid;
		for (int i = 0; i < sender1.length; i++) {
			data[0] = sender1[i][0];
			data[2] = "0";
			data[3] = "Nothing";
			sql = "SELECT idr, ids FROM Evaluation WHERE idr = ? AND ids = ?;";
			param[0] = data[0];
			param[1] = data[1];
			result = DbProxy.selectData(dbName, sql, param);
			if (result.size() <= 0) {
				sql = "insert into Evaluation values (?, ?, ?, ?);";
				DbProxy.addData(dbName, sql, data);
			}
		}
		return true;
	}

}