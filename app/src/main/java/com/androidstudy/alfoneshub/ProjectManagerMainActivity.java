package com.androidstudy.alfoneshub;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.androidstudy.alfoneshub.expenses.ExpensesActivity;
import com.androidstudy.alfoneshub.notifications.NotificationsActivity;
import com.androidstudy.alfoneshub.profile.ProfileActivity;
import com.androidstudy.alfoneshub.reports.ReportHistoryActivity;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.TrackGPS;

public class ProjectManagerMainActivity extends AppCompatActivity {

    TrackGPS gps;
    Double latitude=0.0, longitude=0.0;
    private Boolean exit = false;

    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 4;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 5;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    CardView cardViewReport, cardViewExpenses, cardViewNotification, cardViewProfile;
    TextView textViewTemperature, textViewWeatherDesc, textViewLocation, textViewWeatherUpdates;
    ImageView imageViewWeather;
    ImageButton imageButtonRefresh;
    LinearLayout linearLayoutWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_manager_main);
        try{
            FirebaseInstanceId.getInstance().getToken();
            Log.d("Token",String.valueOf(FirebaseInstanceId.getInstance().getToken()));
        }catch (Exception e){
            Log.d("Error","Token not generated");
        }

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Alfones Admin");
        }

        textViewTemperature = (TextView)findViewById(R.id.txt_temperature);
        textViewWeatherDesc = (TextView)findViewById(R.id.txt_weather_description);
        textViewLocation = (TextView)findViewById(R.id.txt_location);
        textViewWeatherUpdates = (TextView)findViewById(R.id.txt_weather_updates);
        imageViewWeather = (ImageView) findViewById(R.id.img_weather);
        imageButtonRefresh = (ImageButton) findViewById(R.id.imgRefresh);
        linearLayoutWeather = (LinearLayout) findViewById(R.id.linear_layout_weather);

        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
            }
        });

        cardViewReport = (CardView)findViewById(R.id.card_view_reports);
        cardViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectManagerMainActivity.this, ReportHistoryActivity.class);
                startActivity(intent);
            }
        });

        cardViewExpenses = (CardView)findViewById(R.id.card_view_expenses);
        cardViewExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectManagerMainActivity.this, ExpensesActivity.class);
                startActivity(intent);
            }
        });

        cardViewNotification = (CardView)findViewById(R.id.card_view_notification);
        cardViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectManagerMainActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        cardViewProfile = (CardView)findViewById(R.id.card_view_profile);
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectManagerMainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        getPermissions();
        checkLocation();

        setText();
    }

    private void setText() {
        SharedPreferences preferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String weather_id = preferences.getString("weather_id", "weather_id");
        if(weather_id.equals("null") || weather_id.equals("weather_id")){
            weather_id = "";
        }

        String weather_type = preferences.getString("weather_type", "weather_type");
        if(weather_type.equals("null") || weather_type.equals("weather_type")){
            weather_type = "";
        }


        String weather_description = preferences.getString("weather_description", "weather_description");
        if(weather_description.equals("null") || weather_description.equals("weather_description")){
            weather_description = "";
        }


        String weather_temperature = preferences.getString("weather_temperature", "weather_temperature");
        if(weather_temperature.equals("null") || weather_temperature.equals("weather_temperature")){
            weather_temperature = "";
        }



        String weather_location = preferences.getString("weather_location", "weather_location");
        if(weather_location.equals("null") || weather_location.equals("weather_location")){
            weather_location = "";
        }


        if(weather_id.isEmpty() || weather_type.isEmpty()){
            linearLayoutWeather.setVisibility(View.GONE);
            textViewWeatherUpdates.setVisibility(View.VISIBLE);
        }else{

            linearLayoutWeather.setVisibility(View.VISIBLE);
            textViewWeatherUpdates.setVisibility(View.GONE);

            if(!weather_location.isEmpty()){
                textViewLocation.setText(weather_location);
            }

            if(!weather_temperature.isEmpty()){
                textViewTemperature.setText(weather_temperature);
            }


            if(!weather_description.isEmpty()){
                textViewWeatherDesc.setText(weather_description);
            }

            if(!weather_type.isEmpty()){

                if(weather_type.equals("1")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(ProjectManagerMainActivity.this,R.drawable.rain));
                }

                if(weather_type.equals("2")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(ProjectManagerMainActivity.this,R.drawable.sun));
                }

                if(weather_type.equals("3")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(ProjectManagerMainActivity.this,R.drawable.cloud));
                }

                if(weather_type.equals("4")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(ProjectManagerMainActivity.this,R.drawable.wind));
                }
            }
        }

    }


    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    private void getPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                    // MY_PERMISSIONS_REQUEST_CAMERA is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
   

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public void checkLocation() {
        Log.d("method", "check location");
        gps = new TrackGPS(ProjectManagerMainActivity.this);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();

            getWeather(latitude, longitude);
        }
    }


    private void getWeather(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ProjectManagerMainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
//              result.append(address.getLocality()).append("\n");
                result.append(address.getLocality());

                final String location = String.valueOf(result);

                String lat= String.valueOf(latitude);
                String lng= String.valueOf(longitude);

                AndroidNetworking.get("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lng+"&appid=e2e9d59db94d6fd96beb21a35587a783")
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.d("data", String.valueOf(response));
                                JSONObject c=null;
                                JSONArray jsonArrayWeather =null;
                                JSONObject jsonObjectMain =null;

                                try {

                                    jsonArrayWeather = response.getJSONArray("weather");
                                    JSONObject JSONWeather = jsonArrayWeather.getJSONObject(0);
                                    String id = String.valueOf(JSONWeather.getInt("id"));
                                    String main = JSONWeather.getString("main");
                                    String description = JSONWeather.getString("description");

                                    int weather_id = Integer.parseInt(id);

                                    String weather_type = "";

                                    if(weather_id>=200 && weather_id<=531){
                                        //Rainy condition
                                        weather_type="1";
                                    }

                                    if(weather_id==800){
                                        //Sunny
                                        weather_type="2";
                                    }

                                    if(weather_id>=801 && weather_id<=805){
                                        //Cloudy condition
                                        weather_type="3";
                                    }

                                    if(weather_id>=900 && weather_id<=906){
                                        //Windy condition
                                        weather_type="4";
                                    }

                                    jsonObjectMain = response.getJSONObject("main");

                                    String temperature = "";
                                    temperature = String.valueOf(jsonObjectMain.getString("temp"));

                                    //Initialise celsius and fahrenheit;
                                    Double celsius=0.0, fahrenheit = 0.0;
                                    fahrenheit = Double.valueOf(temperature);
                                    celsius = (fahrenheit-273.15);
                                    temperature = String.valueOf(String.format("%.0f", celsius));

                                    SharedPreferences sharedPreferences = ProjectManagerMainActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Config.WEATHER_ID, id);
                                    editor.putString(Config.WEATHER_TYPE, weather_type);
                                    editor.putString(Config.WEATHER_DESCRIPTION, description);
                                    editor.putString(Config.WEATHER_TEMPERATURE, temperature);
                                    editor.putString(Config.WEATHER_LOCATION, location);
                                    editor.commit();

                                    setText();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });


            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

    }
}
