package guru.nidi.android.log;

import android.util.Log;

/**
 *
 */
class AndroidSimpleLog implements SimpleLog {
    private final String name;

    public AndroidSimpleLog(String name) {
        this.name = name;
    }

    @Override
    public void debug(String text) {
        Log.d(name, text);
    }

    @Override
    public void info(String text) {
        Log.i(name, text);
    }

    @Override
    public void warn(String text) {
        Log.w(name, text);
    }

    @Override
    public void error(String text) {
        Log.e(name, text);
    }
}
