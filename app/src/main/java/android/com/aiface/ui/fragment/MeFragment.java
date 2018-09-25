package android.com.aiface.ui.fragment;

import android.com.aiface.R;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.MePresenter;
import android.com.aiface.ui.view.IMeView;
import android.view.View;

public class MeFragment extends BaseFragment<IMeView, MePresenter> implements IMeView{

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter((MainActivity)getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.collect_or_detect_view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initMeView() {

    }
}
