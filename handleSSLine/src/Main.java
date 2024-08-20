import java.util.*;

import java.io.*;



public class Main {
	
	static boolean hasName=false;
	static String nameTmp="";
	static boolean hasSp=false;//有无文本标记
	static int count=0;
	
	static List<String> names = new ArrayList<>();
	
	static String[] endings = {"』", "」", "）"};
	
    public static void main(String[] args) {
		
        String inputFolder = "/storage/emulated/0/AppProjects/Scene/out/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/AppProjects/Scene/json/"; // 输出文件夹路径
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
		String mTextTmp="";//存储中间结果
		
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-16le"));//GBK
           

            String line;
            while ((line = reader.readLine()) != null) {
				//line=line.trim().replaceAll("\\s", "");//去除所有空格
				
		/****************************************************/		
			
				if(line.contains("<*>")){//当前文本行有名字
					nameTmp=line.replace("<*>","").replaceAll("#Z","");//替换掉<*>,写入nameTmp
				}
				
				if((line.startsWith("『"))||(line.startsWith("「"))||(line.startsWith("（"))){//文本是否以「『（开头
					hasSp=true;
				}
				
				/******************多行文本处理******************/
			
				if((!line.startsWith("【"))&&(!line.contains("】"))){//先排除掉【name】这种好处理的情况
					
					if(hasSp){//检测到文本开始标记
					
					if((!line.endsWith("』"))
						&&(!line.endsWith("」"))
						&&(!line.endsWith("）"))){//标记不在一行里
						mTextTmp=mTextTmp+line;//累加结果
						count++;
					}else{
						
						String textToWrite=(mTextTmp+line).replace("#N","\\r\\n");
						String nameToWrite=nameTmp;
						
						//System.out.println(nameToWrite+" : "+textToWrite);
						chats.add(new Chat(nameToWrite,textToWrite,count+1));
						mTextTmp="";
						nameTmp="";
						hasSp=false;
						count=0;
					}
					
					
					
					}else if(!(line.contains("<*>"))
					&&(!line.contains("』"))
					&&(!line.contains("」"))
					&&(!line.contains("）"))
					&&(!line.startsWith("『"))
					&&(!line.startsWith("「"))
					&&(!line.startsWith("（"))){
				   chats.add(new Chat("",line.replace("#N","\\r\\n"),1));
				  // System.out.println(line);
					}
				
				
				
				}else{
					//好处理的情况
					String name=line.substring(0,line.indexOf("】")).replace("【","").replace("】","");//截取名字，并去除【 和 】
					String mesaage=line.substring(line.indexOf("】"),line.length()).replace("【","").replace("】","").replace("#N","\\r\\n");//截取文本
					chats.add(new Chat(name.replaceAll("#Z",""),mesaage,1));//添加到列表
				}
			
				
				
				
		
		
		/****************************************************/
				
					
            }

            reader.close();
           // System.out.println("处理完成：" );
        } catch (IOException e) {
            e.printStackTrace();
		
        }
		return chats;
    }
	
	
	
	
    public static void convertFile(String input,String output){
		
	  List<Chat> chats = extractChats(input);
	  String json = convertToJson(chats);
	 // System.out.println(json);
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
				json.append("\n").append("{").append("\n").append("  ").append("\"message\":\"").append(chat.getMessage().trim()).append("\",").append("\n").append("  ").append("\"linecount\":\"").append(chat.getLineCount()).append("\"").append("\n}");
			}else{
				json.append("\n").append("{").append("\n").append("  ").append("\"name\":\"").append(chat.getName()).append("\",").append("\n").append("  ").append("\"message\":\"").append(chat.getMessage()).append("\",").append("\n").append("  ").append("\"linecount\":\"").append(chat.getLineCount()).append("\"").append("\n}");
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
        private int linecount;
		
        public Chat(String name, String message,int linecount) {
            this.name = name;
            this.message = message;
			this.linecount=linecount;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
		
		public int getLineCount() {
            return linecount;
        }
    }
	
	
	
}
    
