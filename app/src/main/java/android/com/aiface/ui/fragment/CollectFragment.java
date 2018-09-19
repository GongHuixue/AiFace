package android.com.aiface.ui.fragment;

import android.com.aiface.R;
import android.com.aiface.settings.AiFaceEnum.*;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.CollectPresenter;
import android.com.aiface.ui.view.ICollectView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectFragment extends BaseFragment<ICollectView, CollectPresenter> implements ICollectView, View.OnClickListener{
    private final static String TAG = CollectFragment.class.getSimpleName();
    private TextView mTvDescription;
    private View mMeetingll, mAttendancell, mHomell, mGatell;
    private CollectMode mCollectMode = CollectMode.COLLECT_NONE;
    private volatile int mSelectMode = -1;

    @Override
    protected CollectPresenter createPresenter() {
        return new CollectPresenter((MainActivity)getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.collect_or_detect_view;
    }

    @Override
    public void initData() {
        CollectMode collectMode = mSettingManager.getCollectMode();
        mSelectMode = changeCollectToInt(collectMode);
        Log.d(TAG, "initData current select mode = " + mSelectMode);

        updateFocusItem(mSelectMode);
    }

    @Override
    public void initView(View view) {
        TextView mTvMeeting, mTvAttendance, mTvHome, mTvGate;
        ImageView mIvMeeting, mIvAttendance, mIvHome, mIvGate;
        mTvDescription = (TextView)view.findViewById(R.id.tv_description);
        mTvDescription.setText(R.string.collect_title);

        mMeetingll = view.findViewById(R.id.it_meeting);
        mIvMeeting = (ImageView)mMeetingll.findViewById(R.id.iv_image);
        mTvMeeting = (TextView)mMeetingll.findViewById(R.id.tv_textview);
        mTvMeeting.setText(R.string.collect_detect_meeting);
        mIvMeeting.setImageResource(R.mipmap.meeting);
        mMeetingll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("huixue", "xxxxx");
            }
        });

        mAttendancell = view.findViewById(R.id.it_attendance);
        mIvAttendance = (ImageView)mAttendancell.findViewById(R.id.iv_image);
        mTvAttendance = (TextView)mAttendancell.findViewById(R.id.tv_textview);
        mTvAttendance.setText(R.string.collect_detect_attendance);
        mIvAttendance.setImageResource(R.mipmap.attendance);
        mAttendancell.setOnClickListener(this);

        mHomell = view.findViewById(R.id.it_home);
        mIvHome = (ImageView)mHomell.findViewById(R.id.iv_image);
        mTvHome = (TextView)mHomell.findViewById(R.id.tv_textview);
        mTvHome.setText(R.string.collect_detect_home);
        mIvHome.setImageResource(R.mipmap.home);
        mHomell.setOnClickListener(this);

        mGatell = view.findViewById(R.id.it_gate);
        mIvGate = (ImageView)mGatell.findViewById(R.id.iv_image);
        mTvGate = (TextView)mGatell.findViewById(R.id.tv_textview);
        mTvGate.setText(R.string.collect_detect_gate);
        mIvGate.setImageResource(R.mipmap.gate);
        mGatell.setOnClickListener(this);
    }

    @Override
    public void initCollectView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.it_meeting:
                mSelectMode = 1;
                break;
            case R.id.it_attendance:
                mSelectMode = 2;
                break;
            case R.id.it_home:
                mSelectMode = 3;
                break;
            case R.id.it_gate:
                mSelectMode = 4;
                break;
                default:
                    break;
        }
        Log.d(TAG, "onClick select mode = " + mSelectMode);
        changeToCollectMode(mSelectMode);
        updateFocusItem(mSelectMode);
    }

    private void changeToCollectMode(int mode) {
        switch (mode) {
            case 1:
                mCollectMode = CollectMode.COLLECT_MEETING;
                break;
            case 2:
                mCollectMode = CollectMode.COLLECT_ATTENDANCE;
                break;
            case 3:
                mCollectMode = CollectMode.COLLECT_HOME;
                break;
            case 4:
                mCollectMode = CollectMode.COLLECT_GATE;
        }
        mSettingManager.setCollectMode(mCollectMode);
    }

    private int changeCollectToInt(CollectMode mode) {
        int mod;
        switch (mode) {
            case COLLECT_MEETING:
                mod = 1;
                break;
            case COLLECT_ATTENDANCE:
                mod = 2;
                break;
            case COLLECT_HOME:
                mod = 3;
                break;
            case COLLECT_GATE:
                mod = 4;
            case COLLECT_NONE:
            default:
                mod = 0;
        }
        return mod;
    }

    private void updateFocusItem(int mode) {
        Log.d(TAG, "updateFocusItem = " + mode);
        switch (mode) {
            case 1:
                mMeetingll.setFocusableInTouchMode(true);
                break;
            case 2:
                mAttendancell.setFocusableInTouchMode(true);
                break;
            case 3:
                mHomell.setFocusableInTouchMode(true);
                break;
            case 4:
                mGatell.setFocusableInTouchMode(true);
        }
    }
}
