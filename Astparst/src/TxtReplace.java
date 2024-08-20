import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.*;

public class TxtReplace {
    public static void main(String[] args) {
        // JSON文件路径
        String jsonFilePath = "/storage/emulated/0/汉化ast/merge.json";
        String txtFolderPath = "/storage/emulated/0/汉化ast/script";
		File folder = new File(txtFolderPath);
		
		
        try{
			FileReader reader = new FileReader(jsonFilePath);
            // 使用Gson解析JSON文件
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // 遍历JSON对象的键值对并打印
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey().replace("[gaiji text=\"▼\"]","");
                String replacement = entry.getValue().getAsString().replace("[gaiji text=\"▼\"]","");
                System.out.println(key + ": " + replacement);
				if (key.contains("\r\n")) {
					String tmp[] = key.split("\r\n");
					String tmp2[] = replacement.split("\r\n");
					if (tmp.length == tmp2.length) {
						for (int i = 0; i < tmp.length; i++) {
							replaceTextInFiles(folder, tmp[i], tmp2[i]);
						}
					} else {
						System.out.println("原文本和替换文本行数不匹配！");
					}
				} else {
					replaceTextInFiles(folder, key, replacement);
				}
				
				
				
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		
    }
	
	
	
	public static void replaceTextInFiles(File folder, String originalText, String newText) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						// 递归处理子文件夹
						replaceTextInFiles(file, originalText, newText);
					} else if (file.isFile() && file.getName().endsWith(".ast")) {
						// 替换文件中的文本
						replaceTextInFile(file, originalText, newText);
					}
				}
			}
		} else {
			System.out.println("指定路径不是一个文件夹！");
		}
	}

	public static void replaceTextInFile(File file, String originalText, String newText) {
		try {
			// 读取文件内容
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			reader.close();

			// 替换文本
			String content = stringBuilder.toString().replaceAll(originalText, newText);

			// 写入文件
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();

			System.out.println("替换完成：" + file.getAbsolutePath());

		} catch (IOException e) {
			System.out.println("替换文件时出错：" + e.getMessage());
		}
	}
	
	
	
}
