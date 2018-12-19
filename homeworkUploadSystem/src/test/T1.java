package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class T1 {

	public static void main(String[] args) throws IOException {
		// 開起壓縮後輸出的檔案
		ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("E:/test.zip")));
		// 設定壓縮的程度0~9
		zOut.setLevel(0);

		/*
		 * 在壓縮檔內建立一個項目(表示一個壓縮的檔案或目錄，可以目錄結構的方式表示， 解壓縮後可以設定的目錄結構放置檔案)
		 */
		for (int i = 1; i <= 4; i++) {
			File f = new File("E:/test" + i + ".txt");
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
		zOut.close();
	}
}
