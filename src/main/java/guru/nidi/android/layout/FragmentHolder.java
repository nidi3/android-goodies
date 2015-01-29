package guru.nidi.android.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class FragmentHolder {
    private static final Map<Class<?>, List<Field>> fragmentFields = new HashMap<>();
    private final FragmentActivity activity;

    public FragmentHolder(FragmentActivity activity) {
        this.activity = activity;
        List<Field> fields = getFields();
        for (Field f : getClass().getDeclaredFields()) {
            if (Fragment.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                fields.add(f);
            }
        }
    }

    private List<Field> getFields() {
        List<Field> fields = fragmentFields.get(getClass());
        if (fields == null) {
            fields = new ArrayList<>();
            fragmentFields.put(getClass(), fields);
        }
        return fields;
    }

    public void init(Bundle bundle) {
        try {
            if (bundle == null) {
                for (Field f : getFields()) {
                    f.set(this, f.getType().newInstance());
                }
            } else {
                for (Field f : getFields()) {
                    f.set(this, activity.getSupportFragmentManager().getFragment(bundle, f.getName()));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Bundle bundle) {
        try {
            for (Field f : getFields()) {
                activity.getSupportFragmentManager().putFragment(bundle, f.getName(), (Fragment) f.get(this));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
