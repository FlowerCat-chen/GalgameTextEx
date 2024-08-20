import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextExtractor {

    public static void main(String[] args) {
		
		
		String inputFolder = "/storage/emulated/0/AppProjects/Scene/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/AppProjects/Scene/out/"; // 输出文件夹路径
        checkAndCreateFolder(outputFolder);
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String inputFilePath = file.getPath(); // 获取文件的绝对路径
                    String outputFilePath = outputFolder + file.getName(); // 为每个输出文件创建一个新名称

					List<String> lines = readFile(inputFilePath);
					processLines(lines,outputFilePath);
					
                }
            }
        }    
        
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
	
	
	
	
	
	
	
    private static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
			FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,StandardCharsets.UTF_16LE));//u16le
			
            String line;
            while ((line = br.readLine()) != null) {
				//line=line.trim().replaceAll("\\s", "");//去除所有空格
				
				if(!isNotText(line)){//是我们要用的文本
                   lines.add(line);
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static void processLines(List<String> lines,String outfile) {
        String[] dialogMarks = {"「", "（", "『"};//对话开始的标记
        List<String> resultLines = new ArrayList<>();//存储处理后的结果
  

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isEmpty()) {
                continue;
            }

            if (i < lines.size() - 1 && !lines.get(i + 1).isEmpty()) {//检查遍历
                String nextLine = lines.get(i + 1);
                for (String mark : dialogMarks) {
                    if (nextLine.startsWith(mark)) {
						//判断这行是不是结束了
						if((!line.startsWith("（"))
							&&(!line.endsWith("）"))
							&&(!line.startsWith("『"))
							&&(!line.endsWith("』"))
							&&(!line.startsWith("「"))
							&&(!line.endsWith("」"))
							&&(!isNotName(line))){
                        line += "<*>";
              }
                        break;
                    }
                }
            }
/*
            if (line.startsWith("【")) {
                if (!line.contains("】")) {
                    System.out.println("错误");
                    System.out.println(line);
                } else {
                    line = line.replace("】", "】<|>");
                }
            }
*/
            resultLines.add(line);
        }

        String result = String.join("\n", resultLines);
        
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "utf-16le"));
			writer.write(result);
			writer.close();


			System.out.println("处理完成：" + outfile);

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		
    }
	
	private static boolean isNotName(String line){
		return (line.contains("。"))
		||(line.contains("！"))
		//||(line.contains("？"))
		||(line.contains("、"))
		||(line.contains("…"))
		||(line.contains("……"))
		||(line.contains("，"));
	}
	
	private static boolean isNotText(String line) {
		return line.isEmpty() ||
			line.matches("^[__a-zA-Z].*")||
			line.contains("/")||
			line.contains(".")||
			line.contains("__"); // 使用正则表达式检查是否以特定字符开头
	}
	
	
}



