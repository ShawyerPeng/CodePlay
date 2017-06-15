package com.example.finalproject.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.util.DataCleanManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button removeCache = (Button) findViewById(R.id.removeCache);
        Button checkUpdate = (Button) findViewById(R.id.checkUpdate);
        Button aboutUs = (Button) findViewById(R.id.aboutUs);

        final Context context = this;

        removeCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println(DataCleanManager.getCacheSize(getCacheDir()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DataCleanManager.cleanInternalCache(context);
                DataCleanManager.cleanExternalCache(context);
                DataCleanManager.cleanFiles(context);
                DataCleanManager.cleanApplicationData(context);
                DataCleanManager.cleanDatabases(context);
                DataCleanManager.cleanSharedPreference(context);
                try {
                    System.out.println(DataCleanManager.getCacheSize(getCacheDir()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(SettingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
            }
        });

        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
