package android.com.aiface.ui.adapter;

import android.com.aiface.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/2.
 */

public class FragListItemAdapter extends ArrayAdapter {
    private int resourceId;

    public FragListItemAdapter(Context context, int textViewResourceId, List<FragListItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FragListItem fragListItem = (FragListItem) getItem(position);
        View view;

        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        ImageView listItemImage = (ImageView)view.findViewById(R.id.face_item_image);
        TextView listItemName = (TextView) view.findViewById(R.id.face_item_name);
        listItemImage.setImageResource(fragListItem.getImageId());
        listItemName.setText(fragListItem.getItemName());

        return view;
    }
}
