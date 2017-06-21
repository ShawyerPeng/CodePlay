package com.example.finalproject.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.SearchAdapter;
import com.example.finalproject.entity.Search;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler();
    private static Runnable runnableUi;
    private static String url = "http://114.115.212.203:8001";
    private static JsonArray pic;
    private static ArrayList<String> frequent = new ArrayList<String>();
    private static ArrayList<String> pid = new ArrayList<String>();
    private static ArrayList<String> aUrl = new ArrayList<String>();
    private static ArrayList<Search> list;

    private RecyclerView mRecyclerView;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = new ArrayList<Search>();
        list.add(new Search("001",new String[]{"1","2","3"}));
        list.add(new Search("002",new String[]{"4","5","6"}));
        list.add(new Search("003",new String[]{"7","8","9"}));
        adapter = new SearchAdapter(this, list);

        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        final EditText search_text = (EditText) findViewById(R.id.search_text);
        final Button search_btn = (Button) findViewById(R.id.search_btn);
        final TextView search_result = (TextView) findViewById(R.id.search_result);

//        String username = sharedPreferences.getString("username", null);
//        String password = sharedPreferences.getString("password", null);

//        mRecyclerView = (RecyclerView) findViewById(R.id.search_list);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
//        mRecyclerView.setAdapter(adapter);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
                        RequestBody requestBodyPost = new FormBody.Builder().add("name", "admin").add("pwd", "123456").build();
                        Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/do_login/").post(requestBodyPost).build();
                        okHttpClient.newCall(requestPost).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                            }
                        });

                        Request requestSearch = new Request.Builder().url("http://114.115.212.203:8001/search_by_tag/?tag=" + search_text.getText().toString()).build();
                        Response responseSearch = null;
                        try {
                            responseSearch = okHttpClient.newCall(requestSearch).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String bodySearch = null;
                        try {
                            bodySearch = responseSearch.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(bodySearch);

                        JsonObject jsonObject = new JsonParser().parse(bodySearch).getAsJsonObject();
                        JsonArray pic = jsonObject.getAsJsonArray("pic");

                        for (int i=0; i<pic.size(); i++) {
                            JsonObject one = pic.get(i).getAsJsonObject();
                            frequent.add(one.get("frequent").getAsString());
                            pid.add(one.get("pid").getAsString());
                        }

                        // 获取图片
                        for (int i=0; i<pid.size(); i++) {
                            RequestBody requestBodyUrl = new FormBody.Builder().add("pid", pid.get(i)).build();
                            Request requestUrl = new Request.Builder().url("http://114.115.212.203:8001/getpicbyid/").post(requestBodyUrl).build();

                            final Object[] finals = new Object[1];
                            okHttpClient.newCall(requestUrl).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("HistoryActivity", "请求登录失败！");
                                }
                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
                                    aUrl.add(url + jsonObject.getAsJsonPrimitive("url").getAsString());
                                    Log.e("--------", url + jsonObject.getAsJsonPrimitive("url").getAsString());
                                }
                            });
                        }


//                        // 删除标签
//                        String pid_del = "1";
//                        String tags_del = "蜘蛛侠";
//                        RequestBody requestBodyDel = new FormBody.Builder().add("pid", pid_del).add("tags", tags_del).build();
//                        Request requestDel = new Request.Builder().url("http://114.115.212.203:8001/del_tags/").post(requestBodyDel).build();
//                        Response responseDel = null;
//                        try {
//                            responseDel = okHttpClient.newCall(requestDel).execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        String bodyDel = null;
//                        try {
//                            bodyDel = responseDel.body().string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println(bodyDel);

                        handler.post(runnableUi);
                    }
                }).start();
            }
        });

        runnableUi = new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<Search>();
                for (int i=0; i<aUrl.size(); i++) {
                    list.add(new Search(aUrl.get(i), new String[]{"1","2","3"}));
                    Log.e("-----", list.toString());
                    adapter = new SearchAdapter(SearchActivity.this, list);
                    mRecyclerView.setAdapter(adapter);
                }
            }
        };



    }
}
