package com.future.it.park.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class DoctorLogin extends AppCompatActivity {

    private LinearLayout previous;
    private Button login;
    private MaterialDialog md;
    private String URL1;
    private EditText name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        URL1 = PreferenceUtils.getIP(getApplicationContext()) + "login";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        previous = findViewById(R.id.previous);
        login = findViewById(R.id.login);

        name = findViewById(R.id.name);
        name.setText(PreferenceUtils.getDoctorName(getApplicationContext()));
        pass = findViewById(R.id.pass);
        pass.setText(PreferenceUtils.getDoctorPassword(getApplicationContext()));

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())){
                    if(hasInternetConnection()){
                        logIn1();
                        showLoading();
                    }else{
                        showErrorAlert("No Internet Connection");
                    }
                }else{
                    showErrorAlert("Give UserName Or Password Correctly!!!!!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    void showErrorAlert(String msg){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(DoctorLogin.this)
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
        md=new MaterialDialog.Builder(DoctorLogin.this)
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

    public void logIn1() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                md.dismiss();
                try {

                    JSONObject erjson = new JSONObject(response);
                    if(erjson.getString("error").equals("false")) {
                        PreferenceUtils.setDoctorID(getApplicationContext(),erjson.getString("id"));
                        PreferenceUtils.setDoctorName(getApplicationContext(),name.getText().toString());
                        PreferenceUtils.setDoctorPassword(getApplicationContext(),pass.getText().toString());
                        startActivity(new Intent(getApplicationContext(),SearchRecipient.class));
                        finish();
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
                showErrorAlert("Problem To Connection With Server!!!!!!!");
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("password", pass.getText().toString());

                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}

