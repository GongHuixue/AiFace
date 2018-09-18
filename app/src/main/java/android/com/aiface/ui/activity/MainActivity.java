package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.ui.adapter.CommonFragmentPagerAdapter;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.fragment.FragmentFactory;
import android.com.aiface.ui.presenter.MainPresenter;
import android.com.aiface.ui.view.IMainView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<IMainView, MainPresenter> implements ViewPager.OnPageChangeListener, IMainView {
    private final static String TAG = MainActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CommonFragmentPagerAdapter mAdapter;
    private List<BaseFragment> mFragmentList = new ArrayList<>(3);
    private int[] TAB_TITLES = new int[] {R.string.collect, R.string.detect, R.string.me};
    private int[] TAG_IMGS = new int[] {R.drawable.tab_collect_seletor, R.drawable.tab_detect_seletor, R.drawable.tab_me_seletor};

    @Override
    public void initView() {
        mViewPager = (ViewPager)findViewById(R.id.viewpager_container);
        mTabLayout = (TabLayout)findViewById(R.id.bottom_tablayout);
        setTabItems(mTabLayout, this.getLayoutInflater(), TAB_TITLES, TAG_IMGS);

        mFragmentList.add(FragmentFactory.getInstance().getCollectFragment());
        mFragmentList.add(FragmentFactory.getInstance().getDetectFragment());
        mFragmentList.add(FragmentFactory.getInstance().getMeFragment());

        mAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
    }

    private void setTabItems(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitles, int[] tabImages) {
        if((tabTitles.length > 0) && (tabImages.length >0)) {
            for (int i = 0; i < tabTitles.length; i++) {
                TabLayout.Tab tab = tabLayout.newTab();
                View view = inflater.inflate(R.layout.bottom_tab_item, null);
                tab.setCustomView(view);

                ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_top);
                ivIcon.setImageResource(tabImages[i]);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_bottom);
                tvTitle.setText(tabTitles[i]);
                if(i == 0) {
                    tabLayout.addTab(tab, true);
                }else {
                    tabLayout.addTab(tab, false);
                }
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initMainView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
