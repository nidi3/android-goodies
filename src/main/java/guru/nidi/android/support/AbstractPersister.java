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
        return Json.arrayFromLiteral(pref.getString(key, "[]"));
    }

    protected JSONObject getObject(String key) {
        return Json.objectFromLiteral(pref.getString(key, "{}"));
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
