package guru.nidi.android.log;

import android.content.Context;
import android.content.SharedPreferences;
import guru.nidi.android.ApplicationContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class RemoteLoggingCrashHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler oldHandler;
    private final String[] info;
    private final SharedPreferences pref;

    public static void install(String url, String... info) {
        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof RemoteLoggingCrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new RemoteLoggingCrashHandler(oldHandler, ApplicationContextHolder.context(), url, info));
        }
    }

    private RemoteLoggingCrashHandler(Thread.UncaughtExceptionHandler oldHandler, Context context, String url, String... info) {
        this.oldHandler = oldHandler;
        this.info = info;
        pref = getPreferences(context);
        if (getCrashes(pref).length() > 0) {
            new Thread(new RemoteLogger(context, url)).start();
        }
    }

    static String getCrashes(Context context) {
        return getCrashes(getPreferences(context));
    }

    static void clearCrashes(Context context) {
        final SharedPreferences.Editor edit = getPreferences(context).edit();
        edit.clear();
        edit.commit();
    }

    private static String getCrashes(SharedPreferences pref) {
        return pref.getString("all", "");
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("crash-log", Context.MODE_PRIVATE);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        final String all = getCrashes(pref);
        final SharedPreferences.Editor edit = pref.edit();
        edit.putString("all", all + generalInfo() + infoString() + stringOf(ex) + "\n");
        edit.commit();
        oldHandler.uncaughtException(thread, ex);
    }

    private String generalInfo() {
        return "Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "\n";
    }

    private String infoString() {
        String s = "";
        for (int i = 0; i < info.length; i += 2) {
            s += info[i] + ": " + info[i + 1] + "\n";
        }
        return s;
    }

    private String stringOf(Throwable t) {
        String s = "Exception: " + t + " " + t.getMessage() + "\n";
        for (StackTraceElement e : t.getStackTrace()) {
            s += "    at " + e.toString() + "\n";
        }
        return s;
    }


}
