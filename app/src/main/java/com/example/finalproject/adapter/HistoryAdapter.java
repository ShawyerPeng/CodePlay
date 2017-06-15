package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.entity.History;

import java.util.Vector;

import me.gujun.android.taggroup.TagGroup;

public class HistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Vector<History> list;
    private History history;

    @Override
    public int getCount() {
        return list.size();
    }

    public HistoryAdapter(Context context, Vector<History> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.history_item, null);
        history = list.get(position);

        // TextView textView = (TextView) view.findViewById(R.id.History_tags);
        // textView.setText(history.getTags());
        // SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.History_image);
        // draweeView.setImageURI(Uri.parse(history.getImageid()).toString());

        ImageView imageView = (ImageView) view.findViewById(R.id.History_image);
        Glide.with(view).load(history.getImageid()).into(imageView);

        TagGroup mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);
        mTagGroup.setTags(history.getTags());

        return view;
    }
}
