package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.entity.Menu;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<Menu> {
    private int resourceId;
    public MenuAdapter(Context  context, int textViewResourceId, List<Menu> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Menu menu = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView menuimage= (ImageView) view.findViewById(R.id.menu_ico);
        TextView menuname = (TextView) view.findViewById(R.id.menu_name);
        menuimage.setImageResource(menu.getImageid());
        menuname.setText(menu.getName());
        return view;
    }
}
