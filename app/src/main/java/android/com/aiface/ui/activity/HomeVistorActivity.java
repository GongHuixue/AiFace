package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeVistorActivity extends BaseActivity<IHomeView, HomePresenter> implements IHomeView {

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_signed;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected HomePresenter createPresenter() {
        return null;
    }

    @Override
    public void getHostInformation() {

    }
}
