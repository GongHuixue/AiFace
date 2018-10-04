package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.GatePresenter;
import android.com.aiface.ui.view.IGateView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GateRegActivity extends BaseActivity<IGateView, GatePresenter> implements IGateView, View.OnClickListener {
    private final static String TAG = GateRegActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mUserNamell;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg;
    private TextView tvUserName;
    private EditText etUserName;
    private ImageView faceImg;

    private GateFace mGateFace = new GateFace();


    @Override
    public int getLayoutId() {
        return R.layout.activity_gate_reg;
    }

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.gate_title);

        mUserNamell = (LinearLayout)findViewById(R.id.te_user_name);
        tvUserName = (TextView)mUserNamell.findViewById(R.id.tv_name);
        tvUserName.setText(R.string.user_name);
        etUserName = (EditText)mUserNamell.findViewById(R.id.et_name);

        mFaceImageRl = (RelativeLayout)findViewById(R.id.upload_image);
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

    }

    @Override
    protected GatePresenter createPresenter() {
        return new GatePresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                setFaceGroup(Config.HomeGroupId);
                break;
        }
    }

    private void registerGateInfo() {
        if(!TextUtils.isEmpty(etUserName.getText())) {
            mGateFace.setFacePath("");

            greenDaoManager.insertFaceData(mGateFace);

            mToastInstance.showShortToast("注册成功");
        }
    }
}
