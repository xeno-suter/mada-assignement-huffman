package src;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtility {

    public static String readFile(String fileName) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            return new String(bytes, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeLines(String fileName, String[] lines, String delimiter) {
        try {
            Files.write(Paths.get(fileName), String.join(delimiter, lines).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void writeBytes(String fileName, byte[] bytes) {
        try {
            Files.write(Paths.get(fileName), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static byte[] readBytes(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
