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
