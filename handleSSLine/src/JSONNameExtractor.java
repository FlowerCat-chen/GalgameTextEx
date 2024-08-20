import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class JSONNameExtractor {

    public static void main(String[] args) {
        File folder = new File("/storage/emulated/0/AppProjects/Scene/json/"); // 替换为你的文件夹路径
        File outputFile = new File("/storage/emulated/0/AppProjects/Scene/names.txt"); // 输出文件路径

        Set<String> names = new HashSet<>();
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
                        if (jsonObject.has("name")) {
                            names.add(jsonObject.get("name").getAsString().trim().replaceAll("\\s", ""));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
            }
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
            for (String name : names) {
                writer.write(name + System.lineSeparator());
            }
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

        System.out.println("Name extraction completed.");
    }
}
