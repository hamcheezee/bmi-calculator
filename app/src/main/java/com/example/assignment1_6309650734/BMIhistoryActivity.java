package com.example.assignment1_6309650734;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMIhistoryActivity extends AppCompatActivity {
    List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        final TextView BMIlist = findViewById(R.id.BMIlist);

        loadBMIresult();
        BMIlist.setText(createBMIlist());

    } // end onCreate

    private String createBMIlist() {
        String BMIlist = loadBMIresult();

        if(BMIlist.equals("not found")) {
            return "";
        }

        return BMIlist.replace("|", "                ").replace(";", "\n");
    }

    private String loadBMIresult() {
        SharedPreferences sp = getSharedPreferences("BMI", Context.MODE_PRIVATE);
        System.out.println("BMI => " + sp.getString("BMI", "not found"));
        return  sp.getString("BMI", "not found");
    }
}