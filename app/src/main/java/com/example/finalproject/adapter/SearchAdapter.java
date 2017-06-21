package com.example.finalproject.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.finalproject.R;
import com.example.finalproject.entity.Search;

import java.util.ArrayList;

public class SearchAdapter extends BaseQuickAdapter<Search, BaseViewHolder> {
    public SearchAdapter(Context context, ArrayList<Search> list) {
        super(R.layout.activity_search, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Search item) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<item.getTags().length; i++) {
            sb.append(item.getTags()[i]).append(",");
        }
        helper.setText(R.id.first, item.getPid()+" ")
                .setText(R.id.second, sb);

        System.out.println(item.getPid()+" " + sb);
    }
}
