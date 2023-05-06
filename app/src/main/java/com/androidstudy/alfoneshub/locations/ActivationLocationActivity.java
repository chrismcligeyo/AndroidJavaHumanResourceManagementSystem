package com.androidstudy.alfoneshub.locations;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.LocationActivationAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.Location;
import com.androidstudy.alfoneshub.utils.AppStatus;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.TrackGPS;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class ActivationLocationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    MaterialSpinner spinner_activation;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewLocation;
    LocationActivationAdapter locationActivationAdapter;
    private List<Location> locationList = new ArrayList<>();
    TextView textViewCampaignName;
    LinearLayout linearLayoutError;
    Button buttonAddLocation;
    DatePickerDialog mDatePicker;
    String division, region;
    boolean initial_region_init = false;

    TrackGPS gps;
    Double latitude=0.0, longitude=0.0;

    ProgressBar progressBar;
    String activation_name="";

    List<String> divisionList = new ArrayList<String>();
    List<String> regionList = new ArrayList<String>();
    Spinner spinnerDivision, spinnerRegion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_location);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Locations");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        spinner_activation=(MaterialSpinner)findViewById(R.id.spinner_activation);
        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);
        recyclerViewLocation= (RecyclerView)findViewById(R.id.recycler_check_in);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewLocation.setLayoutManager(gridLayoutManager);
        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);

        buttonAddLocation = (Button) findViewById(R.id.button_add_location);
        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ActivationLocationActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_location_dialog_layout);

                progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextLocationName, editTextLocationDescription,  editTextLocationIdentifier, editTextDate, editTextDuration, editTextLocationComments;
                final String[] selectedDate = {""};
                editTextLocationName = (EditText)dialog.findViewById(R.id.edit_text_location_name);
                editTextLocationDescription = (EditText)dialog.findViewById(R.id.edit_text_location_description);
                editTextLocationIdentifier = (EditText)dialog.findViewById(R.id.edit_text_location_identifier);
                editTextLocationComments = (EditText)dialog.findViewById(R.id.edit_text_location_comments);
                spinnerDivision = (Spinner) dialog.findViewById(R.id.spinner_division);
                spinnerRegion = (Spinner) dialog.findViewById(R.id.spinner_region);


                editTextDate = (EditText)dialog.findViewById(R.id.edit_text_date);
                editTextDuration = (EditText)dialog.findViewById(R.id.edit_text_duration);
                editTextDate.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            int mYear = mcurrentDate.get(Calendar.YEAR);
                            int mMonth = mcurrentDate.get(Calendar.MONTH);
                            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            mDatePicker = new DatePickerDialog(ActivationLocationActivity.this, new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    /*0000-00-00*/
                                    selectedmonth += 1;
                                    selectedDate[0] = selectedyear + "-" + selectedmonth + "-" + selectedday;
                                    editTextDate.setText(selectedDate[0]);


                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select date");
                            mDatePicker.show();
                        }
                        return false;
                    }

                });

                fetchDivision();

                Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_info_cancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button buttonCreate = (Button) dialog.findViewById(R.id.dialog_info_create);
                buttonCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE);

                        String location_name = editTextLocationName.getText().toString();
                        if (TextUtils.isEmpty(location_name)) {
                            Toast.makeText(ActivationLocationActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String location_description = editTextLocationDescription.getText().toString();
                        String location_identifier = editTextLocationIdentifier.getText().toString();
                        String location_comments = editTextLocationComments.getText().toString();

                        String date = editTextDate.getText().toString();
                        if (TextUtils.isEmpty(date)) {
                            Toast.makeText(ActivationLocationActivity.this, "Please enter date", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String duration = editTextDuration.getText().toString();
                        if (TextUtils.isEmpty(duration)) {
                            Toast.makeText(ActivationLocationActivity.this, "Please enter duration", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (TextUtils.isEmpty(division)) {
                            Toast.makeText(ActivationLocationActivity.this, "Please enter division", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (TextUtils.isEmpty(region)) {
                            Toast.makeText(ActivationLocationActivity.this, "Please enter region", Toast.LENGTH_LONG).show();
                            return;
                        }


                        SharedPreferences preferences = ActivationLocationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        String user_id = preferences.getString("user_id", "user_id");


                        AndroidNetworking.post(URLs.CREATE_LOCATION)
                                .addBodyParameter("name", location_name)
                                .addBodyParameter("description", location_description)
                                .addBodyParameter("location_identifier", location_identifier)
                                .addBodyParameter("location_comments", location_comments)
                                .addBodyParameter("team_leader_id", user_id)
                                .addBodyParameter("date", date)
                                .addBodyParameter("duration", duration)
                                .addBodyParameter("division", division)
                                .addBodyParameter("region", region)
                                .addBodyParameter("latitude", String.valueOf(latitude))
                                .addBodyParameter("longitude", String.valueOf(longitude))
                                .addBodyParameter("activation", activation_name)
                                .setTag("reset_code")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        progressBar.setVisibility(View.GONE);
                                        int success=0;
                                        //Check for successful login
                                        Log.d("Create location Here", "Result " + response.toString());
                                        try {
                                            success = response.getInt(SUCCESS);
                                            //Login has been succesful.
                                            if (success==1){

                                                Toast.makeText(ActivationLocationActivity.this, "Success, location created", Toast.LENGTH_LONG).show();
                                                if(locationList.size()>0){
                                                    locationList.clear();
                                                }
                                                fetchActivationLocation(activation_name);
                                                locationActivationAdapter.notifyDataSetChanged();
                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(ActivationLocationActivity.this, "Error, location not created", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(ActivationLocationActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(ActivationLocationActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        gps = new TrackGPS(ActivationLocationActivity.this);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            buttonAddLocation.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "Enable location", Toast.LENGTH_SHORT).show();
            buttonAddLocation.setVisibility(View.GONE);
        }

        fetchSpinnerData();
        
    }

    private void fetchDivision() {

        AndroidNetworking.get(URLs.DIVISION_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Division response", response.toString());
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
                                String name = c.getString("name");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                divisionList.add(name);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Set adapter
                        ArrayAdapter<String> divisionDataAdapter = new ArrayAdapter<String>(ActivationLocationActivity.this, R.layout.spinner_item_filter_custom, divisionList);
                        divisionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDivision.setAdapter(divisionDataAdapter);
                        spinnerDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                                String item = parent.getItemAtPosition(position).toString();
                                division = item;

                                // Fetch region
                                fetchRegion(division);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR", anError.toString());
                    }
                });
    }


    private void fetchRegion(String division) {

        if(regionList.size()>0){
            regionList.clear();
        }

        progressBar.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLs.REGION_LIST)
                .addBodyParameter("division", division)
                .setPriority(Priority.HIGH)
                .build().getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Service response", response.toString());
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
                        String name = c.getString("name");
                        String created_at = c.getString("created_at");
                        String updated_at = c.getString("updated_at");
                        regionList.add(name);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressBar.setVisibility(View.GONE);
                ArrayAdapter<String> regionDataAdapter = new ArrayAdapter<String>(ActivationLocationActivity.this, R.layout.spinner_item_filter_custom, regionList);
                regionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRegion.setAdapter(regionDataAdapter);
                spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                        String item = parent.getItemAtPosition(position).toString();
                        region = item;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onError(ANError anError) {
                progressBar.setVisibility(View.GONE);
                Log.d("ERROR", anError.toString());
            }
        });
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

                SharedPreferences preferences = ActivationLocationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                                            Toast.makeText(ActivationLocationActivity.this, "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(locationList.size()>0){
                                                locationList.clear();
                                            }
                                            fetchActivationLocation(activation_name);
                                            locationActivationAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    fetchActivationLocation(activation_name);
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
                                new SweetAlertDialog(ActivationLocationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else  if(role==4){

                SharedPreferences preferences = ActivationLocationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                                            Toast.makeText(ActivationLocationActivity.this, "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(locationList.size()>0){
                                                locationList.clear();
                                            }
                                            fetchActivationLocation(activation_name);
                                            locationActivationAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    fetchActivationLocation(activation_name);
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
                                new SweetAlertDialog(ActivationLocationActivity.this, SweetAlertDialog.ERROR_TYPE)
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

    public void fetchActivationLocation(String activation_name){
        SharedPreferences preferences = ActivationLocationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(ActivationLocationActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.TEAM_LEADER_LOCATION)
                .addBodyParameter("activation_name", activation_name)
                .addBodyParameter("user_id", user_id)
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
                                recyclerViewLocation.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                recyclerViewLocation.setVisibility(View.VISIBLE);
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
                                String activation_id = c.getString("activation_id");
                                String name = c.getString("name");
                                String description = c.getString("description");
                                String team_leader_id = c.getString("team_leader_id");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                Location location = new Location(id, activation_id, name, description, team_leader_id, created_at, updated_at);
                                locationList.add(location);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        locationActivationAdapter = new LocationActivationAdapter(ActivationLocationActivity.this, locationList);
                        recyclerViewLocation.setAdapter(locationActivationAdapter);
                        recyclerViewLocation.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(ActivationLocationActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Location location = locationList.get(position);
                                        Intent intent = new Intent(ActivationLocationActivity.this, ActivationLocationUsersActivity.class);
                                        intent.putExtra("id", location.getId());
                                        intent.putExtra("activation_id", location.getActivation_id());
                                        intent.putExtra("location_id", location.getId());
                                        intent.putExtra("location_name", location.getName());
                                        startActivity(intent);
                                    }
                                })
                        );

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Confimation response", "Result " + anError);
                        new SweetAlertDialog(ActivationLocationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        /**
         * Get selected item for category
         */
        if(spinner.getId() == R.id.spinner_division)
        {
            String item = parent.getItemAtPosition(position).toString();
            division = item;

            // Fetch region
            fetchRegion(division);
        }

        /**
         * Get selected item for region
         */
        if(spinner.getId() == R.id.spinner_region)
        {
            String item = parent.getItemAtPosition(position).toString();
            region = item;

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
