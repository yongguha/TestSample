package com.ygha.spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    String[] fruit = {"apple", "peach", "grape", "orange"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner);

        SpinnerAdapter adapter = new SpinnerAdapter(fruit,this);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                adapter.setPositionSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.notifyDataSetChanged();
    }
}