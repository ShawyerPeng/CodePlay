package com.example.finalproject.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.JsonParser;

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

    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        final EditText input_username = (EditText)findViewById(R.id.login_username_edit);
        final EditText input_password = (EditText)findViewById(R.id.login_password_edit);
        Button button_login = (Button)findViewById(R.id.login_login_btn);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if (!(getSharedPreferences("data", MODE_PRIVATE) == null)) {
            if (sharedPreferences.getBoolean("isRemember", false)) {
                input_username.setText(sharedPreferences.getString("username", ""));
                input_password.setText(sharedPreferences.getString("password", ""));
                btn_login();
                this.finish();
            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", null);
            editor.putString("password", null);
            editor.putBoolean("isRemember", false);
            editor.commit();
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                        final String username = input_username.getText().toString();
                        final String password = input_password.getText().toString();
                        RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                        Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/do_login/").post(requestBodyPost).build();
                        okHttpClient.newCall(requestPost).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("LoginActivity", "请求登录失败！");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String message = new JsonParser().parse(response.body().string()).getAsJsonObject().get("msg").getAsString();
                                if(message.equals("ok")) {
                                    // 保存用户名及密码
                                    sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                                    editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                                    editor.putString("username", username);
                                    editor.putString("password", password);
                                    editor.putBoolean("isRemember", true);
                                    editor.apply();

                                    System.out.println(sharedPreferences.getString("username", username));
                                    System.out.println(sharedPreferences.getString("password", password));

                                    btn_login();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "用户名或密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void btn_login() {
        Intent intent = new Intent(LoginActivity.this, TaggingActivity.class);
        startActivity(intent);
        this.finish();
    }
}
