package android.com.aiface.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.com.aiface.R;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeetingRegActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView, View.OnClickListener {
    private final static String TAG = MeetingRegActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mMeetingNamell, mMeetingTimell, mMeetingAddrll, mParticipantName, mParticipantPart;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg, btn_date, btn_time;
    private TextView tv_date, tv_time;
    private ImageView faceIv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_reg;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
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

        mMeetingTimell = (LinearLayout)findViewById(R.id.te_meeting_time);
        TextView tvMeetingTime = (TextView)mMeetingTimell.findViewById(R.id.tv_name);
        tvMeetingTime.setText(R.string.meeting_time);
        btn_date = (Button)mMeetingTimell.findViewById(R.id.btn_date);
        btn_time = (Button)mMeetingTimell.findViewById(R.id.btn_time);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        tv_date = (TextView)mMeetingTimell.findViewById(R.id.tv_date);
        tv_time = (TextView)mMeetingTimell.findViewById(R.id.tv_time);


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
        btnLocal.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        btnReg = (Button) findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(this);
    }

    @Override
    protected MeetingPresenter createPresenter() {
        return new MeetingPresenter(this);
    }

    @Override
    public void getMeetingInformation() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_date:
                break;
            case R.id.btn_time:
                break;
            case R.id.btn_from_local:
                break;
            case R.id.btn_from_camera:
                break;
            case R.id.btn_reg:
                break;
        }
    }

    private void startLaunchActivity() {

    }

    private void showDateDialog() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        // 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        Date myDate = new Date();
        // 创建一个Date实例
        d.setTime(myDate);
        // 设置日历的时间，把一个新建Date实例myDate传入
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        final String date;
        //初始化默认日期year, month, day
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            /**
             * 点击确定后，在这个方法中获取年月日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
//                date = "" + year + "-" + monthOfYear + "-" + dayOfMonth;
            }
        },year, month, day);
        datePickerDialog.setMessage("请选择日期");
        datePickerDialog.show();
    }

    private void showTimeDialog() {
        /**
         * 0：初始化小时
         * 0：初始化分
         * true:是否采用24小时制
         */
        int mHour, mMinute;
        TimePickerDialog timeDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    //从这个方法中取得获得的时间
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if(hourOfDay<10){
//                            mHour="0"+hourOfDay;
                        }
                        if(minute<10){
//                            mMinute="0"+minute;
                        }
                    }
                }, 0, 0, true);
        timeDialog.show();
    }
}
