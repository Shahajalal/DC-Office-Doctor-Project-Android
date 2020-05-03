package com.future.it.park.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AbedonKorunFirst extends AppCompatActivity {

    private Button send_id;
    private EditText name,father,age,weight,mobile,address,roger_bornona;
    private Spinner doctor_designation;
    private RadioGroup gender;
    private String[] doctorDesignation;
    private String doctorDesignationSend;
    private MaterialDialog md;
    private String URL1;
    private  final AtomicLong LAST_TIME_MS = new AtomicLong();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abedon_korun_first);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        URL1 = PreferenceUtils.getIP(getApplicationContext()) + "create_recipient";
        doctorDesignation = getResources().getStringArray(R.array.doctor_designation);
        gender = findViewById(R.id.gender);
        name = findViewById(R.id.name);
        father = findViewById(R.id.father);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        roger_bornona = findViewById(R.id.roger_bornona);

        send_id = findViewById(R.id.send_id);
        send_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = gender.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                System.out.println(radioButton.getText());
                if(hasInternetConnection()){
                    UploadInforamtion(radioButton.getText().toString());
                    showLoading();
                }


            }
        });

        doctor_designation = findViewById(R.id.doctor_designation);
        doctor_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorDesignationSend = doctorDesignation[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


    void showSuccessAlert(){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(AbedonKorunFirst.this)
                .customView(R.layout.success_response_alert, wrapInScrollView)
                .show();
        View views = md.getCustomView();
        Button btn = views.findViewById(R.id.buttonOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });



    }


    void showErrorAlert(String msg){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(AbedonKorunFirst.this)
                .customView(R.layout.error_response_alert, wrapInScrollView)
                .show();
        View views = md.getCustomView();
        TextView textView=views.findViewById(R.id.positiontext);
        textView.setText(msg);
        Button btn = views.findViewById(R.id.buttonOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md.dismiss();
            }
        });

    }

    void showLoading(){
        boolean wrapInScrollView=true;
        md=new MaterialDialog.Builder(AbedonKorunFirst.this)
                .customView(R.layout.loading_alert, wrapInScrollView)
                .show();

    }

    private boolean hasInternetConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        } catch (Exception e){

        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    public void UploadInforamtion(final String gender) {
        System.out.println(uniqueCurrentTimeMS());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                md.dismiss();
                try {

                    JSONObject erjson = new JSONObject(response);
                    if(erjson.getString("error").equals("false")) {
                        showSuccessAlert();
                    }else{
                        showErrorAlert(erjson.getString("msg"));

                    }
                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                md.dismiss();
                showErrorAlert("Problem In Uploading Data !!!!!!");
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("father", father.getText().toString());
                params.put("age", age.getText().toString());
                params.put("mobile", mobile.getText().toString());
                params.put("weight", weight.getText().toString());
                params.put("address", address.getText().toString());
                params.put("roger_bornona", roger_bornona.getText().toString());
                params.put("doctor_type", doctorDesignationSend);
                params.put("gender", gender);
                params.put("user_id", "DCK"+uniqueCurrentTimeMS().substring(6,uniqueCurrentTimeMS().length()-1));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public  String uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        while(true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime+1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return Long.toString(now);
        }
    }
}
