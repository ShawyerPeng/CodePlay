package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.entity.History;

import java.util.Vector;

public class HistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Vector<History> list;
    History history;

    @Override
    public int getCount() {
        return list.size();
    }

    public HistoryAdapter(Context context,Vector<History> list){
        this.list=list;
        inflater= LayoutInflater.from(context);
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
        ImageView imageView = (ImageView) view.findViewById(R.id.History_image);
        TextView textView = (TextView) view.findViewById(R.id.History_tags);
        history=list.get(position);
        imageView.setImageResource(history.getImageid());
        textView.setText(history.getTags());
        return view;
    }
}
