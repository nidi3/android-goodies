package guru.nidi.android.support;

import java.io.*;

/**
 *
 */
public class IoUtils {
    private IoUtils() {
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            final File[] files = f.listFiles();
            if (files != null) {
                for (File c : files) {
                    delete(c);
                }
            }
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        final byte[] buf = new byte[10000];
        int read;
        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }
    }
}
