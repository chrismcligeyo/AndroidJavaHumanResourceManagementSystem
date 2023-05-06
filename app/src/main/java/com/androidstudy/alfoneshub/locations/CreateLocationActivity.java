package com.androidstudy.alfoneshub.locations;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.TrackGPS;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class CreateLocationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TrackGPS gps;
    Double latitude=0.0, longitude=0.0;
    ProgressBar progressBar;
    String activation_name="";
    List<String> divisionList = new ArrayList<String>();
    List<String> regionList = new ArrayList<String>();
    Spinner spinnerDivision, spinnerRegion;
    EditText editTextLocationName, editTextLocationDescription,  editTextLocationIdentifier, editTextDate, editTextDuration, editTextLocationComments;
    DatePickerDialog mDatePicker;
    String division, region;
    boolean initial_region_init = false;
    Button buttonCreate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Create location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = (ProgressBar) findViewById(R.id.progress_dialog);
        progressBar.setVisibility(View.GONE);
        
        final String[] selectedDate = {""};
        editTextLocationName = (EditText)findViewById(R.id.edit_text_location_name);
        editTextLocationDescription = (EditText)findViewById(R.id.edit_text_location_description);
        editTextLocationIdentifier = (EditText)findViewById(R.id.edit_text_location_identifier);
        editTextLocationComments = (EditText)findViewById(R.id.edit_text_location_comments);
        spinnerDivision = (Spinner) findViewById(R.id.spinner_division);
        spinnerRegion = (Spinner) findViewById(R.id.spinner_region);

        editTextDate = (EditText)findViewById(R.id.edit_text_date);
        editTextDuration = (EditText)findViewById(R.id.edit_text_duration);
        editTextDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    mDatePicker = new DatePickerDialog(CreateLocationActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        buttonCreate = (Button) findViewById(R.id.dialog_info_create);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String location_name = editTextLocationName.getText().toString();
                if (TextUtils.isEmpty(location_name)) {
                    Toast.makeText(CreateLocationActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                    return;
                }

                String location_description = editTextLocationDescription.getText().toString();
                String location_identifier = editTextLocationIdentifier.getText().toString();
                String location_comments = editTextLocationComments.getText().toString();

                String date = editTextDate.getText().toString();
                if (TextUtils.isEmpty(date)) {
                    Toast.makeText(CreateLocationActivity.this, "Please enter date", Toast.LENGTH_LONG).show();
                    return;
                }

                String duration = editTextDuration.getText().toString();
                if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(CreateLocationActivity.this, "Please enter duration", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(division)) {
                    Toast.makeText(CreateLocationActivity.this, "Please enter division", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(region)) {
                    Toast.makeText(CreateLocationActivity.this, "Please enter region", Toast.LENGTH_LONG).show();
                    return;
                }


                SharedPreferences preferences = CreateLocationActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                                Log.d("Create location Here", "Result " + response.toString());
                                try {
                                    success = response.getInt(SUCCESS);
                                    //Login has been succesful.
                                    if (success==1){

                                        Toast.makeText(CreateLocationActivity.this, "Success, location created", Toast.LENGTH_LONG).show();

                                    }else{

                                        Toast.makeText(CreateLocationActivity.this, "Error, location not created", Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {

                                    Toast.makeText(CreateLocationActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                progressBar.setVisibility(View.GONE);
                                Log.d("ERROR",error.toString());
                                Toast.makeText(CreateLocationActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        gps = new TrackGPS(CreateLocationActivity.this);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            buttonCreate.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "Enable location", Toast.LENGTH_SHORT).show();
            buttonCreate.setVisibility(View.GONE);
        }

        fetchDivision();
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
                        ArrayAdapter<String> divisionDataAdapter = new ArrayAdapter<String>(CreateLocationActivity.this, R.layout.spinner_item_filter_custom, divisionList);
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
                ArrayAdapter<String> regionDataAdapter = new ArrayAdapter<String>(CreateLocationActivity.this, R.layout.spinner_item_filter_custom, regionList);
                regionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRegion.setAdapter(regionDataAdapter);

                //TODO Notify on data change

                if(!initial_region_init){

                    if(region != null || region != ""){

                        int selected_region = regionDataAdapter.getPosition(region);
                        spinnerRegion.setSelection(selected_region);

                    }

                    initial_region_init = true;
                }


            }

            @Override
            public void onError(ANError anError) {
                progressBar.setVisibility(View.GONE);
                Log.d("ERROR", anError.toString());
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


        Toast.makeText(this, "CLICCKED", Toast.LENGTH_SHORT).show();

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
    public void onNothingSelected(AdapterView<?> adapterView) {

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
