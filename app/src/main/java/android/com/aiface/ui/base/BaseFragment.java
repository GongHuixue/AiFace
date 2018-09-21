package android.com.aiface.ui.base;

import android.com.aiface.R;
import android.com.aiface.settings.SettingManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment{
    private final static String TAG = BaseFragment.class.getSimpleName();

    protected T mPresenter;
    protected SettingManager mSettingManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mPresenter = createPresenter();

        if(mPresenter != null) {
            mPresenter.attachView((V)this);
        }
        mSettingManager = SettingManager.getSmInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(getLayoutId(), container, false);
        Log.d(TAG, "onCreateView");
        initView(rootview);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public abstract int getLayoutId();

    public abstract void initView(View view);

    public abstract void initData();

    protected abstract T createPresenter();
}
