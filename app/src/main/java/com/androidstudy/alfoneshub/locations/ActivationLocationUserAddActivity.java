package com.androidstudy.alfoneshub.locations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.CheckInHistoryAdapter;
import com.androidstudy.alfoneshub.adapters.LocationUserAddAdapter;
import com.androidstudy.alfoneshub.models.LocationUser;
import com.androidstudy.alfoneshub.models.LocationUserAdd;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class ActivationLocationUserAddActivity extends AppCompatActivity {

    MaterialSpinner spinner_activation;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewCheckInHistory;
    CheckInHistoryAdapter checkInHistoryAdapter;

    LocationUserAddAdapter[] locationUserAddAdapter = new LocationUserAddAdapter[1];
    List<LocationUserAdd> locationUserAddList = new ArrayList<>();
    private List<LocationUser> locationUserList = new ArrayList<>();
    TextView textViewLocationName;
    LinearLayout linearLayoutError;
    Button buttonAddUser;
    RecyclerView recyclerViewAddUser;
    
    String activation_name="";
    String activation_id = "", location_id = "", location_name = "";
    ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_user_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Add location user");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        textViewLocationName = (TextView)findViewById(R.id.txt_location_name);
        
        recyclerViewAddUser= (RecyclerView)findViewById(R.id.recycler_location_user);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewAddUser.setLayoutManager(gridLayoutManager);

        progressBar = (ProgressBar)findViewById(R.id.progress_dialog);
        progressBar.setVisibility(View.GONE);

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            location_id = intent.getStringExtra("location_id");
            location_name = intent.getStringExtra("location_name");
        }

        textViewLocationName.setText(location_name);

        buttonAddUser = (Button)findViewById(R.id.button_add_location_user);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addUsers();
            }
        });

        fetchSpinnerData();

    }

    private void addUsers() {

        pDialog = new SweetAlertDialog(ActivationLocationUserAddActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Assigning users...");
        pDialog.setCancelable(true);
        pDialog.show();


        for (int i=0; i<locationUserAddList.size();i++) {
            final String selected = locationUserAddList.get(i).getSelected();
            final String user_id = locationUserAddList.get(i).getId();
            final String activation_id = locationUserAddList.get(i).getActivation_id();

            if(selected.equalsIgnoreCase("1")){
                SharedPreferences preferences = ActivationLocationUserAddActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String admin_id = preferences.getString("user_id", "user_id");

                AndroidNetworking.post(URLs.TEAM_LEADER_ADD_USER_LOCATION)
                        .addBodyParameter("user_id", user_id)
                        .addBodyParameter("admin_id", admin_id)
                        .addBodyParameter("activation_id", activation_id)
                        .addBodyParameter("location_id", location_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("CHECK IN", response.toString());
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("CHECK IN ERROR", anError.toString());
                            }
                        });


                if(i == locationUserAddList.size()-1){
                    pDialog.dismiss();
                    finish();
                }
            }
        }
    }

    private void fetchSpinnerData() {
        /**
         * Perform network call
         */
        SharedPreferences preferences = ActivationLocationUserAddActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.TEAM_LEADER_ACTIVATION_USERS)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
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
                                LocationUserAdd locationUserAdd = new LocationUserAdd(id, activation_id, name, phone_number, "0");
                                locationUserAddList.add(locationUserAdd);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        locationUserAddAdapter[0] = new LocationUserAddAdapter(ActivationLocationUserAddActivity.this, locationUserAddList);
                        recyclerViewAddUser.setAdapter(locationUserAddAdapter[0]);

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("Users response", "Result " + anError);
                        new SweetAlertDialog(ActivationLocationUserAddActivity.this, SweetAlertDialog.ERROR_TYPE)
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
