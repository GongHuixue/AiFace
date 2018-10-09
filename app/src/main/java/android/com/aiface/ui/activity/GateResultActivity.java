package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.GatePresenter;
import android.com.aiface.ui.view.IGateView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GateResultActivity extends BaseActivity<IGateView, GatePresenter> implements IGateView {
    private final static String TAG = GateResultActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private List<GateFace> gateFaceList = new ArrayList<>();

    @Override
    public void initData() {
        gateFaceList = greenDaoManager.getGateInfo();

        if(gateFaceList.size() <= 0) {
            mToastInstance.showShortToast("没有任何用户记录");
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
        tv_title.setText(R.string.gate_result);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_gate_result;
    }

    @Override
    protected GatePresenter createPresenter() {
        return new GatePresenter(this);
    }
}
