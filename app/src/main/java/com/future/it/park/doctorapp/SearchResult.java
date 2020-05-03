package com.future.it.park.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class SearchResult extends AppCompatActivity {

    private Button submit;
    private CheckBox checkbox;
    private MaterialDialog md;
    private String URL1;
    private TextView name,age,weight,mobile,address,roger_bornona,doctor_type,gender,father_name,unique_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        URL1 = PreferenceUtils.getIP(getApplicationContext()) + "update_recipient";
        String data = getIntent().getStringExtra("data");
        System.out.println(data);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        roger_bornona = findViewById(R.id.roger_bornona);
        doctor_type = findViewById(R.id.doctor_type);
        gender = findViewById(R.id.gender);
        father_name = findViewById(R.id.father_name);
        unique_id = findViewById(R.id.unique_id);
        checkbox = findViewById(R.id.checkbox);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkbox.isChecked()){
                    if(hasInternetConnection()){
                        updateStatus();
                        showLoading();
                    }else{
                        showErrorAlert("No Internet Connection");
                    }
                }else{
                    showErrorAlert("Please Confirm The CheckBox");
                }
            }
        });

        try{
            JSONObject js = new JSONObject(data);

            unique_id.setText(js.getString("user_id"));
            name.setText(js.getString("name"));
            father_name.setText(js.getString("father"));
            age.setText(js.getString("age"));
            weight.setText(js.getString("weight"));
            mobile.setText(js.getString("mobile"));
            address.setText(js.getString("address"));
            roger_bornona.setText(js.getString("roger_bornona"));
            doctor_type.setText(js.getString("doctor_type"));
            gender.setText(js.getString("gender"));

        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),SearchRecipient.class));
        finish();
    }

    void showErrorAlert(String msg){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(SearchResult.this)
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
        md=new MaterialDialog.Builder(SearchResult.this)
                .customView(R.layout.loading_alert, wrapInScrollView)
                .show();

    }

    void showSuccessAlert(){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(SearchResult.this)
                .customView(R.layout.success_response_alert, wrapInScrollView)
                .show();
        View views = md.getCustomView();
        Button btn = views.findViewById(R.id.buttonOk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),SearchRecipient.class);
                startActivity(intent);
                finish();
            }
        });



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

    public void updateStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                md.dismiss();
                System.out.println(response);
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
                showErrorAlert("Problem To Connection With Server!!!!!!!");
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("unique_id", unique_id.getText().toString());
                params.put("id",PreferenceUtils.getDoctorID(getApplicationContext()));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}

