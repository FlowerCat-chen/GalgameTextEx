import com.google.gson.*;

import java.io.*;
import java.util.Map;

public class ValidateJson {
    public static void main(String[] args) {
        String jsonFilePath = "/storage/emulated/0/AppProjects/renpy测试/out/script.json";

        try {
            // 读取JSON文件
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(jsonFilePath));

            // 检查是否为JsonObject
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                validateJsonObject(jsonObject, 1);
            } else {
                System.out.println("JSON文件不是一个有效的JSON对象。");
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定的JSON文件：" + jsonFilePath);
        } catch (JsonSyntaxException e) {
            System.out.println("JSON文件格式错误：" + e.getMessage());
        }
    }

    private static void validateJsonObject(JsonObject jsonObject, int lineNumber) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonObject()) {
                validateJsonObject(value.getAsJsonObject(), lineNumber);
            } else if (value.isJsonArray()) {
                validateJsonArray(value.getAsJsonArray(), lineNumber);
            } else if (value.isJsonPrimitive()) {
                // 如果值是原始类型，什么都不做
            } else if (value.isJsonNull()) {
                // 如果值是null，什么都不做
            } else {
                // 其他类型的值，例如JsonNull或JsonArray
                System.out.println("JSON文件格式错误：第 " + lineNumber + " 行，键：" + key + "，值：" + value);
            }
        }
    }

    private static void validateJsonArray(JsonArray jsonArray, int lineNumber) {
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                validateJsonObject(element.getAsJsonObject(), lineNumber);
            } else if (element.isJsonArray()) {
                validateJsonArray(element.getAsJsonArray(), lineNumber);
            } else if (element.isJsonPrimitive()) {
				System.out.println("好");
                // 如果值是原始类型，什么都不做
            } else if (element.isJsonNull()) {
                // 如果值是null，什么都不做
            } else {
                // 其他类型的值，例如JsonNull或JsonObject
                System.out.println("JSON文件格式错误：第 " + lineNumber + " 行，值：" + element);
            }
        }
    }
}
