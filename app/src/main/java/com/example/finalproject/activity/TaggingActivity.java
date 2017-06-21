package com.example.finalproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.adapter.MenuAdapter;
import com.example.finalproject.entity.Menu;
import com.example.finalproject.entity.User;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.finalproject.R.id.my_image_view;

public class TaggingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GestureDetector.OnGestureListener {
    private static SharedPreferences sharedPreferences;
    private static ClearableCookieJar cookieJar;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private static Runnable init_runnableUi;
    private static Runnable runnableUi;
    private static String url = "http://114.115.212.203:8001/";
    private static String to_url;
    private static String avatar_url;
    private static Uri uri;
    private static String pid;
    private static JsonArray tags;
    private static String _username;
    private static double _honesty;

    private static Button b1,b2,b3,b4,b5,b6,B1,B2,B3,B4,B5,B6,BI1,BI2,BI3,BI4,BI5,BI6,b_add,b_sub;
    private static SimpleDraweeView draweeView;
    private static SimpleDraweeView avatar;
    private EditText input_tag;
    private int tag_num = 0;
    private List<Menu> menulist = new ArrayList<>();
    private Stack<Button> buttonStack = new Stack<Button>();
    private static GestureDetector gestureDetector;

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);

        menulist.add(new Menu("用户信息", R.drawable.ic_menu_friendslist));
        menulist.add(new Menu("历史记录",R.drawable.ic_menu_recent_history));
        menulist.add(new Menu("注销账号",R.drawable.ic_menu_blocked_user));
        menulist.add(new Menu("退出",R.drawable.ic_menu_back));
        MenuAdapter adapter = new MenuAdapter(this, R.layout.menu_item, menulist);
        ListView listView = (ListView) findViewById(R.id.menu_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
        b_add = (Button) findViewById(R.id.btn_AddTag);
        b_sub = (Button) findViewById(R.id.btn_SubmitTag);
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

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if (sharedPreferences.getString("username", null)==null && sharedPreferences.getString("password", null)==null) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
        }

        final Context context = this;
        gestureDetector = new GestureDetector(this);
        handler = new Handler();
        init_runnableUi = new Runnable(){
            @Override
            public void run() {
                avatar = (SimpleDraweeView) findViewById(R.id.avatar);
                avatar.setImageURI(Uri.parse("http://114.115.212.203:8001" + avatar_url));
                avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPicFromPhoto();
                        // getPicFromCamera();
                    }
                });
                TextView username = (TextView) findViewById(R.id.username) ;
                TextView honesty = (TextView) findViewById(R.id.honesty);
                username.setText("用户名：" + _username);
                honesty.setText("诚信值：" + Double.toString(_honesty));

                draweeView = (SimpleDraweeView) findViewById(my_image_view);
                draweeView.setImageURI(uri);
                // 点击查看图片和滑动切换图片的触发事件
                draweeView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });

                ArrayList<Button> buttons = new ArrayList<>();
                buttons.add(b1);buttons.add(b2);buttons.add(b3);buttons.add(b4);buttons.add(b5);buttons.add(b6);
                buttons.get(0).setVisibility(View.GONE);buttons.get(1).setVisibility(View.GONE);buttons.get(2).setVisibility(View.GONE);
                buttons.get(3).setVisibility(View.GONE);buttons.get(4).setVisibility(View.GONE);buttons.get(5).setVisibility(View.GONE);
                for (int i=0; i<6; i++) {
                    if(i<tags.size()) {
                        buttons.get(i).setText(tags.get(i).getAsString());
                        buttons.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        runnableUi = new Runnable(){
            @Override
            public void run() {
                draweeView = (SimpleDraweeView) findViewById(my_image_view);
                draweeView.setImageURI(uri);
                draweeView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });

                ArrayList<Button> buttons = new ArrayList<>();
                buttons.add(b1);buttons.add(b2);buttons.add(b3);buttons.add(b4);buttons.add(b5);buttons.add(b6);
                buttons.get(0).setVisibility(View.GONE);buttons.get(1).setVisibility(View.GONE);buttons.get(2).setVisibility(View.GONE);
                buttons.get(3).setVisibility(View.GONE);buttons.get(4).setVisibility(View.GONE);buttons.get(5).setVisibility(View.GONE);
                for (int i=0; i<6; i++) {
                    if(i<tags.size()) {
                        buttons.get(i).setText(tags.get(i).getAsString());
                        buttons.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        new Thread(new Runnable() {
            public void run() {
                cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                String username = sharedPreferences.getString("username", null);
                String password = sharedPreferences.getString("password", null);
                RequestBody requestBodyPost = new FormBody.Builder().add("name", username).add("pwd", password).build();
                Request requestPost = new Request.Builder().url("http://114.115.212.203:8001/do_login/").post(requestBodyPost).build();
                okHttpClient.newCall(requestPost).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
                Request requestImg = new Request.Builder().url("http://114.115.212.203:8001/getPic/").build();
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
                to_url = "http://114.115.212.203:8001" + url;
                uri = Uri.parse(to_url);

                // 获取个人信息
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
                JsonObject object = new JsonParser().parse(bodyInfo).getAsJsonObject();
                JsonObject user = object.getAsJsonObject("user");
                User aUser = gson.fromJson(user, User.class);
                System.out.println(aUser.toString());
                avatar_url = aUser.getFaceurl();
                _username = aUser.getUname();
                _honesty = aUser.getUhonesty();

                // 加载标签
                Request request = new Request.Builder().url("http://114.115.212.203:8001/tags_by_id?pid=" + pid).get().build();
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(tags);

                handler.post(init_runnableUi);
            }
        }).start();

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_tag.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),"标签不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tag_num>=6){
                    Toast.makeText(getApplicationContext(),"最多添加6个标签！",Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonStack.peek().setText(input_tag.getText());
                buttonStack.peek().setVisibility(View.VISIBLE);
                buttonStack.pop();
                tag_num++;

                input_tag.setText(null);
            }
        });

        b_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringBuilder tag = new StringBuilder();
                ArrayList<String> stringArrayList = new ArrayList<String>();
                stringArrayList.add(B1.getText().toString().trim());
                stringArrayList.add(B2.getText().toString().trim());
                stringArrayList.add(B3.getText().toString().trim());
                stringArrayList.add(B4.getText().toString().trim());
                stringArrayList.add(B5.getText().toString().trim());
                stringArrayList.add(B6.getText().toString().trim());
                stringArrayList.add(BI1.getText().toString().trim());
                stringArrayList.add(BI2.getText().toString().trim());
                stringArrayList.add(BI3.getText().toString().trim());
                stringArrayList.add(BI4.getText().toString().trim());
                stringArrayList.add(BI5.getText().toString().trim());
                stringArrayList.add(BI6.getText().toString().trim());
                for (int i=0; i<stringArrayList.size(); i++) {
                    if (!(stringArrayList.get(i).equals(""))) {
                        tag.append(stringArrayList.get(i)).append(",");
                    }
                }
                if (!(tag.length() == 0)) {
                    if (tag_num >= 6) {
                        Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
                            okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

                            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            String username = sharedPreferences.getString("username", null);
                            String password = sharedPreferences.getString("password", null);
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

                            tag.delete(tag.length() - 1, tag.length());
                            Log.e("Tag: ", tag.toString());
                            RequestBody requestBodyPostTag = new FormBody.Builder().add("pid", pid).add("tag", tag.toString()).build();
                            Request requestPostTag = new Request.Builder().url("http://114.115.212.203:8001/tagit/").post(requestBodyPostTag).build();
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

                            handler.post(runnableUi);

                            Request requestImg = new Request.Builder().url("http://114.115.212.203:8001/getPic/").build();
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
                            to_url = "http://114.115.212.203:8001" + url;
                            uri = Uri.parse("http://114.115.212.203:8001" + url);

                            handler.post(runnableUi);
                        }
                    }).start();
                    input_tag.setText(null);
                    tag_num = 0;
                    B1.setText(null);B2.setText(null);B3.setText(null);B4.setText(null);B5.setText(null);B6.setText(null);
                    BI1.setText(null);BI2.setText(null);BI3.setText(null);BI4.setText(null);BI5.setText(null);BI6.setText(null);
                    B1.setVisibility(View.GONE);B2.setVisibility(View.GONE);B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);B5.setVisibility(View.GONE);B6.setVisibility(View.GONE);
                    BI1.setVisibility(View.GONE);BI2.setVisibility(View.GONE);BI3.setVisibility(View.GONE);
                    BI4.setVisibility(View.GONE);BI5.setVisibility(View.GONE);BI6.setVisibility(View.GONE);
                    b1.setVisibility(View.VISIBLE);b2.setVisibility(View.VISIBLE);b3.setVisibility(View.VISIBLE);
                    b4.setVisibility(View.VISIBLE);b5.setVisibility(View.VISIBLE);b6.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(TaggingActivity.this, "标签为空，请添加后提交", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tag_num >= 6) {
                    Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
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
                if (tag_num >= 6) {
                    Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
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
                    if (tag_num >= 6) {
                        Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
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
                if (tag_num >= 6) {
                    Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
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
                if (tag_num >= 6) {
                    Toast.makeText(getApplicationContext(), "最多添加6个标签！", Toast.LENGTH_SHORT).show();
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
                B1.setText(null);
                b1.setVisibility(View.VISIBLE);
                B1.setVisibility(View.GONE);
                tag_num--;

            }
        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B2.setText(null);
                b2.setVisibility(View.VISIBLE);
                B2.setVisibility(View.GONE);
                tag_num--;

            }
        });
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B3.setText(null);
                b3.setVisibility(View.VISIBLE);
                B3.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B4.setText(null);
                    b4.setVisibility(View.VISIBLE);
                    B4.setVisibility(View.GONE);
                    tag_num--;
            }
        });
        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B5.setText(null);
                b5.setVisibility(View.VISIBLE);
                B5.setVisibility(View.GONE);
                tag_num--;
            }
        });
        B6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B6.setText(null);
                b6.setVisibility(View.VISIBLE);
                B6.setVisibility(View.GONE);
                tag_num--;
            }
        });
        BI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI1.setText(null);
                BI1.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI1);
            }
        });
        BI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI2.setText(null);
                BI2.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI2);
            }
        });
        BI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI3.setText(null);
                BI3.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI3);
            }
        });
        BI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI4.setText(null);
                BI4.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI4);
            }
        });
        BI5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI5.setText(null);
                BI5.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI5);
            }
        });
        BI6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BI6.setText(null);
                BI6.setVisibility(View.GONE);
                tag_num--;
                buttonStack.push(BI6);
            }
        });
    }

    public void btn_feedback(View v){
        Intent intent = new Intent(TaggingActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }

    public void btn_setting(View v){
        Intent intent = new Intent(TaggingActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if (position == 0) {
            intent.setClass(this, UserInfActivity.class);
            startActivity(intent);
        }
        else if(position == 1) {
            intent.setClass(this, HistoryActivity.class);
            startActivity(intent);
        }
        else if (position == 2) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (position == 3) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Intent intent = new Intent(TaggingActivity.this, ImageViewActivity.class);
        intent.putExtra("img_url", to_url);
        startActivity(intent);
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 10 && Math.abs(velocityX) > 0) {
            new Thread(new Runnable() {
                public void run() {
                    // 加载图片
                    Request requestImg = new Request.Builder().url("http://114.115.212.203:8001/getPic/").build();
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
                    to_url = "http://114.115.212.203:8001" + url;
                    uri = Uri.parse(to_url);

                    // 加载标签
                    Request request = new Request.Builder().url("http://114.115.212.203:8001/tags_by_id?pid=" + pid).get().build();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(tags);

                    handler.post(runnableUi);
                }
            }).start();
        } if (e2.getX() - e1.getX() > 10 && Math.abs(velocityX) > 0) {

        }
        return false;
    }

    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void getPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "avatar.jpg")));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
                        File file = new File(Environment.getExternalStorageDirectory() + "/avatar.jpg");
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        avatar.setImageBitmap(photo);
//                        avatar.setImageURI(Uri.p);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context) {
        //Create renderscript
        RenderScript rs = RenderScript.create(context);

        //Create allocation from Bitmap
        Allocation allocation = Allocation.createFromBitmap(rs, bitmap);

        Type t = allocation.getType();

        //Create allocation with the same type
        Allocation blurredAllocation = Allocation.createTyped(rs, t);

        //Create blur script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Set blur radius (maximum 25.0)
        blurScript.setRadius(radius);
        //Set input for script
        blurScript.setInput(allocation);
        //Call script for output allocation
        blurScript.forEach(blurredAllocation);

        //Copy script result into bitmap
        blurredAllocation.copyTo(bitmap);

        //Destroy everything to free memory
        allocation.destroy();
        blurredAllocation.destroy();
        blurScript.destroy();
        t.destroy();
        rs.destroy();
        return bitmap;
    }

    private Bitmap returnBitmap(Uri uri) {
        Bitmap bitmap = null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()));
        File file = resource.getFile();
        bitmap = BitmapFactory.decodeFile(file.getPath());
        return bitmap;
    }

    public static Bitmap getBitMBitmap(String urlpath) {
        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * @param urlpath
     * @return Bitmap
     * 根据url获取布局背景的对象
     */
    public static Drawable getDrawable(String urlpath){
        Drawable d = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            d = Drawable.createFromStream(in, "background.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }
}
