package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeRegActivity extends BaseActivity<IHomeView, HomePresenter> implements IHomeView, View.OnClickListener {
    private final static String TAG = HomeRegActivity.class.getSimpleName();


    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout mHostNamell, mHostAddrll, mGustNamell;
    private RelativeLayout mFaceImageRl;
    private Button btnLocal, btnCamera;
    private TextView tvHostName, tvHostAddr, tvGustName;
    private EditText etHostName, etHostAddr, etGustName;
    private ImageView faceImg;



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
    }

    @Override
    public void initData() {

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
                break;
            case R.id.btn_from_camera:
                break;

        }
    }

    @Override
    public void getHostInformation() {

    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }
}
