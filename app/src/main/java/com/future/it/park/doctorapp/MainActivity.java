package com.future.it.park.doctorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private Button abedon_korun,porishonakn;
    private TextView login,openwebsite;
    private MaterialDialog md;
    private String URL1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL1 = PreferenceUtils.getIP(getApplicationContext()) + "get_porishonkan";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        openwebsite = findViewById(R.id.openwebsite);

        openwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://futureitpark.com/"));
                startActivity(browserIntent);
            }
        });

        abedon_korun = findViewById(R.id.abedon_korun);
        porishonakn = findViewById(R.id.porishonakn);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DoctorLogin.class));
                finish();
            }
        });

        abedon_korun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AbedonKorunFirst.class));
                finish();
            }
        });

        porishonakn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasInternetConnection()){
                    getValue();
                    showLoading();
                }else{
                    showErrorAlert("No Internet Connection");
                }

            }
        });
    }

    void showErrorAlert(String msg){
        boolean wrapInScrollView=true;
        final MaterialDialog md=new MaterialDialog.Builder(MainActivity.this)
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
        md=new MaterialDialog.Builder(MainActivity.this)
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

    public void getValue() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                md.dismiss();
                System.out.println(response);
                try {
                    JSONObject erjson = new JSONObject(response);
                    if(erjson.getString("error").equals("false")) {
                        Intent intent = new Intent(getApplicationContext(),Porishonkan.class);
                        intent.putExtra("data",response);
                        startActivity(intent);
                        finish();
                    }else{

                    }
                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                md.dismiss();
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}
