package com.androidstudy.alfoneshub.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.androidstudy.alfoneshub.LoginActivity;

import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.MainActivity;
//import com.androidstudy.alfoneshub.models.DaoSession;
import com.androidstudy.alfoneshub.models.Merchandise;
//import com.androidstudy.alfoneshub.models.MerchandiseDao;
import com.androidstudy.alfoneshub.models.Product;
//import com.androidstudy.alfoneshub.models.ProductDao;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.androidnetworking.common.ANConstants.SUCCESS;

/**
 * Created by eugene on 7/12/17.
 */

public class ServerCalls {
    Context context;
    //DaoSession daoSession;

    public ServerCalls(Context context) {
        this.context = context;
    }
    int registration_success;
    //Registration Server Call

    public void loginProcess(String email, String password){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String device_token = sharedPreferences.getString("new_device_id","new_device_id");

        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Logging in..");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.LOGIN_ADMIN)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("device_token", device_token)
                .setTag("login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        int success=0;
                        //Check for successful login
                        Log.d("Success Here", "Result " + response.toString());
                        try {
                            success = response.getInt(SUCCESS);
                            //Login has been succesful.
                            if (success==1){
                                //Fetch all user details from server response
                                String user_id = response.getString("user_id");
                                String name = response.getString("name");
                                String phone_number = response.getString("phone_number");
                                String email = response.getString("email");
                                String role = response.getString("role");
                                String permanent_status = response.getString("permanent_status");

                                //Save data to shared preferences
                                SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(Config.LOGGED_IN_NEW_SHARED_PREF, true);
                                editor.putString(Config.USER_ID, user_id);
                                editor.putString(Config.USER_NAME, name);
                                editor.putString(Config.USER_PHONE_NUMBER, phone_number);
                                editor.putString(Config.USER_EMAIL, email);
                                editor.putString(Config.USER_ROLE, role);
                                editor.putString(Config.PERMANENT_STATUS, permanent_status);
                                editor.apply();

                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);

                            }else{
                                pDialog.dismiss();
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Incorrect email or password!")
                                        .show();
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.d("LOGIN_ADMIN ERROR",error.toString());
                        Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getCampaignproduct(){
        //daoSession = ((AlfonesCommunication) context.getApplicationContext()).getDaoSession();
        //final ProductDao productDao = daoSession.getProductDao();

        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String activation_id = sharedPreferences.getString("activation_id","activation_id");
        AndroidNetworking.post(URLs.ACTIVATION_PRODUCT_URL)
                .addBodyParameter("activation_id", activation_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Campaign product", "Result " + response.toString());
                        JSONObject jsonObj = null;
                        JSONArray array =null;

                        try {
                            jsonObj = new JSONObject(String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                                Product insert_product = new Product();
                                insert_product.setProduct_id(c.getString("id"));
                                insert_product.setProduct_name(c.getString("name"));
                                insert_product.setProduct_quantity(c.getString("quantity"));
                                insert_product.setCampaign_id(c.getString("activation_id"));
                                //daoSession.getProductDao().insert(insert_product);
                                Log.d("GREENDAO", "Save product record");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Product response", "Result " + error);
                    }
                });
    }

    public void getCampaignMerchandise(){
        //daoSession = ((AlfonesCommunication) context.getApplicationContext()).getDaoSession();
        //final MerchandiseDao merchandiseDao = daoSession.getMerchandiseDao();

        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String activation_id = sharedPreferences.getString("activation_id","activation_id");
        AndroidNetworking.post(URLs.ACTIVATION_MERCHANDISE_URL)
                .addBodyParameter("activation_id", activation_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Campaign merchandise", "Result " + response.toString());
                        JSONObject jsonObj = null;
                        JSONArray array =null;

                        try {
                            jsonObj = new JSONObject(String.valueOf(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                                Merchandise insert_merchandise = new Merchandise();
                                insert_merchandise.setMerchandise_id(c.getString("id"));
                                insert_merchandise.setMerchandise_name(c.getString("name"));
                                insert_merchandise.setMerchandise_quantity(c.getString("quantity"));
                                insert_merchandise.setCampaign_id(c.getString("activation_id"));
                                //daoSession.getMerchandiseDao().insert(insert_merchandise);
                                Log.d("GREENDAO", "Save merchandise record");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Merchandise response", "Result " + error);
                    }
                });
    }


    public void forgotCredentials(String input_national_id) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Retrieving credentials");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.FORGOT_CREDENTIALS_URL)
                .addBodyParameter("national_id", input_national_id)
                .setTag("forgot_credentials")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        int success=0;
                        //Check for successful login
                        Log.d("Success Here", "Result " + response.toString());
                        try {
                            success = response.getInt(SUCCESS);
                            //Login has been succesful.
                            if (success==1){

                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Successful")
                                        .setContentText("Your credentials will be sent shortly. If it takes too long please request again")
                                        .setConfirmText("Okay! Thanks")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //Restart intent
                                                Intent intent = new Intent(context, LoginActivity.class);
                                                context.startActivity(intent);

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
                                pDialog.dismiss();
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Your ID doesn't match any record")
                                        .show();
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.d("ERROR",error.toString());
                        Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
