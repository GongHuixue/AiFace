package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.database.bean.HomeFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.HomePresenter;
import android.com.aiface.ui.view.IHomeView;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.utils.DensityUtils;

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
        final List<HomeFace> faceList = new ArrayList<>();
        Log.d(TAG, "Vistor Size = " + homeFaceList.size());
        if(homeFaceList.size() <= 0) {
            mToastInstance.showShortToast("没有任何来访者");
        }else {
            for(int i = 0; i < homeFaceList.size(); i++) {
                Log.d(TAG, "visit time = " + homeFaceList.get(i).getVisitTime());
                faceList.add(new HomeFace(homeFaceList.get(i).getId(),
                        homeFaceList.get(i).getHomeAddr(),
                        homeFaceList.get(i).getHostName(),
                        homeFaceList.get(i).getUserId(),
                        homeFaceList.get(i).getGustName(),
                        homeFaceList.get(i).getVisitTime()));
            }
            Log.d(TAG, "faceList = " + faceList.size());
            final Column<String> userName = new Column<String>("姓名", "gustName");
            userName.setAutoCount(true);
            final Column<String> detectTime = new Column<String>("到访时间", "visitTime");
            detectTime.setAutoCount(true);
            final TableData<HomeFace> tableData = new TableData<HomeFace>("", faceList, userName, detectTime);
            tableData.setShowCount(true);
            table.setTableData(tableData);
            table.setZoom(true, 2, 0.2f);
            table.getConfig().setShowTableTitle(false);//no need show table title.
            table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if(cellInfo.row%2 == 0){
                        return ContextCompat.getColor(HomeResultActivity.this,R.color.line);
                    }
                    return TableConfig.INVALID_COLOR;
                }
            });
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

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this,15));
        table = (SmartTable<HomeFace>)findViewById(R.id.table);

        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int screenWith = outMetrics.widthPixels;
        table.getConfig().setMinTableWidth(screenWith); //设置最小宽度 屏幕宽度

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
