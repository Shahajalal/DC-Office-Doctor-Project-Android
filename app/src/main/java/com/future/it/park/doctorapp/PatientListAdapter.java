package com.future.it.park.doctorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.MyViewHolder>{
    public List<PatientList> listdata;
    String URL1,URL2,URL3,URL4;
    MaterialDialog md;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,phone,area;
        public ImageView nImg;
        public CardView idCardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.nImg = itemView.findViewById(R.id.ivUserImage);
            this.name = itemView.findViewById(R.id.txtName);
            this.phone = itemView.findViewById(R.id.txtPhone);
            this.area = itemView.findViewById(R.id.txtArea);
            this.idCardView = itemView.findViewById(R.id.idCardView);
        }
    }


    public PatientListAdapter(List<PatientList> listdata) {
        this.listdata = listdata;
    }

    @Override
    public PatientListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.patient_list_show_template, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PatientList myListData = listdata.get(position);
        holder.name.setText(listdata.get(position).getName());
        holder.phone.setText(listdata.get(position).getCreated_at());
        holder.area.setText(listdata.get(position).getUser_id());
        TextDrawable drawable2 = new TextDrawable.Builder()
                .setColor(ColorGenerator.MATERIAL.getRandomColor())
                .setShape(TextDrawable.SHAPE_ROUND)
                .setText(listdata.get(position).getName().substring(0,2))
                .build();
        holder.nImg.setImageDrawable(drawable2);

        holder.idCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                URL1= PreferenceUtils.getIP(view.getContext())+"get_recipient";
                getRecipient(view.getContext(),listdata.get(position).getUser_id());
                showLoading(view.getContext());
            }
        });

    }



    void showLoading(final Context ctx){
        boolean wrapInScrollView=true;
        md=new MaterialDialog.Builder(ctx)
                .customView(R.layout.loading_alert, wrapInScrollView)
                .show();

    }


    public void getRecipient(final Context ctx,final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                md.dismiss();
                System.out.println(response);
                try {

                    JSONObject erjson = new JSONObject(response);
                    if(erjson.getString("error").equals("false")) {
                        String data = erjson.getString("data");
                        Intent intent = new Intent(ctx,SearchResult.class);
                        intent.putExtra("data",data);
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();
                    }else{
                        //showErrorAlert(erjson.getString("msg"));
                    }
                }catch (Exception e){

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                md.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("unique_id", id);

                return params;
            }
        };
        Volley.newRequestQueue(ctx).add(stringRequest);
    }

//    public void editUser(final Context ctx,final String id) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                md.dismiss();
//                try {
//                    JSONObject erjson = new JSONObject(response);
//                    if(erjson.getString("error").equals("false")) {
//                        System.out.println(erjson.getString("data"));
//                        Intent intent = new Intent(ctx,CollectorProfile.class);
//                        intent.putExtra("data",erjson.getString("data"));
//                        ctx.startActivity(intent);
////                        ((Activity)ctx).finish();
//                    }
//                }catch (Exception e){
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("id",id);
//                return params;
//            }
//        };
//        Volley.newRequestQueue(ctx).add(stringRequest);
//    }



    public void addMyList(List<PatientList> myLists){

        listdata.addAll(myLists);
        notifyDataSetChanged();

    }

    public void addToLast(PatientList myLists){
        listdata.add(myLists);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return listdata.size();
    }




}
