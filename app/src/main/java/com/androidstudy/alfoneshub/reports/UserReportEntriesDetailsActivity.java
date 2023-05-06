package com.androidstudy.alfoneshub.reports;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class UserReportEntriesDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String id = "", activation_id = "", location_id = "", user_id = "", user_name = "" ,customer_name = "", customer_phone = "", customer_id = "", product = ""
            ,product_id = "", product_name = "", product_quantity = "", product_value = "", extra_field_1 = "", extra_field_2 = "", merchandise = "", merchandise_id = "", merchandise_name = "",
            merchandise_quantity = "", customer_feedback = "", image = "", image_caption = "", latitude = "", longitude  = "";

    TextView textViewCustomerName, textViewCustomerPhone, textViewProductName, textViewProductQuantity,
            textViewProductValue, textViewExtraField1, textViewExtraField2, textViewMerchandiseName, textViewMerchandiseQuantity;
    
    Button buttonEditDetails, buttonDeleteDetails;
    ProgressBar progressBar;
    Spinner spinnerProduct, spinnerMerchandise;
    int product_int = 0, merchandise_int = 0;
    List<String> merchandiseList = new ArrayList<String>();
    List<String> productList = new ArrayList<String>();
    String selected_product = "", selected_merchandise = "";
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_entries_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            activation_id = intent.getStringExtra("activation_id");
            location_id = intent.getStringExtra("location_id");
            user_name = intent.getStringExtra("user_name");
            user_id = intent.getStringExtra("user_id");
            customer_name = intent.getStringExtra("customer_name");
            customer_phone = intent.getStringExtra("customer_phone");
            customer_id = intent.getStringExtra("customer_id");
            product = intent.getStringExtra("product");
            product_id = intent.getStringExtra("product_id");
            product_name = intent.getStringExtra("product_name");
            product_quantity = intent.getStringExtra("product_quantity");
            product_value = intent.getStringExtra("product_value");
            extra_field_1 = intent.getStringExtra("extra_field_1");
            extra_field_2 = intent.getStringExtra("extra_field_2");
            merchandise = intent.getStringExtra("merchandise");
            merchandise_id = intent.getStringExtra("merchandise_id");
            merchandise_name = intent.getStringExtra("merchandise_name");
            merchandise_quantity = intent.getStringExtra("merchandise_quantity");
            customer_feedback = intent.getStringExtra("customer_feedback");
            image = intent.getStringExtra("image");
            image_caption = intent.getStringExtra("image_caption");
            latitude = intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");

        }

        productList.add("");
        merchandiseList.add("");

        textViewCustomerName = (TextView)findViewById(R.id.text_view_customer_name);
        textViewCustomerName.setText(customer_name);

        textViewCustomerPhone = (TextView)findViewById(R.id.text_view_customer_phone);
        textViewCustomerPhone.setText(customer_phone);

        textViewProductName = (TextView)findViewById(R.id.text_view_product_name);
        textViewProductName.setText(product_name);

        textViewProductQuantity = (TextView)findViewById(R.id.text_view_product_quantity);
        textViewProductQuantity.setText(product_quantity);

        textViewProductValue = (TextView)findViewById(R.id.text_view_product_value);
        textViewProductValue.setText(product_value);

        textViewExtraField1 = (TextView)findViewById(R.id.text_view_extra_field_1);
        textViewExtraField1.setText(extra_field_1);

        textViewExtraField2 = (TextView)findViewById(R.id.text_view_extra_field_2);
        textViewExtraField2.setText(extra_field_2);

        textViewMerchandiseName = (TextView)findViewById(R.id.text_view_merchandise_name);
        textViewMerchandiseName.setText(merchandise_name);

        textViewMerchandiseQuantity = (TextView)findViewById(R.id.text_view_merchandise_quantity);
        textViewMerchandiseQuantity.setText(merchandise_quantity);
        
        buttonEditDetails = (Button)findViewById(R.id.button_edit_details);
        buttonEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(UserReportEntriesDetailsActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.edit_report_details_dialog_layout);

                progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextCustomerName, editTextCustomerPhone,  editTextCustomerId, editTextProductQuantity, editTextProductValue,
                        editTextExtraValue1, editTextExtraValue2, editTextMerchandiseQuantity, editTextComments;

                editTextCustomerName = (EditText)dialog.findViewById(R.id.edit_text_customer_name);
                if(customer_name.equalsIgnoreCase("null")){
                    customer_name = "";
                }
                editTextCustomerName.setText(customer_name);

                editTextCustomerPhone = (EditText)dialog.findViewById(R.id.edit_text_customer_phone);
                if(customer_phone.equalsIgnoreCase("null")){
                    customer_phone = "";
                }
                editTextCustomerPhone.setText(customer_phone);

                editTextCustomerId = (EditText)dialog.findViewById(R.id.edit_text_customer_id);
                if(customer_id.equalsIgnoreCase("null")){
                    customer_id = "";
                }
                editTextCustomerId.setText(customer_id);

                editTextProductQuantity = (EditText)dialog.findViewById(R.id.edit_text_product_quantity);
                if(product_quantity.equalsIgnoreCase("null")){
                    product_quantity = "";
                }
                editTextProductQuantity.setText(product_quantity);

                editTextProductValue = (EditText)dialog.findViewById(R.id.edit_text_product_value);
                if(product_value.equalsIgnoreCase("null")){
                    product_value = "";
                }
                editTextProductValue.setText(product_value);

                editTextExtraValue1 = (EditText)dialog.findViewById(R.id.edit_text_extra_field_1);
                if(extra_field_1.equalsIgnoreCase("null")){
                    extra_field_1 = "";
                }
                editTextExtraValue1.setText(extra_field_1);

                editTextExtraValue2 = (EditText)dialog.findViewById(R.id.edit_text_extra_field_2);
                if(extra_field_2.equalsIgnoreCase("null")){
                    extra_field_2 = "";
                }
                editTextExtraValue2.setText(extra_field_2);

                editTextMerchandiseQuantity = (EditText)dialog.findViewById(R.id.edit_text_merchandise_quantity);
                if(merchandise_quantity.equalsIgnoreCase("null")){
                    merchandise_quantity = "";
                }
                editTextMerchandiseQuantity.setText(merchandise_quantity);

                editTextComments = (EditText)dialog.findViewById(R.id.edit_text_comments);
                if(customer_feedback.equalsIgnoreCase("null")){
                    customer_feedback = "";
                }
                editTextComments.setText(customer_feedback);

                spinnerProduct = (Spinner) dialog.findViewById(R.id.spinner_product);
                spinnerMerchandise = (Spinner) dialog.findViewById(R.id.spinner_merchandise);
                
                fetchProduct();
                fetchMerchandise();

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

                        String customer_name = editTextCustomerName.getText().toString();
                        if (TextUtils.isEmpty(customer_name)) {
                            Toast.makeText(UserReportEntriesDetailsActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String customer_phone = editTextCustomerPhone.getText().toString();
                        String customer_id = editTextCustomerId.getText().toString();

                        String product_quantity = editTextProductQuantity.getText().toString();
                        if (TextUtils.isEmpty(customer_name)) {
                            Toast.makeText(UserReportEntriesDetailsActivity.this, "Please product quantity", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String product_value = editTextProductValue.getText().toString();
                        String extra_field_1 = editTextExtraValue1.getText().toString();
                        String extra_field_2 = editTextExtraValue2.getText().toString();

                        String merchandise_quantity = editTextMerchandiseQuantity.getText().toString();
                        if (TextUtils.isEmpty(customer_name)) {
                            Toast.makeText(UserReportEntriesDetailsActivity.this, "Please merchandise quantity", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String comments = editTextComments.getText().toString();
                        

                        AndroidNetworking.post(URLs.EDIT_USER_REPORT)
                                .addBodyParameter("id", id)
                                .addBodyParameter("customer_name", customer_name)
                                .addBodyParameter("customer_phone", customer_phone)
                                .addBodyParameter("customer_id", customer_id)
                                .addBodyParameter("product", String.valueOf(product_int))
                                .addBodyParameter("product_id", product_id)
                                .addBodyParameter("product_name", selected_product)
                                .addBodyParameter("product_quantity", String.valueOf(product_quantity))
                                .addBodyParameter("product_value", String.valueOf(product_value))
                                .addBodyParameter("product_extra_field_1", String.valueOf(extra_field_1))
                                .addBodyParameter("product_extra_field_2", String.valueOf(extra_field_2))
                                .addBodyParameter("merchandise", String.valueOf(merchandise_int))
                                .addBodyParameter("merchandise_id", merchandise_id)
                                .addBodyParameter("merchandise_name", selected_merchandise)
                                .addBodyParameter("merchandise_quantity", String.valueOf(merchandise_quantity))
                                .addBodyParameter("customer_feedback", String.valueOf(comments))
                                .setTag("")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        progressBar.setVisibility(View.GONE);
                                        int success=0;
                                        //Check for successful login
                                        Log.d("Edit Here", "Result " + response.toString());
                                        try {
                                            success = response.getInt(SUCCESS);
                                            //Login has been succesful.
                                            if (success==1){

                                                Toast.makeText(UserReportEntriesDetailsActivity.this, "Success, details edited", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(UserReportEntriesDetailsActivity.this, "Error, details not created", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(UserReportEntriesDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(UserReportEntriesDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        buttonDeleteDetails = (Button)findViewById(R.id.button_delete);
        buttonDeleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new SweetAlertDialog(UserReportEntriesDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Deleting details...");
                pDialog.setCancelable(false);
                pDialog.show();
                
                AndroidNetworking.post(URLs.DELETE_USER_REPORT)
                        .addBodyParameter("id", id)
                        .setTag("reset_code")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pDialog.dismiss();
                                int success=0;
                                Log.d("Delete record", "Result " + response.toString());
                                try {
                                    success = response.getInt(SUCCESS);
                                    if (success==1){

                                        finish();
                                    }
                                } catch (JSONException e) {

                                    Toast.makeText(UserReportEntriesDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(UserReportEntriesDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        getSupportActionBar().setTitle(customer_name);
    }

    private void fetchMerchandise() {

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

                                String id = c.getString("id");
                                String name = c.getString("name");
                                String created_at = c.getString("quantity");
                                String updated_at = c.getString("activation_id");
                                merchandiseList.add(name);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> merchandiseAdapter = new ArrayAdapter<String>(UserReportEntriesDetailsActivity.this, R.layout.spinner_item_filter_custom, merchandiseList);
                        merchandiseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMerchandise.setAdapter(merchandiseAdapter);

                        //TODO Notify on data change

                        if(!merchandise_name.isEmpty()){

                            if(merchandise_name != null || merchandise_name != ""){

                                int selected_region = merchandiseAdapter.getPosition(merchandise_name);
                                spinnerMerchandise.setSelection(selected_region);

                            }

                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Merchandise response", "Result " + error);
                    }
                });
    }

    private void fetchProduct() {
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
                                
                                String id = c.getString("id");
                                String name = c.getString("name");
                                String created_at = c.getString("quantity");
                                String updated_at = c.getString("activation_id");
                                productList.add(name);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(UserReportEntriesDetailsActivity.this, R.layout.spinner_item_filter_custom, productList);
                        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProduct.setAdapter(productAdapter);

                        //TODO Notify on data change

                        if(!product_name.isEmpty()){

                            if(product_name != null || product_name != ""){

                                int selected_product = productAdapter.getPosition(product_name);
                                spinnerProduct.setSelection(selected_product);

                            }

                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("Product response", "Result " + error);
                    }
                });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_home:
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_role = sharedPreferences.getString("user_role","user_role");
                int role = Integer.parseInt(user_role);
                if(role == 3){
                    Intent intent = new Intent(UserReportEntriesDetailsActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(UserReportEntriesDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        /**
         * Get selected item for category
         */
        if(spinner.getId() == R.id.spinner_product)
        {
            String item = parent.getItemAtPosition(position).toString();
            product = item;
        }

        /**
         * Get selected item for merchandise
         */
        if(spinner.getId() == R.id.spinner_merchandise)
        {
            String item = parent.getItemAtPosition(position).toString();
            merchandise = item;

        }
        
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
