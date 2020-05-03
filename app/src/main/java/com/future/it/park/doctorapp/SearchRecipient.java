package com.future.it.park.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRecipient extends AppCompatActivity {


    ArrayList<String> user_id,created_at,name;
    String URL1;
    int pageNumber=1;
    RecyclerView recyclerViewExample;
    ProgressBar progressBar;
    PatientListAdapter adapter;


    private boolean isLoading=true;
    private  int pastVisiblesItems,visibleItemCounts,totalItemCount,previous_total=0;
    private  int view_threshold=10;
    private MaterialDialog md;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipient);
        //URL1 = PreferenceUtils.getIP(getApplicationContext()) + "get_recipient";
        URL1 = PreferenceUtils.getIP(getApplicationContext()) + "get_doctor_under_patient";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerViewExample = findViewById(R.id.collectorRecyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewExample.setLayoutManager(linearLayoutManager);

        if(hasInternetConnection()) {
            callHistory();
        }else{
            showErrorAlert("No Internet Connection");
        }
        progressBar = findViewById(R.id.pid);
        progressBar.setVisibility(View.VISIBLE);


        recyclerViewExample.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCounts=linearLayoutManager.getChildCount();
                totalItemCount=linearLayoutManager.getItemCount();
                pastVisiblesItems=linearLayoutManager.findFirstVisibleItemPosition();

                if (dy > 0) {
                    if (isLoading) {
                        if(totalItemCount>previous_total){
                            isLoading=false;
                            previous_total=totalItemCount;
                        }
                    }

                    if (!isLoading && (totalItemCount-visibleItemCounts)<=(pastVisiblesItems+view_threshold)) {
                        pageNumber++;
                        if(hasInternetConnection()) {
                            performPagination();
                        }else{
                            showErrorAlert("No Internet Connection");
                        }
                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                    }
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
        final MaterialDialog md=new MaterialDialog.Builder(SearchRecipient.this)
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

//    void showLoading(){
//        boolean wrapInScrollView=true;
//        md=new MaterialDialog.Builder(SearchRecipient.this)
//                .customView(R.layout.loading_alert, wrapInScrollView)
//                .show();
//
//    }

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



    public void callHistory(){

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                System.out.println(response);

                try{
                    JSONObject js=new JSONObject(response);
                    if(js.getString("error").equals("false")) {

                        JSONObject jsObj = js.getJSONObject("data");

                        JSONArray jsonArray = jsObj.getJSONArray("data");

                        if (jsonArray.length() > 0) {

                            name = new ArrayList<>();
                            user_id = new ArrayList<>();
                            created_at = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                name.add(jsonObject.getString("name"));
                                user_id.add(jsonObject.getString("user_id"));
                                created_at.add(jsonObject.getString("created_at"));

                            }

                            List<PatientList> myListData = new ArrayList<>();

                            for (int i = 0; i < user_id.size(); i++) {
                                PatientList m = new PatientList(user_id.get(i),created_at.get(i),name.get(i));
                                myListData.add(m);

                            }

                            adapter = new PatientListAdapter(myListData);

                            recyclerViewExample.setAdapter(adapter);
                        }
                    }

                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        })

        {

            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("page",Integer.toString(pageNumber));
                params.put("user_id",PreferenceUtils.getDoctorID(getApplicationContext()));
                return params;
            }
        };



        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    public void performPagination(){

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);

                try{
                    JSONObject js=new JSONObject(response);
                    if(js.getString("error").equals("false")) {
                        JSONArray jsonArray = js.getJSONArray("data");
                        if (jsonArray.length() > 0) {

                            name = new ArrayList<>();
                            user_id = new ArrayList<>();
                            created_at = new ArrayList<>();


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                name.add(jsonObject.getString("name"));
                                user_id.add(jsonObject.getString("user_id"));
                                created_at.add(jsonObject.getString("created_at"));

                            }

                            List<PatientList> myListData = new ArrayList<>();

                            for (int i = 0; i < user_id.size(); i++) {
                                PatientList m = new PatientList(user_id.get(i),created_at.get(i),name.get(i));
                                myListData.add(m);

                            }

                            adapter.addMyList(myListData);
                        }
                    }

                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        })

        {


            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("page",Integer.toString(pageNumber));
                params.put("user_id",PreferenceUtils.getDoctorID(getApplicationContext()));
                return params;
            }
        };



        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
