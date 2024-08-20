import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplifiedToTraditional {
	
    public static void main(String[] args) {
    
        String dictionaryFilePath ="/storage/emulated/0/p/dict/dict.txt";
        String directoryPath = "/storage/emulated/0/红莲华script/txtout/";

        // Load the dictionary
        Map<String, String> dictionary = loadDictionary(dictionaryFilePath);

        if (dictionary == null) {
            System.out.println("Failed to load dictionary.");
            return;
        }

        // Process each file in the directory
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            System.out.println("The provided path is not a directory.");
            return;
        }

        for (File file : dir.listFiles()){
		if( file.getName().endsWith(".txt")) {
            processFile(file, dictionary);
        }
		}
    }

    private static Map<String, String> loadDictionary(String filePath) {
        Map<String, String> dictionary = new HashMap<>();
        try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    dictionary.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return dictionary;
    }

    private static void processFile(File file, Map<String, String> dictionary) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            StringBuilder content = new StringBuilder();

            for (String line : lines) {
                for (Map.Entry<String, String> entry : dictionary.entrySet()) {
                    line = line.replace(entry.getKey(), entry.getValue());
                }
                content.append(line).append(System.lineSeparator());
            }

            Files.write(file.toPath(), content.toString().getBytes());
System.out.println("完成了:"+file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
