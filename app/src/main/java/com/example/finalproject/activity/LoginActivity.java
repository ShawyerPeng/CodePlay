package com.example.finalproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalproject.R;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private static Runnable runnableUi;
    private static String url = "http://115.159.188.200:8001/";
    private static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        final EditText input_username = (EditText)findViewById(R.id.login_username_edit);
        final EditText input_password = (EditText)findViewById(R.id.login_password_edit);
        Button button_login = (Button)findViewById(R.id.login_login_btn);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                        String username = input_username.getText().toString();
                        String password = input_password.getText().toString();
                        RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                        Request requestPost = new Request.Builder().url("http://115.159.188.200:8001/do_login/").post(requestBodyPost).build();
                        okHttpClient.newCall(requestPost).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("LoginActivity", "请求登录失败！");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("LoginActivity", response.body().string());
                                btn_login();
                            }
                        });
                    }
                }).start();
            }
        });


    }

    public void btn_login(){
        Intent intent = new Intent(LoginActivity.this, TaggingActivity.class);
        startActivity(intent);
    }

}
