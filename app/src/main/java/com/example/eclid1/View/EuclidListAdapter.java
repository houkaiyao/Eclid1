package com.example.eclid1.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eclid1.R;
import com.example.eclid1.View.EuclidActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {

    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";
    public static final String KEY_TELL_PHONE = "TELL_PHONE";
    public static final String KEY_DATA_TIME = "DATA_TIME";
    private final LayoutInflater mInflater;
    private List<Map<String, Object>> mData;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mViewOverlay = convertView.findViewById(R.id.view_avatar_overlay);
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            viewHolder.mTellPhone = (TextView) convertView.findViewById(R.id.tellphone);
            viewHolder.mDataTime = (TextView) convertView.findViewById(R.id.datetime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Picasso.with(getContext()).load((String) mData.get(position).get(KEY_AVATAR))
//                .resize(com.example.mylibrary.EuclidActivity.sScreenWidth, com.example.mylibrary.EuclidActivity.sProfileImageHeight).centerCrop()
//                .placeholder(R.color.blue)
//                .into(viewHolder.mListItemAvatar);
        Glide.with(getContext()).load((String) mData.get(position).get(KEY_AVATAR))
                .override(com.example.eclid1.View.EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight)
                .centerCrop()
                .placeholder(R.color.blue)
                .into(viewHolder.mListItemAvatar);
        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemDescription.setText((String) mData.get(position).get(KEY_DESCRIPTION_SHORT));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);

        return convertView;
    }

    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;
        TextView mTellPhone;
        TextView mDataTime;
    }
}
