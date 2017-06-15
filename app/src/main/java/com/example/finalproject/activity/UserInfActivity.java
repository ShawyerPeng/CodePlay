package com.example.finalproject.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.UserInfAdapter;
import com.example.finalproject.entity.Tag;
import com.example.finalproject.entity.User;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfActivity extends AppCompatActivity {
    private static SharedPreferences sharedPreferences;
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private static Runnable runnableUi;
    private static String url = "http://114.115.212.203:8001/";
    private static Uri uri;
    private static String pid;

    Vector<Map<String,Object>> list=new Vector<Map<String, Object>>();
    private static String username;
    private static String id;
    private static String email;
    private static String phone;
    private static String career;
    private static String birthday;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inf);

        EditText UserInf = (EditText)findViewById(R.id.UserInf);
        Button btn_edit_info = (Button)findViewById(R.id.btn_edit_info);
        Button btn_edit_submit = (Button)findViewById(R.id.btn_edit_submit);

        handler = new Handler();
        final UserInfAdapter adapter = new UserInfAdapter(this);
        runnableUi = new Runnable(){
            @Override
            public void run() {
                adapter.setList(list);
                listView = (ListView) findViewById(R.id.UserInf_listview);
                listView.setAdapter(adapter);
            }
        };

        btn_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(2).put("isEditable", "true");
                list.get(3).put("isEditable", "true");
                list.get(4).put("isEditable", "true");
                list.get(5).put("isEditable", "true");
                adapter.notifyDataSetChanged();
            }
        });

        btn_edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 上传修改后的文本到服务器
                HashMap text = (HashMap)listView.getItemAtPosition(3);
                System.out.println(text.get("inf"));
                for(int i=0; i<list.size(); i++) {
                    list.get(i).put("isEditable", "false");
                }
                adapter.notifyDataSetChanged();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", null);
                String password = sharedPreferences.getString("password", null);
                RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                Request requestPost = new Request.Builder().url("http://114.212.203:8001/do_login/").post(requestBodyPost).build();
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

                Request requestInfo = new Request.Builder().url("http://114.115.212.203:8001/info/").build();
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
                System.out.println(bodyInfo);

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(bodyInfo).getAsJsonObject();

                JsonObject user = object.getAsJsonObject("user");
                User aUser = gson.fromJson(user, User.class);
                System.out.println(aUser.toString());

                JsonArray tagArray = object.getAsJsonArray("tags");
                List<Tag> tagList = new LinkedList<Tag>();
                for (int i = 0; i < tagArray.size(); i++) {
                    JsonObject tags = tagArray.get(i).getAsJsonObject();
                    String tag = tags.get("tag").getAsString();
                    String frequent = tags.get("frequent").getAsString();
                    System.out.println(tag + " " + frequent);

                    tagList.add(new Tag(tag, frequent));
                }
                for (Tag tag : tagList) {
                    System.out.println(tag.getTag() + "      " + tag.getFrequent());
                }

                username = aUser.getUname();
                id = aUser.getUhonesty();
                email = aUser.getUemail();
                phone = aUser.getUsex();
                career = aUser.getUindentity();
                birthday = aUser.getUhonesty();


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", "用户名:");
                map.put("inf", username);
                map.put("isEditable", "false");
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", "身份证号:");
                map.put("inf", id);
                map.put("isEditable", "false");
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", "邮箱:");
                map.put("inf", email);
                map.put("isEditable", "false");
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", "电话:");
                map.put("inf", phone);
                map.put("isEditable", "false");
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", "职业:");
                map.put("inf", career);
                map.put("isEditable", "false");
                list.add(map);

                map = new HashMap<String, Object>();
                map.put("name", "生日:");
                map.put("inf", birthday);
                map.put("isEditable", "false");
                list.add(map);

                handler.post(runnableUi);
            }
        }).start();

    }

}
