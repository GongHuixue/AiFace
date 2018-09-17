package android.com.aiface.ui.adapter;

import android.com.aiface.ui.base.BaseFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;

    public CommonFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size():0;
    }
}
