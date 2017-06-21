package com.example.finalproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    private static AsyncTask asyncTask;
    private static Handler handler = new Handler();
    private static Runnable runnableUi;
    private static Runnable runnableUi2;
    private static Runnable runnableUi3;
    private static Runnable runnableUi4;
    private static String url = "http://114.115.212.203:8001/";
    private static String aUrl;
    private final static Object[] finalUrl = new Object[1];

    private static ArrayList<History> historyArrayList;
    private static int pos;
    private static ArrayList<String> frequent = new ArrayList<String>();
    private static ArrayList<String> pid = new ArrayList<String>();
    private static ArrayList<String> a_Url = new ArrayList<String>();
    private static JsonArray tags;

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

        final EditText search_text = (EditText) findViewById(R.id.search_text);
        Button search_btn = (Button) findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                a_Url.clear();
                pid.clear();

                new Thread(new Runnable() {
                    public void run() {
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
                            Response responseUrl;
                            try {
                                responseUrl = okHttpClient.newCall(requestUrl).execute();
                                JsonObject jsonObject2 = new JsonParser().parse(responseUrl.body().string()).getAsJsonObject();
                                a_Url.add("http://114.115.212.203:8001" + jsonObject2.getAsJsonPrimitive("url").getAsString());
                                Log.e("--------", "http://114.115.212.203:8001" + jsonObject2.getAsJsonPrimitive("url").getAsString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        handler.post(runnableUi2);
                    }
                }).start();
            }
        });

        final Context context = this;
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

        runnableUi2 = new Runnable() {
            @Override
            public void run() {
                listView = (ListView)findViewById(R.id.History_listview);
                pos = 0;
                new Thread() {
                    @Override
                    public void run() {
                        for(int k=0; k<5; k++) {
                            if (pos < a_Url.size()) {
                                // 加载标签
                                for (int i=0; i<pid.size(); i++) {
                                    Log.e("pid", String.valueOf(pid.get(i)));
                                    Request request = new Request.Builder().url("http://114.115.212.203:8001/tags_by_id/?pid=" + pid.get(i)).get().build();
                                    Response response = null;
                                    try {
                                        response = okHttpClient.newCall(request).execute();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    tags = null;
                                    try {
                                        JsonObject object1 = new JsonParser().parse(response.body().string()).getAsJsonObject();
                                        tags = object1.getAsJsonArray("tags");
                                        Log.e("((()))", tags.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println(tags);

                                    ArrayList<String> a_tag = new ArrayList<String>();
                                    for(int j=0; j<tags.size(); j++){
                                        a_tag.add(tags.get(j).getAsString());
                                    }

                                    list.add(new History(pid.get(i), a_Url.get(i), a_tag.toArray(new String[tags.size()])));
                                }
                            }
                            pos++;
                        }
                        handler.post(runnableUi3);
                    }
                }.start();
            }
        };

        runnableUi3 = new Runnable() {
            @Override
            public void run() {
                adapter = new HistoryAdapter(context, list);
                listView.setAdapter(adapter);
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        for(int i=0; i<5; i++) {
                                            if (pos < a_Url.size()) {
                                                // 加载标签
                                                Log.e("pid", String.valueOf(pid.get(0)));
                                                Request request = new Request.Builder().url("http://114.115.212.203:8001/tags_by_id/?pid=" + pid.get(0)).get().build();
                                                Response response = null;
                                                try {
                                                    response = okHttpClient.newCall(request).execute();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                tags = null;
                                                try {
                                                    JsonObject object1 = new JsonParser().parse(response.body().string()).getAsJsonObject();
                                                    tags = object1.getAsJsonArray("tags");
                                                    Log.e("((()))", tags.toString());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                System.out.println(tags);

                                                ArrayList<String> a_tag = new ArrayList<String>();
                                                for(int j=0; j<tags.size(); j++){
                                                    a_tag.add(tags.get(j).getAsString());
                                                }
                                                list.add(new History(a_Url.get(i), a_tag.toArray(new String[tags.size()])));
                                            }
                                            pos++;
                                        }
                                        handler.post(runnableUi3 );
                                    }
                                }.start();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        Toast.makeText(HistoryActivity.this, "LongClicked", Toast.LENGTH_SHORT).show();

                        final StringBuilder sb = new StringBuilder();
                        for (int i=0; i<list.get(position).getTags().length; i++) {
                            sb.append(list.get(position).getTags()[i]).append(",");
                        }
                        if (!sb.equals("")) {
                            sb.deleteCharAt(sb.length()-1);
                        }

                        Log.e("sb", sb.toString());

                        new Thread() {
                            @Override
                            public void run() {
                                // 删除标签
                                RequestBody requestBodyDel = new FormBody.Builder().add("pid", list.get(position).getPid()).add("tags", sb.toString()).build();
                                Request requestDel = new Request.Builder().url("http://114.115.212.203:8001/del_tags/").post(requestBodyDel).build();
                                Response responseDel = null;
                                try {
                                    responseDel = okHttpClient.newCall(requestDel).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String bodyDel = null;
                                try {
                                    bodyDel = responseDel.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(bodyDel);

                                list.remove(position);
                                handler.post(runnableUi4);
                            }

                        }.start();

                        return true;
                    }
                });
            }
        };

        runnableUi4 = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
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
