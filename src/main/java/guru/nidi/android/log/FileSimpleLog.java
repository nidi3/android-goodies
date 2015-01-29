package guru.nidi.android.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
class FileSimpleLog implements SimpleLog {
    private final File file;
    private final String name;
    private final SimpleLog fallback;

    public FileSimpleLog(File file, String name, SimpleLog fallback) {
        this.file = file;
        this.name = name;
        this.fallback = fallback;
    }

    public void log(String level, String text) {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            out.append(new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date()));
            out.append(" [");
            out.append(level);
            out.append("] ");
            out.append(name);
            out.append(" ");
            out.append(text);
            out.newLine();
            out.flush();
            out.close();
        } catch (IOException e) {
            fallback.warn("Could not log into file: " + e.getMessage());
        }
    }

    @Override
    public void debug(String text) {
        log("DEBUG", text);
    }

    @Override
    public void info(String text) {
        log("INFO ", text);
    }

    @Override
    public void warn(String text) {
        log("WARN ", text);
    }

    @Override
    public void error(String text) {
        log("ERROR", text);
    }
}
