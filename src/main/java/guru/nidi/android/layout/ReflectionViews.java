package guru.nidi.android.layout;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import guru.nidi.android.ApplicationContextHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReflectionViews extends Views {
    private final static Map<String, Integer> IDS = new HashMap<>();
    private final static Map<Class<?>, List<Field>> FIELDS = new HashMap<>();

    public ReflectionViews(Activity activity, int layout) {
        activity.setContentView(layout);

        try {
            for (Field field : getViewFields()) {
                field.set(this, getView(activity, field.getName()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot set view field", e);
        }
    }

    public ReflectionViews(Dialog dialog, int layout) {
        dialog.setContentView(layout);

        try {
            for (Field field : getViewFields()) {
                field.set(this, getView(dialog, field.getName()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot set view field", e);
        }
    }

    public ReflectionViews(View view) {
        try {
            for (Field field : getViewFields()) {
                field.set(this, getView(view, field.getName()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot set view field", e);
        }
    }

    private View getView(Activity activity, String name) {
        final int id = getId(name);
        final View v = activity.findViewById(id);
        if (v == null) {
            throw new RuntimeException("Could not find view '" + name + "'");
        }
        return v;
    }

    private View getView(Dialog dialog, String name) {
        final int id = getId(name);
        final View v = dialog.findViewById(id);
        if (v == null) {
            throw new RuntimeException("Could not find view '" + name + "'");
        }
        return v;
    }

    private View getView(View view, String name) {
        final int id = getId(name);
        final View v = view.findViewById(id);
        if (v == null) {
            throw new RuntimeException("Could not find view '" + name + "'");
        }
        return v;
    }

    private int getId(String name) {
        if (IDS.isEmpty()) {
            try {
                final Class<?> idResClass = Class.forName(ApplicationContextHolder.basePackage() + ".R$id");
                for (Field f : idResClass.getDeclaredFields()) {
                    f.setAccessible(true);
                    IDS.put(f.getName(), f.getInt(null));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot read R.id", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Cannot open class R", e);
            }
        }
        final Integer id = IDS.get(name);
        if (id == null) {
            throw new RuntimeException("Could not find id '" + name + "' in R.id");
        }
        return id;
    }

    private List<Field> getViewFields() {
        List<Field> fields = FIELDS.get(getClass());
        if (fields == null) {
            fields = new ArrayList<>();
            FIELDS.put(getClass(), fields);
            for (Field field : getClass().getDeclaredFields()) {
                if (View.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }
        return fields;
    }
}
