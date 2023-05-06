package com.androidstudy.alfoneshub.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.ReportConfirmationAdapter;
import com.androidstudy.alfoneshub.models.ReportConfirmation;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportConfirmationActivity extends AppCompatActivity {

    MaterialSpinner spinner_activation;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewReportConfirmation;
    ReportConfirmationAdapter reportConfirmationAdapter;
    private List<ReportConfirmation> reportConfirmationList = new ArrayList<>();
    
    String activation_name="";
    TextView textViewCampaignName;
    LinearLayout linearLayoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_confirmation);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Report Confirmation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);
        spinner_activation=(MaterialSpinner)findViewById(R.id.spinner_activation);
        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);
        recyclerViewReportConfirmation= (RecyclerView)findViewById(R.id.recycler_report_confirmation);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewReportConfirmation.setLayoutManager(gridLayoutManager);

        fetchSpinnerData();

    }

    private void fetchSpinnerData() {
        SharedPreferences preferences = ReportConfirmationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.TEAM_LEADER_PREVIOUS_ACTIVATION)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Campaign details", "Result " + response.toString());

                        String JSON_ARRAY = "result";
                        JSONObject c=null;
                        List<String> listCampaign = new ArrayList<String>();
                        try {
                            JSONArray feedArray = response.getJSONArray(JSON_ARRAY);

                            for (int i = 0; i < feedArray.length(); i++) {
                                c = (JSONObject) feedArray.get(i);
                                activation_name = c.getString("activation_name");
                                listCampaign.add(activation_name);
                            }
                            //Get first activation_name
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                activation_name = c.getString("activation_name");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            spinner_activation.setItems(listCampaign);
                            spinner_activation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    activation_name=item;
                                    textViewCampaignName.setText(activation_name);
                                    Toast.makeText(ReportConfirmationActivity.this, "" +activation_name, Toast.LENGTH_SHORT).show();
                                    if(reportConfirmationList.size()>0){
                                        reportConfirmationList.clear();
                                    }
                                    fetchReportConfirmationHistory(activation_name);
                                    reportConfirmationAdapter.notifyDataSetChanged();
                                }
                            });
                            fetchReportConfirmationHistory(activation_name);
                            textViewCampaignName.setText(activation_name);
                        }catch (Exception e){
                            Log.d("ERROR","Spinner not populated");
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        // handle error
                        Log.d("Confirmation response", "Result " + error);
                        new SweetAlertDialog(ReportConfirmationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });

    }

    public void fetchReportConfirmationHistory(String activation_name){
        SharedPreferences preferences = ReportConfirmationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(ReportConfirmationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.REPORT_CONFIRMATION_URL)
                .addBodyParameter("activation_name", activation_name)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("Confimation response", response.toString());
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
                                recyclerViewReportConfirmation.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                recyclerViewReportConfirmation.setVisibility(View.VISIBLE);
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
                                String id = c.getString("id");
                                String date = c.getString("date");
                                String team_leader = c.getString("team_leader");
                                String user_id = c.getString("user_id");
                                String activation_id = c.getString("activation_id");
                                ReportConfirmation checkBoardEntries = new ReportConfirmation(id, date, team_leader, user_id, activation_id);
                                reportConfirmationList.add(checkBoardEntries);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        reportConfirmationAdapter = new ReportConfirmationAdapter(ReportConfirmationActivity.this, reportConfirmationList);
                        recyclerViewReportConfirmation.setAdapter(reportConfirmationAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Confimation response", "Result " + anError);
                        new SweetAlertDialog(ReportConfirmationActivity.this, SweetAlertDialog.ERROR_TYPE)
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
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
