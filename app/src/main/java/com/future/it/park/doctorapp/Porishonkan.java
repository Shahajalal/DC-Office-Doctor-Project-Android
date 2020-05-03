package com.future.it.park.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONObject;

public class Porishonkan extends AppCompatActivity {

    private TextView total,success,doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porishonkan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String data = getIntent().getStringExtra("data");
        System.out.println(data);


        total = findViewById(R.id.total);
        success = findViewById(R.id.success);
        doctor = findViewById(R.id.doctor);

        try {
            JSONObject jsonObject = new JSONObject(data);
            total.setText(jsonObject.getString("total"));
            success.setText(jsonObject.getString("success"));
            doctor.setText(jsonObject.getString("doctors"));
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
