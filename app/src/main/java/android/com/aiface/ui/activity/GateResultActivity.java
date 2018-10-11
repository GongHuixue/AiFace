package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.database.bean.GateFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.GatePresenter;
import android.com.aiface.ui.view.IGateView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GateResultActivity extends BaseActivity<IGateView, GatePresenter> implements IGateView {
    private final static String TAG = GateResultActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private List<GateFace> gateFaceList = new ArrayList<>();
    private SmartTable<GateFace> table;

    @Override
    public void initData() {
        gateFaceList = greenDaoManager.getGateInfo();
        final List<GateFace> faceList = new ArrayList<>();
        if(gateFaceList.size() <= 0) {
            mToastInstance.showShortToast("没有任何用户记录");
        }else {
            for(int i = 0; i < gateFaceList.size(); i ++) {
                faceList.add(new GateFace(gateFaceList.get(i).getId(),
                        gateFaceList.get(i).getUserName(),
                        gateFaceList.get(i).getUserId(),
                        gateFaceList.get(i).getCheckTime()));
            }

            final Column<String> userName = new Column<String>("姓名", "userName");
            userName.setAutoCount(true);
            final Column<String> detectTime = new Column<String>("时间", "checkTime");
            detectTime.setAutoCount(true);
            final TableData<GateFace> tableData = new TableData<GateFace>("", faceList, userName, detectTime);
            tableData.setShowCount(true);
            table.setTableData(tableData);
            table.setZoom(true, 2, 0.2f);
            table.getConfig().setShowTableTitle(false);//no need show table title.
            table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if(cellInfo.row%2 == 0){
                        return ContextCompat.getColor(GateResultActivity.this,R.color.line);
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
        tv_title.setText(R.string.gate_result);

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this,15));
        table = (SmartTable<GateFace>)findViewById(R.id.table);

        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int screenWith = outMetrics.widthPixels;
        table.getConfig().setMinTableWidth(screenWith); //设置最小宽度 屏幕宽度
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
