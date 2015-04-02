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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CpuInfo {
    public enum ProcessorFamily {
        INTEL, ARM, UNKNOWN
    }

    private static final Map<String, List<String>> info = findInfo();

    public static Map<String, List<String>> getInfo() {
        return info;
    }

    public static ProcessorFamily getProcessorFamily() {
        ProcessorFamily res = familyOf(getInfo("Processor"));
        if (res == ProcessorFamily.UNKNOWN) {
            res = familyOf(getInfo("vendor_id"));
        }
        return res;
    }

    public static String getInfo(String key) {
        final List<String> values = info.get(key);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }

    private static ProcessorFamily familyOf(String value) {
        if (value != null && value.contains("ARM")) {
            return ProcessorFamily.ARM;
        }
        if (value != null && value.contains("Intel")) {
            return ProcessorFamily.INTEL;
        }
        return ProcessorFamily.UNKNOWN;
    }

    private static String run(String... cmd) {
        String result = "";
        BufferedReader in = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.waitFor();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (in.ready()) {
                result += in.readLine() + "\n";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.close(in);
        }
        return result;
    }

    private static Map<String, List<String>> findInfo() {
        final String raw = run("/system/bin/cat", "/proc/cpuinfo");
        final String[] lines = raw.split("\n");
        final Map<String, List<String>> info = new LinkedHashMap<>();
        for (String line : lines) {
            if (line.length() > 0) {
                int pos = line.indexOf(':');
                String key, value;
                if (pos < 0) {
                    key = line;
                    value = "";
                } else {
                    key = line.substring(0, pos).trim();
                    value = pos == line.length() - 1 ? "" : line.substring(pos + 1).trim();
                }
                List<String> old = info.get(key);
                if (old == null) {
                    old = new ArrayList<>();
                    info.put(key, old);
                }
                old.add(value);
            }
        }
        return info;
    }
}