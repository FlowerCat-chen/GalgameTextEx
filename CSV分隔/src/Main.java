import java.io.*;

import java.io.*;


public class Main {
    public static void main(String[] args) {
        String inputFile = "/storage/emulated/0/犬猫狐汉化/00000001.csv"; // 输入文件夹路径
        String outputFile= "/storage/emulated/0/犬猫狐汉化/tmp.txt"; // 输出文件夹路径
        try
		{
			convertFile(inputFile, outputFile);
		}
		catch (InterruptedException e)
		{}
	}

    public static void convertFile(String inputFile, String outputFile) throws InterruptedException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if(!line.contains(",,")){
					continue;
				}else{
				String[] data = line.trim().split(",,");
                // 获取日文文本部分（假设日文文本在第三列）
                String result = data[1];
                    writer.write(result+"\n"); // 写入输出文件
                    writer.newLine();          
            }
}
            reader.close();
            writer.close();

            System.out.println("处理完成：" + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	}
