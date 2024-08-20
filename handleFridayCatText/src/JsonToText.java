import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class JsonToText {
	static int index=-1;
	
		public static void main(String[] args) {
			
			String inputFolder = "/storage/emulated/0/p/"; // 输入文件夹路径
			String outputFolder = "/storage/emulated/0/p/translate/"; // 输出文件夹路径
			String jsonFolder = "/storage/emulated/0/p/gt_output/";
			checkAndCreateFolder(outputFolder);
			File folder = new File(inputFolder);
			File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile() && file.getName().endsWith(".TXT")) {
						String inputFilePath = file.getPath(); // 获取文件的绝对路径
						String outputFilePath = outputFolder + file.getName(); // 为每个输出文件创建一个新名称
                        String jsonPath = jsonFolder+extratFileName(file.getName())+".json";
						System.out.println(jsonPath);
						try
						{
							convertFile(inputFilePath, outputFilePath,jsonPath);
						}
						catch (InterruptedException e)
						{}
					}
				}
			}
		}

		public static void checkAndCreateFolder(String folderPath) {
			File folder = new File(folderPath);

			// 检查文件夹是否存在
			if (!folder.exists()) {
				// 如果文件夹不存在，则创建文件夹
				boolean success = folder.mkdirs();

				if (success) {
					System.out.println("文件夹已成功创建：" + folderPath);
				} else {
					System.out.println("创建文件夹失败：" + folderPath);
				}
			} else {
				System.out.println("文件夹已存在：" + folderPath);
			}
		}

		public static void convertFile(String inputFilePath, String outputFilePath,String jsonPath) throws InterruptedException {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "Shift-JIS"));//GBK
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "Shift-JIS"));
                JsonReader.readJson(jsonPath);
				
				String line;
				while ((line = reader.readLine()) != null) {
					
					line=line.trim().replaceAll("\\s", "");
					
					if ((!line.startsWith(";"))
						&& (!line.startsWith("$"))
						&& (!line.startsWith("【"))
						&& (!line.isEmpty())) {
						//   System.out.println(line);
						
						index++;
						String[]tmp=JsonReader.getjsonContent(index);
						//System.out.println(tmp[1]);
						writer.write(tmp[1].replace("<|im_end|>","")); // 写入输出文件
						writer.newLine();
						Thread.sleep(1);
					} else {
						// 其它情况下原封不动地输出到输出文件
						writer.write(line);
						writer.newLine();
					}
				}

				reader.close();
				writer.close();
                index=-1;
				System.out.println("处理完成：" + outputFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
	public static String extratFileName(String fileName) {
            int dotIndex = fileName.lastIndexOf('.');
			String fileNameWithoutExtension = fileName.substring(0, dotIndex);
			return  fileNameWithoutExtension;
		
    }
		
		
		
		
	}
    

    
