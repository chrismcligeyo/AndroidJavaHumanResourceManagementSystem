package com.androidstudy.alfoneshub.locations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.LocationUserAdapter;
import com.androidstudy.alfoneshub.models.LocationUser;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class ActivationLocationUsersActivity extends AppCompatActivity {

    String activation_id = "", location_id = "", location_name = "";

    SweetAlertDialog pDialog;
    RecyclerView recyclerViewLocation;
    LocationUserAdapter locationUserAdapter;
    private List<LocationUser> locationUserList = new ArrayList<>();
    TextView textViewLocationName;
    LinearLayout linearLayoutError;
    Button buttonAddLocationUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_location_users);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Users");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewLocationName = (TextView)findViewById(R.id.text_location_name);
        recyclerViewLocation= (RecyclerView)findViewById(R.id.recycler_user_location);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewLocation.setLayoutManager(gridLayoutManager);

        buttonAddLocationUser = (Button)findViewById(R.id.button_add_location_user);
        buttonAddLocationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivationLocationUsersActivity.this, ActivationLocationUserAddActivity.class);
                intent.putExtra("activation_id", activation_id);
                intent.putExtra("location_id", location_id);
                intent.putExtra("location_name", location_name);
                startActivity(intent);
              
//                final Dialog dialog = new Dialog(ActivationLocationUsersActivity.this);
//                dialog.setCancelable(false);
//                dialog.setContentView(R.layout.add_location_user_dialog_layout);
//
//                final LocationUserAddAdapter[] locationUserAddAdapter = new LocationUserAddAdapter[1];
//                final List<LocationUserAdd> locationUserAddList = new ArrayList<>();
//
//                final RecyclerView recyclerViewAddUser= (RecyclerView)dialog.findViewById(R.id.recycler_add_location_users);
//                StaggeredGridLayoutManager gridLayoutManager =
//                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//                gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
//                recyclerViewAddUser.setLayoutManager(gridLayoutManager);
//
//                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
//                progressBar.setVisibility(View.VISIBLE);
//
//                TextView cancel = (TextView) dialog.findViewById(R.id.dialog_info_cancel);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                final TextView subscribe = (TextView) dialog.findViewById(R.id.dialog_info_ok);
//                subscribe.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        progressBar.setVisibility(View.VISIBLE);
//
//                        for (int i=0; i<locationUserAddList.size();i++) {
//                            final String selected = locationUserAddList.get(i).getSelected();
//                            final String user_id = locationUserAddList.get(i).getId();
//                            final String activation_id = locationUserAddList.get(i).getActivation_id();
//
//                            if(selected.equalsIgnoreCase("1")){
//                                SharedPreferences preferences = ActivationLocationUsersActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                                String admin_id = preferences.getString("user_id", "user_id");
//
//                                final int finalI = i;
//                                AndroidNetworking.post(URLs.TEAM_LEADER_ADD_USER_LOCATION)
//                                        .addBodyParameter("user_id", user_id)
//                                        .addBodyParameter("admin_id", admin_id)
//                                        .addBodyParameter("activation_id", activation_id)
//                                        .addBodyParameter("location_id", location_id)
//                                        .setPriority(Priority.HIGH)
//                                        .build()
//                                        .getAsJSONObject(new JSONObjectRequestListener() {
//                                            @Override
//                                            public void onResponse(JSONObject response) {
//                                                Log.d("CHECK IN", response.toString());
//                                                if(finalI == locationUserAddList.size()-1){
//
//                                                    progressBar.setVisibility(View.GONE);
//
//                                                    if(locationUserList.size()>0){
//                                                        locationUserList.clear();
//                                                    }
//                                                    fetchActivationUserLocation();
//                                                    locationUserAdapter.notifyDataSetChanged();
//
//                                                    dialog.dismiss();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onError(ANError anError) {
//                                                Log.d("CHECK IN ERROR", anError.toString());
//                                                if(finalI == locationUserAddList.size()-1){
//                                                    progressBar.setVisibility(View.GONE);
//
//                                                    if(locationUserList.size()>0){
//                                                        locationUserList.clear();
//                                                    }
//                                                    fetchActivationUserLocation();
//                                                    locationUserAdapter.notifyDataSetChanged();
//
//                                                    dialog.dismiss();
//                                                }
//                                            }
//                                        });
//
//
//                            }
//                        }
//                    }
//                });
//
//                SharedPreferences preferences = ActivationLocationUsersActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                String user_id = preferences.getString("user_id", "user_id");
//
//                AndroidNetworking.post(URLs.TEAM_LEADER_ACTIVATION_USERS)
//                        .addBodyParameter("activation_id", activation_id)
//                        .addBodyParameter("user_id", user_id)
//                        .setPriority(Priority.HIGH)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                progressBar.setVisibility(View.GONE);
//                                Log.d("LOCATION", response.toString());
//
//                                //Get JSON Response
//                                JSONObject jsonObj = null;
//                                JSONArray array =null;
//                                try {
//                                    jsonObj = new JSONObject(String.valueOf(response));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                // Getting JSON Array node
//                                try {
//                                    array = jsonObj.getJSONArray("result");
//                                    if(array.isNull(0)){
//                                        Log.d("ARRAY", "empty");
//                                    }else{
//                                        Log.d("ARRAY", "NOT empty");
//                                        subscribe.setVisibility(View.VISIBLE);
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                //Loop through the created array
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject c = null;
//                                    try {
//                                        c = array.getJSONObject(i);
//                                        String id = c.getString("id");
//                                        String activation_id = c.getString("activation_id");
//                                        String name = c.getString("name");
//                                        String phone_number = c.getString("phone_number");
//                                        LocationUserAdd locationUserAdd = new LocationUserAdd(id, activation_id, name, phone_number, "0");
//                                        locationUserAddList.add(locationUserAdd);
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                //Set adapter listInteractionEntries_id
//                                locationUserAddAdapter[0] = new LocationUserAddAdapter(ActivationLocationUsersActivity.this, locationUserAddList);
//                                recyclerViewAddUser.setAdapter(locationUserAddAdapter[0]);
//
//                            }
//
//                            @Override
//                            public void onError(ANError anError) {
//                                // handle error
//                                progressBar.setVisibility(View.GONE);
//                                Log.d("Users response", "Result " + anError);
//                                new SweetAlertDialog(ActivationLocationUsersActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("ERROR!")
//                                        .setContentText("Something went wrong, Please try again later")
//                                        .show();
//                            }
//                        });
//
//
//                dialog.show();
                
            }
        });

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            location_id = intent.getStringExtra("location_id");
            location_name = intent.getStringExtra("location_name");
        }

        textViewLocationName.setText(location_name.toUpperCase());

        fetchActivationUserLocation();
    }

    public void fetchActivationUserLocation(){
        SharedPreferences preferences = ActivationLocationUsersActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(ActivationLocationUsersActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.TEAM_LEADER_USER_LOCATION)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("location_id", location_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("LOCATION", response.toString());

                        //Get JSON Response
                        JSONObject jsonObj = null;
                        JSONArray array =null;
                        try {
                            jsonObj = new JSONObject(String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Getting JSON Array node
                        try {
                            array = jsonObj.getJSONArray("result");
                            if(array.isNull(0)){
                                Log.d("ARRAY", "empty");
                            }else{
                                Log.d("ARRAY", "NOT empty");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Loop through the created array
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject c = null;
                            try {
                                c = array.getJSONObject(i);
                                String id = c.getString("id");
                                String activation_id = c.getString("activation_id");
                                String name = c.getString("name");
                                String phone_number = c.getString("phone_number");
                                LocationUser location = new LocationUser(id, activation_id, name, phone_number);
                                locationUserList.add(location);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        locationUserAdapter = new LocationUserAdapter(ActivationLocationUsersActivity.this, locationUserList);
                        recyclerViewLocation.setAdapter(locationUserAdapter);
                        
                        

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Confimation response", "Result " + anError);
                        new SweetAlertDialog(ActivationLocationUsersActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
