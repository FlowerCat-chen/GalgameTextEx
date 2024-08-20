import java.util.*;

import java.io.*;



public class Main {
	
	static String p="[player_name]";
	static String mc="[player_name]";
	static String m="Mirari";
	static String f="Fuyu";
	static String alpha="Alpha";
	static String cub="Cub";
	static String i="Wolf-Girl";
	static String radio="Radio";
	static String z="???";
	static String nameIntrans="小陵";
	
    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/wolfgirl/script/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/wolfgirl/out/"; // 输出文件夹路径
        checkAndCreateFolder(outputFolder);
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".rpy")) {
                    String inputFilePath = file.getPath(); // 获取文件的绝对路径
                    String outputFilePath = outputFolder + file.getName().replace("rpy","json"); // 为每个输出文件创建一个新名称

                   
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
           

            String line;
            while ((line = reader.readLine()) != null) {
				line=line.trim();//去除行首
				
				if(!line.isEmpty()){
					//System.out.println(line);
					if(line.contains(p)){//playername
						line=line.replace(p,nameIntrans);
					}
			/*****************/	
				
                if (line.startsWith("p")&&line.contains("\"")) {
					//   player Say
					String content=line.replaceFirst("p","").replace("\"","");
					chats.add(new Chat(nameIntrans, content));
					
                } else if (line.startsWith("m")&&line.contains("\"")) {
					//   Mirari Say
					String content=line.replaceFirst("m","").replace("\"","");
					chats.add(new Chat(m, content));
					
                } else if (line.startsWith("f")&&line.contains("\"")) {
					//   Fuyu Say
					String content=line.replaceFirst("f","").replace("\"","");
					chats.add(new Chat(f, content));
					
                } else if (line.startsWith("alpha")&&line.contains("\"")) {
					//   Alpha Say
					String content=line.replaceFirst("alpha","").replace("\"","");
					chats.add(new Chat(alpha, content));
					
                } else if (line.startsWith("cub")&&line.contains("\"")) {
					//   Cub Say
					String content=line.replaceFirst("cub","").replace("\"","");
					chats.add(new Chat(cub, content));
					
                } else if (line.startsWith("i")&&line.contains("\"")) {
					//   Wolf-Girl Say
					String content=line.replaceFirst("i","").replace("\"","");
					chats.add(new Chat(i, content));
					
                } else if (line.startsWith("radio")&&line.contains("\"")) {
					//   Radio Say
					String content=line.replaceFirst("radio","").replace("\"","");
					chats.add(new Chat(radio, content));
					
                } else if (line.startsWith("z")&&line.contains("\"")) {
						//   Radio Say
						String content=line.replaceFirst("z","").replace("\"","");
						chats.add(new Chat(z, content));
						
				} else if(line.startsWith("\"")){
					String content=line.replace("\"","");
					chats.add(new Chat("", content));
				}
				
				
				
				
				
				
				
				
			/*****************/
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
    
