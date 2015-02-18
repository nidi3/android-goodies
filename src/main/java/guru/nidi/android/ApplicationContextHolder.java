package guru.nidi.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 *
 */
public class ApplicationContextHolder {
    private static Context context;

    public static void init(Context context) {
        ApplicationContextHolder.context = context;
    }

    public static Context context() {
        return context;
    }

    public static String basePackage() {
        return context.getPackageName();
    }

    public static DisplayMetrics displayMetrics() {
        return context.getResources().getDisplayMetrics();
    }

    public static boolean possiblyInternet() {
        ConnectivityManager connectivityManager = service(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static <T> T service(String name) {
        return (T) context.getSystemService(name);
    }

    public static SharedPreferences preferences(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String string(int resource) {
        return context.getString(resource);
    }

    public static String string(int resource, Object... args) {
        return context.getString(resource, args);
    }

    public static int color(int resource) {
        return context.getResources().getColor(resource);
    }

    public static String appVersion() {
        try {
            return context.getPackageManager().getPackageInfo(basePackage(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }
}
