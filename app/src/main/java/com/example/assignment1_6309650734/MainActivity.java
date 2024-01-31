package com.example.assignment1_6309650734;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView BMI = findViewById(R.id.BMI);
        final TextView result = findViewById(R.id.result);
        final EditText weight = findViewById(R.id.weight);
        final EditText height = findViewById(R.id.height);

        EditText weightEditText = (EditText)findViewById(R.id.weight);
        EditText heightEditText = (EditText)findViewById(R.id.height);

        weightEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        heightEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        final Button calculateBtn = findViewById(R.id.calculateBtn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BMIresult = "";
                String S1 = weight.getText().toString();
                String S2 = height.getText().toString();

                float weightValue = Float.parseFloat((S1));
                float heightValue = Float.parseFloat((S2)) / 100;
                double bmi = weightValue / (heightValue * heightValue);

                if(bmi < 18.5) {
                    BMIresult = getString(R.string.underweight);
                } else if(bmi >= 18.5 && bmi <= 22.90) {
                    BMIresult = getString(R.string.normal);
                } else if(bmi >= 23 && bmi <= 24.90) {
                    BMIresult = getString(R.string.obesity1);
                } else if(bmi >= 25 && bmi <= 29.90) {
                    BMIresult = getString(R.string.obesity2);
                } else {
                    BMIresult = getString(R.string.obesity3);
                }

                String BMItext = bmi + "";
                BMItext = formatter.format(Double.parseDouble(BMItext));
                BMI.setText(BMItext);
                result.setText(BMIresult);

                // Save BMI result
                saveBMIresult(weightValue, BMItext, BMIresult);
            }
        }); // end calculateBtn OnClick

        // Redirect to display history page
        final ImageView HistoryBtn = (ImageView)findViewById(R.id.history);

        HistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BMIhistoryActivity.class);
                startActivity(intent);
            }
        });

    } // end onCreate

    private String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private String loadBMIresult() {
        SharedPreferences sp = getSharedPreferences("BMI", Context.MODE_PRIVATE);
        System.out.println("BMI => " + sp.getString("BMI", "not found"));
        return  sp.getString("BMI", "not found");
    }

    private void saveBMIresult(double weight, String BMI, String result) {
        String date = getDate();

        SharedPreferences sp = getSharedPreferences("BMI", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        String oldBMIlist = loadBMIresult();
        String BMIlist = date + "|" + weight + "|" + BMI + "|" + result;

        if(!oldBMIlist.equals("not found")) {
            BMIlist = oldBMIlist + ";" + BMIlist;
        }

        spEditor.putString("BMI", BMIlist);
        spEditor.commit();
    }

} // end MainActivity

class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;

    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                                   "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);

        if(!matcher.matches())
            return "";
        return null;
    }
}