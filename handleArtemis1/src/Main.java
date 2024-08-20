import java.util.*;

import java.io.*;
import java.util.regex.*;



public class Main {
	

	
	static boolean hasText;
	static String textTmp="";
    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/纯情/scenario/00_com/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/纯情/scenario/00_com/out/"; // 输出文件夹路径
        checkAndCreateFolder(outputFolder);
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".lua")) {
                    String inputFilePath = file.getPath(); // 获取文件的绝对路径
                    String outputFilePath = outputFolder + file.getName().replace("lua","json"); // 为每个输出文件创建一个新名称

                   
						convertFile(inputFilePath, outputFilePath);
					
					
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

	

	
	
    public static List<Chat> extractChats(String inputFilePath) {
		List<Chat> chats = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-8"));//GBK
            String name="";
            String line;
            while ((line = reader.readLine()) != null) {
				line=line.trim().replaceAll("\\s", "");//去除所有空格


				if(line.startsWith("[\"text\"]")){
					hasText=true;
					textTmp=line.substring(line.indexOf("{"),line.length())
						.replace("}","")
						.replace("[","")
						.replace("]","")
						.replace("{","")
						.replace(",","")
						.replace("\\n", "\\r\\n");
						System.out.println(textTmp);
				}

                if (line.startsWith("[\"tag\"]")) {
					
					// 查找匹配项并输出结果
					if (line.contains("name")) {
						String tmp=line.substring(line.indexOf("name"),line.length());
						name=tmp.substring(tmp.indexOf("[["),tmp.indexOf("]]")).replace("[[","").replace("]]","");
						
					} else {
						name="";
					}
				
	
					
					if(hasText){
						
						chats.add(new Chat(name, textTmp));
					
						hasText=false;
					}
                }
            }

            reader.close();
            System.out.println("处理完成：" );
        } catch (IOException e) {
            e.printStackTrace();

        }
		return chats;
    }
	
	
	
    public static void convertFile(String input,String output){
		
	  List<Chat> chats = extractChats(input);
	  String json = convertToJson(chats);
	  System.out.println(json);
  	//
	  String outputFilePath = output;

	try {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
		writer.write(json);
		writer.close();


		System.out.println("处理完成：" + outputFilePath);

	} catch (IOException e) {

		e.printStackTrace();

	}
	}
	
	
	
	
	
	
	
	
	
	
	public static String convertToJson(List<Chat> chats) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);
			if(chat.getName().isEmpty()){
				json.append("\n").append("{").append("\n").append("  ").append("\"message\":\"").append(chat.getMessage().trim()).append("\"").append("\n}");
			}else{
				json.append("\n").append("{").append("\n").append("  ").append("\"name\":\"").append(chat.getName()).append("\",").append("\n").append("  ").append("\"message\":\"").append(chat.getMessage()).append("\"").append("\n}");
			}
            if (i < chats.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
    
    
    static class Chat {
        private String name;
        private String message;

        public Chat(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
	
	
	
}
    
