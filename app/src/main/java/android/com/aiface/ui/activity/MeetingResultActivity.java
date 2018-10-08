package android.com.aiface.ui.activity;

import android.com.aiface.R;
import android.com.aiface.database.bean.MeetingFace;
import android.com.aiface.ui.base.BaseActivity;
import android.com.aiface.ui.presenter.MeetingPresenter;
import android.com.aiface.ui.view.IMeetingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MeetingResultActivity extends BaseActivity<IMeetingView, MeetingPresenter> implements IMeetingView {
    private final static String TAG = MeetingResultActivity.class.getSimpleName();

    /*top action bar*/
    private ImageView iv_back;
    private TextView tv_title;

    private List<MeetingFace> meetingFaceList = new ArrayList<>();

    @Override
    public void initData() {
        getMeetingInformation();
        Log.d(TAG, "Meeting size = " + meetingFaceList.size());
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
