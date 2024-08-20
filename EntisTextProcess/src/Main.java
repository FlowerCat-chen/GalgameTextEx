import java.util.*;

import java.io.*;



public class Main {
	
	static boolean hasSp;
	static boolean hasDot;
	static List<String> addrList =new ArrayList<String>();
	
	
    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/AppProjects/wankotolily/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/AppProjects/wankotolily/out/"; // 输出文件夹路径
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
		
		String mText="";
		String mText2="";
		
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-8"));//GBK
           

            String line;
            while ((line = reader.readLine()) != null) {
				if(line.trim().isEmpty()){
					continue;
				}
				
		
		
	if(line.startsWith("◇")&&!line.isEmpty()){
				
		String tmp=line.substring(line.lastIndexOf("◇")+1,line.length());
		
		
		if(tmp.startsWith("「")){
			hasSp=true;
		}		
		
                if ((!tmp.startsWith("\\"))
					&& (!tmp.startsWith("心の声"))
					&& (!tmp.startsWith("＠"))
				    && (!tmp.startsWith("<"))
					&& (!tmp.startsWith("{"))
					&& (!tmp.startsWith("}"))
					&& (!tmp.startsWith(":"))
					&& (!tmp.startsWith("="))
					&& (!tmp.startsWith("\\"))
					) {
						
					if(hasSp){//检测到分隔
					
					if(!tmp.equals("」")){
						mText=mText+tmp;
						String oldhead=line.substring(0,line.lastIndexOf("◇")+1);
						if(!tmp.equals("「")){
						addrList.add(oldhead);
						}
						System.out.println(oldhead);
					}else{	
						List<String> list = new ArrayList<>(addrList);
						
						chats.add(new Chat("", mText+"」",list));
						mText="";
						hasSp=false;
						addrList.clear();
					}
							
					}else{
						//处理多行文本
						
						mText = mText + tmp; // 累加文本内容
						String oldhead=line.substring(0,line.lastIndexOf("◇")+1);
						if(!tmp.equals("「")){
						addrList.add(oldhead);
						}
						System.out.println(oldhead);
						if (checkEndOfSentence(tmp)) {
							List<String> list = new ArrayList<>(addrList);
							chats.add(new Chat("", mText,list));
							mText = ""; // 重置 mText
							addrList.clear();
						}
						
						
						
						
					}
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
	
	
	// 检查是否为句末标志
	private static boolean checkEndOfSentence(String str) {
		return str.endsWith("。") || str.endsWith("？") || str.endsWith("！")|| str.endsWith("…");
	}
	
	
	public static boolean isAlphaNumeric(String text) {
        return text.matches("^[a-zA-Z0-9_-]+$");
    }
	
	
    public static void convertFile(String input,String output){
		
	  List<Chat> chats = extractChats(input);
	  
	  String json = convertToJson(chats);
	  //System.out.println(json);
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
			if (chat.getName().isEmpty()) {
				List<String> tmp = chat.getAddr();
				json.append("{\"message\":\"").append(chat.getMessage().trim()).append("\", \"addr\": [");
				for (int j = 0; j < tmp.size(); j++) {
					json.append("\"").append(tmp.get(j)).append("\"");
					if (j < tmp.size() - 1) {
						json.append(",");
					}
				}
				json.append("]}");
			} else {
				json.append("{\"name\":\"").append(chat.getName()).append("\", \"message\":\"").append(chat.getMessage()).append("\"}");
			}
			if (i < chats.size() - 1) {
				json.append(",");
			}
		}
		json.append("]");
		return json.toString();
	}
	
	
	
	
	
	/*
	public static String convertToJson(List<Chat> chats) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < chats.size(); i++) {
            Chat chat = chats.get(i);
			if(chat.getName().isEmpty()){
				
				List<String> tmp=chat.getAddr();
				for(String addrss:tmp){
					//获取所有的地址。然后加入下面的json。key为addr
				}
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

	*/
    static class Chat {
        private String name;
        private String message;
        private List<String> addr;
		
        public Chat(String name, String message,List<String> addrs) {
            this.name = name;
            this.message = message;
			this.addr=addrs;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
		
		List<String> getAddr(){
			return addr;
		}
    }
	
	
	
}
    
