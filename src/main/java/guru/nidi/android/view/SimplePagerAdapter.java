package guru.nidi.android.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {
    private final Fragment[] fragments;

    public SimplePagerAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final Fragment fragment = fragments[position];
        return fragment instanceof Named ? ((Named) fragment).getName() : "";
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    public int indexOf(Fragment fragment) {
        for (int i = 0; i < fragments.length; i++) {
            if (fragments[i] == fragment) {
                return i;
            }
        }
        return -1;
    }
}
