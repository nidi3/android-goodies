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
