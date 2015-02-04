package guru.nidi.android.support;

import android.net.http.AndroidHttpClient;
import android.os.Build;
import guru.nidi.android.ApplicationContextHolder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;

/**
 *
 */
public class HttpConnector {
    private static HttpConnector defaultConnector;
    private final AndroidHttpClient client;

    public HttpConnector(int soTimeout, int connectionTimeout) {
        client = AndroidHttpClient.newInstance(userAgent());
        HttpConnectionParams.setSoTimeout(client.getParams(), soTimeout);
        HttpConnectionParams.setConnectionTimeout(client.getParams(), connectionTimeout);
    }

    public static HttpConnector setDefault(int soTimeout, int connectionTimeout) {
        return defaultConnector = new HttpConnector(soTimeout, connectionTimeout);
    }

    public static HttpConnector getDefault() {
        return defaultConnector;
    }

    public HttpResponse send(HttpRequestBase request) throws IOException {
        return client.execute(request);
    }

    private String userAgent() {
        StringBuilder result = new StringBuilder(64)
                .append("Dalvik/")
                .append(System.getProperty("java.vm.version"))
                .append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE;
        result.append(version.length() > 0 ? version : "1.0");

        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ").append(model);
            }
        }
        String id = Build.ID;
        if (id.length() > 0) {
            result.append(" Build/").append(id);
        }
        result.append(") ")
                .append(ApplicationContextHolder.basePackage())
                .append('/')
                .append(ApplicationContextHolder.appVersion());
        return result.toString();
    }

    public void close() {
        client.close();
    }

}
