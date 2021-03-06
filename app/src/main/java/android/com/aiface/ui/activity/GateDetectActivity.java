package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.baidu.Config;
import android.com.aiface.baidu.utils.ImageSaveUtil;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.GatePresenter;
import android.com.aiface.ui.view.IGateView;
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

public class GateDetectActivity extends BaseActivity<IGateView, GatePresenter> implements IGateView {
    private final static String TAG = GateDetectActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private LinearLayout teGatell;
    private EditText etName;
    private ImageView faceImg;

    private GateFace mGateFace;
    private List<GateFace> gateFaceList = new ArrayList<>();
    private DateTime mDateTime = DateTime.getDTInstance();


    private String returnUserId;
    private String returnUserName;


    @Override
    public int getLayoutId() {
        return R.layout.activity_gate_access;
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
        tv_title.setText(R.string.gate_result);

        teGatell = findViewById(R.id.te_gate_name);
        TextView tvName = (TextView)teGatell.findViewById(R.id.tv_name);
        tvName.setText(R.string.user_name);
        etName = (EditText)teGatell.findViewById(R.id.et_name);

        faceImg = findViewById(R.id.face_iv);
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
        String dateTime = mDateTime.convertTimeToString(mDateTime.getSystemTime());

        if((returnUserId != null) && (returnUserName != null)) {
            mGateFace = (GateFace) greenDaoManager.queryFaceByIdName(returnUserId, returnUserName, Config.GateGroupId);
        }else {
            mToastInstance.showLongToast("请先注册个人信息");
            return;
        }

        if(mGateFace != null) {
            etName.setText(mGateFace.getUserName());
            etName.setEnabled(false);

            faceImg.setVisibility(View.VISIBLE);
            Bitmap bmp = ImageSaveUtil.loadCameraBitmap(this, "head_tmp.jpg");
            if (bmp != null) {
                faceImg.setImageBitmap(bmp);
            }

            mToastInstance.showLongToast("安全通过");

            greenDaoManager.updateDetectTime(returnUserId, returnUserName, dateTime, Config.GateGroupId);
        }
    }

    @Override
    protected GatePresenter createPresenter() {
        return new GatePresenter(this);
    }


}
