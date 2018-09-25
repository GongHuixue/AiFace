package android.com.aiface.ui.fragment;

import android.com.aiface.R;
import android.com.aiface.settings.AiFaceEnum.*;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.DetectPresenter;
import android.com.aiface.ui.view.IDetectView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetectFragment extends BaseFragment<IDetectView, DetectPresenter> implements IDetectView{
    private TextView mTvDescription;
    private LinearLayout mMeetingll, mAttendancell, mHomell, mGatell;

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
        mTvDescription = (TextView)view.findViewById(R.id.tv_description);
        mTvDescription.setText(R.string.detect_title);

        mMeetingll = (LinearLayout)view.findViewById(R.id.it_meeting);
        mIvMeeting = (ImageView)mMeetingll.findViewById(R.id.iv_image);
        mTvMeeting = (TextView)mMeetingll.findViewById(R.id.tv_textview);
        mTvMeeting.setText(R.string.collect_detect_meeting);
        mIvMeeting.setImageResource(R.drawable.meeting);

        mAttendancell = (LinearLayout)view.findViewById(R.id.it_attendance);
        mIvAttendance = (ImageView)mAttendancell.findViewById(R.id.iv_image);
        mTvAttendance = (TextView)mAttendancell.findViewById(R.id.tv_textview);
        mTvAttendance.setText(R.string.collect_detect_attendance);
        mIvAttendance.setImageResource(R.drawable.attendance);

        mHomell = (LinearLayout)view.findViewById(R.id.it_home);
        mIvHome = (ImageView)mHomell.findViewById(R.id.iv_image);
        mTvHome = (TextView)mHomell.findViewById(R.id.tv_textview);
        mTvHome.setText(R.string.collect_detect_home);
        mIvHome.setImageResource(R.drawable.home);

        mGatell = (LinearLayout)view.findViewById(R.id.it_gate);
        mIvGate = (ImageView)mGatell.findViewById(R.id.iv_image);
        mTvGate = (TextView)mGatell.findViewById(R.id.tv_textview);
        mTvGate.setText(R.string.collect_detect_gate);
        mIvGate.setImageResource(R.drawable.gate);
    }

    @Override
    public void initDetectView() {

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
