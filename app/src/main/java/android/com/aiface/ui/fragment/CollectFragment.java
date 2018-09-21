package android.com.aiface.ui.fragment;

import android.app.Activity;
import android.com.aiface.R;
import android.com.aiface.settings.AiFaceEnum.*;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.activity.MeetingRegActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.CollectPresenter;
import android.com.aiface.ui.view.ICollectView;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectFragment extends BaseFragment<ICollectView, CollectPresenter> implements ICollectView, View.OnTouchListener{
    private final static String TAG = CollectFragment.class.getSimpleName();
    private TextView mTvDescription;
    private View mMeetingll, mAttendancell, mHomell, mGatell;
    private CollectMode mCollectMode = CollectMode.COLLECT_NONE;
    private volatile int mSelectMode = -1;
    private Context mContext;

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
        Button mStart;
        mTvDescription = (TextView)view.findViewById(R.id.tv_description);
        mTvDescription.setText(R.string.collect_title);

        mMeetingll = view.findViewById(R.id.it_meeting);
        mIvMeeting = (ImageView)mMeetingll.findViewById(R.id.iv_image);
        mTvMeeting = (TextView)mMeetingll.findViewById(R.id.tv_textview);
        mTvMeeting.setText(R.string.collect_detect_meeting);
        mIvMeeting.setImageResource(R.mipmap.meeting);
        mMeetingll.setOnTouchListener(this);

        mAttendancell = view.findViewById(R.id.it_attendance);
        mIvAttendance = (ImageView)mAttendancell.findViewById(R.id.iv_image);
        mTvAttendance = (TextView)mAttendancell.findViewById(R.id.tv_textview);
        mTvAttendance.setText(R.string.collect_detect_attendance);
        mIvAttendance.setImageResource(R.mipmap.attendance);
        mAttendancell.setOnTouchListener(this);

        mHomell = view.findViewById(R.id.it_home);
        mIvHome = (ImageView)mHomell.findViewById(R.id.iv_image);
        mTvHome = (TextView)mHomell.findViewById(R.id.tv_textview);
        mTvHome.setText(R.string.collect_detect_home);
        mIvHome.setImageResource(R.mipmap.home);
        mHomell.setOnTouchListener(this);

        mGatell = view.findViewById(R.id.it_gate);
        mIvGate = (ImageView)mGatell.findViewById(R.id.iv_image);
        mTvGate = (TextView)mGatell.findViewById(R.id.tv_textview);
        mTvGate.setText(R.string.collect_detect_gate);
        mIvGate.setImageResource(R.mipmap.gate);
        mGatell.setOnTouchListener(this);

        mStart = view.findViewById(R.id.btn_start);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, MeetingRegActivity.class);
                mContext.startActivity(intent);
                Log.d(TAG,"start register");
            }
        });

        updateFocusItem(mSelectMode);
    }

    @Override
    public void initCollectView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onTouch");
            onClick(view);
        }
        return false;
    }

    private void onClick(View view) {
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
                break;
            case COLLECT_NONE:
            default:
                mod = 1;
        }
        return mod;
    }

    private void updateFocusItem(int mode) {
        Log.d(TAG, "updateFocusItem = " + mode);
        switch (mode) {
            case 1:
                mMeetingll.setFocusableInTouchMode(true);
                //mMeetingll.setFocusable(true);
                break;
            case 2:
                mAttendancell.setFocusableInTouchMode(true);
                //mAttendancell.setFocusable(true);
                break;
            case 3:
                mHomell.setFocusableInTouchMode(true);
                //mHomell.setFocusable(true);
                break;
            case 4:
                mGatell.setFocusableInTouchMode(true);
                //mGatell.setFocusable(true);
                break;
                default:
                    break;
        }
    }
}
