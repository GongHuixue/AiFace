package android.com.aiface.ui.fragment;

import android.com.aiface.R;
import android.com.aiface.baidu.APIService;
import android.com.aiface.baidu.Config;
import android.com.aiface.settings.AiFaceEnum.*;
import android.com.aiface.ui.activity.FaceDetectActivity;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.DetectPresenter;
import android.com.aiface.ui.view.IDetectView;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetectFragment extends BaseFragment<IDetectView, DetectPresenter> implements IDetectView, View.OnTouchListener{
    private final static String TAG = DetectFragment.class.getSimpleName();
    private TextView mTvDescription;
    private View mMeetingll, mAttendancell, mHomell, mGatell;
    private volatile int mDetectMode = 1;
    private DetectMode detectMode = DetectMode.DETECT_NONE;
    private Context mContext;

    @Override
    protected DetectPresenter createPresenter() {
        return new DetectPresenter((MainActivity)getActivity());
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
        TextView mTvMeeting, mTvAttendance, mTvHome, mTvGate;
        ImageView mIvMeeting, mIvAttendance, mIvHome, mIvGate;
        Button mStart;
        mTvDescription = (TextView)view.findViewById(R.id.tv_description);
        mTvDescription.setText(R.string.detect_title);

        mMeetingll = (LinearLayout)view.findViewById(R.id.it_meeting);
        mIvMeeting = (ImageView)mMeetingll.findViewById(R.id.iv_image);
        mTvMeeting = (TextView)mMeetingll.findViewById(R.id.tv_textview);
        mTvMeeting.setText(R.string.collect_detect_meeting);
        mIvMeeting.setImageResource(R.drawable.meeting);
        mMeetingll.setOnTouchListener(this);

        mAttendancell = (LinearLayout)view.findViewById(R.id.it_attendance);
        mIvAttendance = (ImageView)mAttendancell.findViewById(R.id.iv_image);
        mTvAttendance = (TextView)mAttendancell.findViewById(R.id.tv_textview);
        mTvAttendance.setText(R.string.collect_detect_attendance);
        mIvAttendance.setImageResource(R.drawable.attendance);
        mAttendancell.setOnTouchListener(this);

        mHomell = (LinearLayout)view.findViewById(R.id.it_home);
        mIvHome = (ImageView)mHomell.findViewById(R.id.iv_image);
        mTvHome = (TextView)mHomell.findViewById(R.id.tv_textview);
        mTvHome.setText(R.string.collect_detect_home);
        mIvHome.setImageResource(R.drawable.home);
        mHomell.setOnTouchListener(this);

        mGatell = (LinearLayout)view.findViewById(R.id.it_gate);
        mIvGate = (ImageView)mGatell.findViewById(R.id.iv_image);
        mTvGate = (TextView)mGatell.findViewById(R.id.tv_textview);
        mTvGate.setText(R.string.collect_detect_gate);
        mIvGate.setImageResource(R.drawable.gate);
        mGatell.setOnTouchListener(this);

        mStart = view.findViewById(R.id.btn_start);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToDetectMode(mDetectMode);

                setDetectFaceGroup();

                ((MainActivity)getActivity()).jumpToActivity(FaceDetectActivity.class);
                Log.d(TAG,"start register");
            }
        });
    }

    @Override
    public void initDetectView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            onClick(v);
        }

        return false;
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.it_meeting:
                mDetectMode = 1;
                break;
            case R.id.it_attendance:
                mDetectMode = 2;
                break;
            case R.id.it_home:
                mDetectMode = 3;
                break;
            case R.id.it_gate:
                mDetectMode = 4;
                break;
            default:
                break;
        }
    }

    private void setDetectFaceGroup() {
        String groupId = "";
        switch (mDetectMode) {
            case 1:
                groupId = Config.MeetingGroupId;
                break;
            case 2:
                groupId = Config.AttendanceGroupId;
                break;
            case 3:
                groupId = Config.HomeGroupId;
                break;
            case 4:
                groupId = Config.GateGroupId;
                break;
        }
        APIService.getInstance().setGroupId(groupId);
    }

    private void changeToDetectMode(int mode) {
        switch (mode) {
            case 1:
                detectMode = DetectMode.DETECT_MEETING;
                break;
            case 2:
                detectMode = DetectMode.DETECT_ATTENDANCE;
                break;
            case 3:
                detectMode = DetectMode.DETECT_HOME;
                break;
            case 4:
                detectMode = DetectMode.DETECT_GATE;
        }
        Log.d(TAG, "set current detect mode = " + detectMode);
        mSettingManager.setDetectMode(detectMode);
    }

    private int changeDetectToInt(DetectMode mode) {
        int mod;
        switch (mode) {
            case DETECT_MEETING:
                mod = 1;
                break;
            case DETECT_ATTENDANCE:
                mod = 2;
                break;
            case DETECT_HOME:
                mod = 3;
                break;
            case DETECT_GATE:
                mod = 4;
            case DETECT_NONE:
            default:
                mod = 0;
                break;
        }
        return mod;
    }

}
