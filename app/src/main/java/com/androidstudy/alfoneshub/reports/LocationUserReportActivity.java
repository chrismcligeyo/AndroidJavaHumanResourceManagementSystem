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
import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.adapters.LocationUserHistoryAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.LocationUserHistory;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class LocationUserReportActivity extends AppCompatActivity {

    String activation_id, location_id, location_name, team_leader_id,team_leader_name, interaction, sales, merchandise, date="";
    SweetAlertDialog pDialog;
    TextView textViewLocationName, textViewDate;

    RecyclerView recyclerViewReportHistory;
    LocationUserHistoryAdapter locationUserHistoryAdapter;
    private List<LocationUserHistory> locationUserHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_user_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("User Reports");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewDate = (TextView)findViewById(R.id.txt_activation_date);
        textViewLocationName = (TextView)findViewById(R.id.txt_activation_name);

        recyclerViewReportHistory= (RecyclerView)findViewById(R.id.recycler_report_history);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewReportHistory.setLayoutManager(gridLayoutManager);

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            Log.d("ACTIVATION_ID", activation_id);
            location_id = intent.getStringExtra("location_id");
            Log.d("LOCATION_ID", location_id);
            location_name = intent.getStringExtra("location_name");
            team_leader_id = intent.getStringExtra("team_leader_id");
            team_leader_name = intent.getStringExtra("team_leader_name");
            interaction = intent.getStringExtra("interaction");
            sales = intent.getStringExtra("sales");
            merchandise = intent.getStringExtra("merchandise");
            date = intent.getStringExtra("date");

        }

        textViewLocationName.setText(location_name.toUpperCase());
        textViewDate.setText(date.toUpperCase());

        fetchRecordsHistory(date);
        
    }

    public void fetchRecordsHistory(final String date){
        SharedPreferences preferences = LocationUserReportActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        Log.d("USER_ID", user_id);

        pDialog = new SweetAlertDialog(LocationUserReportActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_role = sharedPreferences.getString("user_role","user_role");
        int role = Integer.parseInt(user_role);
        if(role == 3 || role == 5 || role == 7){

            AndroidNetworking.post(URLs.PROJECT_MANAGER_LOCATION_USER_REPORT_ENTRIES_URL)
                    .addBodyParameter("activation_id", activation_id)
                    .addBodyParameter("date", date)
                    .addBodyParameter("location_id", location_id)
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
                                    String user_id = c.getString("user_id");
                                    String user_name = c.getString("user_name");
                                    String interaction = c.getString("interaction");
                                    String sales = c.getString("sales");
                                    String merchandise = c.getString("merchandise");
                                    String created_at = c.getString("created_at");
                                    String updated_at = c.getString("updated_at");
                                    LocationUserHistory checkBoardEntries = new LocationUserHistory(id, activation_id, user_id, user_name, interaction, sales, merchandise, created_at, updated_at);
                                    locationUserHistoryList.add(checkBoardEntries);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Set adapter listInteractionEntries_id
                            locationUserHistoryAdapter = new LocationUserHistoryAdapter(LocationUserReportActivity.this, locationUserHistoryList);
                            recyclerViewReportHistory.setAdapter(locationUserHistoryAdapter);
                            recyclerViewReportHistory.addOnItemTouchListener(
                                    new MyRecyclerItemClickListener(LocationUserReportActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            LocationUserHistory locationHistory = locationUserHistoryList.get(position);
                                            Intent intent = new Intent(LocationUserReportActivity.this, UserReportEntriesActivity.class);
                                            intent.putExtra("id", locationHistory.getId());
                                            intent.putExtra("activation_id", locationHistory.getActivation_id());
                                            intent.putExtra("location_id", location_id);
                                            intent.putExtra("user_id", locationHistory.getUser_id());
                                            intent.putExtra("user_name", locationHistory.getUser_name());
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
                            new SweetAlertDialog(LocationUserReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("ERROR!")
                                    .setContentText("Something went wrong, Please try again later")
                                    .show();
                        }
                    });

        }else if(role ==4){

            AndroidNetworking.post(URLs.TEAM_LEADER_LOCATION_USER_REPORT_ENTRIES_URL)
                    .addBodyParameter("activation_id", activation_id)
                    .addBodyParameter("user_id", user_id)
                    .addBodyParameter("date", date)
                    .addBodyParameter("location_id", location_id)
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
                                    String user_id = c.getString("user_id");
                                    String user_name = c.getString("user_name");
                                    String interaction = c.getString("interaction");
                                    String sales = c.getString("sales");
                                    String merchandise = c.getString("merchandise");
                                    String created_at = c.getString("created_at");
                                    String updated_at = c.getString("updated_at");
                                    LocationUserHistory checkBoardEntries = new LocationUserHistory(id, activation_id, user_id, user_name, interaction, sales, merchandise, created_at, updated_at);
                                    locationUserHistoryList.add(checkBoardEntries);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //Set adapter listInteractionEntries_id
                            locationUserHistoryAdapter = new LocationUserHistoryAdapter(LocationUserReportActivity.this, locationUserHistoryList);
                            recyclerViewReportHistory.setAdapter(locationUserHistoryAdapter);
                            recyclerViewReportHistory.addOnItemTouchListener(
                                    new MyRecyclerItemClickListener(LocationUserReportActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            LocationUserHistory locationHistory = locationUserHistoryList.get(position);
                                            Intent intent = new Intent(LocationUserReportActivity.this, UserReportEntriesActivity.class);
                                            intent.putExtra("id", locationHistory.getId());
                                            intent.putExtra("activation_id", locationHistory.getActivation_id());
                                            intent.putExtra("location_id", location_id);
                                            intent.putExtra("user_id", locationHistory.getUser_id());
                                            intent.putExtra("user_name", locationHistory.getUser_name());
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
                            new SweetAlertDialog(LocationUserReportActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                    Intent intent = new Intent(LocationUserReportActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(LocationUserReportActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
