package guru.nidi.android.support;

import android.content.SharedPreferences;
import guru.nidi.android.ApplicationContextHolder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class AbstractPersister {
    protected final SharedPreferences pref;

    protected AbstractPersister(String name) {
        pref = ApplicationContextHolder.preferences(name);
    }

    protected JSONArray getArray(String key) {
        return JsonUtils.jsonToArray(pref.getString(key, "[]"));
    }

    protected JSONObject getObject(String key) {
        return JsonUtils.jsonToObject(pref.getString(key, "{}"));
    }

    protected interface Setter {
        void set(SharedPreferences.Editor editor);
    }

    protected void set(Setter setter) {
        final SharedPreferences.Editor edit = pref.edit();
        setter.set(edit);
        edit.apply();
    }

}
