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
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.adapters.CampaignDetailsAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.CampaignDetail;
import com.androidstudy.alfoneshub.utils.AppStatus;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportHistoryActivity extends AppCompatActivity {
    RecyclerView reportHistoryRecycler;
    CampaignDetailsAdapter campaignReportAdapter;
    private List<CampaignDetail> activationDetailList = new ArrayList<>();

    SweetAlertDialog pDialog;
    LinearLayout linearLayoutError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Report History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);
        reportHistoryRecycler= (RecyclerView)findViewById(R.id.recycler_report_history);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        reportHistoryRecycler.setLayoutManager(gridLayoutManager);

        performCall();
    }

    private void performCall() {
        /**
         * Perform network call
         */
        if (AppStatus.getInstance(this.getApplicationContext()).isOnline()) {
            pDialog = new SweetAlertDialog(ReportHistoryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
            pDialog.setTitleText("Loading reports...");
            pDialog.setCancelable(false);
            pDialog.show();
            //Get user ID

            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String user_role = sharedPreferences.getString("user_role","user_role");
            int role = Integer.parseInt(user_role);
            if(role==3){

                SharedPreferences preferences = ReportHistoryActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.PROJECT_MANAGER_PREVIOUS_ACTIVATION)
                        .addBodyParameter("user_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Campaign response", response.toString());
                                pDialog.dismiss();
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
                                        reportHistoryRecycler.setVisibility(View.GONE);
                                        linearLayoutError.setVisibility(View.VISIBLE);
                                    }else{
                                        Log.d("ARRAY", "NOT empty");
                                        reportHistoryRecycler.setVisibility(View.VISIBLE);
                                        linearLayoutError.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Loop through the created array
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);
                                        String id = c.getString("activation_id");
                                        String activation_name = c.getString("activation_name");
                                        String activation_description = c.getString("activation_description");
                                        String activation_start_date = c.getString("activation_start_date");
                                        String activation_end_date = c.getString("activation_end_date");
                                        String activation_company = c.getString("activation_company");
                                        CampaignDetail activation = new CampaignDetail(id, activation_name, activation_description,activation_start_date, activation_end_date,activation_company);
                                        activationDetailList.add(activation);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //Set adapter listactivation_id
                                campaignReportAdapter = new CampaignDetailsAdapter(ReportHistoryActivity.this, activationDetailList);
                                reportHistoryRecycler.setAdapter(campaignReportAdapter);
                                reportHistoryRecycler.addOnItemTouchListener(
                                        new MyRecyclerItemClickListener(ReportHistoryActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                CampaignDetail activation = activationDetailList.get(position);
                                                Intent intent = new Intent(ReportHistoryActivity.this, LocationReportActivity.class);
                                                intent.putExtra("activation_id", activation.getId());
                                                intent.putExtra("activation_name", activation.getCampaign_name());
                                                intent.putExtra("activation_description", activation.getCampaign_description());
                                                intent.putExtra("activation_start_date", activation.getCampaign_start_date());
                                                intent.putExtra("activation_end_date", activation.getCampaign_end_date());
                                                intent.putExtra("activation_company", activation.getCampaign_company());
                                                startActivity(intent);
                                            }
                                        })
                                );


                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(ReportHistoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else  if(role==4){

                SharedPreferences preferences = ReportHistoryActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.TEAM_LEADER_PREVIOUS_ACTIVATION)
                        .addBodyParameter("user_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Campaign response", response.toString());
                                pDialog.dismiss();
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
                                        reportHistoryRecycler.setVisibility(View.GONE);
                                        linearLayoutError.setVisibility(View.VISIBLE);
                                    }else{
                                        Log.d("ARRAY", "NOT empty");
                                        reportHistoryRecycler.setVisibility(View.VISIBLE);
                                        linearLayoutError.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Loop through the created array
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);
                                        String id = c.getString("activation_id");
                                        String activation_name = c.getString("activation_name");
                                        String activation_description = c.getString("activation_description");
                                        String activation_start_date = c.getString("activation_start_date");
                                        String activation_end_date = c.getString("activation_end_date");
                                        String activation_company = c.getString("activation_company");
                                        CampaignDetail activation = new CampaignDetail(id, activation_name, activation_description,activation_start_date, activation_end_date,activation_company);
                                        activationDetailList.add(activation);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //Set adapter listactivation_id
                                campaignReportAdapter = new CampaignDetailsAdapter(ReportHistoryActivity.this, activationDetailList);
                                reportHistoryRecycler.setAdapter(campaignReportAdapter);
                                reportHistoryRecycler.addOnItemTouchListener(
                                        new MyRecyclerItemClickListener(ReportHistoryActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                CampaignDetail activation = activationDetailList.get(position);
                                                Intent intent = new Intent(ReportHistoryActivity.this, LocationReportActivity.class);
                                                intent.putExtra("activation_id", activation.getId());
                                                intent.putExtra("activation_name", activation.getCampaign_name());
                                                intent.putExtra("activation_description", activation.getCampaign_description());
                                                intent.putExtra("activation_start_date", activation.getCampaign_start_date());
                                                intent.putExtra("activation_end_date", activation.getCampaign_end_date());
                                                intent.putExtra("activation_company", activation.getCampaign_company());
                                                startActivity(intent);
                                            }
                                        })
                                );


                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(ReportHistoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else if(role==5 || role==7){

                SharedPreferences preferences = ReportHistoryActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.OPERATIONS_PREVIOUS_ACTIVATION)
                        .addBodyParameter("user_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Campaign response", response.toString());
                                pDialog.dismiss();
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
                                        reportHistoryRecycler.setVisibility(View.GONE);
                                        linearLayoutError.setVisibility(View.VISIBLE);
                                    }else{
                                        Log.d("ARRAY", "NOT empty");
                                        reportHistoryRecycler.setVisibility(View.VISIBLE);
                                        linearLayoutError.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Loop through the created array
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject c = null;
                                    try {
                                        c = array.getJSONObject(i);
                                        String id = c.getString("activation_id");
                                        String activation_name = c.getString("activation_name");
                                        String activation_description = c.getString("activation_description");
                                        String activation_start_date = c.getString("activation_start_date");
                                        String activation_end_date = c.getString("activation_end_date");
                                        String activation_company = c.getString("activation_company");
                                        CampaignDetail activation = new CampaignDetail(id, activation_name, activation_description,activation_start_date, activation_end_date,activation_company);
                                        activationDetailList.add(activation);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //Set adapter listactivation_id
                                campaignReportAdapter = new CampaignDetailsAdapter(ReportHistoryActivity.this, activationDetailList);
                                reportHistoryRecycler.setAdapter(campaignReportAdapter);
                                reportHistoryRecycler.addOnItemTouchListener(
                                        new MyRecyclerItemClickListener(ReportHistoryActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                CampaignDetail activation = activationDetailList.get(position);
                                                Intent intent = new Intent(ReportHistoryActivity.this, LocationReportActivity.class);
                                                intent.putExtra("activation_id", activation.getId());
                                                intent.putExtra("activation_name", activation.getCampaign_name());
                                                intent.putExtra("activation_description", activation.getCampaign_description());
                                                intent.putExtra("activation_start_date", activation.getCampaign_start_date());
                                                intent.putExtra("activation_end_date", activation.getCampaign_end_date());
                                                intent.putExtra("activation_company", activation.getCampaign_company());
                                                startActivity(intent);
                                            }
                                        })
                                );


                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(ReportHistoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }


        }else{
            Log.d("Error","No internet connection");
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
                    Intent intent = new Intent(ReportHistoryActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(ReportHistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
