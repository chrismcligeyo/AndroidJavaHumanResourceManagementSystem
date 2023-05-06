package com.androidstudy.alfoneshub.expenses.activations.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.androidstudy.alfoneshub.adapters.ExpensesAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.expenses.activations.ActivationExpensesItemsActivity;
import com.androidstudy.alfoneshub.models.Expenses;
import com.androidstudy.alfoneshub.utils.AppStatus;
import com.androidstudy.alfoneshub.utils.Config;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class PendingExpensesFragment extends Fragment {

    MaterialSpinner spinner_activation;
    SweetAlertDialog pDialog;

    RecyclerView recyclerViewExpenses;
    ExpensesAdapter expensesAdapter;
    private List<Expenses> expensesList = new ArrayList<>();
    TextView textViewCampaignName;
    LinearLayout linearLayoutError;
    String activation_name="";
    DatePickerDialog mDatePicker;
    
    Button buttonCreateRequisition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner_activation=(MaterialSpinner)view.findViewById(R.id.spinner_activation);
        recyclerViewExpenses= (RecyclerView)view.findViewById(R.id.recycler_expenses);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewExpenses.setLayoutManager(gridLayoutManager);
        linearLayoutError = (LinearLayout)view.findViewById(R.id.linear_layout_error);
        linearLayoutError.setVisibility(View.GONE);

        buttonCreateRequisition = (Button)view.findViewById(R.id.button_create_requisition);
        buttonCreateRequisition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_activation_expense_dialog_layout);

                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextRequisitionTitle, editTextItemDate;
                final String[] selectedDate = {""};
                editTextRequisitionTitle = (EditText)dialog.findViewById(R.id.edit_text_requisition_title);
                editTextItemDate = (EditText)dialog.findViewById(R.id.edit_text_required_date);
                editTextItemDate.setOnTouchListener(new View.OnTouchListener() {

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
                                    selectedDate[0] = selectedyear + "-" + selectedmonth + "-" + selectedday;
                                    editTextItemDate.setText(selectedDate[0]);


                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select date");
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

                        String requisition_title = editTextRequisitionTitle.getText().toString();
                        if (TextUtils.isEmpty(requisition_title)) {
                            Toast.makeText(getActivity(), "Please enter title", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_date = editTextItemDate.getText().toString();
                        if (TextUtils.isEmpty(item_date)) {
                            Toast.makeText(getActivity(), "Please enter item description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        String user_id = preferences.getString("user_id", "user_id");


                        AndroidNetworking.post(URLs.ACTIVATION_EXPENSES_CREATE)
                                .addBodyParameter("activation_name", activation_name)
                                .addBodyParameter("expense_type", "1")
                                .addBodyParameter("admin_id", user_id)
                                .addBodyParameter("requisition_title", requisition_title)
                                .addBodyParameter("required_date", selectedDate[0])
                                .setTag("reset_code")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        progressBar.setVisibility(View.GONE);
                                        int success=0;
                                        //Check for successful login
                                        Log.d("Success Here", "Result " + response.toString());
                                        try {
                                            success = response.getInt(SUCCESS);
                                            //Login has been succesful.
                                            if (success==1){

                                                Toast.makeText(getActivity(), "Success, requisition created", Toast.LENGTH_LONG).show();
                                                if(expensesList.size()>0){
                                                    expensesList.clear();
                                                }
                                                fetchActivationExpenses(activation_name);
                                                expensesAdapter.notifyDataSetChanged();
                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(getActivity(), "Error, item not created", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(getActivity(), "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(getActivity(), "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        fetchSpinnerData();

    }

    private void fetchSpinnerData() {
        /**
         * Perform network call
         */
        if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String user_role = sharedPreferences.getString("user_role","user_role");
            int role = Integer.parseInt(user_role);
            if(role==3){

                SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.PROJECT_MANAGER_PREVIOUS_ACTIVATION)
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
                                    spinner_activation.setItems(listCampaign);
                                    spinner_activation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                            activation_name=item;
                                            Toast.makeText(getActivity(), "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(expensesList.size()>0){
                                                expensesList.clear();
                                            }
                                            fetchActivationExpenses(activation_name);
                                            expensesAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    fetchActivationExpenses(activation_name);
                                }catch (Exception e){
                                    Log.d("ERROR","Spinner not populated");
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else  if(role==4){

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
                                    spinner_activation.setItems(listCampaign);
                                    spinner_activation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                            activation_name=item;
                                            Toast.makeText(getActivity(), "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(expensesList.size()>0){
                                                expensesList.clear();
                                            }
                                            fetchActivationExpenses(activation_name);
                                            expensesAdapter.notifyDataSetChanged();
                                        }
                                    });

                                    fetchActivationExpenses(activation_name);

                                }catch (Exception e){
                                    Log.e("ERROR", e.toString());
                                    Log.d("ERROR","Spinner not populated");
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }else  if(role==5 || role==7){

                SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String user_id = preferences.getString("user_id", "user_id");
                AndroidNetworking.post(URLs.OPERATIONS_PREVIOUS_ACTIVATION)
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
                                    spinner_activation.setItems(listCampaign);
                                    spinner_activation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                                        @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                                            activation_name=item;
                                            Toast.makeText(getActivity(), "" +activation_name, Toast.LENGTH_SHORT).show();
                                            if(expensesList.size()>0){
                                                expensesList.clear();
                                            }
                                            fetchActivationExpenses(activation_name);
                                            expensesAdapter.notifyDataSetChanged();
                                        }
                                    });

                                    fetchActivationExpenses(activation_name);

                                }catch (Exception e){
                                    Log.e("ERROR", e.toString());
                                    Log.d("ERROR","Spinner not populated");
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pDialog.dismiss();
                                // handle error
                                Log.d("Expense response", "Result " + anError);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Something went wrong, Please try again later")
                                        .show();
                            }
                        });
            }


        }else{
            Log.d("Error","No internet connection");
        }
    }

    public void fetchActivationExpenses(String activation_name){
        
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "user_id");

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        AndroidNetworking.post(URLs.ACTIVATION_PENDING_EXPENSES)
                .addBodyParameter("activation_name", activation_name)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("LOCATION", response.toString());

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
                                recyclerViewExpenses.setVisibility(View.GONE);
                                linearLayoutError.setVisibility(View.VISIBLE);
                            }else{
                                Log.d("ARRAY", "NOT empty");
                                recyclerViewExpenses.setVisibility(View.VISIBLE);
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
                                String expense_type = c.getString("expense_type");
                                String activation_id = c.getString("activation_id");
                                String activation_name = c.getString("activation_name");
                                String admin_id = c.getString("admin_id");
                                String requisition_title = c.getString("requisition_title");
                                String amount = c.getString("amount");
                                String required_date = c.getString("required_date");
                                String requisition_status = c.getString("requisition_status");
                                String reconciliation_status = c.getString("reconciliation_status");
                                String submitted_status = c.getString("submitted_status");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                Expenses expenses = new Expenses(id, expense_type, activation_id, activation_name, admin_id, requisition_title, amount, required_date, requisition_status, reconciliation_status, submitted_status, created_at, updated_at);
                                expensesList.add(expenses);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        expensesAdapter = new ExpensesAdapter(getActivity(), expensesList);
                        recyclerViewExpenses.setAdapter(expensesAdapter);
                        recyclerViewExpenses.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(getActivity(), new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Expenses expenses = expensesList.get(position);
                                        Intent intent = new Intent(getActivity(), ActivationExpensesItemsActivity.class);
                                        intent.putExtra("id", expenses.getId());
                                        intent.putExtra("requisition_title", expenses.getRequisition_title());
                                        intent.putExtra("expense_type", expenses.getExpense_type());
                                        intent.putExtra("activation_id", expenses.getActivation_id());
                                        intent.putExtra("activation_name", expenses.getActivation_name());
                                        intent.putExtra("admin_id", expenses.getAdmin_id());
                                        intent.putExtra("requisition_title", expenses.getRequisition_title());
                                        intent.putExtra("amount", expenses.getAmount());
                                        intent.putExtra("required_date", expenses.getRequired_date());
                                        intent.putExtra("requisition_status", expenses.getRequisition_status());
                                        intent.putExtra("reconciliation_status", expenses.getReconciliation_status());
                                        intent.putExtra("submitted_status", expenses.getSubmitted_status());
                                        intent.putExtra("created_at", expenses.getCreated_at());
                                        intent.putExtra("updated_at", expenses.getUpdated_at());
                                        startActivity(intent);
                                    }
                                })
                        );

                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d("Confimation response", "Result " + anError);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });
    }
}
