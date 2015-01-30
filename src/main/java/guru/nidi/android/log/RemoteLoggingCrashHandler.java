package guru.nidi.android.log;

import android.os.Build;
import guru.nidi.android.ApplicationContextHolder;

import java.util.Date;

/**
 *
 */
public class RemoteLoggingCrashHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler oldHandler;
    private final String[] info;
    private final CrashHandlerPersister persister;

    public static void install(String url, String... info) {
        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof RemoteLoggingCrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new RemoteLoggingCrashHandler(oldHandler, url, info));
        }
    }

    private RemoteLoggingCrashHandler(Thread.UncaughtExceptionHandler oldHandler, String url, String[] info) {
        this.oldHandler = oldHandler;
        this.info = info;
        persister = new CrashHandlerPersister();
        if (persister.getCrashes().length() > 0) {
            new Thread(new RemoteLogger(url)).start();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        final String additionalInfo = persister.formatKeyValue(
                "Time", persister.formatDate(new Date()),
                "App-Version", ApplicationContextHolder.appVersion(), "Android-Version", Build.VERSION.RELEASE,
                "Manufacturer", Build.MANUFACTURER, "Model", Build.MODEL);
        persister.addCrash(additionalInfo + persister.formatKeyValue(info) + persister.formatThrowable(ex));
        if (oldHandler != null) {
            oldHandler.uncaughtException(thread, ex);
        }
    }
}
