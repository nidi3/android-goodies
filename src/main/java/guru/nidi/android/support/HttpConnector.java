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
package guru.nidi.android.support;

import android.net.http.AndroidHttpClient;
import android.os.Build;
import guru.nidi.android.ApplicationContextHolder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

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

    public static void close(HttpResponse response) {
        if (response != null && response.getEntity() != null) {
            try {
                response.getEntity().consumeContent();
            } catch (IOException e) {
                //give up
            }
        }
    }

    public static String toString(HttpResponse response) throws IOException {
        try {
            return EntityUtils.toString(response.getEntity());
        } finally {
            close(response);
        }
    }

    public static byte[] toByteArray(HttpResponse response) throws IOException {
        try {
            return EntityUtils.toByteArray(response.getEntity());
        } finally {
            close(response);
        }
    }

    public HttpResponse send(HttpRequestBase request) throws IOException {
        return client.execute(request);
    }

    public HttpResponse sendAndClose(HttpRequestBase request) throws IOException {
        HttpResponse response = null;
        try {
            response = send(request);
            return response;
        } finally {
            close(response);
        }
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
