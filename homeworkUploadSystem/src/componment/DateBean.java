package componment;

import java.util.*;

public class DateBean {
	Calendar calend = null;

	public DateBean() {
		// 時間調整，電腦時間怪怪的！
		calend = Calendar.getInstance(new java.util.SimpleTimeZone(+28800000, "x"));
		// calend = Calendar.getInstance();
		// calend.setTime(new Date());
	}

	public String getYear() {
		String r = "" + calend.get(Calendar.YEAR);
		return r;
	}

	public int getYearI() {
		return calend.get(Calendar.YEAR);
	}

	public String getMonth() {
		String r = "";
		int m = 1 + calend.get(Calendar.MONTH);
		r = r + m;
		if (m < 10) {
			r = "0" + r;
		}
		return r;
	}

	public int getMonthI() {
		return 1 + calend.get(Calendar.MONTH);
	}

	public String getDay() {
		String r = "";
		int m = calend.get(Calendar.DAY_OF_MONTH);
		r = r + m;
		if (m < 10) {
			r = "0" + r;
		}
		return r;
	}

	public int getDayI() {
		return calend.get(Calendar.DAY_OF_MONTH);
	}

	public String getHour() {
		String r = "";
		int m = calend.get(Calendar.HOUR_OF_DAY);
		r = r + m;
		if (m < 10) {
			r = "0" + r;
		}
		return r;
	}

	public int getHourI() {
		return calend.get(Calendar.HOUR_OF_DAY);
	}

	public String getMinute() {
		String r = "";
		int m = calend.get(Calendar.MINUTE);
		r = r + m;
		if (m < 10) {
			r = "0" + r;
		}
		return r;
	}

	public int getMinuteI() {
		return calend.get(Calendar.MINUTE);
	}

	public String getSecond() {
		String r = "";
		int m = calend.get(Calendar.SECOND);
		r = r + m;
		if (m < 10) {
			r = "0" + r;
		}
		return r;
	}

	public String getDate() {
		return getYear() + "/" + getMonth() + "/" + getDay();
	}

	public String getTime() {
		return getHour() + ":" + getMinute() + ":" + getSecond();
	}

	public String getDateTime() {
		return getDate() + " " + getTime();
	}

	public static String addTime(String startTime, String maxExamTime) {
		String maxEndTime = "";
		int minute = 0, hour = 0, day = 0, month = 0, year = 0;
		String minuteStr = "", hourStr = "", dayStr = "", monthStr = "";
		minute = Integer.parseInt(startTime.substring(14, 16)) + Integer.parseInt(maxExamTime.substring(3, 5));
		if (minute >= 60) {
			hour = 1;
			minute = minute - 60;
		}
		if (minute < 10) {
			minuteStr = "0" + minute;
		} else {
			minuteStr = "" + minute;
		}
		hour = hour + Integer.parseInt(startTime.substring(11, 13)) + Integer.parseInt(maxExamTime.substring(0, 2));
		if (hour >= 24) {
			day = 1;
			hour = hour - 24;
		}
		if (hour < 10) {
			hourStr = "0" + hour;
		} else {
			hourStr = "" + hour;
		}
		day = day + Integer.parseInt(startTime.substring(8, 10));
		month = Integer.parseInt(startTime.substring(5, 7));
		year = year + Integer.parseInt(startTime.substring(0, 4));
		if (month == 2) {
			if (day > 28) {
				month = month + 1;
				day = day - 28;
			}
		} else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			if (day > 30) {
				month = month + 1;
				day = day - 30;
			}
		} else {
			if (day > 31) {
				month = month + 1;
				day = day - 31;
			}
		}
		if (month > 12) {
			month = month - 12;
			year = year + 1;
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}
		maxEndTime = year + "/" + monthStr + "/" + dayStr + " " + hourStr + ":" + minuteStr + ":00";
		return maxEndTime;
	}
}
