package guru.nidi.android.support;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class JsonUtils {
    private JsonUtils() {
    }

    public static JSONArray stringsToArray(List<String> ss) {
        final JSONArray res = new JSONArray();
        for (String s : ss) {
            res.put(s);
        }
        return res;
    }

    public static List<String> arrayToStrings(JSONArray json) {
        if (json == null) {
            return new ArrayList<>();
        }
        final List<String> res = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            res.add(json.optString(i));
        }
        return res;
    }

    public static JSONArray jsonToArray(String json) {
        try {
            return (json == null || json.length() == 0) ? new JSONArray() : new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException("Illegal json: " + json, e);
        }
    }

    public static JSONObject jsonToObject(String json) {
        try {
            return json == null ? new JSONObject() : new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException("Illegal json: " + json, e);
        }
    }

    public static JSONObject put(JSONObject obj, String key, Object value) {
        try {
            return obj.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Serializer<S, T> {
        T serialize(S obj) throws JSONException;
    }

    public static <T> JSONArray toArray(List<T> list, Serializer<T, ?> serializer) {
        try {
            final JSONArray res = new JSONArray();
            for (T obj : list) {
                res.put(serializer.serialize(obj));
            }
            return res;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <S, T> T toObject(S obj, Serializer<S, T> serializer) {
        if (obj == null) {
            return null;
        }
        try {
            return serializer.serialize(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public interface ArrayDeserializer<T> {
        T deserialize(JSONArray array, int index) throws JSONException;
    }

    public interface ObjectDeserializer<T> {
        T deserialize(JSONObject obj) throws JSONException;
    }

    public static <T> List<T> fromArray(JSONArray array, ArrayDeserializer<T> deserializer) {
        try {
            final List<T> res = new ArrayList<>();
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    res.add(deserializer.deserialize(array, i));
                }
            }
            return res;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> fromArray(JSONArray array, ObjectDeserializer<T> deserializer) {
        try {
            final List<T> res = new ArrayList<>();
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    final JSONObject obj = array.optJSONObject(i);
                    if (obj != null) {
                        res.add(deserializer.deserialize(obj));
                    }
                }
            }
            return res;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(JSONObject obj, ObjectDeserializer<T> deserializer) {
        if (obj == null) {
            return null;
        }
        try {
            return deserializer.deserialize(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fromDate(Date date, String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public static Date toDate(String date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static <T extends Enum<T>> T toEnum(String name, Class<T> clazz) {
        return name == null || name.length() == 0 ? null : Enum.valueOf(clazz, name);
    }

    public static <T extends Enum<T>> T toEnum(String name, T defaultValue) {
        return name == null || name.length() == 0 ? defaultValue : (T) Enum.valueOf((Class<Enum>) defaultValue.getClass().getEnclosingClass(), name);
    }

    public static String fromEnum(Enum<?> e) {
        return e == null ? null : e.name();
    }

}
