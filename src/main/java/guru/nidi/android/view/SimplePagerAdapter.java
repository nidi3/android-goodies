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
