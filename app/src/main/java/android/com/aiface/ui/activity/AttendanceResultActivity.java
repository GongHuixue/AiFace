package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceResultActivity extends BaseActivity<IAttendanceView, AttendancePresenter> implements IAttendanceView {
    private final static String TAG = AttendanceResultActivity.class.getSimpleName();
    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private AttendanceFace mAttendanceFace = new AttendanceFace();
    private List<AttendanceFace> mAttendanceFacelist = new ArrayList<>();

    @Override
    public void initData() {
        getAttendanceMember();
        Log.d(TAG, "member list size = " + mAttendanceFacelist.size());
        if(mAttendanceFacelist.size() < 0) {
            mToastInstance.showLongToast("目前没有任何员工信息");
        }
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

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendance_result;
    }


    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    @Override
    public void getAttendanceMember() {
        mAttendanceFacelist = greenDaoManager.getAttendanceInfo();

    }
}
