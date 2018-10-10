package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.AttendancePresenter;
import android.com.aiface.ui.view.IAttendanceView;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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

public class AttendanceResultActivity extends BaseActivity<IAttendanceView, AttendancePresenter> implements IAttendanceView {
    private final static String TAG = AttendanceResultActivity.class.getSimpleName();
    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private SmartTable<AttendanceFace> table;

    private List<AttendanceFace> mAttendanceFacelist = new ArrayList<>();
    private Column<String> userNameCol, userPartCol, onWorkTimeCol, offWorkTimeCol;

    @Override
    public void initData() {
        getAttendanceMember();
        final List<AttendanceFace> faceList = new ArrayList<>();
        Log.d(TAG, "member list size = " + mAttendanceFacelist.size());
        if(mAttendanceFacelist.size() == 0) {
            mToastInstance.showLongToast("目前没有任何员工信息");
        }else if(mAttendanceFacelist.size() > 0) {
            for(int i = 0; i < mAttendanceFacelist.size(); i ++) {
                faceList.add(new AttendanceFace(mAttendanceFacelist.get(i).getId(),
                        mAttendanceFacelist.get(i).getUserId(),
                        mAttendanceFacelist.get(i).getAttendancePart(),
                        mAttendanceFacelist.get(i).getAttendanceName(),
                        mAttendanceFacelist.get(i).getOnworktime(),
                        mAttendanceFacelist.get(i).getOffworktime()));
            }

            Log.d(TAG, "faceList = " + faceList.size());
            final Column<String> userNameCol = new Column<>("姓名", "attendanceName");
            userNameCol.setAutoCount(true);
            final Column<String> userPartCol = new Column<>("部门", "attendancePart");
            userPartCol.setAutoCount(true);
            final Column<String> onWorkTimeCol = new Column<>("上班时间", "onworktime");
            onWorkTimeCol.setAutoCount(true);
            final Column<String> offWorkTimeCol = new Column<>("下班时间", "offworktime");
            offWorkTimeCol.setAutoCount(true);

            final TableData<AttendanceFace> tableData = new TableData<>("Attendance", faceList, userNameCol, userPartCol, onWorkTimeCol, offWorkTimeCol);
            tableData.setShowCount(true);
            table.setTableData(tableData);
            table.setZoom(true, 2, 0.2f);
            table.getConfig().setShowTableTitle(false);//no need show table title.
            table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if(cellInfo.row%2 == 0){
                        return ContextCompat.getColor(AttendanceResultActivity.this,R.color.line);
                    }
                    return TableConfig.INVALID_COLOR;
                }
            });
        }
    }

    @Override
    public void initView() {
        Log.d(TAG, "init View");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.attendance_result);

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this,15));
        table = (SmartTable<AttendanceFace>)findViewById(R.id.table);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attendance_result;
    }


    @Override
    protected AttendancePresenter createPresenter() {
        return new AttendancePresenter(this);
    }

    @Override
    public void getAttendanceMember() {
        mAttendanceFacelist = greenDaoManager.getAttendanceInfo();
    }
}
