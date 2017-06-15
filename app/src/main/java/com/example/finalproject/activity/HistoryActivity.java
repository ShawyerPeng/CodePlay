package com.example.finalproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.HistoryAdapter;
import com.example.finalproject.entity.History;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends Activity {
    private static SharedPreferences sharedPreferences;
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler = new Handler();
    private static Runnable runnableUi;
    private static String url = "http://114.115.212.203:8001/";
    private static String aUrl;
    private final static Object[] finalUrl = new Object[1];

    private static ArrayList<History> historyArrayList;
    private static int pos;

    private static HistoryAdapter adapter;
    private static Vector<History> list = new Vector<History>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        pos = 0;

        new Thread(new Runnable() {
            public void run() {
                cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                String password = sharedPreferences.getString("password", null);
                System.out.println(username + " " + password);
                RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/do_login/").post(requestBodyPost).build();
                okHttpClient.newCall(requestPost).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("TaggingActivity", "请求登录失败！");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("TaggingActivity", response.body().string());
                    }
                });

                Request requestInfo = new Request.Builder().url("http://114.115.212.203:8001/history/").build();
                Response responseInfo = null;
                try {
                    responseInfo = okHttpClient.newCall(requestInfo).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String bodyInfo = null;
                try {
                    bodyInfo = responseInfo.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(bodyInfo).getAsJsonObject();
                JsonObject history = object.getAsJsonObject("history");

                historyArrayList = new ArrayList<History>();
                ArrayList<String> tags = new ArrayList<String>();
                for (Map.Entry<String, JsonElement> entry : history.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    JsonArray jsonArray = entry.getValue().getAsJsonArray();
                    for (int i=0; i<jsonArray.size(); i++) {
                        tags.add(jsonArray.get(i).getAsString());
                    }
                    historyArrayList.add(new History(entry.getKey(), tags.toArray(new String[tags.size()])));
                    tags.clear();
                }

                handler.post(runnableUi);
            }
        }).start();

        final Context context = HistoryActivity.this;
        runnableUi = new Runnable() {
            @Override
            public void run() {
                listView = (ListView)findViewById(R.id.History_listview);
                initData();

                adapter = new HistoryAdapter(context, list);
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
        };
    }

    public void initData(){
        for(int i=0; i<5; i++) {
            if (pos < historyArrayList.size()) {
                list.add(new History(getUrlByPid(historyArrayList.get(pos).getImageid()), historyArrayList.get(pos).getTags()));
            }
            pos++;
        }
    }
    public void load(){
        for(int i=0; i<5; i++) {
            if (pos < historyArrayList.size()) {
                list.add(new History(getUrlByPid(historyArrayList.get(pos).getImageid()), historyArrayList.get(pos).getTags()));
            }
            pos++;
        }
    }

    public String getUrlByPid(String pid) {
        RequestBody requestBodyUrl = new FormBody.Builder().add("pid", pid).build();
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
                aUrl = jsonObject.getAsJsonPrimitive("url").getAsString();
                finals[0] = HistoryActivity.url + aUrl.substring(1, aUrl.length());
                Log.e("Final Url",(String) finals[0]);
            }
        });

        while (finals[0] == null) {}
        return (String)finals[0];
    }
}
