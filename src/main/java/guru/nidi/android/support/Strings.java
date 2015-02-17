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
