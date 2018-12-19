package componment;

import java.io.*;

public class MyUtil {
	// 將Unicode碼的字串s轉成Big5碼並傳回
	public static String unicodeToBig5(String s) {
		try {
			return new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
			return s;
		}
	}

	// 將字串s以十六進位顯示
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = "0000" + Integer.toHexString(ch);
			str = str + s4.substring(s4.length() - 4) + " ";
		}
		return str;
	}

	public static void printTeacherHead(PrintWriter pw) {
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<title>Online Exam</title>");
		pw.println("<script language=javascript>");
		pw.println("function checkDel(url) {if (confirm('Sure, y/n ?')==1)   { location.href=url; } }");
		pw.println(
				"function checkString(s1,s2) {  if ((s1==\"\")||(s2==\"\")) { alert(\"請填完整\");  return false;  } }  ");
		pw.println(
				"function checkClearExam(s1,s2) {  if ((s2[0].checked)&&(s1.length==0)) { alert(\"請填完整\");  return false;  } ");
		pw.println("   if (confirm('Sure, y/n ?')==1)   {  return true;  }    return false;}  ");
		pw.println("function checkModify(f) {                ");
		pw.println("   if (f.length == 0) { alert(\"請填完整\");    return false;  }    ");
		pw.println("   if (confirm('Sure, y/n ?')==1)   {  return true;  }    ");
		pw.println("   return false;   }             ");
		pw.println("</script></head>");
		pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000  link=#00FFFF vlink=#CCFF33 alink=#FFCCFF > <font size=2>");
	}

	public static void printHtmlHead(PrintWriter pw) {
		pw.println("<!DOCTYPE html>");
		pw.println("<meta charset='UTF-8'>");
		pw.println("<title>Online Exam</title>");
		pw.println("<script language='JavaScript'>");
		pw.println("function checkDel(url) {if (confirm('Sure, y/n ?')==1)   { location.href=url; } }");
		pw.println(
				"function checkString(s1,s2) {  if ((s1==\"\")||(s2==\"\")) { alert(\"請填完整\");  return false;  } }  ");
		pw.println("function look(){if(event.altKey) alert(\"禁止按Alt鍵!\");} document.onkeydown=look; ");
		pw.println("function scroll(){window.status=\"hello\"; setTimeout(\"scroll()\",100);  }   ");
		pw.println("function checkModify(f) {                ");
		pw.println("   if (f.length == 0) { alert(\"請填完整\");    return false;  }    ");
		pw.println("   if (confirm('Sure, y/n ?')==1)   {  return true;  }    ");
		pw.println("   return false;   }             ");
		pw.println("</script></head>");
		// pw.println("<BODY onLoad=\"look(); scroll(); return true;\"
		// oncontextmenu=window.event.returnValue=false TEXT=#FFFFFF
		// BGCOLOR=#000000 link=#00FFFF vlink=#CCFF33 alink=#FFCCFF>");
		pw.println("<BODY TEXT=#FFFFFF BGCOLOR=#000000 link=#00FFFF vlink=#CCFF33 alink=#FFCCFF>");
	}
}
