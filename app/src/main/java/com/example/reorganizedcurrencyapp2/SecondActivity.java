package com.example.reorganizedcurrencyapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        CurrencyAdapter adapter = new CurrencyAdapter(SecondActivity.this, CurrenciesList.readArrayList(this));

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ListView listView = findViewById(R.id.listView);
                        listView.setAdapter(adapter);
                                            }
                }, 700);
    }
}