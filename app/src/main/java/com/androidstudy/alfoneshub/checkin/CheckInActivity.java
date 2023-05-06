package com.androidstudy.alfoneshub.checkin;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.CheckInHistoryAdapter;
import com.androidstudy.alfoneshub.models.CheckInHistory;
import com.androidstudy.alfoneshub.utils.AppStatus;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheckInActivity extends AppCompatActivity {

    MaterialSpinner spinner_activation;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewCheckInHistory;
    CheckInHistoryAdapter checkInHistoryAdapter;
    private List<CheckInHistory> checkInHistoryList = new ArrayList<>();
    TextView textViewCampaignName;
    LinearLayout linearLayoutError;
    Button buttonCheckIn;
    
    String activation_name="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Check-In");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinner_activation=(MaterialSpinner)findViewById(R.id.spinner_activation);
        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);
        recyclerViewCheckInHistory= (RecyclerView)findViewById(R.id.recycler_check_in);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewCheckInHistory.setLayoutManager(gridLayoutManager);
        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);

        buttonCheckIn = (Button)findViewById(R.id.button_check_in);
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               updateCheckIn();
            }
        });

        fetchSpinnerData();

    }

    private void updateCheckIn() {

        pDialog = new SweetAlertDialog(CheckInActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Checking in...");
        pDialog.setCancelable(true);
        pDialog.show();

        for (int i=0; i<checkInHistoryList.size();i++) {
            final String check_in = checkInHistoryList.get(i).getCheck_in();
            final String user_id = checkInHistoryList.get(i).getUser_id();
            final String activation_id = checkInHistoryList.get(i).getActivation_id();

            if(check_in.equalsIgnoreCase("1")){
                SharedPreferences preferences = CheckInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String admin_id = preferences.getString("user_id", "user_id");

                final int finalI = i;
                AndroidNetworking.post(URLs.TEAM_LEADER_ADD_CHECK_IN_URL)
                        .addBodyParameter("user_id", user_id)
                        .addBodyParameter("admin_id", admin_id)
                        .addBodyParameter("activation_id", activation_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("CHECK IN", response.toString());
                                if(finalI == checkInHistoryList.size()-1){
                                    pDialog.dismiss();

                                    if(checkInHistoryList.size()>0){
                                        checkInHistoryList.clear();
                                    }
                                    fetchCheckInHistory(activation_name);
                                    checkInHistoryAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("CHECK IN ERROR", anError.toString());
                                if(finalI == checkInHistoryList.size()-1){
                                    pDialog.dismiss();

                                    if(checkInHistoryList.size()>0){
                                        checkInHistoryList.clear();
                                    }
                                    fetchCheckInHistory(activation_name);
                                    checkInHistoryAdapter.notifyDataSetChanged();
                                }
                            }
                        });


            }
        }
    }

    private void fetchSpinnerData() {
        /**
         * Perform network call
         */
        if (AppStatus.getInstance(this.getApplicationContext()).isOnline()) {

            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String user_role = sharedPreferences.getString("user_role","user_role");
            int role = Integer.parseInt(user_role);
            if(role==3){

                SharedPreferences preferences = CheckInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.PROJECT_MANAGER_PREVIOUS_ACTIVATION)
                        .addBodyParameter("user_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Campaign response", response.toString());

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
                                            Toast.makeText(CheckInActivity.this, "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(checkInHistoryList.size()>0){
                                                checkInHistoryList.clear();
                                            }
                                            fetchCheckInHistory(activation_name);
                                            checkInHistoryAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    fetchCheckInHistory(activation_name);
                                    textViewCampaignName.setText(activation_name);
                                }catch (Exception e){
                                    Log.d("ERROR","Spinner not populated");
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(CheckInActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else  if(role==4){

                SharedPreferences preferences = CheckInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.TEAM_LEADER_PREVIOUS_ACTIVATION)
                        .addBodyParameter("user_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Campaign response", response.toString());
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
                                            Toast.makeText(CheckInActivity.this, "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(checkInHistoryList.size()>0){
                                                checkInHistoryList.clear();
                                            }
                                            fetchCheckInHistory(activation_name);
                                            checkInHistoryAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    fetchCheckInHistory(activation_name);
                                    textViewCampaignName.setText(activation_name);
                                }catch (Exception e){
                                    Log.d("ERROR","Spinner not populated");
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(CheckInActivity.this, SweetAlertDialog.ERROR_TYPE)
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


    public void fetchCheckInHistory(String activation_name){
        SharedPreferences preferences = CheckInActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(CheckInActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.TEAM_LEADER_CHECK_IN_URL)
                .addBodyParameter("activation_name", activation_name)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("TEAM LEADER CHECK IN", response.toString());

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
                                recyclerViewCheckInHistory.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                                buttonCheckIn.setVisibility(View.GONE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                recyclerViewCheckInHistory.setVisibility(View.VISIBLE);
                                linearLayoutError.setVisibility(View.GONE);
                                buttonCheckIn.setVisibility(View.VISIBLE);
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
                                String user_id = c.getString("user_id");
                                String user_name = c.getString("user_name");
                                String check_in = c.getString("check_in");
                                CheckInHistory checkInHistory = new CheckInHistory(id, activation_id, user_id, user_name, check_in);
                                checkInHistoryList.add(checkInHistory);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        checkInHistoryAdapter = new CheckInHistoryAdapter(CheckInActivity.this, checkInHistoryList);
                        recyclerViewCheckInHistory.setAdapter(checkInHistoryAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Confimation response", "Result " + anError);
                        new SweetAlertDialog(CheckInActivity.this, SweetAlertDialog.ERROR_TYPE)
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
