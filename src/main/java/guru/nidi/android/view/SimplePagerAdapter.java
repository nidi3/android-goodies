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
package guru.nidi.android.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;

    public SimplePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public SimplePagerAdapter(FragmentManager fm, Fragment... fragments) {
        this(fm, Arrays.asList(fragments));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final Fragment fragment = fragments.get(position);
        return fragment instanceof Named ? ((Named) fragment).getName() : "";
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int indexOf(Fragment fragment) {
        return fragments.indexOf(fragment);
    }
}
