package com.example.finalproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.finalproject.R;
import com.example.finalproject.entity.History;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Vector;

import me.gujun.android.taggroup.TagGroup;

public class HistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Vector<History> list;
    History history;

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
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.History_image);
        TagGroup mTagGroup = (TagGroup) view.findViewById(R.id.tag_group);

        history = list.get(position);

//        ImageView imageView = (ImageView) view.findViewById(R.id.History_image);
//        imageView.setImageURI(Uri.parse(history.getImageid()));
//        System.out.println("********" + Uri.parse(history.getImageid()));
//        TextView textView = (TextView) view.findViewById(R.id.History_tags);
//        textView.setText(history.getTags());

        draweeView.setImageURI(Uri.parse(history.getImageid()).toString());
        mTagGroup.setTags(history.getTags());

        return view;
    }
}
