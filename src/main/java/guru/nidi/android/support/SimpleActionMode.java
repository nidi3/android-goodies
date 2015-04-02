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

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SimpleActionMode<T> {
    private ListView view;
    private ActionMode mode;
    private final int menuId;

    public SimpleActionMode(int menuId) {
        this.menuId = menuId;
    }

    public boolean isActivated() {
        return mode != null;
    }

    public void registerOnClick(final Activity activity, final ListView view, final Callback<T> callback) {
        this.view = view;
        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                if (isActivated()) {
                    return false;
                }
                if (!callback.acceptSelection(mode, getItemAt(position))) {
                    return false;
                }
                view.setItemChecked(position, true);
                callback.actionMode = SimpleActionMode.this;
                mode = activity.startActionMode(callback);
                return true;
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (isActivated()) {
                    if (!callback.acceptSelection(mode, getItemAt(position))) {
                        view.setItemChecked(position, !view.isItemChecked(position));
                    }
                } else {
                    callback.onItemClick(parent, v, position, id);
                }
            }
        });
    }

    public T getItemAt(int position) {
        return (T) view.getItemAtPosition(position);
    }

    public List<T> getSelection() {
        final List<T> res = new ArrayList<>();
        final SparseBooleanArray pos = view.getCheckedItemPositions();
        for (int i = 0; i < pos.size(); i++) {
            final int key = pos.keyAt(i);
            if (pos.valueAt(key)) {
                res.add(getItemAt(key));
            }
        }
        return res;
    }

    public void clearSelection() {
        for (int i = 0; i < view.getAdapter().getCount(); i++) {
            view.setItemChecked(i, false);
        }
    }

    public static class Callback<T> implements ActionMode.Callback {
        private SimpleActionMode<T> actionMode;

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item, List<T> selection) {
            return false;
        }

        public boolean acceptSelection(ActionMode mode, T selection) {
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(actionMode.menuId, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return onActionItemClicked(mode, item, actionMode.getSelection());
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode.mode = null;
            actionMode.clearSelection();
        }
    }
}