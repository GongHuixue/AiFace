package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.database.bean.HomeFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
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

public class HomeRegActivity extends BaseActivity<IHomeView, HomePresenter> implements IHomeView, View.OnClickListener {
    private final static String TAG = HomeRegActivity.class.getSimpleName();


    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mHostNamell, mHostAddrll, mGustNamell;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera, btnReg;
    private TextView tvHostName, tvHostAddr, tvGustName;
    private EditText etHostName, etHostAddr, etGustName;
    private ImageView faceImg;

    private HomeFace mHomeFace = new HomeFace();
    private List<HomeFace> mHomeFaceList = new ArrayList<>();



    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.home_title);

        mHostNamell = (LinearLayout)findViewById(R.id.home_host_name);
        tvHostName = (TextView)mHostNamell.findViewById(R.id.tv_name);
        tvHostName.setText(R.string.host_name);
        etHostName = (EditText)mHostNamell.findViewById(R.id.et_name);

        mHostAddrll = (LinearLayout)findViewById(R.id.home_host_addr);
        tvHostAddr = (TextView)mHostAddrll.findViewById(R.id.tv_name);
        tvHostAddr.setText(R.string.host_addr);
        etHostAddr = (EditText)mHostAddrll.findViewById(R.id.et_name);

        mGustNamell = (LinearLayout)findViewById(R.id.home_gust_name);
        tvGustName = (TextView)mGustNamell.findViewById(R.id.tv_name);
        tvGustName.setText(R.string.gust_name);
        etGustName = (EditText)mGustNamell.findViewById(R.id.et_name);

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
        getHostInformation();

        if(mHomeFaceList.size() > 0) {
            mHomeFace = mHomeFaceList.get(0);
            updateHostInfo();
        }else {
            mToastInstance.showShortToast("请先输入主人信息");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_reg;
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
                setFaceGroup(Config.HomeGroupId);
                registerGustInfo();
                break;
        }
    }

    @Override
    public void getHostInformation() {
        mHomeFaceList = greenDaoManager.getHomeHostInfo();
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    private void updateHostInfo() {
        Log.d(TAG, "updateHostInfo, host name = " + etHostName.getText().toString() + ", host addr = " + etHostAddr.getText().toString());

        etHostName.setText(mHomeFace.getHostName());
        etHostAddr.setText(mHomeFace.getHomeAddr());
        etHostAddr.setEnabled(false);
        etHostName.setEnabled(false);
    }

    private void registerGustInfo() {
        if((!TextUtils.isEmpty(etHostName.getText())) && (!TextUtils.isEmpty(etHostAddr.getText())) &&
                (!TextUtils.isEmpty(etGustName.getText()))) {
            Log.d(TAG, "registerGustInfo, host name = " + etHostName.getText().toString() + ", host addr = " + etHostAddr.getText().toString() +
                    ", gust name = " + etGustName.getText().toString());
            mHomeFace.setHomeAddr(etHostAddr.getText().toString());
            mHomeFace.setHostName(etHostName.getText().toString());
            mHomeFace.setGustName(etGustName.getText().toString());

            greenDaoManager.insertFaceData(mHomeFace);

            mToastInstance.showShortToast("注册成功");
        }
    }
}
