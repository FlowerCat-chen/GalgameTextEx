import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import org.json.*;
import com.google.gson.*;

public class Astparse {
	static int index=-1;
	static JsonElement jsonElement = null;
	static JsonParser parser = new JsonParser();
	
	public static void main(String[] args) throws FileNotFoundException {
		
		
		

		String inputFolder = "/storage/emulated/0/汉化ast/script"; // 输入文件夹路径
		String outputFolder = "/storage/emulated/0/汉化ast/script/out/"; // 输出文件夹路径
		
		String jsonPath = "/storage/emulated/0/汉化ast/split.json";
		FileReader reader = new FileReader(jsonPath);
		
		// 使用Gson的JsonParser来解析JSON文件
		jsonElement = parser.parse(reader);
		
		
		checkAndCreateFolder(outputFolder);
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

		if (listOfFiles != null) {
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().endsWith(".ast")) {
					String inputFilePath = file.getPath(); // 获取文件的绝对路径
					String outputFilePath = outputFolder + file.getName(); // 为每个输出文件创建一个新名称
				
					System.out.println(jsonPath);
					try
					{
						convertFile(inputFilePath, outputFilePath);
					}
					catch (InterruptedException e)
					{}
				}
			}
		}
		
		
		
		
	}

	
	
	
	public static String getValueFromJsonFile(String key) {     

        try {
			
            // 如果jsonElement是一个JsonObject，我们尝试获取对应的值
            if (jsonElement.isJsonObject()) {
                JsonElement value = jsonElement.getAsJsonObject().get(key);
                if (value != null) {
                    // 根据需要，你可以返回value.getAsString(), value.getAsBoolean(), etc.
                    // 这里为了通用性，我们直接返回JsonElement对象
                    return value.getAsString();
                } else {
                    //throw new IllegalArgumentException("Key not found in the JSON file: " + key);
				   return"没有找到◆◆"+key;
                }
            } else {
                throw new IllegalArgumentException("The JSON file does not contain a JSON object at the root.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file: " , e);
        }

        // 如果JSON文件中没有找到该键，或者JSON文件格式不正确，则返回null或抛出异常（取决于你的需求）
        // 这里我们假设没有找到键时抛出异常
    //    throw new IllegalArgumentException("Key not found in the JSON file: " + key);
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

	public static void convertFile(String inputFilePath, String outputFilePath) throws InterruptedException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-8"));//GBK
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
			

			String line;
			while ((line = reader.readLine()) != null) {
				if (/*line.startsWith("name")||*/line.replaceAll("\\s", "").startsWith("\"")){
					  // System.out.println(line);
                 String key=line.replaceAll("\"","").replaceAll(",","").replaceAll("\\s", "").trim();
			  // System.out.println(key);
					//writer.write(getValueFromJsonFile(key));
					if(key.equals("」"))
					{
						writer.write("     "+"\""+"」"+"\"");
						writer.newLine();
					}else{
						String tmp=key.replace("」","").replace("「","").trim().replaceAll("\\s", "");
						String kuohaoS=key.contains("「")? "「":"";
				        String kuohaoE=key.contains("」")? "」":"";
							writer.write("     "+"\""+kuohaoS+getValueFromJsonFile(tmp)+kuohaoE+"\""+",");
							writer.newLine();
		}
					
					Thread.sleep(1);
				} else {
					// 其它情况下原封不动地输出到输出文件
					writer.write(line);
					writer.newLine();
				}
			}

			reader.close();
			writer.close();
		
			System.out.println("处理完成：" + outputFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static String extratFileName(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		String fileNameWithoutExtension = fileName.substring(0, dotIndex);
		return  fileNameWithoutExtension;

    }




}
    

    

