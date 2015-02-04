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
            HttpResponse response = null;
            try {
                post.setEntity(new StringEntity(log.getLogs()));
                response = connector.send(post);
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
            } finally {
                if (response != null && response.getEntity() != null) {
                    try {
                        response.getEntity().consumeContent();
                    } catch (IOException e) {
                        //give up
                    }
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
