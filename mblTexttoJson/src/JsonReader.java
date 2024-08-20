import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonReader {

	public static JsonArray jsonArray ;
	
	
    public static void readJson(String filePath) {    
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Gson gson = new Gson();
            jsonArray = gson.fromJson(content, JsonArray.class);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
public static String[] getjsonContent(int index){
	String[] result = new String[2];
	if (index >= 0 && index < jsonArray.size()) {
		JsonObject jsonObject = jsonArray.get(index).getAsJsonObject();
		String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : "";
		String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";
		result[0] = name;
		result[1] = message;
	} 
        return result;
	
	}
	
	public static void main(String[] args) {
		readJson("/storage/emulated/0/AppProjects/机翻测试wanko/gt_output/KIE_H01.json");
		System.out.println(getjsonContent(0)[1]);
	}
	
	
	
	
}
