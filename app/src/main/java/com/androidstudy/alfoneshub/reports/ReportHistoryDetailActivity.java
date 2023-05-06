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
import com.jaredrummler.materialspinner.MaterialSpinner;

import com.androidstudy.alfoneshub.ProjectManagerMainActivity;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.MainActivity;
import com.androidstudy.alfoneshub.adapters.ReportHistoryAdapter;
import com.androidstudy.alfoneshub.models.ReportHistory;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportHistoryDetailActivity extends AppCompatActivity {
    String activation_id, activation_name, activation_description, activation_start_date,activation_end_date,
            activation_company, date="";

    String interaction = "0", product= "0", merchandise="0";
    TextView textViewCampaignName, textViewCampaignDate;
    MaterialSpinner spinner_date;
    SweetAlertDialog pDialog;
    
    RecyclerView recyclerViewReportHistory;
    ReportHistoryAdapter reportHistoryAdapter;
    private List<ReportHistory> reportHistoryList = new ArrayList<>();

    public BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Report History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewCampaignDate = (TextView)findViewById(R.id.txt_activation_date);
        textViewCampaignName = (TextView)findViewById(R.id.txt_activation_name);

        spinner_date=(MaterialSpinner)findViewById(R.id.spinner_date);
        recyclerViewReportHistory= (RecyclerView)findViewById(R.id.recycler_report_history);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewReportHistory.setLayoutManager(gridLayoutManager);

        Intent intent = this.getIntent();
        if (null != intent) {
            activation_id = intent.getStringExtra("activation_id");
            activation_name = intent.getStringExtra("activation_name");
            activation_description = intent.getStringExtra("activation_description");
            activation_start_date = intent.getStringExtra("activation_start_date");
            activation_end_date = intent.getStringExtra("activation_end_date");
            activation_company = intent.getStringExtra("activation_company");

        }

        textViewCampaignName.setText(activation_name.toUpperCase());

        fetchSpinnerData();


        mChart = (BarChart) findViewById(R.id.chart1);
    }

    public void setChart(){


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(Float.parseFloat(interaction), 0));
        entries.add(new BarEntry(Float.parseFloat(product), 1));
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


    private void fetchSpinnerData() {
        AndroidNetworking.post(URLs.ACTIVATION_DATE_URL)
                .addBodyParameter("activation_id", activation_id)
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
                                date = c.getString("date");
                                listCampaign.add(date);
                            }
                            //Get first date
                            for (int i = 0; i < 1; i++) {
                                c = (JSONObject) feedArray.get(i);
                                date = c.getString("date");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try{
                            spinner_date.setItems(listCampaign);
                            spinner_date.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                    date=item;
                                    Toast.makeText(ReportHistoryDetailActivity.this, "Date" +date, Toast.LENGTH_SHORT).show();
                                    if(reportHistoryList.size()>0){
                                        reportHistoryList.clear();
                                    }
//
                                      fetchRecordsHistory(date);
                                      fetchSummary(date);
                                      reportHistoryAdapter.notifyDataSetChanged();
                                      textViewCampaignDate.setText(date.toUpperCase());
                                }
                            });
                              fetchRecordsHistory(date);
                              fetchSummary(date);
                              textViewCampaignDate.setText(date.toUpperCase());
                        }catch (Exception e){
                            Log.d("ERROR","Spinner not populated");
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        // handle error
                        Log.d("Checklist response", "Result " + error);
                        new SweetAlertDialog(ReportHistoryDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });

    }
    
    public void fetchRecordsHistory(String date){
        SharedPreferences preferences = ReportHistoryDetailActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(ReportHistoryDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.REPORT_ENTRIES_URL)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("date", date)
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
                        reportHistoryAdapter = new ReportHistoryAdapter(ReportHistoryDetailActivity.this, reportHistoryList);
                        recyclerViewReportHistory.setAdapter(reportHistoryAdapter);
//                        recyclerViewReportHistory.addOnItemTouchListener(
//                                new MyRecyclerItemClickListener(ReportHistoryDetailActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View view, int position) {
//                                        ReportHistory checkBoardEntries = reportHistoryList.get(position);
//                                        Intent intent = new Intent(ReportHistoryDetailActivity.this, ReportHistoryDetailActivity.class);
//                                        intent.putExtra("id", checkBoardEntries.getId());
//                                        intent.putExtra("customer_name", checkBoardEntries.getCustomer_name());
//                                        intent.putExtra("customer_phone", checkBoardEntries.getCustomer_phone());
//                                        intent.putExtra("customer_comment", checkBoardEntries.getCustomer_feedback());
//                                        intent.putExtra("activation_date", checkBoardEntries.getCreated_at());
//                                        intent.putExtra("product", checkBoardEntries.getProduct());
//                                        intent.putExtra("product_name", checkBoardEntries.getProduct_name());
//                                        intent.putExtra("product_quantity", checkBoardEntries.getProduct_quantity());
//                                        intent.putExtra("product_value", checkBoardEntries.getProduct_value());
//                                        intent.putExtra("extra_field_1", checkBoardEntries.getExtra_field_1());
//                                        intent.putExtra("extra_field_2", checkBoardEntries.getExtra_field_2());
//                                        intent.putExtra("merchandise", checkBoardEntries.getMerchandise());
//                                        intent.putExtra("merchandise_quantity", checkBoardEntries.getMerchandise_quantity());
//                                        intent.putExtra("location", checkBoardEntries.getLocation_id());
//                                        startActivity(intent);
//                                    }
//                                })
//                        );


                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Expense response", "Result " + anError);
                        new SweetAlertDialog(ReportHistoryDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }

    public void fetchSummary(String date){
        SharedPreferences preferences = ReportHistoryDetailActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.REPORT_SUMMARY_URL)
                .addBodyParameter("activation_id", activation_id)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("date", date)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("summary response", response.toString());
                        try {
                            interaction = response.getString("interaction_count");
                            product = response.getString("sales_count");
                            merchandise = response.getString("merchandise_count");

                            setChart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error

                        Log.d("Summary response", "Result " + anError);
                        new SweetAlertDialog(ReportHistoryDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                        setChart();
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
                    Intent intent = new Intent(ReportHistoryDetailActivity.this, ProjectManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(role == 4){
                    Intent intent = new Intent(ReportHistoryDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
