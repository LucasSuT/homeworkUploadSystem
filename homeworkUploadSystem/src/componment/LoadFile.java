package componment;

import java.util.Scanner;
import java.io.*;

public class LoadFile {
	public static String[][] getData(String fileName) throws IOException {
		String[] temp = new String[200];
		String t = "";
		String[][] name;
		int k = 0;
		Scanner scan = new Scanner(new File(fileName));
		while (scan.hasNext()) {
			t = scan.nextLine().trim();
			if (Character.isDigit(t.charAt(1))) {
				temp[k] = t;
				k++;
			}
		}
		name = new String[k][2];
		for (int i = 0; i < k; i++) {
			name[i] = temp[i].split(" ", 3);
		}
		scan.close();
		return name;
	}

	public static String[][] getData(String fileName, String split, int num) {
		String[] temp = new String[200];
		String t = "";
		String[][] name = null;
		int k = 0;
		Scanner scan = null;
		try {
			scan = new Scanner(new File(fileName));
			while (scan.hasNext()) {
				t = scan.nextLine();
				temp[k] = t;
				k++;
			}
			name = new String[k][num];
			for (int i = 0; i < k; i++) {
				name[i] = temp[i].split(split, num + 1);
			}
		} catch (Exception e) {
			System.out.println("FAIL");
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return name;
	}
}
