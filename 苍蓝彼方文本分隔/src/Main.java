import java.io.*;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        String inputFolder = "/storage/emulated/0/苍蓝彼方项目/merge2/"; // 输入文件夹路径
        String outputFolder = "/storage/emulated/0/苍蓝彼方项目/out2/"; // 输出文件夹路径
        checkAndCreateFolder(outputFolder);
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles(); // 获取文件夹下的所有文件和文件夹

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".bs5")) {
                    String inputFilePath = file.getPath(); // 获取文件的绝对路径
                    String outputFilePath = outputFolder + file.getName(); // 为每个输出文件创建一个新名称

                    convertFile(inputFilePath, outputFilePath);
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
	
	
    public static void convertFile(String inputFilePath, String outputFilePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\u2402")) { // 如果遇到␂开头的行
                    System.out.println(line);
                    String[] parts = line.split("\u2402");
                    if (parts.length > 3) {
                        String chineseText = parts[3].trim();
                        System.out.print(chineseText);
                        writer.write(chineseText); // 写入输出文件
                        writer.newLine();
                    }
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
}
