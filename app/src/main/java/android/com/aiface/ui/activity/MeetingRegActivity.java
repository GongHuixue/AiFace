package android.com.aiface.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.com.aiface.R;
import android.com.aiface.baidu.APIService;
import android.com.aiface.baidu.Config;
import android.com.aiface.baidu.exception.FaceError;
import android.com.aiface.baidu.model.RegResult;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.baidu.utils.OnResultListener;
import android.com.aiface.database.bean.MeetingFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.com.aiface.utils.DateTime;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MeetingRegActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView, View.OnClickListener {
    private final static String TAG = MeetingRegActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mMeetingNamell, mMeetingTimell, mSetMeetingTimell, mMeetingAddrll, mParticipantName, mParticipantPart;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg, btn_date, btn_time;
    private EditText etMeetingName, etMeetingTime, etMeetingAddr, etParticipantName, etParticipantPart;
    private TextView tv_date, tv_time;
    private ImageView faceIv;
    private String dateSet, timeSet;
    private StringBuffer datetime;
    private long longTime;

    private List<MeetingFace> meetingFaceList = new ArrayList<>();
    private Calendar mCalendar = Calendar.getInstance();
    private DateTime mDateTime = DateTime.getDTInstance();
    private MeetingFace mMeetingFace = new MeetingFace();
    boolean meetingPassed = false;

    private String facePath;
    private Bitmap faceBmp;
    private String returnUserId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_reg;
    }

    @Override
    public void initData() {

        getMeetingInformation();
        Log.d(TAG, "meeting list size = " + meetingFaceList.size());
        if (meetingFaceList.size() > 0) {
            mMeetingFace = meetingFaceList.get(0);

            if (mMeetingFace.getMeetingTime() < mDateTime.getSystemTime()) {
                meetingPassed = true;
            }

            updateDateTimeLL(meetingPassed);

            if (meetingPassed) {
                mToastInstance.showShortToast("没有有效的会议，请创建新的会议");
                // need create a new meeting
            } else {
                updateEtMeetingInfo();
                //update meeting name/time/addr
            }
            Log.d(TAG, "Show the latest meeting info");
        } else {
            updateDateTimeLL(true);
            mToastInstance.showShortToast("没有有效的会议，请创建新的会议");
            Log.d(TAG, "No any meeting information");
        }

        registerFaceListener(new IFaceRegCallback() {
            @Override
            public void FaceRegSuccess(String userId) {
                Log.d(TAG, "FaceRegSuccess userId = " + userId);
                returnUserId = userId;
                insertMeetingInfo();
            }

            @Override
            public void FaceRegFailed() {
                Log.e(TAG, "FaceRegFailed");
            }
        });
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.meeting_title);


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

        mSetMeetingTimell = (LinearLayout) findViewById(R.id.set_meeting_time);
        TextView tvSetMeetingTime = (TextView) mSetMeetingTimell.findViewById(R.id.tv_name);
        tvSetMeetingTime.setText(R.string.meeting_time);
        btn_date = (Button) mSetMeetingTimell.findViewById(R.id.btn_date);
        btn_time = (Button) mSetMeetingTimell.findViewById(R.id.btn_time);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        tv_date = (TextView) mSetMeetingTimell.findViewById(R.id.tv_date);
        tv_time = (TextView) mSetMeetingTimell.findViewById(R.id.tv_time);


        mParticipantName = (LinearLayout) findViewById(R.id.te_participant_name);
        TextView tvParticipantName = (TextView) mParticipantName.findViewById(R.id.tv_name);
        etParticipantName = (EditText) mParticipantName.findViewById(R.id.et_name);
        tvParticipantName.setText(R.string.participant_name);

        mParticipantPart = (LinearLayout) findViewById(R.id.te_participant_part);
        TextView tvParticipantPart = (TextView) mParticipantPart.findViewById(R.id.tv_name);
        etParticipantPart = (EditText) mParticipantPart.findViewById(R.id.et_name);
        tvParticipantPart.setText(R.string.participant_part);

        mFaceImageRl = (RelativeLayout) findViewById(R.id.upload_face_rl);
        faceIv = (ImageView) mFaceImageRl.findViewById(R.id.iv_face_image);
        btnLocal = (Button) mFaceImageRl.findViewById(R.id.btn_from_local);
        btnCamera = (Button) mFaceImageRl.findViewById(R.id.btn_from_camera);
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
        meetingFaceList = greenDaoManager.getMeetingInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_date:
                showDateDialog();
                break;
            case R.id.btn_time:
                showTimeDialog();
                break;
            case R.id.btn_from_local:
                uploadFromAlbum();
                break;
            case R.id.btn_from_camera:
                uploadFromCamera();
                break;
            case R.id.btn_reg:
                setFaceGroup(Config.MeetingGroupId);

                getMeetingTime();

                registerFaceImage(facePath, etParticipantName.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);
        if ((requestCode == REQUEST_CODE_DETECT_FACE) && (resultCode == Activity.RESULT_OK)) {
            facePath = ImageSaveUtil.loadCameraBitmapPath(this, "head_tmp.jpg");
            if (faceBmp != null) {
                faceBmp.recycle();
            }
            Log.d(TAG, "camera facePath = " + facePath);
            faceBmp = ImageSaveUtil.loadBitmapFromPath(this, facePath);
            if (faceBmp != null) {
                faceIv.setImageBitmap(faceBmp);
            }else {
                Log.e(TAG, "Bitmap is null");
            }
        } else if ((requestCode == REQUEST_CODE_PICK_IMAGE) && (resultCode == Activity.RESULT_OK)) {
            Uri uri = data.getData();
            facePath = getFacePathFromURI(uri);
            Log.d(TAG, "local facePath = " + facePath);
            if (faceBmp != null) {
                faceBmp.recycle();
            }

            faceBmp = ImageSaveUtil.loadBitmapFromPath(this, facePath);
            if (faceBmp != null) {
                faceIv.setImageBitmap(faceBmp);
            }else {
                Log.e(TAG, "Bitmap is null");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(faceBmp != null) {
            faceBmp.recycle();
        }

        unregisterFaceListener();
    }

    private long getMeetingTime() {
        long time = 0;
        Log.d(TAG, "date = " + dateSet + ", time = " + timeSet);
        if ((dateSet != null) && (timeSet != null)) {
            datetime = new StringBuffer(dateSet).append(" ").append(timeSet);
            Log.d(TAG, "date + time " + datetime.toString());
            try {
                time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(datetime.toString()).getTime();
                longTime = time;
                Log.d(TAG, "getMeetingTime Time = " + time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            /**
             * 点击确定后，在这个方法中获取年月日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                tv_date.setText(DateFormat.format("yyyy-MM-dd", mCalendar));
                dateSet = DateFormat.format("yyyy-MM-dd", mCalendar).toString();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setMessage("请选择日期");
        datePickerDialog.show();
    }

    private void showTimeDialog() {
        /**
         * 0：初始化小时
         * 0：初始化分
         * true:是否采用24小时制
         */
        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                tv_time.setText(DateFormat.format("HH:mm", mCalendar));
                timeSet = DateFormat.format("HH:mm", mCalendar).toString();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
        timeDialog.setMessage("请选择时间");
        timeDialog.show();
    }

    private void updateEtMeetingInfo() {
        etMeetingName.setText(mMeetingFace.getMeetingName());
        etMeetingTime.setText(mDateTime.convertTimeToString(mMeetingFace.getMeetingTime()));
        etMeetingAddr.setText(mMeetingFace.getMeetingAddr());

        etMeetingName.setEnabled(false);
        etMeetingTime.setEnabled(false);
        etMeetingAddr.setEnabled(false);
    }

    private void updateDateTimeLL(boolean meetingPassed) {
        if (meetingPassed) {
            mSetMeetingTimell.setVisibility(View.VISIBLE);
            mMeetingTimell.setVisibility(View.GONE);
        } else {
            mSetMeetingTimell.setVisibility(View.GONE);
            mMeetingTimell.setVisibility(View.VISIBLE);
        }
    }

    private void insertMeetingInfo() {
        if((!TextUtils.isEmpty(etMeetingName.getText())) && (!TextUtils.isEmpty(etMeetingAddr.getText())) &&
                (!TextUtils.isEmpty(etParticipantName.getText())) &&
                (!TextUtils.isEmpty(etParticipantPart.getText())) && (!TextUtils.isEmpty(datetime.toString()))) {
            Log.d(TAG, "insertMeetingInfo: name = " + etMeetingName.getText().toString() +
                    ", time = " + datetime.toString() + ", addr = " + etMeetingAddr.getText().toString() +
                    ", part = " + etParticipantPart.getText().toString() + ", username = " + etParticipantName.getText().toString());

            mMeetingFace.setMeetingName(etMeetingName.getText().toString().trim());
            mMeetingFace.setMeetingTime(longTime);
            mMeetingFace.setMeetingTimeString(datetime.toString());
            mMeetingFace.setUserId(returnUserId);
            mMeetingFace.setMeetingAddr(etMeetingAddr.getText().toString().trim());
            mMeetingFace.setParticipantPart(etParticipantPart.getText().toString().trim());
            mMeetingFace.setParticipantName(etParticipantName.getText().toString().trim());
            greenDaoManager.insertFaceData(mMeetingFace);
        }
    }
}
