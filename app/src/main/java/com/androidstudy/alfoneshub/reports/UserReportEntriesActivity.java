package com.androidstudy.alfoneshub.reports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.adapters.ReportHistoryAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.ReportHistory;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class UserReportEntriesActivity extends AppCompatActivity {

    String activation_id, location_id, user_name, user_id, interaction, sales, merchandise, date="";
    SweetAlertDialog pDialog;
    TextView textViewUserName, textViewDate;

    RecyclerView recyclerViewReportHistory;
    ReportHistoryAdapter reportHistoryAdapter;
    private List<ReportHistory> reportHistoryList = new ArrayList<>();

    public BarChart mChart;
    Button buttonConfirmReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_entries);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("User Entries");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewDate = (TextView)findViewById(R.id.txt_activation_date);
        textViewUserName = (TextView)findViewById(R.id.txt_user_name);
        mChart = (BarChart) findViewById(R.id.chart1);

        recyclerViewReportHistory= (RecyclerView)findViewById(R.id.recycler_report_history);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewReportHistory.setLayoutManager(gridLayoutManager);

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            location_id = intent.getStringExtra("location_id");
            user_name = intent.getStringExtra("user_name");
            user_id = intent.getStringExtra("user_id");
            interaction = intent.getStringExtra("interaction");
            sales = intent.getStringExtra("sales");
            merchandise = intent.getStringExtra("merchandise");
            date = intent.getStringExtra("date");

        }

        textViewUserName.setText(user_name.toUpperCase());
        textViewDate.setText(date.toUpperCase());

        buttonConfirmReport = (Button)findViewById(R.id.button_confirm_report);
        buttonConfirmReport.setVisibility(View.GONE);
        buttonConfirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = UserReportEntriesActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String admin_id = preferences.getString("user_id", "user_id");

                pDialog = new SweetAlertDialog(UserReportEntriesActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Confirming report...");
                pDialog.setCancelable(false);
                pDialog.show();

                AndroidNetworking.post(URLs.ACTIVATION_REPORTS_CONFIRMATION)
                        .addBodyParameter("activation_id", activation_id)
                        .addBodyParameter("user_id", user_id)
                        .addBodyParameter("admin_id", admin_id)
                        .addBodyParameter("date", date)
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

                                        Toast.makeText(UserReportEntriesActivity.this, "Success, reports confirmed", Toast.LENGTH_LONG).show();
                                        buttonConfirmReport.setVisibility(View.GONE);

                                    }else{
                                        Toast.makeText(UserReportEntriesActivity.this, "Report not confirmed", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(UserReportEntriesActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(UserReportEntriesActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_role = sharedPreferences.getString("user_role","user_role");
        int role = Integer.parseInt(user_role);
        if(role == 4){
            buttonConfirmReport.setVisibility(View.VISIBLE);
        }

        fetchReportConfirmation();
        fetchRecordsHistory(date);
        setChart();
    }

    private void fetchReportConfirmation() {

        SharedPreferences preferences = UserReportEntriesActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String admin_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.ACTIVATION_CHECK_REPORTS_CONFIRMATION)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("admin_id", admin_id)
                .addBodyParameter("date", date)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int success=0;
                        //Check for successful login
                        Log.d("Success Here", "Result " + response.toString());
                        try {
                            success = response.getInt(SUCCESS);
                            //Login has been succesful.
                            if (success==1){

                                Toast.makeText(UserReportEntriesActivity.this, "Success, reports confirmed", Toast.LENGTH_LONG).show();
                                buttonConfirmReport.setVisibility(View.GONE);

                            }else{
                                Toast.makeText(UserReportEntriesActivity.this, "Error, report not confirmed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(UserReportEntriesActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("ERROR",error.toString());
                        Toast.makeText(UserReportEntriesActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setChart(){


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(Float.parseFloat(interaction), 0));
        entries.add(new BarEntry(Float.parseFloat(sales), 1));
        entries.add(new BarEntry(Float.parseFloat(merchandise), 2));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Interactions");
        labels.add("Sales");
        labels.add("Merchandise");

        BarDataSet dataset = new BarDataSet(entries, "Statistics");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(labels, dataset);
        mChart.setData(data);
        mChart.animateY(5000);
        mChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        mChart.getAxisRight().setTextColor(Color.WHITE); // left y-axis
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getLegend().setTextColor(Color.WHITE);

    }

    public void fetchRecordsHistory(String date){
        pDialog = new SweetAlertDialog(UserReportEntriesActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();


        AndroidNetworking.post(URLs.REPORT_ENTRIES_URL)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("date", date)
                .addBodyParameter("location_id", location_id)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("Checkboard response", response.toString());
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
                                String activation_id = c.getString("activation_id");
                                String location_id = c.getString("location_id");
                                String user_id = c.getString("user_id");
                                String user_name = c.getString("user_name");
                                String customer_name = c.getString("customer_name");
                                String customer_phone = c.getString("customer_phone");
                                String customer_id = c.getString("customer_id");
                                String product = c.getString("product");
                                String product_id = c.getString("product_id");
                                String product_name = c.getString("product_name");
                                String product_quantity = c.getString("product_quantity");
                                String product_value = c.getString("product_value");
                                String extra_field_1 = c.getString("extra_field_1");
                                String extra_field_2 = c.getString("extra_field_2");
                                String merchandise = c.getString("merchandise");
                                String merchandise_id = c.getString("merchandise_id");
                                String merchandise_name = c.getString("merchandise_name");
                                String merchandise_quantity = c.getString("merchandise_quantity");
                                String customer_feedback = c.getString("customer_feedback");
                                String image = c.getString("image");
                                String image_caption = c.getString("image_caption");
                                String latitude = c.getString("latitude");
                                String longitude = c.getString("longitude");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                ReportHistory checkBoardEntries = new ReportHistory( id, activation_id, location_id, user_id, user_name, customer_name, customer_phone, customer_id, product
                                        ,product_id, product_name, product_quantity, product_value, extra_field_1, extra_field_2, merchandise, merchandise_id, merchandise_name,
                                        merchandise_quantity, customer_feedback, image, image_caption, latitude, longitude, created_at, updated_at);
                                reportHistoryList.add(checkBoardEntries);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        reportHistoryAdapter = new ReportHistoryAdapter(UserReportEntriesActivity.this, reportHistoryList);
                        recyclerViewReportHistory.setAdapter(reportHistoryAdapter);
                        recyclerViewReportHistory.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(UserReportEntriesActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        ReportHistory reportHistory = reportHistoryList.get(position);
                                        Intent intent = new Intent(UserReportEntriesActivity.this, UserReportEntriesDetailsActivity.class);
                                        intent.putExtra("id", reportHistory.getId());
                                        intent.putExtra("activation_id", reportHistory.getActivation_id());
                                        intent.putExtra("user_id", reportHistory.getUser_id());
                                        intent.putExtra("user_name", reportHistory.getUser_name());
                                        intent.putExtra("customer_name", reportHistory.getCustomer_name());
                                        intent.putExtra("customer_phone", reportHistory.getCustomer_phone());
                                        intent.putExtra("customer_id", reportHistory.getCustomer_id());
                                        intent.putExtra("product", reportHistory.getProduct());
                                        intent.putExtra("product_id", reportHistory.getProduct_id());
                                        intent.putExtra("product_name", reportHistory.getProduct_name());
                                        intent.putExtra("product_quantity", reportHistory.getProduct_quantity());
                                        intent.putExtra("product_value", reportHistory.getProduct_value());
                                        intent.putExtra("extra_field_1", reportHistory.getExtra_field_1());
                                        intent.putExtra("extra_field_2", reportHistory.getExtra_field_2());
                                        intent.putExtra("merchandise", reportHistory.getMerchandise());
                                        intent.putExtra("merchandise_id", reportHistory.getMerchandise_id());
                                        intent.putExtra("merchandise_name", reportHistory.getMerchandise_name());
                                        intent.putExtra("merchandise_quantity", reportHistory.getMerchandise_quantity());
                                        intent.putExtra("customer_feedback", reportHistory.getCustomer_feedback());
                                        intent.putExtra("image", reportHistory.getImage());
                                        intent.putExtra("image_caption", reportHistory.getImage_caption());
                                        intent.putExtra("latitude", reportHistory.getLatitude());
                                        intent.putExtra("longitude", reportHistory.getLongitude());
                                        startActivity(intent);
                                    }
                                })
                        );


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Expense response", "Result " + anError);
                        new SweetAlertDialog(UserReportEntriesActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });



    }

    @Override
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
                    Intent intent = new Intent(UserReportEntriesActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(UserReportEntriesActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
