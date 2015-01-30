package guru.nidi.android.log;

import android.content.SharedPreferences;
import guru.nidi.android.support.AbstractPersister;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class CrashHandlerPersister extends AbstractPersister {
    private static final String KEY = "all";
    private static final int MAX_SIZE = 10000;

    public CrashHandlerPersister() {
        super("crash-log");
    }

    public String getCrashes() {
        return pref.getString(KEY, "");
    }

    public void addCrash(final String crash) {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                final String old = getCrashes();
                final String limited = old.substring(old.length() - Math.min(old.length(), MAX_SIZE));
                editor.putString(KEY, limited + crash + "\n");
            }
        });
    }

    public void addSendTry(Throwable t) {
        addCrash("Unsuccessful try to send at " + formatDate(new Date()) + "\n" + formatThrowable(t));
    }

    public void clearCrashes() {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                editor.clear();
            }
        });
    }

    public String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public String formatThrowable(Throwable t) {
        String s = "Exception: " + t + "\n";
        for (StackTraceElement e : t.getStackTrace()) {
            s += "    at " + e.toString() + "\n";
        }
        return s;
    }

    public String formatKeyValue(String... keyValues) {
        String s = "";
        for (int i = 0; i < keyValues.length; i += 2) {
            s += keyValues[i] + ": " + keyValues[i + 1] + "\n";
        }
        return s;
    }

}

