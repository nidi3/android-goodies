package guru.nidi.android.support;

import android.content.SharedPreferences;
import guru.nidi.android.ApplicationContextHolder;
import org.json.JSONArray;
import org.json.JSONException;
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
        try {
            return new JSONArray(pref.getString(key, "[]"));
        } catch (JSONException e) {
            throw new AssertionError(e);
        }
    }

    protected JSONObject getObject(String key) {
        try {
            return new JSONObject(pref.getString(key, "{}"));
        } catch (JSONException e) {
            throw new AssertionError(e);
        }
    }

    protected interface Setter {
        void set(SharedPreferences.Editor editor);
    }

    protected void set(Setter setter) {
        final SharedPreferences.Editor edit = pref.edit();
        setter.set(edit);
        edit.apply();
    }

    protected JSONObject put(JSONObject obj, String key, Object value) {
        try {
            return obj.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected JSONObject ofString(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
