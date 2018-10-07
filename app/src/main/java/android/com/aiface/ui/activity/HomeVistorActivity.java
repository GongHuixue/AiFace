package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.database.bean.HomeFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeVistorActivity extends BaseActivity<IHomeView, HomePresenter> implements IHomeView {

    private final static String TAG = HomeVistorActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout teHostName, teHostAddr, teGustName;
    private EditText etHostName, etHostAddr, etGustName;
    private ImageView faceImg;

    private HomeFace mHomeFace;
    private List<HomeFace> homeFaceList = new ArrayList<>();


    private String returnUserId;
    private String returnUserName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_vistor;
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
        tv_title.setText(R.string.home_result);

        teHostName = (LinearLayout)findViewById(R.id.te_host_name);
        TextView tvHostName = (TextView)teHostName.findViewById(R.id.tv_name);
        tvHostName.setText(R.string.host_name);
        etHostName = (EditText)teHostName.findViewById(R.id.et_name);

        teHostAddr = (LinearLayout)findViewById(R.id.te_host_addr);
        TextView tvHostAddr = (TextView)teHostAddr.findViewById(R.id.tv_name);
        tvHostAddr.setText(R.string.host_addr);
        etHostAddr = (EditText)teHostAddr.findViewById(R.id.et_name);

        teGustName = (LinearLayout)findViewById(R.id.te_gust_name);
        TextView tvGustName = (TextView)teGustName.findViewById(R.id.tv_name);
        tvGustName.setText(R.string.gust_name);
        etGustName = (EditText)teGustName.findViewById(R.id.et_name);
    }

    @Override
    public void initData() {
        /*get detect result from faceDetectActivity*/
        Intent intent = getIntent();
        boolean detectSuccess = intent.getBooleanExtra("login_success", false);
        if(detectSuccess) {
            returnUserId = intent.getStringExtra("uid");
            returnUserName = intent.getStringExtra("user_info");
        }

        if((returnUserId != null) && (returnUserName != null)) {
            mHomeFace = (HomeFace) greenDaoManager.queryFaceByIdName(returnUserId, returnUserName, "Home");
        }else {
            mToastInstance.showLongToast("陌生人");
            return;
        }

        if(mHomeFace != null) {
            etHostName.setText(mHomeFace.getHostName());
            etHostAddr.setText(mHomeFace.getHomeAddr());
            etGustName.setText(mHomeFace.getGustName());

            etHostName.setEnabled(false);
            etHostAddr.setEnabled(false);
            etGustName.setEnabled(false);

            faceImg.setVisibility(View.VISIBLE);
            Bitmap bmp = ImageSaveUtil.loadCameraBitmap(this, "head_tmp.jpg");
            if (bmp != null) {
                faceImg.setImageBitmap(bmp);
            }

        }

        mToastInstance.showLongToast("欢迎回家");
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void getHostInformation() {
        homeFaceList = greenDaoManager.getHomeHostInfo();
    }
}
