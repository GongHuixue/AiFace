package android.com.aiface.ui.activity;

import android.com.aiface.AiFaceApplication;
import android.com.aiface.R;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.com.aiface.utils.DateTime;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceQueryActivity extends BaseActivity<IAttendanceView, AttendancePresenter> implements IAttendanceView {
    private final static String TAG = AttendanceQueryActivity.class.getSimpleName();

    private LinearLayout partll, namell, onworkll, offworkll;
    private EditText etPart, etName, etOnWorkTime, etOffWorkTime;
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
        if(mAttendanceFacelist.size() < 0) {
            mToastInstance.showLongToast("目前没有任何员工信息");
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
            mAttendanceFace = (AttendanceFace) greenDaoManager.queryFaceByIdName(returnUserId, returnUserName, "Attendance");
        }

        /*update EditText Information*/
        if((mAttendanceFace.getAttendanceName() != null) && (mAttendanceFace.getAttendancePart() != null)) {
            etPart.setText(mAttendanceFace.getAttendancePart());
            etName.setText(mAttendanceFace.getAttendanceName());
            etPart.setEnabled(false);
            etName.setEnabled(false);
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
            /*first detect*/
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
                }else {
                    mAttendanceFace.setAttendanceName(mAttendanceFace.getAttendanceName());
                    mAttendanceFace.setAttendancePart(mAttendanceFace.getAttendancePart());
                    mAttendanceFace.setUserId(mAttendanceFace.getUserId());
                    mAttendanceFace.setOnworktime(dateTime);
                    etOnWorkTime.setText(dateTime);
                    etOffWorkTime.setText("");
                    greenDaoManager.insertFaceData(mAttendanceFace);
                }
            }
        }
    }
}
