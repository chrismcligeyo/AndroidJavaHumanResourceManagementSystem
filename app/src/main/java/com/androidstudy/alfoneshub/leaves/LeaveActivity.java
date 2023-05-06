package com.androidstudy.alfoneshub.leaves;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.UserLeaveAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.UserLeave;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class LeaveActivity extends AppCompatActivity {

    RecyclerView leaveListRecycler;
    UserLeaveAdapter userLeaveAdapter;
    private List<UserLeave> userLeaveList = new ArrayList<>();
    SweetAlertDialog pDialog;
    LinearLayout linearLayoutError;
    Button buttonCreateLeave;
    ProgressBar progressBar;

    TextView textViewDaysAllowed, textViewDaysSpent, textViewLeaveApplication;
    Spinner spinnerLeaveReason;
    DatePickerDialog mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Work Leave");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
       
        linearLayoutError = (LinearLayout)findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);
        leaveListRecycler= (RecyclerView)findViewById(R.id.recycler_leaves);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        leaveListRecycler.setLayoutManager(gridLayoutManager);

        textViewDaysAllowed = (TextView)findViewById(R.id.text_view_days_allowed);
        textViewDaysSpent = (TextView)findViewById(R.id.text_view_days_spent);
        textViewLeaveApplication = (TextView)findViewById(R.id.text_view_leave_application);
        
        buttonCreateLeave = (Button)findViewById(R.id.button_create_leave);
        buttonCreateLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LeaveActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_leave_dialog_layout);

                progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextStartDate, editTextEndDate,  editTextLeaveDescription;
                final String[] startDate = {""};
                final String[] endDate = {""};
                final String[] reason = {""};
                editTextStartDate = (EditText)dialog.findViewById(R.id.edit_text_start_date);
                editTextEndDate = (EditText)dialog.findViewById(R.id.edit_text_end_date);
                editTextLeaveDescription = (EditText)dialog.findViewById(R.id.edit_text_leave_description);

                List<String> reasonList = new ArrayList<String>();
                reasonList.add("Sick leave");
                reasonList.add("Vacation");
                reasonList.add("Maternity Leave");
                reasonList.add("Partenity Leave");
                reasonList.add("Casual Leave");
                reasonList.add("Compassionate");
                reasonList.add("Work from home");
                reasonList.add("Comp off");
                reasonList.add("Half day leave 1");
                reasonList.add("Half day leave 2");

                spinnerLeaveReason = (Spinner) dialog.findViewById(R.id.spinner_leave_reason);
                ArrayAdapter<String> divisionDataAdapter = new ArrayAdapter<String>(LeaveActivity.this, R.layout.spinner_item_filter_custom, reasonList);
                divisionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLeaveReason.setAdapter(divisionDataAdapter);
                spinnerLeaveReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                        String item = parent.getItemAtPosition(position).toString();
                        reason[0] = item;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                

               
                editTextStartDate.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            int mYear = mcurrentDate.get(Calendar.YEAR);
                            int mMonth = mcurrentDate.get(Calendar.MONTH);
                            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            mDatePicker = new DatePickerDialog(LeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    /*0000-00-00*/
                                    selectedmonth += 1;
                                    startDate[0] = selectedyear + "-" + selectedmonth + "-" + selectedday;
                                    editTextStartDate.setText(startDate[0]);


                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select start date");
                            mDatePicker.show();
                        }
                        return false;
                    }

                });

                editTextEndDate.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN) {
                            Calendar mcurrentDate = Calendar.getInstance();
                            int mYear = mcurrentDate.get(Calendar.YEAR);
                            int mMonth = mcurrentDate.get(Calendar.MONTH);
                            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            mDatePicker = new DatePickerDialog(LeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    /*0000-00-00*/
                                    selectedmonth += 1;
                                    endDate[0] = selectedyear + "-" + selectedmonth + "-" + selectedday;
                                    editTextEndDate.setText(endDate[0]);


                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select end date");
                            mDatePicker.show();
                        }
                        return false;
                    }

                });

                

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

                        String leave_description = editTextLeaveDescription.getText().toString();
                        if (TextUtils.isEmpty(leave_description)) {
                            Toast.makeText(LeaveActivity.this, "Enter leave description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String start_date = editTextStartDate.getText().toString();
                        if (TextUtils.isEmpty(start_date)) {
                            Toast.makeText(LeaveActivity.this, "Enter start date", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String end_date = editTextEndDate.getText().toString();
                        if (TextUtils.isEmpty(end_date)) {
                            Toast.makeText(LeaveActivity.this, "Enter end date", Toast.LENGTH_LONG).show();
                            return;
                        }


                        SharedPreferences preferences = LeaveActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        String user_id = preferences.getString("user_id", "user_id");


                        AndroidNetworking.post(URLs.CREATE_LEAVE)
                                .addBodyParameter("reasons", reason[0])
                                .addBodyParameter("description", leave_description)
                                .addBodyParameter("start_date", start_date)
                                .addBodyParameter("end_date", end_date)
                                .addBodyParameter("user_id", user_id)
                                .setTag("reset_code")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        progressBar.setVisibility(View.GONE);
                                        int success=0;
                                        //Check for successful login
                                        Log.d("Create leave Here", "Result " + response.toString());
                                        try {
                                            success = response.getInt(SUCCESS);
                                            //Login has been succesful.
                                            if (success==1){

                                                Toast.makeText(LeaveActivity.this, "Success, leave created", Toast.LENGTH_LONG).show();
                                                if(userLeaveList.size()>0){
                                                    userLeaveList.clear();
                                                }

                                                getLeave();
                                                userLeaveAdapter.notifyDataSetChanged();
                                                dialog.dismiss();


                                            }else{
                                                Toast.makeText(LeaveActivity.this, "Error, leave not created", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Log.d("ERROR TRY", e.toString());
                                            Toast.makeText(LeaveActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(LeaveActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        getLeaveSummary();
        getLeave();
    }

    private void getLeaveSummary() {

        SharedPreferences preferences = LeaveActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");
        AndroidNetworking.post(URLs.LEAVE_SUMMARY)
                .addBodyParameter("id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Leave summary", response.toString());

                        try {
                            String days_allowed = response.getString("days_allowed");
                            textViewDaysAllowed.setText(days_allowed);

                            String days_spent = response.getString("days_spent");
                            textViewDaysSpent.setText(days_spent);

                            String leave_application = response.getString("leave_application");
                            textViewLeaveApplication.setText(leave_application);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Leave summary", "Result " + anError);
                    }
                });

    }

    private void getLeave() {

        pDialog = new SweetAlertDialog(LeaveActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Fetching leave history...");
        pDialog.setCancelable(false);

        SharedPreferences preferences = LeaveActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        AndroidNetworking.post(URLs.LEAVE_LIST)
                .addBodyParameter("id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LEAVE LIST", response.toString());
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
                                leaveListRecycler.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                leaveListRecycler.setVisibility(View.VISIBLE);
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
                                String admin_id = c.getString("admin_id");
                                String name = c.getString("name");
                                String date_from = c.getString("date_from");
                                String date_to = c.getString("date_to");
                                String reasons = c.getString("reasons");
                                String description = c.getString("description");
                                String attachment = c.getString("attachment");
                                String status = c.getString("status");
                                String status_report = c.getString("status_report");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                UserLeave userLeave = new UserLeave(id, admin_id, name, date_from, date_to, reasons, description, attachment, status, status_report, created_at, updated_at);
                                userLeaveList.add(userLeave);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        userLeaveAdapter = new UserLeaveAdapter(LeaveActivity.this, userLeaveList);
                        leaveListRecycler.setAdapter(userLeaveAdapter);
                        leaveListRecycler.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(LeaveActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        UserLeave userLeave = userLeaveList.get(position);
                                        Intent intent = new Intent(LeaveActivity.this, LeaveDetailsActivity.class);
                                        intent.putExtra("id", userLeave.getId());
                                        intent.putExtra("admin_id", userLeave.getAdmin_id());
                                        intent.putExtra("name", userLeave.getName());
                                        intent.putExtra("date_from", userLeave.getDate_from());
                                        intent.putExtra("date_to", userLeave.getDate_to());
                                        intent.putExtra("reasons", userLeave.getReasons());
                                        intent.putExtra("attachment", userLeave.getAttachment());
                                        intent.putExtra("description", userLeave.getDescription());
                                        intent.putExtra("status", userLeave.getStatus());
                                        intent.putExtra("status_report", userLeave.getStatus_report());
                                        intent.putExtra("created_at", userLeave.getCreated_at());
                                        intent.putExtra("updated_at", userLeave.getUpdated_at());
                                        startActivity(intent);
                                    }
                                })
                        );
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.d("ERROR",error.toString());
                        Toast.makeText(LeaveActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
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
