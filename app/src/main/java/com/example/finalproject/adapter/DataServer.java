package com.example.finalproject.adapter;

import com.example.finalproject.entity.Search;

import java.util.ArrayList;
import java.util.List;

public class DataServer {
    private DataServer() {
    }

    public static List<Search> getSampleData(int lenth) {
        List<Search> list = new ArrayList<>();
        for (int i = 0; i < lenth; i++) {
            Search status = new Search();
            status.setPid("Chad" + i);
            status.setTags(new String[]{"1","2","3"});
            list.add(status);
        }
        return list;
    }

    public static List<Search> addData(List list, int dataSize) {
        for (int i = 0; i < dataSize; i++) {
            Search status = new Search();
            status.setPid("Chad" + i);
            status.setTags(new String[]{"1","2","3"});
            list.add(status);
        }

        return list;
    }
}
