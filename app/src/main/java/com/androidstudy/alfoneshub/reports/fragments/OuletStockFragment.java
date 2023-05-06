package com.androidstudy.alfoneshub.reports.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

/**
 * A simple {@link Fragment} subclass.
 */
public class OuletStockFragment extends Fragment {

    MaterialSpinner materialSpinnerProduct, materialSpinnerActivation, materialSpinnerLocation;
    EditText editTextDate, editTextOpeningStock, editTextClosingStock;
    Button buttonSubmit;

    String activation_name="", location_name="", product_name="";
    DatePickerDialog mDatePicker;
    String selectedDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_oulet_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        materialSpinnerProduct = (MaterialSpinner)view.findViewById(R.id.spinner_product);
        materialSpinnerActivation = (MaterialSpinner)view.findViewById(R.id.spinner_activation);
        materialSpinnerLocation = (MaterialSpinner)view.findViewById(R.id.spinner_location);

        editTextDate = (EditText) view.findViewById(R.id.edit_text_date);
        editTextDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            /*0000-00-00*/
                            selectedmonth += 1;
                            selectedDate = selectedyear + "-" + selectedmonth + "-" + selectedday;
                            editTextDate.setText(selectedDate);


                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }
                return false;
            }

        });

        editTextOpeningStock = (EditText) view.findViewById(R.id.edit_text_opening_stock);
        editTextClosingStock = (EditText) view.findViewById(R.id.edit_text_closing_stock);

        buttonSubmit = (Button)view.findViewById(R.id.button_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input_date = editTextDate.getText().toString();
                if (TextUtils.isEmpty(input_date)) {
                    editTextDate.setError("Enter date");
                    return;
                }

                String input_opening_stock= editTextOpeningStock.getText().toString();
                if (TextUtils.isEmpty(input_opening_stock)) {
                    editTextOpeningStock.setError("Enter opening stock");
                    return;
                }

                String input_closing_stock = editTextClosingStock.getText().toString();
                if (TextUtils.isEmpty(input_closing_stock)) {
                    editTextClosingStock.setError("Enter closing stock");
                    return;
                }


                String input_activation = activation_name;
                if (TextUtils.isEmpty(input_activation)) {
                    Toast.makeText(getActivity(), "Select Activation", Toast.LENGTH_SHORT).show();
                    return;
                }

                String input_location = location_name;
                if (TextUtils.isEmpty(input_location)) {
                    Toast.makeText(getActivity(), "Select Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                String input_product = product_name;
                if (TextUtils.isEmpty(input_product)) {
                    Toast.makeText(getActivity(), "Select Product", Toast.LENGTH_SHORT).show();
                    return;
                }

                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Submitting sales...");
                pDialog.setCancelable(false);
                pDialog.show();

                SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.SUBMIT_OUTLET_STOCKS)
                        .addBodyParameter("activation_name", input_activation)
                        .addBodyParameter("location_name", input_location)
                        .addBodyParameter("product_name", input_product)
                        .addBodyParameter("date", input_date)
                        .addBodyParameter("opening_stock", input_opening_stock)
                        .addBodyParameter("closing_stock", input_closing_stock)
                        .addBodyParameter("admin_id", user_id)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Registration response", response.toString());

                                pDialog.dismiss();

                                int registration_success = 0;
                                try {
                                    registration_success = response.getInt("success");

                                    if(registration_success==1){

                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("SUCCESS!")
                                                .setContentText("Report has been submitted")
                                                .setConfirmText("OK. Thank you!")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();

                                                        //Restart intent
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        getActivity().startActivity(intent);

                                                    }
                                                })
                                                .show();

                                    }else if(registration_success==2){

                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("ERROR!")
                                                .setContentText("This record has already been submitted")
                                                .setConfirmText("Retry")
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                }).show();

                                    }else{

                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("ERROR!")
                                                .setContentText("Something went wrong")
                                                .setConfirmText("Retry")
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                }).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pDialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // handle error
                                pDialog.dismiss();
                                Log.d("Registration response", "Result " + anError);
                            }
                        });

            }
        });

        populateActivationSpinner();
    }


    public void populateActivationSpinner(){
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
                            materialSpinnerActivation.setItems(listCampaign);
                            materialSpinnerActivation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    activation_name=item;
                                    Toast.makeText(getActivity(), "" +activation_name, Toast.LENGTH_SHORT).show();
                                    fetchActivationProducts(activation_name);
                                    fetchActivationLocations(activation_name);
                                }
                            });

                            fetchActivationProducts(activation_name);
                            fetchActivationLocations(activation_name);

                        }catch (Exception e){
                            Log.e("ERROR", e.toString());
                            Log.d("ERROR","Spinner not populated");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("Expense response", "Result " + anError);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }


    public void fetchActivationLocations(String activation_name){
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.TEAM_LEADER_LOCATION)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("activation_name", activation_name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Location response", response.toString());
                        String JSON_ARRAY = "result";
                        JSONObject c=null;
                        List<String> listLocation = new ArrayList<String>();
                        try {
                            JSONArray feedArray = response.getJSONArray(JSON_ARRAY);

                            for (int i = 0; i < feedArray.length(); i++) {
                                c = (JSONObject) feedArray.get(i);
                                location_name = c.getString("name");
                                listLocation.add(location_name);
                            }
                            //Get first activation_name
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                location_name = c.getString("name");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            materialSpinnerLocation.setItems(listLocation);
                            materialSpinnerLocation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    location_name=item;
                                }
                            });

                        }catch (Exception e){
                            Log.e("ERROR", e.toString());
                            Log.d("ERROR","Spinner not populated");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("Location response", "Result " + anError);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    public void fetchActivationProducts(String activation_name){
        AndroidNetworking.post(URLs.TEAM_LEADER_PRODUCT)
                .addBodyParameter("activation_name", activation_name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Product response", response.toString());
                        String JSON_ARRAY = "result";
                        JSONObject c=null;
                        List<String> listProduct= new ArrayList<String>();
                        try {
                            JSONArray feedArray = response.getJSONArray(JSON_ARRAY);

                            for (int i = 0; i < feedArray.length(); i++) {
                                c = (JSONObject) feedArray.get(i);
                                product_name = c.getString("name");
                                listProduct.add(product_name);
                            }
                            //Get first activation_name
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                product_name = c.getString("name");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            materialSpinnerProduct.setItems(listProduct);
                            materialSpinnerProduct.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    product_name=item;
                                }
                            });

                        }catch (Exception e){
                            Log.e("ERROR", e.toString());
                            Log.d("ERROR","Spinner not populated");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("Product response", "Result " + anError);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

}
