package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.com.aiface.utils.DateTime;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDetectActivity extends BaseActivity<IAttendanceView, AttendancePresenter> implements IAttendanceView {
    private final static String TAG = AttendanceDetectActivity.class.getSimpleName();
    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout partll, namell, onworkll, offworkll;
    private EditText etPart, etName, etOnWorkTime, etOffWorkTime;
    private ImageView faceImg;
    private String returnUserId;
    private String returnUserName;

    private AttendanceFace mAttendanceFace = new AttendanceFace();
    private List<AttendanceFace> mAttendanceFacelist = new ArrayList<>();
    private DateTime mDateTime = DateTime.getDTInstance();

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendance_query;
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.attendance_result);
        faceImg = (ImageView) findViewById(R.id.face_signed);

        partll = (LinearLayout) findViewById(R.id.te_participant_part);
        TextView tvPart = (TextView)partll.findViewById(R.id.tv_name);
        tvPart.setText(R.string.attendance_part);
        etPart = (EditText)partll.findViewById(R.id.et_name);

        namell = (LinearLayout) findViewById(R.id.te_participant_name);
        TextView tvName = (TextView)namell.findViewById(R.id.tv_name);
        tvName.setText(R.string.attendance_name);
        etName = (EditText)namell.findViewById(R.id.et_name);

        onworkll = (LinearLayout)findViewById(R.id.te_onwork);
        TextView tvOnWork = (TextView)onworkll.findViewById(R.id.tv_name);
        tvOnWork.setText(R.string.onwork_time);
        etOnWorkTime = (EditText)onworkll.findViewById(R.id.et_name);

        offworkll = (LinearLayout)findViewById(R.id.te_offwork);
        TextView tvOffWork = (TextView)offworkll.findViewById(R.id.tv_name);
        tvOffWork.setText(R.string.offwork_time);
        etOffWorkTime = (EditText)offworkll.findViewById(R.id.et_name);
    }

    @Override
    public void initData() {
        getAttendanceMember();
        Log.d(TAG, "member list size = " + mAttendanceFacelist.size());
        if(mAttendanceFacelist.size() <= 0) {
            mToastInstance.showLongToast("目前没有任何员工信息");
            return;
        }

        /*get detect result from faceDetectActivity*/
        Intent intent = getIntent();
        boolean detectSuccess = intent.getBooleanExtra("login_success", false);
        if(detectSuccess) {
            returnUserId = intent.getStringExtra("uid");
            returnUserName = intent.getStringExtra("user_info");
        }

        /*query member infor from database*/
        if((returnUserId != null) && (returnUserName != null)) {
            mAttendanceFace = (AttendanceFace) greenDaoManager.queryFaceByIdName(returnUserId, returnUserName, Config.AttendanceGroupId);
        }

        if(mAttendanceFace != null) {
            /*update EditText Information*/
            if ((mAttendanceFace.getAttendanceName() != null) && (mAttendanceFace.getAttendancePart() != null)) {
                etPart.setText(mAttendanceFace.getAttendancePart());
                etName.setText(mAttendanceFace.getAttendanceName());
                etPart.setEnabled(false);
                etName.setEnabled(false);

                etOnWorkTime.setEnabled(false);
                etOffWorkTime.setEnabled(false);

                faceImg.setVisibility(View.VISIBLE);
                Bitmap bmp = ImageSaveUtil.loadCameraBitmap(this, "head_tmp.jpg");
                if (bmp != null) {
                    faceImg.setImageBitmap(bmp);
                }
            }
        }

        insertOrUpdateTime();
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    @Override
    public void getAttendanceMember() {
        mAttendanceFacelist = greenDaoManager.getAttendanceInfo();
    }

    private void insertOrUpdateTime() {
        String dateTime = mDateTime.convertTimeToString(mDateTime.getSystemTime());
        if(mAttendanceFace != null) {
            Log.d(TAG, "mAttendanceFace on = " + mAttendanceFace.getOnworktime() + ", off = " + mAttendanceFace.getOffworktime());
            if (mAttendanceFace.getOnworktime() == null) {
                greenDaoManager.updateWorkTime(returnUserId, returnUserName, dateTime, null);
                etOnWorkTime.setText(dateTime);
                etOffWorkTime.setText("");
            }else if((mAttendanceFace.getOnworktime() !=null) && (mAttendanceFace.getOffworktime() == null)) {//onwork, but have not offwork
                greenDaoManager.updateWorkTime(returnUserId, returnUserName, null, dateTime);
                etOnWorkTime.setText(mAttendanceFace.getOnworktime());
                etOffWorkTime.setText(dateTime);
            }else if((mAttendanceFace.getOnworktime() != null) && (mAttendanceFace.getOffworktime() != null)) {//offwork, and need judge whether the same day
                if(mDateTime.isTheSameDay(mAttendanceFace.getOffworktime())) {
                    //the same day
                    mToastInstance.showShortToast("你已经下班啦");
                    etOnWorkTime.setText(mAttendanceFace.getOnworktime());
                    etOffWorkTime.setText(mAttendanceFace.getOffworktime());

//                    Log.d(TAG, "mAttendanceFace on = " + mAttendanceFace.getOnworktime() + ", off = " + mAttendanceFace.getOffworktime());
//                    AttendanceFace attendanceFace = new AttendanceFace();
//                    attendanceFace.setAttendanceName(mAttendanceFace.getAttendanceName());
//                    attendanceFace.setAttendancePart(mAttendanceFace.getAttendancePart());
//                    attendanceFace.setUserId(returnUserId);
//                    attendanceFace.setOnworktime("2018-10-08 12:00");
//                    attendanceFace.setOffworktime("2018-10-08 17:00");
//                    greenDaoManager.insertFaceData(attendanceFace);
                } else {
                    mAttendanceFace.setAttendanceName(mAttendanceFace.getAttendanceName());
                    mAttendanceFace.setAttendancePart(mAttendanceFace.getAttendancePart());
                    mAttendanceFace.setUserId(mAttendanceFace.getUserId());
                    /*need change on/off work time in mAttendanceFace*/
                    mAttendanceFace.setOnworktime(dateTime);
                    mAttendanceFace.setOffworktime("");
                    etOnWorkTime.setText(dateTime);
                    etOffWorkTime.setText("");
                    Log.d(TAG, "insert new data");
                    greenDaoManager.insertFaceData(mAttendanceFace);
                }
            }
        }
    }
}
