package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRegActivity extends BaseActivity<IAttendanceView, AttendancePresenter> implements IAttendanceView, View.OnClickListener {
    private final static String TAG = AttendanceRegActivity.class.getSimpleName();


    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mAttendanceNamell, mAttendancePartll;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg;
    private TextView tvPartName, tvUserName;
    private EditText etPartName, etUserName;
    private ImageView faceImg;

    private AttendanceFace mAttendanceFace = new AttendanceFace();
    private List<AttendanceFace> mAttendanceFacelist = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendance_reg;
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.attendance_title);

        mAttendancePartll = (LinearLayout)findViewById(R.id.te_attendance_part);
        tvPartName = (TextView)mAttendancePartll.findViewById(R.id.tv_name);
        tvPartName.setText(R.string.attendance_part);
        etPartName = (EditText)mAttendancePartll.findViewById(R.id.et_name);

        mAttendanceNamell = (LinearLayout)findViewById(R.id.te_attendance_name);
        tvUserName = (TextView)mAttendanceNamell.findViewById(R.id.tv_name);
        tvUserName.setText(R.string.attendance_name);
        etUserName = (EditText)mAttendanceNamell.findViewById(R.id.et_name);

        mFaceImageRl = (RelativeLayout)findViewById(R.id.upload_face_rl);
        btnLocal = (Button)mFaceImageRl.findViewById(R.id.btn_from_local);
        btnCamera = (Button)mFaceImageRl.findViewById(R.id.btn_from_camera);
        btnLocal.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        faceImg = (ImageView)mFaceImageRl.findViewById(R.id.iv_face_image);

        btnReg = (Button)findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getAttendanceMember();

        if(mAttendanceFacelist.size() == 0) {
            mToastInstance.showShortToast("目前没有任何员工信息，请先注册");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_from_local:
                uploadFromAlbum();
                break;
            case R.id.btn_from_camera:
                uploadFromCamera();
                break;
            case R.id.btn_reg:
                setFaceGroup(Config.AttendanceGroupId);
                registerAttendanceInfo();
                break;
        }
    }

    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    @Override
    public void getAttendanceMember() {
        mAttendanceFacelist = greenDaoManager.getAttendanceInfo();
    }

    private void registerAttendanceInfo() {
        if((!TextUtils.isEmpty(etPartName.getText())) && (!TextUtils.isEmpty(etUserName.getText()))){
            Log.d(TAG, "registerAttendanceInfo: part = " + etPartName.getText().toString() + ", name = " + etUserName.getText().toString());
            mAttendanceFace.setAttendanceName(etUserName.getText().toString());
            mAttendanceFace.setAttendancePart(etPartName.getText().toString());
            greenDaoManager.insertFaceData(mAttendanceFace);
            mToastInstance.showShortToast("注册成功");
        }
    }
}
