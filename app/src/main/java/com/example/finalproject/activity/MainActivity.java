package com.example.finalproject.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalproject.R;
import com.example.finalproject.entity.Tag;
import com.example.finalproject.entity.User;
import com.facebook.drawee.view.SimpleDraweeView;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private static Runnable runnableUi;
    private static String url = "http://114.115.212.203:8001/";
    private static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.tag);
        Button button = (Button) findViewById(R.id.button);
        Button button1 = (Button) findViewById(R.id.imgButton);
        Button button2 = (Button) findViewById(R.id.submit);

        // 创建属于主线程的handler
        handler = new Handler();
        runnableUi = new Runnable(){
            @Override
            public void run() {
                SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                draweeView.setImageURI(uri);
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                        RequestBody requestBodyPost = new FormBody.Builder().add("name", "admin").add("pwd", "123456").build();
                        Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/do_login/").post(requestBodyPost).build();
                        okHttpClient.newCall(requestPost).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("MainActivity", "请求失败！");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    System.out.println(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   new Thread(new Runnable() {
                       public void run() {
                           Request requestGet = new Request.Builder().url("http://114.115.212.203:8001/info/").build();
                           Response response = null;
                           try {
                               response = okHttpClient.newCall(requestGet).execute();
                               String body = response.body().string();

                               Gson gson = new Gson();
                               JsonParser parser = new JsonParser();
                               JsonObject object = parser.parse(body).getAsJsonObject();

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

                               Request requestImg = new Request.Builder().url("http://114.115.212.203:8001/getPic/").build();
                               Response responseImg = okHttpClient.newCall(requestImg).execute();
                               String bodyImg = responseImg.body().string();
                               System.out.println(bodyImg);

                               JsonObject pic = parser.parse(bodyImg).getAsJsonObject();
                               String pid = pic.get("pid").getAsString();
                               url = pic.get("url").getAsString();
                               System.out.println(pid + " " + url);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                           uri = Uri.parse("http://114.115.212.203:8001" + url);
                           handler.post(runnableUi);
                       }
                   }).start();
               }
           }
        );

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = editText.getText().toString();
                RequestBody requestBodyPost = new FormBody.Builder().add("pid", "6").add("tag", tag).build();
                Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/tagit/").post(requestBodyPost).build();
                okHttpClient.newCall(requestPost).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("MainActivity", "请求失败！");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
