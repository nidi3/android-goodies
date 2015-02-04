package guru.nidi.android.log;

import android.net.http.AndroidHttpClient;
import guru.nidi.android.ApplicationContextHolder;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;

/**
 *
 */
public class LogSender implements Runnable {
    private static final AndroidHttpClient client;
    private static String defaultUrl;
    private static int defaultRetries;
    private final SavingLogger log;
    private final String url;
    private final int retries;

    static {
        client = AndroidHttpClient.newInstance(System.getProperty("http.agent"));
        HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
    }

    public static void setDefaults(String url, int retries) {
        defaultUrl = url;
        defaultRetries = retries;
    }

    public static void send(SavingLogger log) {
        new LogSender(log).send();
    }

    public LogSender(SavingLogger log) {
        this(log, defaultUrl, defaultRetries);
    }

    public LogSender(SavingLogger log, String url, int retries) {
        this.log = log;
        this.url = url;
        this.retries = retries;
    }

    public void send() {
        if (!log.getLogs().isEmpty()) {
            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        final HttpPost post = new HttpPost(url());
        int tries = 0;
        while (retries == 0 || tries < retries) {
            tries++;
            try {
                post.setEntity(new StringEntity(log.getLogs()));
                final HttpResponse response = client.execute(post);
                final StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException("Received response code " + status.getStatusCode() + " " + status.getReasonPhrase());
                }
                log.clearLogs();
                client.close();
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
