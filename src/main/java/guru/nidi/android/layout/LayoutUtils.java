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

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import guru.nidi.android.ApplicationContextHolder;
import guru.nidi.android.view.SimplePagerAdapter;
import guru.nidi.android.view.SlidingTabLayout;

import java.lang.reflect.Field;

/**
 *
 */
public class LayoutUtils {
    private LayoutUtils() {
    }

    public static int getRelativeLeft(View view) {
        return view.getLeft() + ((view.getParent() == view.getRootView())
                ? 0
                : getRelativeLeft((View) view.getParent()));
    }

    public static int getRelativeTop(View view) {
        return view.getTop() + ((view.getParent() == view.getRootView())
                ? 0
                : getRelativeTop((View) view.getParent()));
    }

    public static FrameLayout.LayoutParams positionAtTopRight(View view, View target) {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.leftMargin = target.getWidth() - target.getPaddingRight() + getRelativeLeft(target) - getRelativeLeft((View) view.getParent());
        params.topMargin = target.getPaddingTop() + getRelativeTop(target) - getRelativeTop((View) view.getParent()) - view.getHeight() / 2;
        return params;
    }

    public static View titleViewOfFragment(SlidingTabLayout slider, Fragment fragment) {
        final ViewGroup titles = (ViewGroup) slider.getChildAt(0);
        final SimplePagerAdapter adapter = (SimplePagerAdapter) slider.getViewPager().getAdapter();
        return titles.getChildAt(adapter.indexOf(fragment));
    }

    public static boolean isPortrait(Activity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static View currentFragmentRootView(ViewPager pager) {
        return ((ViewGroup) currentFragment(pager).getView()).getChildAt(0);
    }

    public static Fragment currentFragment(ViewPager pager) {
        return ((FragmentPagerAdapter) pager.getAdapter()).getItem(pager.getCurrentItem());
    }

    public static int getMinHeight(View view) {
        try {
            final Field mMinHeight = View.class.getDeclaredField("mMinHeight");
            mMinHeight.setAccessible(true);
            return (Integer) mMinHeight.get(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationContextHolder.displayMetrics());
    }

    public static int spToPixel(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ApplicationContextHolder.displayMetrics());
    }

    public static int attrOfStyle(int style, int attr) {
        TypedArray a = ApplicationContextHolder.context().obtainStyledAttributes(style, new int[]{attr});
        int size = a.getColor(0, -1);
        a.recycle();
        return size;
    }

}
