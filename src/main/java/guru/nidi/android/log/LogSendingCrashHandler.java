package guru.nidi.android.log;

/**
 *
 */
public class LogSendingCrashHandler implements Thread.UncaughtExceptionHandler {
    private final Thread.UncaughtExceptionHandler oldHandler;
    private final SavingLog log;

    public static void install() {
        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof LogSendingCrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new LogSendingCrashHandler(oldHandler));
        }
    }

    private LogSendingCrashHandler(Thread.UncaughtExceptionHandler oldHandler) {
        this.oldHandler = oldHandler;
        log = new SavingLog();
        LogSender.send(log);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        log.log("Application crash", ex);
        if (oldHandler != null) {
            oldHandler.uncaughtException(thread, ex);
        }
    }
}
