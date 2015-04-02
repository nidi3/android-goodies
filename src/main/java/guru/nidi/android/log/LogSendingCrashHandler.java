/*
 * Copyright (C) 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
