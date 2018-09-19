package android.com.aiface.ui.base;

import android.com.aiface.R;
import android.com.aiface.settings.SettingManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment{
    protected T mPresenter;
    protected SettingManager mSettingManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        initView(rootview);
//        setImageText(rootview);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public void setImageText(View view) {
        TextView mTvMeeting, mTvAttendance, mTvHome, mTvGate;
        ImageView mIvMeeting, mIvAttendance, mIvHome, mIvGate;

        mIvMeeting = (ImageView) view.findViewById(R.id.iv_image);
        mIvMeeting.setImageResource(R.mipmap.meeting);
        mTvMeeting = (TextView)view.findViewById(R.id.tv_textview);
        mTvMeeting.setText(R.string.collect_detect_meeting);

        mIvAttendance = (ImageView) view.findViewById(R.id.iv_image);
        mIvAttendance.setImageResource(R.mipmap.attendance);
        mTvAttendance = (TextView)view.findViewById(R.id.tv_textview);
        mTvAttendance.setText(R.string.collect_detect_attendance);
    }

    public abstract int getLayoutId();

    public abstract void initView(View view);

    public abstract void initData();

    protected abstract T createPresenter();
}
