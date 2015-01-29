package guru.nidi.android.log;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class SimpleLogFactory {
    public static SimpleLog getLog(Class<?> name) {
        return getLog(name.getName());
    }

    public static SimpleLog getLog(String name) {
        File file = createFile();
        final AndroidSimpleLog androidSimpleLog = new AndroidSimpleLog(name);
        if (file == null) {
            return androidSimpleLog;
        }
        return new FileSimpleLog(file, name, androidSimpleLog);
    }

    private static File createFile() {
        File file = new File("sdcard/simple-log.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file.canWrite()) {
                file = null;
            }
        } catch (IOException e) {
            file = null;
        }
        return file;
    }
}
