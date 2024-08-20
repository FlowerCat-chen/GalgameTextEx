import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        // 从文件中读取文本
        String text = readTextFromFile("/storage/emulated/0/DCIM/script.txt");
   
        List<Chat> chats = extractChats(text);
        String json = convertToJson(chats);
        System.out.println(json);
		//
		String outputFilePath = "/storage/emulated/0/out.json";

		try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
			writer.write(json);
		    writer.close();

           
		    System.out.println("处理完成：" + outputFilePath);
      
		} catch (IOException e) {
        
		e.printStackTrace();
    
	}
    
		
    }

    public static String readTextFromFile(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Shift_JIS"));
		try{
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

	

    public static List<Chat> extractChats(String text) {
        List<Chat> chats = new ArrayList<>();
        Pattern pattern = Pattern.compile("<NAME_PLATE>(.*?)\n(.*?)\n<PAUSE>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            String chatText = matcher.group(2).trim();
            if (name.isEmpty()) {
                name = "";
            }
			//自己处理chattext中所有的换行。直接不要，或者
			String replacedText = chatText.replaceAll("\\n", "\\\\r\\\\n");
            chats.add(new Chat(name, replacedText));
        }
        return chats;
    }

    // 省略其他代码...
	
	
	
	
	

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

