package componment;

import java.util.*;
import ExamDB.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Homework {
	private String type;
	private String id;
	private String content;
	private String deadline; // 2006/01/01 23:00
	private boolean expired;
	private int weights;
	private String language;

	public Homework(String t, String i, String c, String d, boolean e, int w) {
		type = t;
		id = i;
		content = c;
		deadline = d;
		expired = e;
		setWeights(w);
	}
	
	public Homework(String t, String i, String c, String d, boolean e) {
		type = t;
		id = i;
		content = c;
		deadline = d;
		expired = e;
	}
	public Homework(String t, String i, String c, String d, boolean e, String l) {
		type = t;
		id = i;
		content = c;
		deadline = d;
		expired = e;
		language = l;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getNoBRContent() {
		String temp = content.replaceAll("<BR>", "");
		return temp;
	}

	public String getDeadline() {
		return deadline;
	}

	public boolean isExpired() {
		return expired;
	}

	public String getlanguage() {
		return language;
	}
	public static Homework getHomework(String dbName, String hwId) {
		Homework hw = null;
		String c = "", d = "", t = "", w = "";
		boolean _expired = false;
		String sql = "SELECT * FROM Message WHERE type = 'hw' and id= ?;";
		String[] data = null;
		String[] param = new String[1];
		param[0] = hwId;
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
//		System.out.println("pass  "+result.size());
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			_expired = false;
			t = data[2];
			d = data[3];
			c = data[4];
			w = data[5];
			if(w==null)w="0";
			if (d == null) {
				d = "無";
			} else if (d.length() >= 16) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					Date deadLine;
					deadLine = sdf.parse(d);
					if (new Date().getTime() > deadLine.getTime()) {
						_expired = true;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			hw = new Homework(t, hwId, c, d, _expired, Integer.parseInt(w));
//			hw = new Homework(t, hwId, c, d, _expired);
		}
//		System.out.println("pass2");
		return hw;
	}

	public static void addHomework(String dbName, String id, String type, String content, String deadline, String weights, String language) {
		String sql = "insert into Message values (?, ?, ?, ?, ?, ?, ?);";
		String[] data = new String[7];
		if (deadline.length() < 16) {
			deadline = "200001011200";
		}
		data[0] = "hw";
		data[1] = id;
		data[2] = type;
		data[3] = deadline;
		data[4] = content;
		data[5] = weights;
		data[6] = language;
		DbProxy.addData(dbName, sql, data);
	}

	public boolean setHomework(String dbName) {
		String sql = "UPDATE Message SET content = ?, memo = ?, weights = ? WHERE type ='hw' and id = ?;";
		String[] data = new String[4];
		data[0] = content;
		data[1] = deadline;
		data[2] = String.valueOf(weights);
		data[3] = id;
		
		String result = DbProxy.setData(dbName, sql, data);
		if (result.equals("OK")) {
			return true;
		}
		return false;
	}

	public static boolean delHomework(String dbName, String id) {
		String sql = "delete from Message where type = 'hw' and id= ?;";
		DbProxy.delData(dbName, sql, id);
		return true;
	}

	public static ArrayList<Homework> getAllHomeworks(String dbName, String hwType) {
		ArrayList<Homework> hws = new ArrayList<Homework>();
		Homework hwTemp;
		String t = "", id = "", c = "", d = "",l="";
		boolean _expired = false;
		String sql = "SELECT * FROM message WHERE type='hw' and title= ? ORDER BY id;";
//		String sql = "SELECT * FROM message WHERE type='hw' and memo='2017/09/18 23:00' ORDER BY id;";
//		String sql = "SELECT * FROM Message WHERE type='hw';";
		String[] data = null;
		String[] param = new String[1];
		param[0] = hwType;
//		System.out.println(param[0].getBytes().length);
//		System.out.println(dbName+"  "+sql+"  "+param[0]);
		ArrayList<?> result = DbProxy.selectData(dbName, sql, param);
//		ArrayList<?> result = DbProxy.selectData(dbName, sql);
//		System.out.println(result.size());
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			_expired = false;
			id = data[1];
			t = data[2];
			d = data[3];
			c = data[4];
			l = data[6];
			if (d == null) {
				d = "無";
			} else if (d.length() >= 16) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					Date deadLine;
					deadLine = sdf.parse(d);
					if (new Date().getTime() > deadLine.getTime()) {
						_expired = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			hwTemp = new Homework(t, id, c, d, _expired,l);
			hws.add(hwTemp);
		}
		return hws;
	}

	public static ArrayList<Homework> getAllHomeworks(String dbName) {
		ArrayList<Homework> hws = new ArrayList<Homework>();
		Homework hwTemp;
		String t = "", id = "", c = "", d = "";
		boolean _expired = false;
		String sql = "SELECT * FROM Message WHERE type='hw' ORDER BY id;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			_expired = false;
			id = data[1];
			t = data[2];
			d = data[3];
			c = data[4];
			if (d == null) {
				d = "無";
			} else if (d.length() >= 16) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					Date deadLine;
					deadLine = sdf.parse(d);
					if (new Date().getTime() > deadLine.getTime()) {
						_expired = true;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			hwTemp = new Homework(t, id, c, d, _expired);
			hws.add(hwTemp);
		}
		return hws;
	}

	public static void hwStatistic(String dbName, ArrayList<?> sts, ArrayList<String> hws, boolean[][] checkList) {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				checkList[i][j] = false;
			}
		}
		String temp = "", hwId = "", nameId = "";
		Student st = null;
		int x = 0, y = 0;
		String sql = "SELECT * FROM message WHERE type='hw' ORDER BY id;";
		String[] data = null;
		ArrayList<?> result = DbProxy.selectData(dbName, sql);
		for (int i = 0; i < result.size(); i++) {
			data = (String[]) result.get(i);
			temp = data[1];
			hws.add(temp);	//hws = c.message all hw_id
		}
		sql = "SELECT * FROM Homework ORDER BY id;";
		result = DbProxy.selectData(dbName, sql);//c.homwork all column
		for (int k = 0; k < result.size(); k++) {
			data = (String[]) result.get(k);
			nameId = data[0];
			hwId = data[2];
			for (int i = 0; i < sts.size(); i++) {
				st = (Student) sts.get(i);
				if (st.getName().equals(nameId)) {
					x = i;
					break;
				}
			}
			//				c.message all hw_id(all homework from teacher)
			for (int i = 0; i < hws.size(); i++) { //match all c.message homework_id to c.homework homework_title所有已出作業題號和所有已交作業題號比對
				temp = (String) hws.get(i);
				if (temp.equals(hwId)) {
				// i=c.homwork which column
					y = i;
					break;
				}
			}
			if (x >= 100)
				x = 0;
			if (y >= 100)
				y = 0;
			checkList[x][y] = true;  //[第幾個student][第幾個作業]
		} // end for
	}

	public int getWeights() {
		return weights;
	}

	public void setWeights(int weights) {
		this.weights = weights;
	}
}