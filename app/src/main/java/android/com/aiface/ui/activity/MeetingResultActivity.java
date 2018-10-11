package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.AttendanceFace;
import android.com.aiface.database.bean.MeetingFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class MeetingResultActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView {
    private final static String TAG = MeetingResultActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private List<MeetingFace> meetingFaceList = new ArrayList<>();
    private SmartTable<MeetingFace> table;

    @Override
    public void initData() {
        getMeetingInformation();
        final List<MeetingFace> faceList = new ArrayList<>();
        Log.d(TAG, "Meeting size = " + meetingFaceList.size());

        if(meetingFaceList.size() > 0) {
            for (int i = 0; i < meetingFaceList.size(); i++) {
                faceList.add(new MeetingFace(meetingFaceList.get(i).getId(),
                        meetingFaceList.get(i).getMeetingName(),
                        meetingFaceList.get(i).getMeetingTime(),
                        meetingFaceList.get(i).getMeetingTimeString(),
                        meetingFaceList.get(i).getMeetingAddr(),
                        meetingFaceList.get(i).getUserId(),
                        meetingFaceList.get(i).getParticipantName(),
                        meetingFaceList.get(i).getParticipantPart(),
                        meetingFaceList.get(i).getSigned()));
            }
            Log.d(TAG, "faceList = " + faceList.size());
            final Column<String> meetingName = new Column<String>("会议名称", "meetingName");
            meetingName.setAutoCount(true);
            final Column<String> meetingTime = new Column<String>("会议时间", "meetingTimeString");
            meetingTime.setAutoCount(true);
            final Column<String> meetingAddr = new Column<String>("会议地址","meetingAddr");
            meetingAddr.setAutoCount(true);
            final Column<String> userName = new Column<String>("参会者姓名","participantName");
            userName.setAutoCount(true);
            final Column<String> userPart = new Column<String>("参会者部门","participantPart");
            userPart.setAutoCount(true);
            final Column<Boolean> signed = new Column<>("是否签到", "signed");
            signed.setAutoCount(true);

            final TableData<MeetingFace> tableData = new TableData<MeetingFace>("", faceList, meetingName, meetingTime, meetingAddr,
                    userName, userPart, signed);
            tableData.setShowCount(true);
            table.setTableData(tableData);
            table.setZoom(true, 2, 0.2f);
            table.getConfig().setShowTableTitle(false);//no need show table title.
            table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                @Override
                public int getBackGroundColor(CellInfo cellInfo) {
                    if(cellInfo.row%2 == 0){
                        return ContextCompat.getColor(MeetingResultActivity.this,R.color.line);
                    }
                    return TableConfig.INVALID_COLOR;
                }
            });
        }else {
            mToastInstance.showLongToast("没有任何注册的会议");
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
        tv_title.setText(R.string.meeting_result);

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this,15));
        table = (SmartTable<MeetingFace>)findViewById(R.id.table);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_meeting_result;
    }

    @Override
    protected MeetingPresenter createPresenter() {
        return new MeetingPresenter(this);
    }

    @Override
    public void getMeetingInformation() {
        meetingFaceList = greenDaoManager.getMeetingInfo();
    }
}
