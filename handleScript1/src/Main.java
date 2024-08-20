import java.util.*;

import java.io.*;
import java.util.regex.*;



public class Main {
	

	
	static boolean hasText;
    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/scp/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/scp/out/"; // 输出文件夹路径
        checkAndCreateFolder(outputFolder);
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String inputFilePath = file.getPath(); // 获取文件的绝对路径
                    String outputFilePath = outputFolder + file.getName().replace("txt","json"); // 为每个输出文件创建一个新名称

                   
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "Shift-JIS"));//GBK
			StringBuilder sb = new StringBuilder();
            String currentLine;
			
            while ((currentLine = reader.readLine()) != null) {
                sb.append(currentLine);
                sb.append("\n");
            }
            reader.close();
			
			String input = sb.toString();

            // 正则表达式匹配大括号中的内容
            String regex = "\\{([^}]*)\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            // 提取匹配到的文本
            while (matcher.find()) {
                String textInBraces = matcher.group(1); // 获取大括号内的内容
                System.out.println("文本内容:");
                System.out.println(textInBraces.trim()); // 输出去除前后空白的文本内容
                System.out.println("-------------------------");
				handleText(chats,textInBraces.trim().replaceAll("\\s", ""));
            }
			
			
            System.out.println("处理完成：" );
        } catch (IOException e) {
            e.printStackTrace();
		
        }
		return chats;
    }
	
	
	
	public static void handleText(List<Chat> chats,String text){
		if(text.contains("【")&&text.contains("】")){
			//有姓名
			String name=text.substring(0,text.indexOf("】")+1).replace("【","").replace("】","");
			String content=text.substring(text.indexOf("】")+1,text.length());
			chats.add(new Chat(name,content));
		}else{
			String content=text.substring(text.indexOf("】")+1,text.length());
			chats.add(new Chat("",content));
		}
		
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
				json.append("{\"message\":\"").append(chat.getMessage().trim()).append("\"}");
			}else{
				json.append("{\"name\":\"").append(chat.getName()).append("\", \"message\":\"").append(chat.getMessage()).append("\"}");
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
    
