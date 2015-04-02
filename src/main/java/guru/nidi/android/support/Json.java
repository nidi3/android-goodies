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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * fromXxx means serializing into JSON.
 * toXxx means deserializing into Java object.
 */
public class Json {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public interface Serializer<S, T> {
        T serialize(S obj) throws JSONException;
    }

    public interface ArrayDeserializer<T> {
        T deserialize(JSONArray array, int index) throws JSONException;
    }

    public interface ObjectDeserializer<T> {
        T deserialize(JSONObject obj) throws JSONException;
    }

    private Json() {
    }

    public static JSONObject put(JSONObject obj, String key, Object value) {
        try {
            return obj.put(key, value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONArray fromStringList(List<String> ss) {
        final JSONArray res = new JSONArray();
        for (String s : ss) {
            res.put(s);
        }
        return res;
    }

    public static List<String> toStringList(JSONArray json) {
        if (json == null) {
            return new ArrayList<>();
        }
        final List<String> res = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            res.add(json.optString(i));
        }
        return res;
    }

    public static JSONObject fromMap(Map<String, Object> map) {
        if (map == null) {
            return new JSONObject();
        }
        try {
            final JSONObject res = new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                res.put(entry.getKey(), entry.getValue());
            }
            return res;
        } catch (JSONException e) {
            throw new RuntimeException("Non serializable map: " + map, e);
        }
    }

    public static Map<String, Object> toMap(JSONObject json) {
        if (json == null) {
            return new HashMap<>();
        }
        final Map<String, Object> res = new HashMap<>();
        final Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            final String key = keys.next();
            res.put(key, json.opt(key));
        }
        return res;
    }

    public static JSONArray arrayFromLiteral(String json) {
        try {
            return (json == null || json.length() == 0) ? new JSONArray() : new JSONArray(json);
        } catch (JSONException e) {
            throw new RuntimeException("Illegal json: " + json, e);
        }
    }

    public static JSONObject objectFromLiteral(String json) {
        try {
            return json == null ? new JSONObject() : new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException("Illegal json: " + json, e);
        }
    }

    public static <T> JSONArray fromList(List<T> list, Serializer<T, ?> serializer) {
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

    public static <S, T> T fromObject(S obj, Serializer<S, T> serializer) {
        if (obj == null) {
            return null;
        }
        try {
            return serializer.serialize(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(JSONArray array, ArrayDeserializer<T> deserializer) {
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

    public static <T> List<T> toList(JSONArray array, ObjectDeserializer<T> deserializer) {
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

    public static <T> T toObject(JSONObject obj, ObjectDeserializer<T> deserializer) {
        if (obj == null) {
            return null;
        }
        try {
            return deserializer.deserialize(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fromDate(Date date) {
        return fromDate(date, DATE_FORMAT);
    }

    public static String fromDate(Date date, String format) {
        return date == null ? null : new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public static Date toDate(String date) {
        return toDate(date, DATE_FORMAT);
    }

    public static Date toDate(String date, String format) {
        try {
            return date == null ? null : new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static <T extends Enum<T>> T toEnum(String name, Class<T> clazz) {
        return name == null || name.length() == 0 ? null : Enum.valueOf(clazz, name);
    }

    public static <T extends Enum<T>> T toEnum(String name, T defaultValue) {
        final Class<? extends Enum> baseClazz = defaultValue.getClass();
        final Class<? extends Enum> clazz = baseClazz.isEnum() ? baseClazz : (Class<? extends Enum>) baseClazz.getEnclosingClass();
        return name == null || name.length() == 0 ? defaultValue : (T) Enum.valueOf(clazz, name);
    }

    public static String fromEnum(Enum<?> e) {
        return e == null ? null : e.name();
    }

}
