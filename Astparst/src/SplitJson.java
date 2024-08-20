import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.*;
import java.nio.file.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SplitJson {
    public static void main(String[] args) {
        // JSON文件路径
        String jsonFilePath = "/storage/emulated/0/汉化ast/merge.json";
        String outputFilePath = "/storage/emulated/0/汉化ast/split.json";

        try {
            FileReader reader = new FileReader(jsonFilePath);
            // 使用Gson解析JSON文件
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			JsonObject newJsonObject = new JsonObject();
            // 遍历JSON对象的键值对并打印
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                System.out.println(key + ": " + value);
				
				if (key.contains("\r\n")) {
					
					String keys[] = key.split("\r\n");
					String texts[] =value.getAsString().split("\r\n");
					
					if (keys.length == texts.length) {
						
						for (int i = 0; i < keys.length; i++) {
							newJsonObject.addProperty(keys[i], texts[i].replaceAll("\\s", ""));
						}
						
					} else {
						System.out.println("原文本和替换文本行数不匹配！");
						String TexttoSplit=value.getAsString().trim().replaceAll("\\s", "");
						List<String> messages = splitMessage(TexttoSplit, keys.length);
						
						for (int i = 0; i < keys.length; i++) {
							newJsonObject.addProperty(keys[i], messages.get(i));
						}
						
					}
				
						
		}else{
			
			newJsonObject.addProperty(key, value.getAsString().replaceAll("\\s", ""));
			
			
			
		}
						

                // 创建新的 JSON 对象并添加当前键值对
             
                // 将新的 JSON 对象写入到输出文件中              
            }
			
			writeJsonToFile(newJsonObject, outputFilePath);
			System.out.println("JSON data has been successfully written to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonToFile(JsonObject jsonObject, String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        Gson gson = new Gson();
        gson.toJson(jsonObject, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }
	
	
	
	private static List<String> splitMessage(String message, int addrCount) {
        List<String> messages = new ArrayList<>();
        int chunkSize = (int) Math.ceil((double) message.length() / addrCount);
        for (int i = 0; i < addrCount; i++) {
            int start = i * chunkSize;
            int end = Math.min((i + 1) * chunkSize, message.length());
            messages.add(message.substring(start, end));
        }
        return messages;
    }
	
	
	
	
}
