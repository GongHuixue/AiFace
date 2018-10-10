package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.HomeFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;

import java.util.ArrayList;
import java.util.List;

public class HomeResultActivity extends BaseActivity<IHomeView, HomePresenter> implements IHomeView {
    private final static String TAG = HomeResultActivity.class.getSimpleName();
    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private HomeFace mHomeFace;
    private List<HomeFace> homeFaceList = new ArrayList<>();
    private SmartTable<HomeFace> table;
    @Override
    public void initData() {
        getHostInformation();

        Log.d(TAG, "Vistor Size = " + homeFaceList.size());
        if(homeFaceList.size() <= 0) {
            mToastInstance.showShortToast("没有任何来访者");
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
        tv_title.setText(R.string.home_result);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home_result;
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
