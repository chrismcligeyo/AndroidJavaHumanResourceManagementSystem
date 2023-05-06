package com.androidstudy.alfoneshub.expenses.activations;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.adapters.ExpensesItemAdapter;
import com.androidstudy.alfoneshub.adapters.MyRecyclerItemClickListener;
import com.androidstudy.alfoneshub.models.ExpensesItem;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class ActivationExpensesItemsActivity extends AppCompatActivity {

    String id ="", expense_type="", activation_id="", activation_name="", admin_id="", requisition_title="", amount="", required_date="", requisition_status="", reconciliation_status="", submitted_status="", created_at="", updated_at="";
    TextView textViewRequisitionTitle, textViewAmount, textViewStatus, textViewDate, textViewProfile;

    SweetAlertDialog pDialog;

    RecyclerView recyclerViewExpensesItem;
    ExpensesItemAdapter expensesItemAdapter;
    private List<ExpensesItem> expensesItemList = new ArrayList<>();

    Button buttonAddItem, buttonSubmit;
    LinearLayout linearLayoutExpenseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_expenses_items);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewRequisitionTitle = (TextView) findViewById(R.id.text_view_requisition_title);
        textViewAmount = (TextView) findViewById(R.id.text_view_amount);
        textViewStatus = (TextView) findViewById(R.id.text_view_status);
        textViewDate = (TextView) findViewById(R.id.text_view_date);
        textViewProfile = (TextView) findViewById(R.id.text_view_profile);

        buttonAddItem = (Button) findViewById(R.id.button_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ActivationExpensesItemsActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_activation_expense_item_dialog_layout);

                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextItemName, editTextItemDescription, editTextItemSupplier, editTextItemUnitCost, editTextItemQuantity, editTextQuantityDays, editTextAmount;
                editTextItemName = (EditText)dialog.findViewById(R.id.edit_text_item_name);
                editTextItemDescription = (EditText)dialog.findViewById(R.id.edit_text_item_descrption);
                editTextItemSupplier = (EditText)dialog.findViewById(R.id.edit_text_item_supplier);
                editTextItemUnitCost = (EditText)dialog.findViewById(R.id.edit_text_item_unit_cost);
                editTextItemQuantity = (EditText)dialog.findViewById(R.id.edit_text_item_quantity);
                editTextQuantityDays = (EditText)dialog.findViewById(R.id.edit_text_item_days);
                editTextAmount = (EditText)dialog.findViewById(R.id.edit_text_item_amount);

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

                        String item_name = editTextItemName.getText().toString();
                        if (TextUtils.isEmpty(item_name)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter item name", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_description = editTextItemDescription.getText().toString();
                        if (TextUtils.isEmpty(item_description)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter item description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_supplier = editTextItemSupplier.getText().toString();
                        if (TextUtils.isEmpty(item_supplier)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter item supplier", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_unit_cost = editTextItemUnitCost.getText().toString();
                        if (TextUtils.isEmpty(item_unit_cost)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter unit cost", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_quantity = editTextItemQuantity.getText().toString();
                        if (TextUtils.isEmpty(item_quantity)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter quantity", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_days = editTextQuantityDays.getText().toString();
                        if (TextUtils.isEmpty(item_days)) {
                            Toast.makeText(ActivationExpensesItemsActivity.this, "Please enter days", Toast.LENGTH_LONG).show();
                            return;
                        }

                        int item_amount = Integer.valueOf(item_unit_cost)*Integer.valueOf(item_quantity)*Integer.valueOf(item_days);
                        editTextAmount.setText(String.valueOf(item_amount));


                        AndroidNetworking.post(URLs.ACTIVATION_EXPENSES_ITEM_CREATE)
                                .addBodyParameter("expenses_id", id)
                                .addBodyParameter("item_supplier", item_supplier)
                                .addBodyParameter("item_name", item_name)
                                .addBodyParameter("item_description", item_description)
                                .addBodyParameter("item_unit_cost", item_unit_cost)
                                .addBodyParameter("item_quantity", item_quantity)
                                .addBodyParameter("item_days", item_days)
                                .addBodyParameter("item_amount", String.valueOf(item_amount))
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

                                                Toast.makeText(ActivationExpensesItemsActivity.this, "Success, item created", Toast.LENGTH_LONG).show();

                                                if(expensesItemList.size()>0){
                                                    expensesItemList.clear();
                                                }
                                                fetchExpensesItems();
                                                expensesItemAdapter.notifyDataSetChanged();
                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(ActivationExpensesItemsActivity.this, "Error, item not created", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(ActivationExpensesItemsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(ActivationExpensesItemsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

                dialog.show();
            }
        });

        buttonSubmit = (Button) findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(ActivationExpensesItemsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Submitting...");
                pDialog.setCancelable(false);
                pDialog.show();

                AndroidNetworking.post(URLs.ACTIVATION_EXPENSES_SUBMIT)
                        .addBodyParameter("id", id)
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

                                        Toast.makeText(ActivationExpensesItemsActivity.this, "Success, requisition submitted", Toast.LENGTH_LONG).show();
                                        linearLayoutExpenseItem.setVisibility(View.GONE);

                                    }else{
                                        Toast.makeText(ActivationExpensesItemsActivity.this, "Error, item not deleted", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ActivationExpensesItemsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(ActivationExpensesItemsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        recyclerViewExpensesItem= (RecyclerView)findViewById(R.id.recycler_expenses);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewExpensesItem.setLayoutManager(gridLayoutManager);


        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            activation_id = intent.getStringExtra("activation_id");
            expense_type = intent.getStringExtra("expense_type");
            activation_name = intent.getStringExtra("activation_name");
            admin_id = intent.getStringExtra("admin_id");
            requisition_title = intent.getStringExtra("requisition_title");

            getSupportActionBar().setTitle(requisition_title);

            amount = intent.getStringExtra("amount");
            required_date = intent.getStringExtra("required_date");
            requisition_status = intent.getStringExtra("requisition_status");
            reconciliation_status = intent.getStringExtra("reconciliation_status");
            submitted_status = intent.getStringExtra("submitted_status");
            created_at = intent.getStringExtra("created_at");
            updated_at = intent.getStringExtra("updated_at");

            textViewRequisitionTitle.setText(String.valueOf(requisition_title.toUpperCase()));
            textViewAmount.setText(amount);

            int[] colorArray = this.getResources().getIntArray(R.array.mdcolor_400);
            int randomColor = getRandom(colorArray);
            String expenses_name = String.valueOf(requisition_title.toUpperCase());
            expenses_name = String.valueOf(expenses_name.charAt(0));
            textViewProfile.setBackgroundColor(randomColor);
            textViewProfile.setText(expenses_name);

            if(requisition_status.equalsIgnoreCase("1")){

                textViewStatus.setText("APPROVED");

            }else if(requisition_status.equalsIgnoreCase("2")){

                textViewStatus.setText("DECLINE");

            }else{

                textViewStatus.setText("PENDING");
            }

        }


        linearLayoutExpenseItem = (LinearLayout) findViewById(R.id.linear_layout_expense_item);
        if(submitted_status.equalsIgnoreCase("1")){
            linearLayoutExpenseItem.setVisibility(View.GONE);
        }else{
            linearLayoutExpenseItem.setVisibility(View.VISIBLE);
        }

        fetchExpensesItems();

    }
    
    public void fetchExpensesItems(){

        pDialog = new SweetAlertDialog(ActivationExpensesItemsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
        pDialog.setTitleText("Loading details...");
        pDialog.setCancelable(false);
        pDialog.show();
        
        AndroidNetworking.post(URLs.ACTIVATION_EXPENSES_ITEMS)
                .addBodyParameter("expenses_id", id)
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
                                String expenses_id = c.getString("expenses_id");
                                String item_supplier = c.getString("item_supplier");
                                String item_name = c.getString("item_name");
                                String item_description = c.getString("item_description");
                                String item_unit_cost = c.getString("item_unit_cost");
                                String item_quantity = c.getString("item_quantity");
                                String item_days = c.getString("item_days");
                                String item_amount = c.getString("item_amount");
                                String reconciliation_status = c.getString("reconciliation_status");
                                String created_at = c.getString("created_at");
                                String updated_at = c.getString("updated_at");
                                ExpensesItem expensesItem = new ExpensesItem(id, expenses_id, item_supplier, item_name, item_description, item_unit_cost, item_quantity, item_days, item_amount, reconciliation_status, created_at, updated_at);
                                expensesItemList.add(expensesItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Set adapter listInteractionEntries_id
                        expensesItemAdapter = new ExpensesItemAdapter(ActivationExpensesItemsActivity.this, expensesItemList);
                        recyclerViewExpensesItem.setAdapter(expensesItemAdapter);
                        recyclerViewExpensesItem.addOnItemTouchListener(
                                new MyRecyclerItemClickListener(ActivationExpensesItemsActivity.this, new MyRecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        ExpensesItem expensesItem = expensesItemList.get(position);
                                        Intent intent = new Intent(ActivationExpensesItemsActivity.this, ActivationExpensesItemDetailsActivity.class);
                                        intent.putExtra("id", expensesItem.getId());
                                        intent.putExtra("requisition_title", requisition_title);
                                        intent.putExtra("requisition_status", requisition_status);
                                        intent.putExtra("reconciliation_status", reconciliation_status);
                                        intent.putExtra("submitted_status", submitted_status);
                                        intent.putExtra("expenses_id", expensesItem.getExpenses_id());
                                        intent.putExtra("item_supplier", expensesItem.getItem_supplier());
                                        intent.putExtra("item_name", expensesItem.getItem_name());
                                        intent.putExtra("item_description", expensesItem.getItem_description());
                                        intent.putExtra("item_unit_cost", expensesItem.getItem_unit_cost());
                                        intent.putExtra("item_quantity", expensesItem.getItem_quantity());
                                        intent.putExtra("item_days", expensesItem.getItem_days());
                                        intent.putExtra("item_amount", expensesItem.getItem_amount());
                                        intent.putExtra("item_reconciliation_status", expensesItem.getReconciliation_status());
                                        intent.putExtra("created_at", expensesItem.getCreated_at());
                                        intent.putExtra("updated_at", expensesItem.getUpdated_at());
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
                        new SweetAlertDialog(ActivationExpensesItemsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("Something went wrong, Please try again later")
                                .show();
                    }
                });

    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
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
