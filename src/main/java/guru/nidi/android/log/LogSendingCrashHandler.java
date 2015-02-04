package guru.nidi.android.log;

/**
 *
 */
public class LogSendingCrashHandler implements Thread.UncaughtExceptionHandler {
    private final LogSender sender;
    private final Thread.UncaughtExceptionHandler oldHandler;

    public static void install(LogSender sender) {
        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof LogSendingCrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new LogSendingCrashHandler(sender, oldHandler));
        }
    }

    private LogSendingCrashHandler(LogSender sender, Thread.UncaughtExceptionHandler oldHandler) {
        this.sender = sender;
        this.oldHandler = oldHandler;
        sender.send();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace(System.err);
        sender.log("Application crash", ex);
        if (oldHandler != null) {
            oldHandler.uncaughtException(thread, ex);
        }
    }
}
