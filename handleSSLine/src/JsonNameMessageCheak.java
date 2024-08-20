import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonNameMessageCheak {

    public static void main(String[] args) {
        File folder = new File("/storage/emulated/0/AppProjects/Scene/json/"); // 替换为你的文件夹路径
        //检查提取的json有无漏洞
        String attention="";
       
        Gson gson = new Gson();
        JsonParser parser = new JsonParser(); // 使用旧版 JsonParser

        for (File file : folder.listFiles(new java.io.FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".json");
				}
			})) {
            try {
                String jsonContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
                JsonElement jsonElement = parser.parse(jsonContent); // 使用 parse 方法
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                Iterator<JsonElement> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    JsonElement element = iterator.next();
                    if (element.isJsonObject()) {
                        JsonObject jsonObject = element.getAsJsonObject();
                       
						String name=jsonObject.has("name") ? jsonObject.get("name").getAsString():"";
						String message=jsonObject.has("message") ? jsonObject.get("message").getAsString():"";
						
						if(isNotName(name)){ 
					    	attention=attention+"名字错误："+name+"文件："+file.getName()+"\n";		
				     		System.out.println("名字错误："+name+"文件："+file.getName());  
						}
						
						if(!name.isEmpty()){
						
						if((message.startsWith("「"))&&(!message.endsWith("」"))){ 
					    	attention=attention+"文本错误："+message+"文件："+file.getName()+"\n";
						    System.out.println("文本错误："+message+"文件："+file.getName());
						}
						
						if((message.startsWith("『"))&&(!message.endsWith("』"))){
							attention=attention+"文本错误："+message+"文件："+file.getName()+"\n";
							System.out.println("文本错误："+message+"文件："+file.getName());
						}
							
						if((message.startsWith("（"))&&(!message.endsWith("）"))){ 
							attention=attention+"文本错误："+message+"文件："+file.getName()+"\n";
						    System.out.println("文本错误："+message+"文件："+file.getName());
						}
								if(!areBracketsBalanced(message)){
									attention=attention+"文本不平："+message+"文件："+file.getName()+"\n";
									System.out.println("文本不平："+message+"文件："+file.getName());
								}
						}
						
						
						
						
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
            }
        }

		
        FileWriter writer = null;
        try {
            writer = new FileWriter("/storage/emulated/0/AppProjects/Scene/zbads.txt");
    
                writer.write(attention);
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing writer: " + e.getMessage());
                }
            }
        }

        System.out.println("Cheak completed.");
    }
	
	
	
	private static boolean isNotName(String line){
		return (line.contains("。"))
			||(line.contains("！"))
			||(line.contains("？")&&(!line.startsWith("？")))
			||(line.contains("、"))
			||(line.contains("…"))
			||(line.contains("……"))
			||(line.contains("，"))
			||(line.contains("（"))
			||(line.contains("「"))
			||(line.contains("#"))
			||(line.contains("『"))
			||(line.contains("」"))
			||(line.contains("』"))
			||(line.contains("）"));
	}
	
	
	
	// Method to check if brackets are balanced
    public static boolean areBracketsBalanced(String text) {
        // Stack to keep track of opening brackets
        Stack<Character> stack = new Stack<>();

        // Iterate through each character in the string
        for (char ch : text.toCharArray()) {
            // Push opening brackets onto the stack
            if (ch == '（' || ch == '『' || ch == '「') {
                stack.push(ch);
            }
            // Check for closing brackets and match with the top of the stack
            else if (ch == '）' || ch == '』' || ch == '」') {
                // Check if stack is empty or top of the stack doesn't match
                if (stack.isEmpty() || !isMatchingPair(stack.pop(), ch)) {
                    return false;
                }
            }
        }

        // If stack is empty, all opening brackets have matching closing brackets
        return stack.isEmpty();
    }

    // Method to check if opening and closing brackets match
    private static boolean isMatchingPair(char opening, char closing) {
        switch (opening) {
            case '（': return closing == '）';
            case '『': return closing == '』';
            case '「': return closing == '」';
            default: return false;
        }
    }
	
	
	
}






