package tool;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeoutException;

public class C_Tool {
	// String gccPath = "C:/MinGW/bin/";
	String gccPath = "/usr/bin/";
	String inputPath = "";
	String outputPath = "";
	String[] hook_list = {
		"system.so",
		"fopen.so"
	};

	public C_Tool(String ipath, String opath) {
		inputPath = ipath;
		outputPath = opath;
	}

	public boolean complierFile(String fileName,String language,String oldfileName,ArrayList<String> error,String packageName[]) {
		Runtime rt = Runtime.getRuntime();
		Process proc = null;
		try {
			System.getProperty("os.name");
			String[] cmd = new String[3];
			// cmd[0] = "cmd.exe";
			// cmd[1] = "/C";
			// cmd[2] = gccPath+"gcc.exe \""+inputPath+fileName+"\" -o
			// \""+outputPath+fileName+".exe\"";
			// cmd[2] = gccPath + "gcc -o \"" + inputPath + fileName + "\" \"" +
			// outputPath + fileName + ".c\"";
			String[] outputFile = fileName.split("\\.");
			System.out.println("language="+language);
			if(language.equals("C"))
			{
				cmd[0] = "/bin/sh";
				cmd[1] = "-c";
				cmd[2] = "gcc " + inputPath + fileName + " -lm -o " + outputPath + outputFile[0];
						
				System.out.println("\nExecing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
				
				proc = rt.exec(cmd);
				
//				proc=rt.exec("gcc -o /home/lucas/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/homwrokUploadSystem//WEB-INF/temp/040/0_040_main /home/lucas/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/homwrokUploadSystem//WEB-INF/uploadHW/040/0_040_main.c");
			}
			else if(language.equals("Python"))
			{
				cmd[0] = "/bin/sh";
				cmd[1] = "-c";
				cmd[2] = "pyinstaller -F " + inputPath + fileName+" --distpath "+outputPath+" --specpath "+outputPath;
				String s="/home/islab/.local/bin/pyinstaller -F " + inputPath + fileName+" --distpath "+outputPath+" --specpath "+outputPath;
				System.out.println("\nExecing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
//				System.out.println(s);
				proc = rt.exec(cmd);
//				proc = rt.exec(s);
//				System.out.println("\npyinstaller -F "+inputPath+fileName);
//				proc=rt.exec("pyinstaller -F /home/lucas/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/homwrokUploadSystem//WEB-INF/uploadHW/040/0_040_hello.py");
			}
			else if(language.equals("Java"))
			{
				File newFile = new File(inputPath + fileName);
				File abstractFile = new File(inputPath + oldfileName);
				boolean done = newFile.renameTo(abstractFile);
				System.out.println(done);
				
				cmd[0] = "/bin/sh";
				cmd[1] = "-c";
				cmd[2] = "javac -d " + outputPath+ " "+inputPath + oldfileName;
				
				System.out.println("\nExecing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
				proc = rt.exec(cmd);
				//取得package name
				BufferedReader buf=new BufferedReader(new FileReader(abstractFile));
				while(buf.ready())
				{
					String line=buf.readLine();
					//System.out.println(line);
					if(line.indexOf("package")!=-1)
					{
						String s2=line.substring(line.indexOf("package"));
						//System.out.println(s2);
						StringTokenizer st = new StringTokenizer(s2);
						st.nextToken();
						String s3=st.nextToken();
						//System.out.println(s3);
						String s4[]=s3.split(";");
						System.out.println(s4[0]);
						packageName[0]=s4[0];
						break;
					}
				}
			}
			else if(language.equals("C#"))
			{
				cmd[0] = "/bin/sh";
				cmd[1] = "-c";
				cmd[2] = "mcs " + inputPath + fileName;
						
				System.out.println("\nExecing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
				
				proc = rt.exec(cmd);
			}
			// any error???
			int exitVal = proc.waitFor();
			if (exitVal == 0) {
				if(outputFile[1].equals("java"))
				{
					String s[]=fileName.split("_");
					File newFile = new File(inputPath + fileName);
//					File abstractFile = new File(inputPath + s[2]);
					File abstractFile = new File(inputPath + oldfileName);
					boolean done = abstractFile.renameTo(newFile);
					System.out.println(done);
				}
				else if(language.equals("C#"))
				{
//					System.out.println(outputFile[0]);
					String s="mv "+inputPath+outputFile[0]+".exe "+outputPath;
					proc = rt.exec(s);
//					System.out.println(s);
					proc.waitFor();
				}
				return true;
			}
			else
			{
			//編譯失敗讀取錯誤訊息
				StringBuilder sb =new StringBuilder();
				InputStreamReader isr = new InputStreamReader(proc.getErrorStream());
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				int count=0;
				try {
					 while((line=br.readLine())!=null /*&& count<5*/)
			         {
						 System.out.println(line);
						 if(line.indexOf("opt/")==-1 && line.indexOf("home/")==-1)
						 {
//							 System.out.println(line.indexOf("home/")+" "+line.indexOf("home/"));
							 sb.append(line+"\n");
							 count++;
						 }
			         }
					 String content=sb.toString();
					 int index = 0;
					 while ((index = content.indexOf("\n")) != -1) { // 處理公佈欄的換行字元
					 content = content.substring(0, index) + " <BR> " + content.substring(index + "\n".length());
					 }
					 error.add(content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("cant read");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (proc != null) {
				proc.destroy();
			}
			rt.freeMemory();
		}
		String[] outputFile = fileName.split("\\.");
		if(outputFile[1].equals("java"))
		{
			String s[]=fileName.split("_");
			File newFile = new File(inputPath + fileName);
			File abstractFile = new File(inputPath + s[2]);
			boolean done = abstractFile.renameTo(newFile);
			System.out.println(done);
		}
		return false;
	}

	public boolean checkResult(String fileName, String inputData, final String true_result)
			throws IOException, InterruptedException, TimeoutException {
		return this.checkResult(fileName, inputData, true_result, "./",new String[1]);
	}
	public boolean checkResult(String fileName, String inputData, final String true_result, String hook_path,String oldFileName,String packageName[])
			throws IOException, InterruptedException, TimeoutException {
		String[] outputFile = fileName.split("\\.");
		if(outputFile[1].equals("java"))return this.checkResult(oldFileName, inputData, true_result, "./",packageName);
		return this.checkResult(fileName, inputData, true_result, "./",packageName);
	}

	public boolean checkResult(String fileName, String inputData, final String true_result, String hook_path,String packageName[])
			throws IOException, InterruptedException, TimeoutException {
		long timeout = 8000;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		Process process = null;
		sb2.append(true_result);
		sb2.append("\n");

		// Process process = Runtime.getRuntime().exec(outputPath + fileName +
		// ".sh");
		String[] outputFile = fileName.split("\\.");
		// hook C standard library to prevent from executing danger function
		String[] envp = { "LD_PRELOAD=" };
		for (String hook : hook_list) envp[0] += hook_path + hook + ":";
//		System.out.println(envp[0]);
		if(outputFile[1].equals("c"))process = Runtime.getRuntime().exec(outputPath + outputFile[0], envp);
		else if(outputFile[1].equals("py"))process = Runtime.getRuntime().exec(outputPath + outputFile[0]);
		else if(outputFile[1].equals("java"))
		{
			String[] cmd = new String[3];
			cmd[0] = "/bin/sh";
			cmd[1] = "-c";
			if(packageName[0]==null)cmd[2] = "java -cp " + outputPath + " " + outputFile[0];
			else cmd[2] = "java -cp " + outputPath + " " + packageName[0]+"."+outputFile[0];
			
			System.out.println("\nExecing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
			process = Runtime.getRuntime().exec(cmd);
		}
		else if(outputFile[1].equals("cs"))
		{
			process = Runtime.getRuntime().exec("mono "+outputPath + outputFile[0]+".exe");
		}
		Worker worker = new Worker(process);
		OutputStreamWriter osr = new OutputStreamWriter(process.getOutputStream());
		osr.write(inputData + "\n");
		osr.flush();

		worker.start();
		try {
			worker.join(timeout);
			if (worker.exit != null) {
				InputStreamReader isr = new InputStreamReader(process.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					sb.append(line + "\n");

				// �h���h�l�ťդδ���Ÿ�
				while (sb.indexOf(" \n") >= 0) {
					int s = sb.indexOf(" \n");
					sb.delete(s, s + 1);
				}
				while (sb.indexOf("\n\n") >= 0) {
					int s = sb.indexOf("\n\n");
					sb.delete(s, s + 1);
				}
				if (sb.length() > 0) {
					while (sb.substring(0, 1).equals("\n")) {
						sb.delete(0, 1);
					}
				}
				while (sb.indexOf(" \n") >= 0) {
					int s = sb.indexOf(" \n");
					sb.delete(s, s + 1);
				}
				while (sb2.indexOf(" \n") >= 0) {
					int s = sb2.indexOf(" \n");
					sb2.delete(s, s + 1);
				}
				while (sb2.indexOf("\n\n") >= 0) {
					int s = sb2.indexOf("\n\n");
					sb2.delete(s, s + 1);
				}
				if (sb2.length() > 0) {
					while (sb2.substring(0, 1).equals("\n")) {
						sb2.delete(0, 1);
					}
				}
				while (sb2.indexOf(" \n") >= 0) {
					int s = sb2.indexOf(" \n");
					sb2.delete(s, s + 1);
				}
				System.out.println("Excute Resutlt:\n" + sb.toString());
				System.out.println("True Result:\n" + sb2.toString());

				if (sb.toString().equals(sb2.toString()))
					return true;
				else
					return false;
			} else {
				throw new TimeoutException();
			}
		} catch (InterruptedException ex) {
			worker.interrupt();
			Thread.currentThread().interrupt();
			throw ex;
		} finally {
			process.destroy();
		}
	}

	private class Worker extends Thread {
		private final Process process;
		private Integer exit;

		private Worker(Process process) {
			this.process = process;
		}

		public void run() {
			try {
				exit = new Integer(process.waitFor());
			} catch (InterruptedException ignore) {
				return;
			}
		}
	}

	public int checkSourceCodeContext(String fileName) {
		FileReader fr;
		int result = 0;
		try {
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("system") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("process.h") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("fopen") >= 0) {
					result = 1;
					break;
					/*
					 * int s = 0,e; while(line.indexOf(",",s)>=0){ s =
					 * line.indexOf(",",s)+1; } e = line.indexOf(")",s); line =
					 * line.substring(s,e); line = line.replace(" ", "");
					 * if(!line.equals("\"rb\"")&&!line.equals("\"r\"")){ result
					 * = 2; break; }
					 */
				} else if (line.indexOf("popen") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("wpopen") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("open(") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("resin") >= 0) {
					result = 1;
					break;
				} else if (line.indexOf("webapps") >= 0) {
					result = 1;
					break;}
//				} else if(line.indexOf("import")>=0) {
//					result=1;
//					break;
//				} else if(line.indexOf("write")>=0) {
//					result=1;
//					break;
//				}
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
