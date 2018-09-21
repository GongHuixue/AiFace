package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MeetingRegActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView {
    private final static String TAG = MeetingRegActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mMeetingNamell, mMeetingAddrll, mParticipantName, mParticipantPart;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg;
    private ImageView faceIv;
    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_reg;
    }

    @Override
    public void initData() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.meeting_title);


        mMeetingNamell = (LinearLayout)findViewById(R.id.te_meeting_name);
        TextView tvMeetingName = (TextView)mMeetingNamell.findViewById(R.id.tv_name);
        EditText etMeetingName = (EditText)mMeetingNamell.findViewById(R.id.et_name);
        tvMeetingName.setText(R.string.meeting_name);

        mMeetingAddrll = (LinearLayout)findViewById(R.id.te_meeting_addr);
        TextView tvMeetingAddr = (TextView)mMeetingAddrll.findViewById(R.id.tv_name);
        EditText etMeetingAddr = (EditText)mMeetingAddrll.findViewById(R.id.et_name);
        tvMeetingAddr.setText(R.string.meeting_addr);

        mParticipantName = (LinearLayout)findViewById(R.id.te_participant_name);
        TextView tvParticipantName = (TextView)mParticipantName.findViewById(R.id.tv_name);
        EditText etParticipantName = (EditText)mParticipantName.findViewById(R.id.et_name);
        tvParticipantName.setText(R.string.participant_name);

        mParticipantPart = (LinearLayout)findViewById(R.id.te_participant_part);
        TextView tvParticipantPart= (TextView)mParticipantPart.findViewById(R.id.tv_name);
        EditText etParticipantPart = (EditText)mParticipantPart.findViewById(R.id.et_name);
        tvParticipantPart.setText(R.string.participant_part);

        mFaceImageRl = (RelativeLayout)findViewById(R.id.upload_face_rl);
        faceIv = (ImageView)mFaceImageRl.findViewById(R.id.iv_face_image);
        faceIv.setImageResource(R.drawable.face_default);
        btnLocal = (Button)mFaceImageRl.findViewById(R.id.btn_from_local);
        btnCamera = (Button)mFaceImageRl.findViewById(R.id.btn_from_camera);
    }

    @Override
    protected MeetingPresenter createPresenter() {
        return new MeetingPresenter(this);
    }

    @Override
    public void getMeetingInformation() {

    }
}
