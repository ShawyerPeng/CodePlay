package com.example.finalproject.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HistoryAdapter;
import com.example.finalproject.entity.History;

import java.util.Vector;

public class HistoryActivity extends Activity  {
    int num;
    HistoryAdapter adapter;
    Handler handler = new Handler();
    Vector<History> list = new Vector<History>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = (ListView) findViewById(R.id.History_listview);
        initData();
        adapter = new HistoryAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        load();
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }
    public void initData(){
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
    }

    public void load(){
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
        list.add(new History(R.drawable.logo,"标签1,标签2,标签3,\n" + "标签1,标签2,标签3"));
    }



}
