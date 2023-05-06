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
import android.widget.CheckBox;
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

public class ActivationActivity extends AppCompatActivity {
    TextView textViewCampaignName, textViewCampaignDate, textViewCampaignCompany, textViewProfile,
            textViewEdit, textViewTermsCondition;
    String activation_id="", activation_name="", activation_date="", activation_status="",
            activation_company="", spinner_activation_name="";
    SweetAlertDialog pDialog;
    MaterialSpinner spinner_activation;
    Button btnUpdate;
    CheckBox checkBoxTerms;
    int update_success = 0;
    
    CardView cardViewCampaignEdit;
    Boolean editPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Activation Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        textViewCampaignName = (TextView)findViewById(R.id.txtCampaignName);
        textViewCampaignDate = (TextView)findViewById(R.id.textCampaignDate);
        textViewCampaignCompany = (TextView)findViewById(R.id.textCampaignCompany);
        textViewProfile = (TextView)findViewById(R.id.text_view_profile);
        textViewEdit = (TextView)findViewById(R.id.txt_activation_edit);
       

        cardViewCampaignEdit = (CardView)findViewById(R.id.card_view_activation_edit);
        cardViewCampaignEdit.setVisibility(View.GONE);
        spinner_activation=(MaterialSpinner)findViewById(R.id.spinner_activation);
        checkBoxTerms = (CheckBox)findViewById(R.id.checkbox_terms);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editPressed==false){

                    new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Update activation")
                            .setContentText("You are about to update your current activation! Do you wish to continue")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                        editPressed=true;
                                        textViewEdit.setText("DONE");
                                        cardViewCampaignEdit.setVisibility(View.VISIBLE);
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
                    cardViewCampaignEdit.setVisibility(View.GONE);
                    checkBoxTerms.setChecked(false);
                }
            }
        });

        btnUpdate = (Button)findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(spinner_activation_name)) {
                    spinner_activation.setError("Select Activation");
                    return;
                }

                if(!checkBoxTerms.isChecked()){
                    checkBoxTerms.setError("You have to agree before continuing");
                    return;
                }

                updateCampaign(spinner_activation_name);
            }
        });
        
        fetchCampaignDetails();
    }

    private void updateCampaign(String spinner_activation_name) {
        pDialog = new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Updating...");
        pDialog.setCancelable(false);
        pDialog.show();
        SharedPreferences preferences = ActivationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.USER_ACTIVATION_DETAILS_UPDATE)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("activation_name", spinner_activation_name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("update response", response.toString());
                        try {
                            update_success = response.getInt("success");
                            activation_name = response.getString("activation_name");
                            activation_company = response.getString("activation_company");
                            activation_date = response.getString("activation_date");
                            activation_status = response.getString("activation_status");

                            if(update_success==1 || update_success ==2){
                                new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Update activation")
                                        .setContentText("Activation has been successfully updated ")
                                        .setConfirmText("Ok! Thanks")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                editPressed = false;
                                                cardViewCampaignEdit.setVisibility(View.GONE);
                                                checkBoxTerms.setChecked(false);
                                                setDetails();
                                            }

                                        })
                                        .show();
                            }else{
                                new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
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
                        Log.d("activation response", "Result " + anError);
                        new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    private void fetchCampaignDetails() {
        pDialog = new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        SharedPreferences preferences = ActivationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.USER_ACTIVATION_DETAILS)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("summary response", response.toString());
                        try {
                            activation_id = response.getString("activation_id");
                            activation_name = response.getString("activation_name");
                            activation_company = response.getString("activation_company");
                            activation_date = response.getString("activation_date");
                            activation_status = response.getString("activation_status");

                            setDetails();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("activation response", "Result " + anError);
                        new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                        setDetails();
                    }
                });

    }

    private void setDetails() {
        textViewCampaignName.setText(activation_name.toUpperCase());
        textViewCampaignDate.setText(activation_date);
        textViewCampaignCompany.setText(activation_company.toUpperCase());
        if(!activation_name.isEmpty()){
            textViewProfile.setText(activation_name = String.valueOf(activation_name.charAt(0)).toUpperCase());
        }
    }

    private void fetchSpinnerData() {
        SharedPreferences preferences = ActivationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.get(URLs.ACTIVE_ACTIVATION_LIST_URL)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Activation list", "Result " + response.toString());

                        String JSON_ARRAY = "result";
                        JSONObject c=null;
                        List<String> listCampaign = new ArrayList<String>();
                        try {
                            JSONArray feedArray = response.getJSONArray(JSON_ARRAY);

                            for (int i = 0; i < feedArray.length(); i++) {
                                c = (JSONObject) feedArray.get(i);
                                spinner_activation_name = c.getString("activation_name");
                                listCampaign.add(spinner_activation_name);
                            }
                            //Get first activation_name
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                spinner_activation_name = c.getString("activation_name");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            spinner_activation.setItems(listCampaign);
                            spinner_activation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    spinner_activation_name=item;
                                    Toast.makeText(ActivationActivity.this, "" +spinner_activation_name, Toast.LENGTH_SHORT).show();
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
                        new SweetAlertDialog(ActivationActivity.this, SweetAlertDialog.ERROR_TYPE)
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
