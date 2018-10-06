package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.GatePresenter;
import android.com.aiface.ui.view.IGateView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GateAccessActivity extends BaseActivity<IGateView, GatePresenter> implements IGateView {

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
    protected GatePresenter createPresenter() {
        return null;
    }


}
