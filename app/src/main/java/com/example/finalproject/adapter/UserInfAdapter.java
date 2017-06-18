package com.example.finalproject.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.Map;
import java.util.Vector;

public class UserInfAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Vector<Map<String,Object>> list;

    @Override
    public int getCount() {
        return list.size();
    }

    public UserInfAdapter(Context context){
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

    public void setList(Vector<Map<String,Object>> list){
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Map<String,Object> map = list.get(position);
        View view = inflater.inflate(R.layout.userinf_item, null);

        TextView name = (TextView) view.findViewById(R.id.UserInfName);
        EditText inf = (EditText) view.findViewById(R.id.UserInf);
        name.setText((String)map.get("name"));
        inf.setText(map.get("inf").toString());

        if ((map.get("isEditable")).equals("true")) {
            inf.setEnabled(true);
        } else {
            inf.setEnabled(false);
        }

        inf.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                map.put("inf", s.toString());
            }
        });

        return view;
    }
}
