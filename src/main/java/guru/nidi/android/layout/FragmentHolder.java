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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 */
public class FragmentHolder {
    private final List<Field> allFields = new ArrayList<>();
    private final List<Field> fields = new ArrayList<>();
    private final FragmentActivity activity;
    private final Set<String> ignoreSet = new HashSet<>();

    public FragmentHolder(FragmentActivity activity) {
        this.activity = activity;
        for (Field f : getClass().getDeclaredFields()) {
            if (Fragment.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                allFields.add(f);
            }
        }
        fields.addAll(allFields);
    }

    public void ignore(String... ignores) {
        if (ignores.length == 0) {
            ignoreSet.clear();
        } else {
            ignoreSet.addAll(Arrays.asList(ignores));
        }
        fields.clear();
        for (Field f : allFields) {
            if (!ignoreSet.contains(f.getName())) {
                fields.add(f);
            }
        }
    }

    public List<Fragment> getFragments(Fragment... fragments) {
        final List<Fragment> res = new ArrayList<>();
        for (int i = 0; i < fragments.length; i++) {
            if (hasFragment(fragments[i])) {
                res.add(fragments[i]);
            }
        }
        return res;
    }

    public void init(Bundle bundle) {
        try {
            if (bundle == null) {
                for (Field f : fields) {
                    f.set(this, f.getType().newInstance());
                }
            } else {
                for (Field f : fields) {
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
            for (Field f : fields) {
                activity.getSupportFragmentManager().putFragment(bundle, f.getName(), (Fragment) f.get(this));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            //TODO this can happen: Fragment ChatFragment{4250f830} is not currently in the FragmentManager
            //for the moment just swallow it
        }
    }

    private boolean hasFragment(Fragment fragment) {
        try {
            for (Field f : fields) {
                if (f.get(this) == fragment) {
                    return true;
                }
            }
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
