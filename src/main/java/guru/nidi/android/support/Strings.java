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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static guru.nidi.android.ApplicationContextHolder.string;

/**
 *
 */
public class Strings {
    public static final Strings EMPTY = new Strings(Collections.<String>emptyList());
    private final List<String> ls;

    public Strings() {
        this(new ArrayList<String>());
    }

    private Strings(List<String> ls) {
        this.ls = ls;
    }

    public Strings addIf(boolean cond, String s) {
        if (cond) {
            ls.add(s);
        }
        return this;
    }

    public Strings addIf(boolean cond, int resId) {
        return addIf(cond, string(resId));
    }

    public String join(String delim) {
        String res = "";
        for (String s : ls) {
            res += delim + s;
        }
        return res.length() == 0 ? res : res.substring(delim.length());
    }

    public boolean isEmpty() {
        return ls.isEmpty();
    }
}
