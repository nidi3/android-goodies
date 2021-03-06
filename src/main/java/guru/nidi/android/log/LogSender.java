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

import guru.nidi.android.ApplicationContextHolder;
import guru.nidi.android.support.HttpConnector;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 *
 */
public class LogSender implements Runnable {
    private static LogSender defaultSender;
    private final SavingLog log;
    private final HttpConnector connector;
    private final String url;
    private final int retries;

    public static LogSender setDefault(SavingLog log, HttpConnector connector, String url, int retries) {
        return defaultSender = new LogSender(log, connector, url, retries);
    }

    public static LogSender getDefault() {
        return defaultSender;
    }

    public LogSender(SavingLog log, HttpConnector connector, String url, int retries) {
        this.log = log;
        this.connector = connector;
        this.url = url;
        this.retries = retries;
    }

    public void send() {
        if (!log.getLogs().isEmpty()) {
            new Thread(this).start();
        }
    }

    public LogSender log(String message) {
        log.log(message);
        return this;
    }

    public LogSender log(String message, Throwable t) {
        log.log(message, t);
        return this;
    }

    public static void logAndSend(String message) {
        getDefault().log(message).send();
    }

    public static void logAndSend(String message, Throwable t) {
        getDefault().log(message, t).send();
    }

    @Override
    public void run() {
        final HttpPost post = new HttpPost(url());
        int tries = 0;
        while (retries == 0 || tries < retries) {
            tries++;
            try {
                post.setEntity(new StringEntity(log.getLogs()));
                final HttpResponse response = connector.sendAndClose(post);
                final StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException("Received response code " + status.getStatusCode() + " " + status.getReasonPhrase());
                }
                log.clearLogs();
                return;
            } catch (IOException e) {
                log.log("Unsuccessful try to send", e);
                try {
                    Thread.sleep(60 * 1000 * retryIntervalMinutes());
                } catch (InterruptedException ie) {
                    //ignore
                }
            }
        }
    }

    private String url() {
        return url + (url.contains("?") ? "&" : "?") + "app=" + ApplicationContextHolder.basePackage();
    }

    private int retryIntervalMinutes() {
        return (int) (60 + Math.random() * 60);
    }
}
