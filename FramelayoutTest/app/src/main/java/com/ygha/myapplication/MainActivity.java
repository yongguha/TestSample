package com.ygha.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    Group group1, group2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group1 = findViewById(R.id.group3);
        group2 = findViewById(R.id.group4);


    }

    public void onClickTest(View v){
        group1.setVisibility(View.VISIBLE);
        group2.setVisibility(View.GONE);

    }
}