package com.example.finalproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaggingActivity extends AppCompatActivity {
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private static Runnable runnableUi;
    private static String url = "http://115.159.188.200:8001/";
    private static Uri uri;
    private static String pid;

    private static SharedPreferences sharedPreferences;

    private Button b1,b2,b3,b4,b5,b6,B1,B2,B3,B4,B5,B6,BI1,BI2,BI3,BI4,BI5,BI6,b_sub,b_test;
    private EditText input_tag;
    private int tag_num=0;
    private Stack<Button> buttonStack= new Stack<Button>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);

        b1 = (Button) findViewById(R.id.btn_cankao1);
        b2 = (Button) findViewById(R.id.btn_cankao2);
        b3 = (Button) findViewById(R.id.btn_cankao3);
        b4 = (Button) findViewById(R.id.btn_cankao4);
        b5 = (Button) findViewById(R.id.btn_cankao5);
        b6 = (Button) findViewById(R.id.btn_cankao6);
        B1 = (Button) findViewById(R.id.btn_wode1);
        B2 = (Button) findViewById(R.id.btn_wode2);
        B3 = (Button) findViewById(R.id.btn_wode3);
        B4 = (Button) findViewById(R.id.btn_wode4);
        B5 = (Button) findViewById(R.id.btn_wode5);
        B6 = (Button) findViewById(R.id.btn_wode6);
        BI1 = (Button) findViewById(R.id.btn_wode_input1);
        BI2 = (Button) findViewById(R.id.btn_wode_input2);
        BI3 = (Button) findViewById(R.id.btn_wode_input3);
        BI4 = (Button) findViewById(R.id.btn_wode_input4);
        BI5 = (Button) findViewById(R.id.btn_wode_input5);
        BI6 = (Button) findViewById(R.id.btn_wode_input6);
        input_tag = (EditText) findViewById(R.id.input_tag);
        b_sub = (Button) findViewById(R.id.btn_SubmitTag);
        b_test = (Button) findViewById(R.id.btn_JustTest);

        B1.setVisibility(View.GONE);
        B2.setVisibility(View.GONE);
        B3.setVisibility(View.GONE);
        B4.setVisibility(View.GONE);
        B5.setVisibility(View.GONE);
        B6.setVisibility(View.GONE);
        BI1.setVisibility(View.GONE);
        BI2.setVisibility(View.GONE);
        BI3.setVisibility(View.GONE);
        BI4.setVisibility(View.GONE);
        BI5.setVisibility(View.GONE);
        BI6.setVisibility(View.GONE);

        buttonStack.push(BI1);
        buttonStack.push(BI2);
        buttonStack.push(BI3);
        buttonStack.push(BI4);
        buttonStack.push(BI5);
        buttonStack.push(BI6);

        handler = new Handler();

        runnableUi = new Runnable(){
            @Override
            public void run() {
                SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                draweeView.setImageURI(uri);
            }
        };

        b_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonStack.peek().setText(input_tag.getText());
                buttonStack.peek().setVisibility(View.VISIBLE);
                buttonStack.pop();
                tag_num++;
            }
        });

        b_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonStack.peek().setText(input_tag.getText());
                buttonStack.peek().setVisibility(View.VISIBLE);
                buttonStack.pop();
                tag_num++;

                new Thread(new Runnable() {
                    public void run() {
                        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                        String username = sharedPreferences.getString("username", null);
                        String password = sharedPreferences.getString("password", null);
                        System.out.println("+++++++++++");
                        System.out.println(username + " " + password);
                        RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                        Request requestPost = new Request.Builder().url("http://115.159.188.200:8001/do_login/").post(requestBodyPost).build();
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

                        Request requestImg = new Request.Builder().url("http://115.159.188.200:8001/getPic/").build();
                        Response responseImg = null;
                        try {
                            responseImg = okHttpClient.newCall(requestImg).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String bodyImg = null;
                        try {
                            bodyImg = responseImg.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(bodyImg);

                        JsonParser parser = new JsonParser();
                        JsonObject pic = parser.parse(bodyImg).getAsJsonObject();
                        pid = pic.get("pid").getAsString();
                        url = pic.get("url").getAsString();
                        System.out.println(pid + " " + url);

                        uri = Uri.parse("http://115.159.188.200:8001" + url);
                        handler.post(runnableUi);


                        System.out.println("*******************" + BI1.getText() + BI2.getText() + BI3.getText() + BI4.getText() + BI5.getText() + BI6.getText());
                        String tag = BI5.getText().toString();
                        RequestBody requestBodyPostTag = new FormBody.Builder().add("pid", pid).add("tag", tag).build();
                        Request requestPostTag = new Request.Builder().url("http://115.159.188.200:8001/tagit/").post(requestBodyPostTag).build();
                        okHttpClient.newCall(requestPostTag).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("TaggingActivity", "上传标签失败！");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("TaggingActivity", "tag:" + response.body().string());
                            }
                        });
                    }
                }).start();

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b1.setVisibility(View.GONE);
                B1.setVisibility(View.VISIBLE);
                B1.setText(b1.getText());
                tag_num++;
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b2.setVisibility(View.GONE);
                B2.setVisibility(View.VISIBLE);
                B2.setText(b2.getText());
                tag_num++;
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b3.setVisibility(View.GONE);
                B3.setVisibility(View.VISIBLE);
                B3.setText(b3.getText());
                tag_num++;
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b4.setVisibility(View.GONE);
                B4.setVisibility(View.VISIBLE);
                B4.setText(b4.getText());
                tag_num++;
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b5.setVisibility(View.GONE);
                B5.setVisibility(View.VISIBLE);
                B5.setText(b5.getText());
                tag_num++;
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                b6.setVisibility(View.GONE);
                B6.setVisibility(View.VISIBLE);
                B6.setText(b6.getText());
                tag_num++;
            }
        });
        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setVisibility(View.VISIBLE);
                B1.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setVisibility(View.VISIBLE);
                B2.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b3.setVisibility(View.VISIBLE);
                B3.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b4.setVisibility(View.VISIBLE);
                B4.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b5.setVisibility(View.VISIBLE);
                B5.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b6.setVisibility(View.VISIBLE);
                B6.setVisibility(View.GONE);
                tag_num--;
            }
        });
        BI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI1.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI1);

            }
        });
        BI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI2.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI2);
            }
        });
        BI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI3.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI3);
            }
        });
        BI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI4.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI4);
            }
        });
        BI5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI5.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI5);
            }
        });
        BI6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI6.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI6);
            }
        });


    }

    public void btn_feedback(View v){
        Intent intent = new Intent(TaggingActivity.this,FeedbackActivity.class);
        Toast.makeText(this, "feedback", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}
