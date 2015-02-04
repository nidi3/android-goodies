package guru.nidi.android.log;

import android.content.SharedPreferences;
import android.os.Build;
import guru.nidi.android.ApplicationContextHolder;
import guru.nidi.android.support.AbstractPersister;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class SavingLogger extends AbstractPersister {
    private static final String KEY = "all";
    private static final int MAX_SIZE = 10000;

    public SavingLogger() {
        super("crash-log");
    }

    String getLogs() {
        return pref.getString(KEY, "");
    }

    void clearLogs() {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                editor.clear();
            }
        });
    }

    public SavingLogger log(final String message) {
        return log(message, null);
    }

    public SavingLogger log(final String message, final Throwable t) {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                final String old = getLogs();
                final String limited = old.substring(old.length() - Math.min(old.length(), MAX_SIZE));
                final String info = formatKeyValue(
                        "App-Version", ApplicationContextHolder.appVersion(),
                        "Android-Version", Build.VERSION.RELEASE,
                        "Manufacturer", Build.MANUFACTURER,
                        "Model", Build.MODEL);
                editor.putString(KEY, limited +
                        formatDate(new Date()) + message + "\n" +
                        info + (t == null ? "" : formatThrowable(t) + "\n"));
            }
        });
        return this;
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm ").format(date);
    }

    private String formatThrowable(Throwable t) {
        String s = "Exception: " + t + "\n";
        for (StackTraceElement e : t.getStackTrace()) {
            s += "    at " + e.toString() + "\n";
        }
        return s;
    }

    private String formatKeyValue(String... keyValues) {
        String s = "";
        for (int i = 0; i < keyValues.length; i += 2) {
            s += keyValues[i] + ": " + keyValues[i + 1] + "\n";
        }
        return s;
    }

}

