package guru.nidi.android.log;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;

/**
 *
 */
class RemoteLogger implements Runnable {
    private final Context context;
    private final String url;
    private final AndroidHttpClient client;

    public RemoteLogger(Context context, String url) {
        this.context = context;
        this.url = url;
        client = AndroidHttpClient.newInstance(System.getProperty("http.agent"));
        HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
    }

    @Override
    public void run() {
        final HttpPost post = new HttpPost(url);
        for (; ; ) {
            try {
                post.setEntity(new StringEntity(RemoteLoggingCrashHandler.getCrashes(context)));
                client.execute(post);
                RemoteLoggingCrashHandler.clearCrashes(context);
                client.close();
                return;
            } catch (IOException e) {
                try {
                    Thread.sleep(60 * 1000 * retryIntervalMinutes());
                } catch (InterruptedException e1) {
                    //ignore
                }
            }
        }
    }

    private int retryIntervalMinutes() {
        return (int) (60 + Math.random() * 60);
    }
}
