import java.io.*;
import java.util.*;
import com.google.gson.Gson;

public class JsonToText {
	static int index=-1;
	static boolean hasSp;
	static List<String> headList =new ArrayList<String>();
	
		public static void main(String[] args) throws IOException, InterruptedException {
			
			String inputFolder = "/storage/emulated/0/AppProjects/wankotolily/"; // 输入文件夹路径
			String outputFolder = "/storage/emulated/0/AppProjects/wankotolily/out/"; // 输出文件夹路径
			String jsonFolder = "/storage/emulated/0/AppProjects/wankotolily/json/";
			checkAndCreateFolder(outputFolder);
			File folder = new File(inputFolder);
			File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile() && file.getName().endsWith(".txt")) {
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

		public static void convertFile(String inputFilePath, String outputFilePath,String jsonPath) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException, IOException {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-8"));//GBK
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
                JsonReader.readJson(jsonPath);
				
				String line;
				while ((line = reader.readLine()) != null) {
					
					if(line.trim().isEmpty()){
						continue;
					}
					
            if(line.startsWith("◇")&&!line.isEmpty()){
		        String tmp=line.substring(line.lastIndexOf("◇")+1,line.length()).trim().replaceAll("\\s", "");
				
				
				
			   
				   	   String oldhead=line.substring(0,line.lastIndexOf("◇")+1);
				       String newhead=line.substring(0,line.lastIndexOf("◇")+1).replaceAll("◇","◆");
						// 其它情况下原封不动地输出到输出文件
					   String newline=line.substring(line.lastIndexOf("◇")+1,line.length());
					   
					   
						writer.write(line);
						writer.newLine();
				//		writer.
						
						
						
					// Thread.sleep(10);			
					
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
		
		
		
	public static String divideString(String input, int index) {
        // 计算片段总数
        int totalSegments = (int) Math.ceil((double) input.length() / 26);

        // 如果index大于片段总数，返回空字符串
        if (index > totalSegments) {
            return "";
        }
		
		if (input.length()<26) {
            return input;
        }
		
		if(index<0){
			return"";
		}
        // 计算切分起始位置和结束位置
        //int startIndex = index * 26;
  //      int endIndex = (int)Math.min((index+1) * 26, input.length());
		int startIndex = 0;
        int endIndex = Math.min(26, input.length());
		
		
        // 切分字符串
        return input.substring(startIndex, endIndex);
    }
		
		

	// 检查是否为句末标志
	private static boolean checkEndOfSentence(String str) {
		return str.endsWith("。") || str.endsWith("？") || str.endsWith("！")|| str.endsWith("…");
	}
	
		
		
		
	public static String extratFileName(String fileName) {
            int dotIndex = fileName.lastIndexOf('.');
			String fileNameWithoutExtension = fileName.substring(0, dotIndex);
			return  fileNameWithoutExtension;
		
    }
		
		
		
		
	}
    

    
