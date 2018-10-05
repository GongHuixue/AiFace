package android.com.aiface.ui.activity;

import android.app.Activity;
import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
    private ImageView faceIv;

    private String facePath;
    private Bitmap faceBmp;
    private String returnUserId;


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
        faceIv = (ImageView)mFaceImageRl.findViewById(R.id.iv_face_image);

        btnReg = (Button)findViewById(R.id.btn_reg);
        btnReg.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getAttendanceMember();

        if(mAttendanceFacelist.size() == 0) {
            mToastInstance.showShortToast("目前没有任何员工信息，请先注册");
        }

        registerFaceListener(new IFaceRegCallback() {
            @Override
            public void FaceRegSuccess(String userId) {
                returnUserId = userId;
                insertAttendanceInfo();
            }

            @Override
            public void FaceRegFailed() {

            }
        });
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
                registerFaceImage(facePath, etUserName.getText().toString().trim());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);
        if ((requestCode == REQUEST_CODE_DETECT_FACE) && (resultCode == Activity.RESULT_OK)) {
            facePath = ImageSaveUtil.loadCameraBitmapPath(this, "head_tmp.jpg");
            if (faceBmp != null) {
                faceBmp.recycle();
            }
            Log.d(TAG, "facePath = " + facePath);
            faceBmp = ImageSaveUtil.loadBitmapFromPath(this, facePath);
            if (faceBmp != null) {
                faceIv.setImageBitmap(faceBmp);
            }else {
                Log.e(TAG, "Bitmap is null");
            }
        } else if ((requestCode == REQUEST_CODE_PICK_IMAGE) && (resultCode == Activity.RESULT_OK)) {
            Uri uri = data.getData();
            facePath = getFacePathFromURI(uri);

            if (faceBmp != null) {
                faceBmp.recycle();
            }

            faceBmp = ImageSaveUtil.loadBitmapFromPath(this, facePath);
            if (faceBmp != null) {
                faceIv.setImageBitmap(faceBmp);
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

    private void insertAttendanceInfo() {
        if((!TextUtils.isEmpty(etPartName.getText())) && (!TextUtils.isEmpty(etUserName.getText()))){
            Log.d(TAG, "insertAttendanceInfo: part = " + etPartName.getText().toString() + ", name = " + etUserName.getText().toString());
            mAttendanceFace.setAttendanceName(etUserName.getText().toString().trim());
            mAttendanceFace.setAttendancePart(etPartName.getText().toString().trim());
            mAttendanceFace.setUserId(returnUserId);

            greenDaoManager.insertFaceData(mAttendanceFace);
        }
    }
}
