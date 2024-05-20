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
}
