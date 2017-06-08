package com.example.finalproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;

import java.util.Stack;

public class TaggingActivity extends AppCompatActivity {
    Button b1,b2,b3,b4,b5,b6,B1,B2,B3,B4,B5,B6,BI1,BI2,BI3,BI4,BI5,BI6,b_sub;
    EditText input_tag;
    int tag_num=0;
    Stack<Button> buttonStack=new Stack<Button>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tagging);

        b1= (Button) findViewById(R.id.btn_cankao1);
        b2= (Button) findViewById(R.id.btn_cankao2);
        b3= (Button) findViewById(R.id.btn_cankao3);
        b4= (Button) findViewById(R.id.btn_cankao4);
        b5= (Button) findViewById(R.id.btn_cankao5);
        b6= (Button) findViewById(R.id.btn_cankao6);

        B1= (Button) findViewById(R.id.btn_wode1);
        B2= (Button) findViewById(R.id.btn_wode2);
        B3= (Button) findViewById(R.id.btn_wode3);
        B4= (Button) findViewById(R.id.btn_wode4);
        B5= (Button) findViewById(R.id.btn_wode5);
        B6= (Button) findViewById(R.id.btn_wode6);

        B1.setVisibility(View.GONE);
        B2.setVisibility(View.GONE);
        B3.setVisibility(View.GONE);
        B4.setVisibility(View.GONE);
        B5.setVisibility(View.GONE);
        B6.setVisibility(View.GONE);

        BI1= (Button) findViewById(R.id.btn_wode_input1);
        BI2= (Button) findViewById(R.id.btn_wode_input2);
        BI3= (Button) findViewById(R.id.btn_wode_input3);
        BI4= (Button) findViewById(R.id.btn_wode_input4);
        BI5= (Button) findViewById(R.id.btn_wode_input5);
        BI6= (Button) findViewById(R.id.btn_wode_input6);

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

        b_sub= (Button) findViewById(R.id.btn_SubmitTag);
        input_tag= (EditText) findViewById(R.id.input_tag);


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


    }

    public void btn_feedback(View v){
        Intent intent =new Intent(TaggingActivity.this,FeedbackActivity.class);
        Toast.makeText(this,"feedback",Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}
