package android.com.aiface.ui.fragment;

import android.com.aiface.R;
import android.com.aiface.ui.activity.MainActivity;
import android.com.aiface.ui.adapter.FragListItem;
import android.com.aiface.ui.adapter.FragListItemAdapter;
import android.com.aiface.ui.base.BaseFragment;
import android.com.aiface.ui.presenter.MePresenter;
import android.com.aiface.ui.view.IMeView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends BaseFragment<IMeView, MePresenter> implements IMeView{
    private List<FragListItem> meListItems = new ArrayList<>();
    private FragListItemAdapter fragListItemAdapter;
    @Override
    protected MePresenter createPresenter() {
        return new MePresenter((MainActivity)getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.me_fragment;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {
        initListViewData();
        ListView listView = (ListView)view.findViewById(R.id.me_lv);
        fragListItemAdapter = new FragListItemAdapter(getActivity(), R.layout.me_list_item, meListItems);
        listView.setAdapter(fragListItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startLaunchActivity(position);
            }
        });
    }

    @Override
    public void initMeView() {

    }

    private void initListViewData() {
        FragListItem meeting = new FragListItem("会议记录", R.drawable.meeting);
        FragListItem attendance = new FragListItem("我的考勤", R.drawable.attendance);
        FragListItem home = new FragListItem("访客记录", R.drawable.home);
        FragListItem gate = new FragListItem("安检闸机", R.drawable.gate);

        meListItems.add(meeting);
        meListItems.add(attendance);
        meListItems.add(home);
        meListItems.add(gate);
    }

    private void startLaunchActivity(int position) {

    }
}
