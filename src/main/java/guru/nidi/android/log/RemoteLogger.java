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
class RemoteLogger implements Runnable {
    private final String url;
    private final CrashHandlerPersister persister;
    private final AndroidHttpClient client;

    public RemoteLogger(String url) {
        this.url = url;
        persister = new CrashHandlerPersister();
        client = AndroidHttpClient.newInstance(System.getProperty("http.agent"));
        HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
    }

    @Override
    public void run() {
        final HttpPost post = new HttpPost(url());
        for (; ; ) {
            try {
                post.setEntity(new StringEntity(persister.getCrashes()));
                final HttpResponse response = client.execute(post);
                final StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    throw new IOException("Received response code " + status.getStatusCode() + " " + status.getReasonPhrase());
                }
                persister.clearCrashes();
                client.close();
                return;
            } catch (IOException e) {
                persister.addSendTry(e);
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
