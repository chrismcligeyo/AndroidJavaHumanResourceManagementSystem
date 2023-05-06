package com.androidstudy.alfoneshub.payslips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.adapters.PayslipAdapter;
import com.androidstudy.alfoneshub.leaves.LeaveDetailsActivity;
import com.androidstudy.alfoneshub.models.Payslip;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

public class PayslipActivity extends AppCompatActivity {

    RecyclerView payslipRecycler;
    PayslipAdapter payslipAdapter;
    private List<Payslip> payslipList = new ArrayList<>();
    SweetAlertDialog pDialog;
    LinearLayout linearLayoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Payslips");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);
        payslipRecycler= (RecyclerView)findViewById(R.id.recycler_payslips);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        payslipRecycler.setLayoutManager(gridLayoutManager);

        getPayslip();
    }

    private void getPayslip() {

        pDialog = new SweetAlertDialog(PayslipActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Fetching payslips...");
        pDialog.setCancelable(false);

        SharedPreferences preferences = PayslipActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.FETCH_PAYSLIPS)
                .addBodyParameter("id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
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
                                payslipRecycler.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                payslipRecycler.setVisibility(View.VISIBLE);
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
                                String payslip_id = c.getString("payslip_id");
                                String admin_id = c.getString("admin_id");
                                String name = c.getString("name");
                                String employee_no = c.getString("employee_no");
                                String gross_salary = c.getString("gross_salary");
                                String nssf_contibution = c.getString("nssf_contibution");
                                String taxable_pay = c.getString("taxable_pay");
                                String personal_relief = c.getString("personal_relief");
                                String insurance_relief = c.getString("insurance_relief");
                                String paye = c.getString("paye");
                                String nhif_contribution = c.getString("nhif_contribution");
                                String net_pay = c.getString("net_pay");
                                String tax_rate = c.getString("tax_rate");
                                String month = c.getString("month");
                                String year = c.getString("year");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                Payslip payslip = new Payslip(id, payslip_id, admin_id, name, employee_no, gross_salary, nssf_contibution, taxable_pay, personal_relief, insurance_relief, paye, nhif_contribution, net_pay, tax_rate, month, year, created_at, updated_at);
                                payslipList.add(payslip);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        payslipAdapter = new PayslipAdapter(PayslipActivity.this, payslipList);
                        payslipRecycler.setAdapter(payslipAdapter);
                        payslipRecycler.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(PayslipActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Payslip payslip = payslipList.get(position);
                                        Intent intent = new Intent(PayslipActivity.this, PayslipDetailsActivity.class);
                                        intent.putExtra("id", payslip.getId());
                                        intent.putExtra("payslip_id", payslip.getPayslip_id());
                                        intent.putExtra("admin_id", payslip.getAdmin_id());
                                        intent.putExtra("name", payslip.getName());
                                        intent.putExtra("employee_no", payslip.getEmployee_no());
                                        intent.putExtra("gross_salary", payslip.getGross_salary());
                                        intent.putExtra("nssf_contibution", payslip.getNssf_contibution());
                                        intent.putExtra("taxable_pay", payslip.getTaxable_pay());
                                        intent.putExtra("personal_relief", payslip.getPersonal_relief());
                                        intent.putExtra("insurance_relief", payslip.getInsurance_relief());
                                        intent.putExtra("paye", payslip.getPaye());
                                        intent.putExtra("nhif_contribution", payslip.getNhif_contribution());
                                        intent.putExtra("net_pay", payslip.getNet_pay());
                                        intent.putExtra("tax_rate", payslip.getTax_rate());
                                        intent.putExtra("month", payslip.getMonth());
                                        intent.putExtra("year", payslip.getYear());
                                        intent.putExtra("created_at", payslip.getCreated_at());
                                        intent.putExtra("updated_at", payslip.getUpdated_at());
                                        startActivity(intent);
                                    }
                                })
                        );
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.d("ERROR",error.toString());
                        Toast.makeText(PayslipActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

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
