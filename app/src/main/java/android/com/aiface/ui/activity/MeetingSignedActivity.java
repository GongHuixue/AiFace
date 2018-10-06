package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.MeetingFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.com.aiface.utils.DateTime;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeetingSignedActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView {
    private final static String TAG = MeetingSignedActivity.class.getSimpleName();
    private LinearLayout mMeetingNamell, mMeetingTimell, mSetMeetingTimell, mMeetingAddrll, mParticipantName, mParticipantPart;
    private EditText etMeetingName, etMeetingTime, etMeetingAddr, etParticipantName, etParticipantPart;


    private String returnUserId;
    private String returnUserName;

    private List<MeetingFace> meetingFaceList = new ArrayList<>();
    private Calendar mCalendar = Calendar.getInstance();
    private DateTime mDateTime = DateTime.getDTInstance();
    private MeetingFace mMeetingFace = new MeetingFace();
    private MeetingFace meetingFaceDb = new MeetingFace();
    boolean meetingPassed = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_signed;
    }

    @Override
    public void initView() {
        mMeetingNamell = (LinearLayout) findViewById(R.id.te_meeting_name);
        TextView tvMeetingName = (TextView) mMeetingNamell.findViewById(R.id.tv_name);
        etMeetingName = (EditText) mMeetingNamell.findViewById(R.id.et_name);
        tvMeetingName.setText(R.string.meeting_name);

        mMeetingAddrll = (LinearLayout) findViewById(R.id.te_meeting_addr);
        TextView tvMeetingAddr = (TextView) mMeetingAddrll.findViewById(R.id.tv_name);
        etMeetingAddr = (EditText) mMeetingAddrll.findViewById(R.id.et_name);
        tvMeetingAddr.setText(R.string.meeting_addr);

        mMeetingTimell = (LinearLayout) findViewById(R.id.te_meeting_time);
        TextView tvMeetingTime = (TextView) mMeetingTimell.findViewById(R.id.tv_name);
        tvMeetingTime.setText(R.string.meeting_time);
        etMeetingTime = (EditText) mMeetingTimell.findViewById(R.id.et_name);

        mParticipantName = (LinearLayout) findViewById(R.id.te_participant_name);
        TextView tvParticipantName = (TextView) mParticipantName.findViewById(R.id.tv_name);
        etParticipantName = (EditText) mParticipantName.findViewById(R.id.et_name);
        tvParticipantName.setText(R.string.participant_name);

        mParticipantPart = (LinearLayout) findViewById(R.id.te_participant_part);
        TextView tvParticipantPart = (TextView) mParticipantPart.findViewById(R.id.tv_name);
        etParticipantPart = (EditText) mParticipantPart.findViewById(R.id.et_name);
        tvParticipantPart.setText(R.string.participant_part);
    }

    @Override
    public void initData() {
        getMeetingInformation();
        Log.d(TAG, "Meeting size = " + meetingFaceList.size());
        if(meetingFaceList.size() > 0) {
            meetingFaceDb = meetingFaceList.get(0);
        }else {
            mToastInstance.showLongToast("目前没有创建任何会议");
        }

        /*get detect result from faceDetectActivity*/
        Intent intent = getIntent();
        boolean detectSuccess = intent.getBooleanExtra("login_success", false);
        if(detectSuccess) {
            returnUserId = intent.getStringExtra("uid");
            returnUserName = intent.getStringExtra("user_info");
        }

        /*get meeting information from database*/
        if((returnUserName != null) && (returnUserId != null)) {
            mMeetingFace = (MeetingFace) greenDaoManager.queryFaceByIdName(returnUserId, returnUserName, "Meeting");
        }

        /*update meeting infor*/
        if(mMeetingFace != null) {
            etMeetingAddr.setText(mMeetingFace.getMeetingAddr());
            etMeetingName.setText(mMeetingFace.getMeetingName());
            etMeetingTime.setText(mMeetingFace.getMeetingTimeString());

            etParticipantName.setText(mMeetingFace.getParticipantName());
            etParticipantPart.setText(mMeetingFace.getParticipantPart());

            etMeetingAddr.setEnabled(false);
            etMeetingTime.setEnabled(false);
            etMeetingName.setEnabled(false);
            etParticipantPart.setEnabled(false);
            etParticipantPart.setEnabled(false);
        }
    }

    @Override
    protected MeetingPresenter createPresenter() {
        return new MeetingPresenter(this);
    }

    @Override
    public void getMeetingInformation() {
        meetingFaceList = greenDaoManager.getMeetingInfo();
    }

}
