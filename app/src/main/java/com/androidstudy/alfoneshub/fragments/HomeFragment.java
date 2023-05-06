package com.androidstudy.alfoneshub.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flaviofaria.kenburnsview.KenBurnsView;

import com.androidstudy.alfoneshub.R;
//import com.androidstudy.alfoneshub.models.DaoSession;
import com.androidstudy.alfoneshub.utils.AlfonesCommunication;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.ServerCalls;
import com.androidstudy.alfoneshub.utils.TrackGPS;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by eugene on 7/12/17.
 */

public class HomeFragment extends Fragment {
    TrackGPS gps;
    Double latitude=0.0, longitude=0.0;

    TextView textViewCampaignName, textViewCampaignDate, textViewCampaignCompany, textViewProfile, 
            textViewProduct, textViewMerchandise, textViewAssignmentName;

    TextView textViewTemperature, textViewWeatherDesc, textViewLocation, textViewWeatherUpdates;
    ImageView imageViewWeather;
    ImageButton imageButtonRefresh;
    LinearLayout linearLayoutWeather;
    public TourGuide mTourGuideHandler;

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);



            KenBurnsView kbv = (KenBurnsView) view.findViewById(R.id.image);
            textViewCampaignName = (TextView)view.findViewById(R.id.txtCampaignName);
            textViewCampaignDate = (TextView)view.findViewById(R.id.textCampaignDate);
            textViewCampaignCompany = (TextView)view.findViewById(R.id.textCampaignCompany);

            textViewProfile = (TextView)view.findViewById(R.id.text_view_profile);

            textViewTemperature = (TextView)view.findViewById(R.id.txt_temperature);
            textViewWeatherDesc = (TextView)view.findViewById(R.id.txt_weather_description);
            textViewLocation = (TextView)view.findViewById(R.id.txt_location);
            textViewWeatherUpdates = (TextView)view.findViewById(R.id.txt_weather_updates);
            imageViewWeather = (ImageView) view.findViewById(R.id.img_weather);
            imageButtonRefresh = (ImageButton) view.findViewById(R.id.imgRefresh);
            linearLayoutWeather = (LinearLayout) view.findViewById(R.id.linear_layout_weather);

            imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkLocation();
                }
            });

            SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            Boolean isFirstTimeLaunchHome = preferences.getBoolean(Config.IS_FIRST_TIME_LAUNCH_HOME_SHARED_PREF, false);

            if(!isFirstTimeLaunchHome){
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.IS_FIRST_TIME_LAUNCH_HOME_SHARED_PREF, true);
                editor.commit();

                final ToolTip toolTipCampaignName = new ToolTip().
                        setTitle("Activation!").
                        setDescription("See details of the current activation");
                final Overlay overlayCampaignName = new Overlay().disableClickThroughHole(false).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTourGuideHandler.cleanUp();

                    }
                });

                ToolTip toolTip = new ToolTip().
                        setTitle("Welcome!").
                        setDescription("Get weather updates from here");
                Overlay overlay = new Overlay().disableClickThroughHole(false).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTourGuideHandler.cleanUp();
                        mTourGuideHandler.setToolTip(toolTipCampaignName).setOverlay(overlayCampaignName).playOn(textViewCampaignName);
                    }
                });

                mTourGuideHandler = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
                        .setPointer(new Pointer())
                        .setToolTip(toolTip)
                        .setOverlay(overlay)
                        .playOn(imageViewWeather);

                imageViewWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTourGuideHandler.cleanUp();
                        mTourGuideHandler.setToolTip(toolTipCampaignName).setOverlay(overlayCampaignName).playOn(textViewCampaignName);
                    }
                });

            }

            Log.d("HOME","called");
            getCampaignDetails();
            setText();

        }



        return view;
    }


    private void setText() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String activation_name = preferences.getString("activation_name", "activation_name");
        if(activation_name.equals("null") || activation_name.equals("activation_name")){
            activation_name = "NONE";
        }

        String location_name = preferences.getString("location_name", "location_name");
        if(location_name.equals("null") || location_name.equals("location_name")){
            location_name = "NONE";
        }

        String activation_date = preferences.getString("activation_date", "activation_date");
        if(activation_date.equals("null") || activation_date.equals("activation_date")){
            activation_date = "";
        }

        String activation_company = preferences.getString("activation_company", "activation_company");
        if(activation_company.equals("null") || activation_company.equals("activation_company")){
            activation_company = "";
        }


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
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.rain));
                }

                if(weather_type.equals("2")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.sun));
                }

                if(weather_type.equals("3")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.cloud));
                }

                if(weather_type.equals("4")){
                    imageViewWeather.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.wind));
                }
            }
        }


        textViewAssignmentName.setText(location_name);
        textViewCampaignName.setText(activation_name.toUpperCase());
        textViewCampaignCompany.setText(activation_company.toUpperCase());
        textViewCampaignDate.setText(activation_date);

        if (!activation_name.isEmpty()) {
            textViewProfile.setText(activation_name = String.valueOf(activation_name.charAt(0)).toUpperCase());
        }
    }


    public void getCampaignDetails(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Syncing..");
        pDialog.setCancelable(false);
        pDialog.show();
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.ACTIVATION_DETAILS_SYNC_URL)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("summary response", response.toString());
                        try {
                            String activation_id = response.getString("activation_id");
                            String activation_name = response.getString("activation_name");
                            String activation_date = response.getString("activation_date");
                            String activation_status = response.getString("activation_status");
                            String client_name = response.getString("client_name");
                            String location_id = response.getString("location_id");
                            String location_name = response.getString("location_name");
                            String latitude = response.getString("latitude");
                            String longitude = response.getString("longitude");
                            String enable_geo_fence = response.getString("enable_geo_fence");





                            //DaoSession daoSession = ((AlfonesCommunication) getActivity().getApplicationContext()).getDaoSession();
                            //daoSession.getProductDao().deleteAll();
                            //daoSession.getMerchandiseDao().deleteAll();

                            ServerCalls serverCallsMerchandise = new ServerCalls(getActivity());
                            serverCallsMerchandise.getCampaignMerchandise();

                            ServerCalls serverCallsProduct = new ServerCalls(getActivity());
                            serverCallsProduct.getCampaignproduct();

                            setText();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("activation response", "Result " + anError);
                    }
                });

    }

    public void checkLocation() {
        Log.d("method", "check location");
        gps = new TrackGPS(getActivity());
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();

            getWeather(latitude, longitude);
        }
    }


    private void getWeather(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
