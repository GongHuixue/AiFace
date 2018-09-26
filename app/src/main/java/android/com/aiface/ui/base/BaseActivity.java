package android.com.aiface.ui.base;

import android.com.aiface.database.GreenDaoManager;
import android.com.aiface.settings.SettingManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;
    protected SettingManager mSettingManager;
    protected GreenDaoManager greenDaoManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if(mPresenter != null) {
            mPresenter.attachView((V)this);
        }
        mSettingManager = SettingManager.getSmInstance();

        setContentView(getLayoutId());
        initCommonPart();
        initView();
        initData();
    }

    private void initCommonPart() {
        greenDaoManager = GreenDaoManager.getSingleInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();

    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
