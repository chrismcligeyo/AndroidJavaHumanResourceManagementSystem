package com.androidstudy.alfoneshub.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TeamLeaderActivity extends AppCompatActivity {
    TextView textViewTeamLeaderName, textViewTeamLeaderPhone, textViewProfile, textViewEdit;
    String team_leader_id="", team_leader_name="", team_leader_phone="", spinner_team_leader_name="";
    SweetAlertDialog pDialog;
    MaterialSpinner spinner_team_leader;
    Button btnUpdate;

    int update_success = 0;

    CardView cardViewTeamLeaderEdit;
    Boolean editPressed = false;
    TextView textViewCampaignName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_leader);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Team Leader");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);
        textViewTeamLeaderName = (TextView)findViewById(R.id.txt_team_leader_name);
        textViewTeamLeaderPhone = (TextView)findViewById(R.id.txt_team_leader_phone);
        textViewProfile = (TextView)findViewById(R.id.text_view_profile);
        textViewEdit = (TextView)findViewById(R.id.txt_campaign_edit);

        cardViewTeamLeaderEdit = (CardView)findViewById(R.id.card_view_campaign_edit);
        cardViewTeamLeaderEdit.setVisibility(View.GONE);
        spinner_team_leader=(MaterialSpinner)findViewById(R.id.spinner_team_leader);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPressed==false){

                    new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Update team leader")
                            .setContentText("You are about to update your current team leader! Do you wish to continue")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    editPressed=true;
                                    textViewEdit.setText("DONE");
                                    cardViewTeamLeaderEdit.setVisibility(View.VISIBLE);
                                    fetchSpinnerData();
                                }

                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                }else{
                    editPressed=false;
                    textViewEdit.setText("EDIT");
                    cardViewTeamLeaderEdit.setVisibility(View.GONE);
                }
            }
        });

        btnUpdate = (Button)findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(spinner_team_leader_name)) {
                    spinner_team_leader.setError("Select Activation");
                    return;
                }
                

                updateTeamLeader(spinner_team_leader_name);
            }
        });

        SharedPreferences preferences = TeamLeaderActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        String activation_name = preferences.getString("activation_name", "activation_name");
        textViewCampaignName.setText(activation_name);

        fetchTeamLeaderDetails();
    }

    private void updateTeamLeader(String spinner_team_leader_name) {
        pDialog = new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Updating...");
        pDialog.setCancelable(false);
        pDialog.show();
        SharedPreferences preferences = TeamLeaderActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.USER_TEAM_LEADER_DETAILS_UPDATE)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("team_leader_name", spinner_team_leader_name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("update response", response.toString());
                        try {
                            update_success = response.getInt("success");

                            if(update_success==1) {
                                team_leader_id = response.getString("team_leader_id");
                                team_leader_name = response.getString("team_leader_name");
                                team_leader_phone = response.getString("team_leader_phone");
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Update team leader")
                                        .setContentText("Team leader has been successfully updated ")
                                        .setConfirmText("Ok! Thanks")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                editPressed = false;
                                                cardViewTeamLeaderEdit.setVisibility(View.GONE);
                                                textViewEdit.setText("EDIT");
                                                setDetails();
                                            }

                                        })
                                        .show();
                            }

                            else if(update_success==2){
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("You are currently NOT in any activation. Please join an activation")
                                        .show();
                            }

                            else if(update_success==0){
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Failed to update team leader")
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("tl response", "Result " + anError);
                        new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    private void fetchTeamLeaderDetails() {
        pDialog = new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        SharedPreferences preferences = TeamLeaderActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        String activation_id = preferences.getString("activation_id", "activation_id");
        AndroidNetworking.post(URLs.USER_TEAM_LEADER_DETAILS)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("activation_id", activation_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("summary response", response.toString());
                        try {

                            int success = response.getInt("success");
                            if (success==1){
                                team_leader_id = response.getString("team_leader_id");
                                team_leader_name = response.getString("team_leader_name");
                                team_leader_phone = response.getString("team_leader_phone");
                                setDetails();
                            }

                            else if(success==2){
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("You are currently NOT in any activation. Please join an activation")
                                        .show();

                                textViewEdit.setVisibility(View.GONE);
                            }

                            else if(success==3){
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Currently NO is team leader assigned to you. Please select your team leader")
                                        .show();
                            }

                            else if(success==4){
                                new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Currently NOT team leader assigned to you. Please select your team leader")
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("tl response", "Result " + anError);
                        new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                        setDetails();
                    }
                });

    }

    private void setDetails() {
        textViewTeamLeaderName.setText(team_leader_name.toUpperCase());
        textViewTeamLeaderPhone.setText(team_leader_phone);
        textViewProfile.setText(team_leader_name = String.valueOf(team_leader_name.charAt(0)).toUpperCase());
    }

    private void fetchSpinnerData() {
        SharedPreferences preferences = TeamLeaderActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.ACTIVE_ACTIVATION_TEAM_LEADERS)
                .addBodyParameter("user_id", user_id)
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
                                spinner_team_leader_name = c.getString("team_leader_name");
                                listCampaign.add(spinner_team_leader_name);
                            }
                            //Get first activation_name
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                spinner_team_leader_name = c.getString("team_leader_name");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            spinner_team_leader.setItems(listCampaign);
                            spinner_team_leader.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    spinner_team_leader_name=item;
                                    Toast.makeText(TeamLeaderActivity.this, "" +spinner_team_leader_name, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            Log.d("ERROR","Spinner not populated");
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        // handle error
                        Log.d("CheckInHistory response", "Result " + error);
                        new SweetAlertDialog(TeamLeaderActivity.this, SweetAlertDialog.ERROR_TYPE)
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
