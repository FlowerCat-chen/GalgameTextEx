import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class jsonReplace {
    public static void main(String[] args) {
        try {
            // 读取第一个 JSON 文件
            JsonArray jsonArray1 = new Gson().fromJson(new FileReader("/storage/emulated/0/AppProjects/wankotolily/json/wanko.json"), JsonArray.class);

            // 读取第二个 JSON 文件
            JsonArray jsonArray2 = new Gson().fromJson(new FileReader("/storage/emulated/0/AppProjects/wankotolily/json/wanko_cn.json"), JsonArray.class);

            if (jsonArray1.size() == jsonArray2.size()) {
                for (int i = 0; i < jsonArray1.size(); i++) {
                    JsonObject obj1 = jsonArray1.get(i).getAsJsonObject();
                    JsonObject obj2 = jsonArray2.get(i).getAsJsonObject();

                    if (obj2.has("message")) {
                        String message = obj2.get("message").getAsString().replaceAll("<|im_end|>","").replaceAll("||","");
                        obj1.addProperty("message", message);
                    }
                }

                // 将更新后的 JSON 写入第一个文件
                FileWriter writer = new FileWriter("/storage/emulated/0/AppProjects/wankotolily/json/new.json");
                new Gson().toJson(jsonArray1, writer);
                writer.close();
            } else {
                System.out.println("两个文件中的元素数量不一致，无法进行替换操作。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
