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

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class SimpleLogFactory {
    public static SimpleLog getLog(Class<?> name) {
        return getLog(name.getName());
    }

    public static SimpleLog getLog(String name) {
        File file = createFile();
        final AndroidSimpleLog androidSimpleLog = new AndroidSimpleLog(name);
        if (file == null) {
            return androidSimpleLog;
        }
        return new FileSimpleLog(file, name, androidSimpleLog);
    }

    private static File createFile() {
        File file = new File("sdcard/simple-log.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file.canWrite()) {
                file = null;
            }
        } catch (IOException e) {
            file = null;
        }
        return file;
    }
}
