package utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class FileUtils {
    public static void saveKeyToFile(String filePath, String keyBase64) throws IOException {
        Files.write(Paths.get(filePath), keyBase64.getBytes());
    }
    public static void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            throw new IOException("Failed to delete file: " + filePath);
        }
    }
}
