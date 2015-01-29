package guru.nidi.android.layout;

import android.app.Activity;
import android.view.View;

/**
 *
 */
public class Views {
    public <T> T findView(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public <T> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

}
