package com.androidstudy.alfoneshub.reports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.adapters.LocationHistoryAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.LocationHistory;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class LocationReportActivity extends AppCompatActivity {

    String activation_id, activation_name, activation_description, activation_start_date,activation_end_date,
            activation_company, date="";

    String interaction = "0", product= "0", merchandise="0";
    TextView textViewCampaignName, textViewCampaignDate;
    MaterialSpinner spinner_date;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewReportHistory;
    LocationHistoryAdapter locationHistoryAdapter;
    private List<LocationHistory> locationHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Location Reports");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewCampaignDate = (TextView)findViewById(R.id.txt_activation_date);
        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);

        spinner_date=(MaterialSpinner)findViewById(R.id.spinner_date);
        recyclerViewReportHistory= (RecyclerView)findViewById(R.id.recycler_report_history);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewReportHistory.setLayoutManager(gridLayoutManager);

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            activation_name = intent.getStringExtra("activation_name");
            activation_description = intent.getStringExtra("activation_description");
            activation_start_date = intent.getStringExtra("activation_start_date");
            activation_end_date = intent.getStringExtra("activation_end_date");
            activation_company = intent.getStringExtra("activation_company");

        }

        textViewCampaignName.setText(activation_name.toUpperCase());

        fetchSpinnerData();
    }

    private void fetchSpinnerData() {
        AndroidNetworking.post(URLs.ACTIVATION_DATE_URL)
                .addBodyParameter("activation_id", activation_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Campaign dates", "Result " + response.toString());

                        String JSON_ARRAY = "result";
                        JSONObject c=null;
                        List<String> listCampaign = new ArrayList<String>();
                        try {
                            JSONArray feedArray = response.getJSONArray(JSON_ARRAY);

                            for (int i = 0; i < feedArray.length(); i++) {
                                c = (JSONObject) feedArray.get(i);
                                date = c.getString("date");
                                listCampaign.add(date);
                            }
                            //Get first date
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                date = c.getString("date");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            spinner_date.setItems(listCampaign);
                            spinner_date.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    date=item;
                                    Toast.makeText(LocationReportActivity.this, "Date" +date, Toast.LENGTH_SHORT).show();
                                    if(locationHistoryList.size()>0){

                                        locationHistoryList.clear();
                                    }
//
                                    fetchRecordsHistory(date);

                                    locationHistoryAdapter.notifyDataSetChanged();
                                    textViewCampaignDate.setText(date.toUpperCase());
                                }
                            });
                            fetchRecordsHistory(date);
                            textViewCampaignDate.setText(date.toUpperCase());
                        }catch (Exception e){
                            Log.d("ERROR","Spinner not populated");
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        // handle error
                        Log.d("Checklist response", "Result " + error);
                        new SweetAlertDialog(LocationReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });

    }

    public void fetchRecordsHistory(final String date){
        SharedPreferences preferences = LocationReportActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(LocationReportActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_role = sharedPreferences.getString("user_role","user_role");
        int role = Integer.parseInt(user_role);
        if(role == 3 || role == 5 || role== 7){

            AndroidNetworking.post(URLs.PROJECT_MANAGER_LOCATION_REPORT_ENTRIES_URL)
                    .addBodyParameter("activation_id", activation_id)
                    .addBodyParameter("date", date)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pDialog.dismiss();
                            Log.d("Checkboard response", response.toString());
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
                                    String location_id = c.getString("location_id");
                                    String location_name = c.getString("location_name");
                                    String team_leader_id = c.getString("team_leader_id");
                                    String team_leader_name = c.getString("team_leader_name");
                                    String interaction = c.getString("interaction");
                                    String sales = c.getString("sales");
                                    String merchandise = c.getString("merchandise");
                                    String created_at = c.getString("created_at");
                                    String updated_at = c.getString("updated_at");
                                    LocationHistory checkBoardEntries = new LocationHistory(id, activation_id, location_id, location_name, team_leader_id, team_leader_name, interaction, sales, merchandise, created_at, updated_at);
                                    locationHistoryList.add(checkBoardEntries);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Set adapter listInteractionEntries_id
                            locationHistoryAdapter = new LocationHistoryAdapter(LocationReportActivity.this, locationHistoryList);
                            recyclerViewReportHistory.setAdapter(locationHistoryAdapter);
                            recyclerViewReportHistory.addOnItemTouchListener(
                                    new MyRecyclerItemClickListener(LocationReportActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            LocationHistory locationHistory = locationHistoryList.get(position);
                                            Intent intent = new Intent(LocationReportActivity.this, LocationUserReportActivity.class);
                                            intent.putExtra("id", locationHistory.getId());
                                            intent.putExtra("activation_id", locationHistory.getActivation_id());
                                            intent.putExtra("location_id", locationHistory.getLocation_id());
                                            intent.putExtra("location_name", locationHistory.getLocation_name());
                                            intent.putExtra("team_leader_id", locationHistory.getTeam_leader_id());
                                            intent.putExtra("team_leader_name", locationHistory.getTeam_leader_name());
                                            intent.putExtra("interaction", locationHistory.getInteraction());
                                            intent.putExtra("sales", locationHistory.getSales());
                                            intent.putExtra("merchandise", locationHistory.getMerchandise());
                                            intent.putExtra("date", date);
                                            startActivity(intent);
                                        }
                                    })
                            );


                        }

                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            pDialog.dismiss();
                            Log.d("Expense response", "Result " + anError);
                            new SweetAlertDialog(LocationReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR!")
                                    .setContentText("Something went wrong, Please try again later")
                                    .show();
                        }
                    });

        }else if(role ==4){

            AndroidNetworking.post(URLs.TEAM_LEADER_LOCATION_REPORT_ENTRIES_URL)
                    .addBodyParameter("activation_id", activation_id)
                    .addBodyParameter("user_id", user_id)
                    .addBodyParameter("date", date)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pDialog.dismiss();
                            Log.d("Checkboard response", response.toString());
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
                                    String location_id = c.getString("location_id");
                                    String location_name = c.getString("location_name");
                                    String team_leader_id = c.getString("team_leader_id");
                                    String team_leader_name = c.getString("team_leader_name");
                                    String interaction = c.getString("interaction");
                                    String sales = c.getString("sales");
                                    String merchandise = c.getString("merchandise");
                                    String created_at = c.getString("created_at");
                                    String updated_at = c.getString("updated_at");
                                    LocationHistory checkBoardEntries = new LocationHistory(id, activation_id, location_id, location_name, team_leader_id, team_leader_name, interaction, sales, merchandise, created_at, updated_at);
                                    locationHistoryList.add(checkBoardEntries);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Set adapter listInteractionEntries_id
                            locationHistoryAdapter = new LocationHistoryAdapter(LocationReportActivity.this, locationHistoryList);
                            recyclerViewReportHistory.setAdapter(locationHistoryAdapter);
                            recyclerViewReportHistory.addOnItemTouchListener(
                                    new MyRecyclerItemClickListener(LocationReportActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            LocationHistory locationHistory = locationHistoryList.get(position);
                                            Intent intent = new Intent(LocationReportActivity.this, LocationUserReportActivity.class);
                                            intent.putExtra("id", locationHistory.getId());
                                            intent.putExtra("activation_id", locationHistory.getActivation_id());
                                            intent.putExtra("location_id", locationHistory.getLocation_id());
                                            intent.putExtra("location_name", locationHistory.getLocation_name());
                                            intent.putExtra("team_leader_id", locationHistory.getTeam_leader_id());
                                            intent.putExtra("team_leader_name", locationHistory.getTeam_leader_name());
                                            intent.putExtra("interaction", locationHistory.getInteraction());
                                            intent.putExtra("sales", locationHistory.getSales());
                                            intent.putExtra("merchandise", locationHistory.getMerchandise());
                                            intent.putExtra("date", date);
                                            startActivity(intent);
                                        }
                                    })
                            );


                        }

                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            pDialog.dismiss();
                            Log.d("Expense response", "Result " + anError);
                            new SweetAlertDialog(LocationReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR!")
                                    .setContentText("Something went wrong, Please try again later")
                                    .show();
                        }
                    });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_home:
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_role = sharedPreferences.getString("user_role","user_role");
                int role = Integer.parseInt(user_role);
                if(role == 3){
                    Intent intent = new Intent(LocationReportActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(LocationReportActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
