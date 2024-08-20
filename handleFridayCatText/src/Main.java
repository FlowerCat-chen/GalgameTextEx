import java.util.*;

import java.io.*;



public class Main {
	
	static boolean hasName;
	static boolean haslineBteak;
	
	static String nameTmp="";
	
    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/周五遇见小猫/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/周五遇见小猫/out/"; // 输出文件夹路径
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
			
			String[] paragraphs = input.split("\\n\\s*\\n");//段落

			// Print out each paragraph
			for (String para:paragraphs) {
			//	System.out.println("Paragraph " + ":");
			//	System.out.println(para);
			//	System.out.println(); // Add a blank line between paragraphs for clarity
			
				if(para.contains("&")){
					//是我们要的段落
					//先整形，去掉换行，改为\r\n
					String paratmp = para.replaceAll("\\n", "\\\\r\\\\n");
					String parawithoutLb=para.replaceAll("\\n","");
					//System.out.println(para);
					//System.out.println();
					if(paratmp.contains("#")){//带名字
						String tmp[]=paratmp.substring(paratmp.indexOf("#"),paratmp.length()).split("\\\\r\\\\n");
						
						String name=tmp[0].contains("=")?tmp[0].substring(1,tmp[0].indexOf("=")):tmp[0].substring(1,tmp[0].length());
						
						String content=parawithoutLb.substring(parawithoutLb.indexOf("\"")+1,parawithoutLb.length()).replace("\"","");
						chats.add(new Chat(name,content));
						//System.out.println("名字:"+name+"   "+"内容:"+content);
						//System.out.println();
					}else{
						String text=parawithoutLb.substring(parawithoutLb.indexOf("\"")+1,parawithoutLb.length()).replace("\"","");
						//System.out.println(text);
						//System.out.println();
						chats.add(new Chat("", text));
					}
				}
				
			}
			
			
			
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
    
