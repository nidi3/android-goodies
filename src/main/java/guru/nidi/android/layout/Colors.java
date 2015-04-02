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
package guru.nidi.android.layout;

/**
 *
 */
public class Colors {
    private Colors() {
    }

    public static int blend(int base, int color) {
        float alpha = ((color >> 24) & 0xff) / 255f;
        return ((base >> 24) << 24) + (sat(r(base) + (int) (alpha * r(color))) << 16)
                + (sat(g(base) + (int) (alpha * g(color))) << 8)
                + (sat(b(base) + (int) (alpha * b(color))));
    }

    public static int lightness(int color, float delta) {
        return ((color >> 24) << 24) + (sat((int) (r(color) * delta)) << 16)
                + (sat((int) (g(color) * delta)) << 8)
                + (sat((int) (b(color) * delta)));
    }

    public static int withAlpha(int color, int alpha) {
        return ((alpha & 0xff) << 24) + (color & 0xffffff);
    }

    private static int r(int v) {
        return (v >> 16) & 0xff;
    }

    private static int g(int v) {
        return (v >> 8) & 0xff;
    }

    private static int b(int v) {
        return v & 0xff;
    }

    private static int sat(int v) {
        return v > 0xff ? 0xff : v;
    }

}
