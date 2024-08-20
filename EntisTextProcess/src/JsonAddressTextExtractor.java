import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonAddressTextExtractor {
    public static void main(String[] args) {
        try {
            // 读取 JSON 文件
            JsonArray jsonArray = new Gson().fromJson(new FileReader("/storage/emulated/0/AppProjects/wankotolily/json/new.json"), JsonArray.class);

            // 创建文件写入器
            FileWriter writer = new FileWriter("/storage/emulated/0/AppProjects/wankotolily/json/out.txt");

            // 处理每个对象
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                JsonArray addrArray = obj.getAsJsonArray("addr");
                String message = obj.get("message").getAsString();

                if (addrArray.size() == 1) {
                    String addr = addrArray.get(0).getAsString();
                    writer.write(addr);
                    writer.write(message);
					writer.write("\n\n");
                } else {
                    List<String> messages = splitMessage(message, addrArray.size());
                    for (int i = 0; i < addrArray.size(); i++) {
                        String addr = addrArray.get(i).getAsString();
                        writer.write(addr);
                        writer.write(messages.get(i) + "\n\n");
                    }
                }
            }

            // 关闭写入器
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 分割消息以匹配地址数量
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
