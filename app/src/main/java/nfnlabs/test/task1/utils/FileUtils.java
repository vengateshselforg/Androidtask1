package nfnlabs.test.task1.utils;

import java.io.File;

public class FileUtils {
    public static void deleteFile(File file) {
        try {
            if (file != null) {
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
