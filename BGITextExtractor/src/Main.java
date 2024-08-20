import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        byte[] scriptBuffer = readFileToByteArray("/storage/emulated/0/Exagear/TailTale/scenario.arc~/S00_01.bp"); // Replace with your actual script buffer

        int firstTextOffset;
        int offset = indexOf(scriptBuffer, new byte[]{0x7F, 0, 0, 0}, 0, false);
        if (offset < 0 || offset > 128) {
            firstTextOffset = 0;
        } else {
            firstTextOffset = toInt32(Arrays.copyOfRange(scriptBuffer, offset + 4, offset + 8));
        }

        int intTextOffsetLabel = indexOf(scriptBuffer, new byte[]{0, 3, 0, 0, 0}, 0, false);

        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("/storage/emulated/0/Exagear/TailTale/scenario.arc~/S00_01.txt"));
            while (intTextOffsetLabel != -1 && intTextOffsetLabel < firstTextOffset) {
                int intTextOffset = toInt32(Arrays.copyOfRange(scriptBuffer, intTextOffsetLabel + 5, intTextOffsetLabel + 9));

                if (intTextOffset > firstTextOffset && intTextOffset < scriptBuffer.length) {
                    int endOfTextBlock = indexOf(scriptBuffer, new byte[]{0x00}, intTextOffset, false);
                    if (endOfTextBlock != -1) {
                        byte[] bytesTextBlock = Arrays.copyOfRange(scriptBuffer, intTextOffset, endOfTextBlock);
                        String strText = new String(bytesTextBlock, StandardCharsets.UTF_16); // Assuming UTF-16 encoding
                        strText = strText.replace("\n", "\\n");

                        bw.write("<" + (intTextOffsetLabel + 5) + "," + intTextOffset + "," + bytesTextBlock.length + ">" + strText);
                        bw.newLine();
                    }
                }

                intTextOffsetLabel = indexOf(scriptBuffer, new byte[]{0, 3, 0, 0, 0}, intTextOffsetLabel + 1, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom methods to mimic C# functionalities

    // Simulate Array.IndexOf method
    private static int indexOf(byte[] source, byte[] pattern, int startIndex, boolean ignoreCase) {
        for (int i = startIndex; i <= source.length - pattern.length; i++) {
            boolean found = true;
            for (int j = 0; j < pattern.length; j++) {
                if (ignoreCase && source[i + j] != pattern[j]) {
                    found = false;
                    break;
                }
                if (!ignoreCase && source[i + j] != pattern[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    // Simulate BitConverter.ToInt32 method
    private static int toInt32(byte[] bytes) {
        return (bytes[0] & 0xFF) |
			((bytes[1] & 0xFF) << 8) |
			((bytes[2] & 0xFF) << 16) |
			((bytes[3] & 0xFF) << 24);
    }
	
	
	public static byte[] readFileToByteArray(String filePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096]; // Adjust buffer size as needed
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
	
	
	
}
