package com.androidstudy.alfoneshub.expenses.general;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.OpenableColumns;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.androidnetworking.interfaces.UploadProgressListener;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class GeneralExpensesItemDetailsActivity extends AppCompatActivity {

    String id ="", expenses_id ="", item_supplier ="", item_name ="", item_description ="", item_unit_cost ="",
            item_quantity ="", item_days ="", item_amount ="", created_at ="", updated_at ="", submitted_status = "", requisition_status = "", reconciliation_status = "", item_reconciliation_status = "";
    TextView textViewItemName, textViewSupplier, textViewDescription, textViewUnitCost, textViewQuantity, textViewDays, textViewAmount, textViewProfile;
    
    Button buttonEdit, buttonDelete, buttonReconcile;
    SweetAlertDialog pDialog;
    LinearLayout linearLayoutRequisitionEdit, linearLayoutReconcile;
    EditText editTextChooseImage;
    File imageFile=null;

    private int REQUEST_CAMERA_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_expenses_item_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            expenses_id = intent.getStringExtra("expenses_id");
            item_supplier = intent.getStringExtra("item_supplier");
            item_name = intent.getStringExtra("item_name");
            getSupportActionBar().setTitle(item_name);
            item_description = intent.getStringExtra("item_description");
            item_unit_cost = intent.getStringExtra("item_unit_cost");
            item_quantity = intent.getStringExtra("item_quantity");
            item_days = intent.getStringExtra("item_days");
            item_amount = intent.getStringExtra("item_amount");
            created_at = intent.getStringExtra("created_at");
            updated_at = intent.getStringExtra("updated_at");
            submitted_status = intent.getStringExtra("submitted_status");
            requisition_status = intent.getStringExtra("requisition_status");
            reconciliation_status = intent.getStringExtra("reconciliation_status");
            item_reconciliation_status = intent.getStringExtra("item_reconciliation_status");

        }


        textViewItemName = (TextView) findViewById(R.id.text_view_item_name);
        textViewItemName.setText(item_name.toUpperCase());

        textViewSupplier = (TextView) findViewById(R.id.text_view_supplier);
        textViewSupplier.setText(item_supplier.toUpperCase());

        textViewDescription = (TextView) findViewById(R.id.text_view_description);
        textViewDescription.setText(item_description.toUpperCase());

        textViewUnitCost = (TextView) findViewById(R.id.text_view_unit_cost);
        textViewUnitCost.setText(item_unit_cost.toUpperCase());

        textViewQuantity = (TextView) findViewById(R.id.text_view_quantity);
        textViewQuantity.setText(item_quantity.toUpperCase());

        textViewDays = (TextView) findViewById(R.id.text_view_days);
        textViewDays.setText(item_days.toUpperCase());


        textViewAmount = (TextView) findViewById(R.id.text_view_amount);
        textViewAmount.setText(item_amount.toUpperCase());

        textViewProfile = (TextView) findViewById(R.id.text_view_profile);

        int[] colorArray = this.getResources().getIntArray(R.array.mdcolor_400);
        int randomColor = getRandom(colorArray);
        String expenses_name = String.valueOf(item_name.toUpperCase());
        expenses_name = String.valueOf(expenses_name.charAt(0));
        textViewProfile.setBackgroundColor(randomColor);
        textViewProfile.setText(expenses_name);

        buttonEdit = (Button) findViewById(R.id.button_edit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(GeneralExpensesItemDetailsActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.edit_activation_expense_item_dialog_layout);

                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

                final EditText editTextItemName, editTextItemDescription, editTextItemSupplier, editTextItemUnitCost, editTextItemQuantity, editTextQuantityDays, editTextAmount;
                editTextItemName = (EditText)dialog.findViewById(R.id.edit_text_item_name);
                editTextItemName.setText(item_name);

                editTextItemDescription = (EditText)dialog.findViewById(R.id.edit_text_item_descrption);
                editTextItemDescription.setText(item_description);

                editTextItemSupplier = (EditText)dialog.findViewById(R.id.edit_text_item_supplier);
                editTextItemSupplier.setText(item_supplier);

                editTextItemUnitCost = (EditText)dialog.findViewById(R.id.edit_text_item_unit_cost);
                editTextItemUnitCost.setText(item_unit_cost);

                editTextItemQuantity = (EditText)dialog.findViewById(R.id.edit_text_item_quantity);
                editTextItemQuantity.setText(item_quantity);

                editTextQuantityDays = (EditText)dialog.findViewById(R.id.edit_text_item_days);
                editTextQuantityDays.setText(item_days);

                editTextAmount = (EditText)dialog.findViewById(R.id.edit_text_item_amount);

                Button buttonCancel = (Button) dialog.findViewById(R.id.dialog_info_cancel);
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button buttonEdit = (Button) dialog.findViewById(R.id.dialog_info_edit);
                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE);

                        String item_name = editTextItemName.getText().toString();
                        if (TextUtils.isEmpty(item_name)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item name", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_description = editTextItemDescription.getText().toString();
                        if (TextUtils.isEmpty(item_description)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_supplier = editTextItemSupplier.getText().toString();
                        if (TextUtils.isEmpty(item_supplier)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item supplier", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_unit_cost = editTextItemUnitCost.getText().toString();
                        if (TextUtils.isEmpty(item_unit_cost)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter unit cost", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_quantity = editTextItemQuantity.getText().toString();
                        if (TextUtils.isEmpty(item_quantity)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter quantity", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_days = editTextQuantityDays.getText().toString();
                        if (TextUtils.isEmpty(item_days)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter days", Toast.LENGTH_LONG).show();
                            return;
                        }

                        int item_amount = Integer.valueOf(item_unit_cost)*Integer.valueOf(item_quantity)*Integer.valueOf(item_days);
                        editTextAmount.setText(String.valueOf(item_amount));


                        AndroidNetworking.post(URLs.GENERAL_EXPENSES_ITEM_EDIT)
                                .addBodyParameter("id", id)
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

                                                Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Success, item edit", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();

                                            }else{
                                                Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, item not edit", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("ERROR",error.toString());
                                        Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });
                        
                        

                    }
                });

                dialog.show();
            }
        });

        buttonDelete = (Button)findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new SweetAlertDialog(GeneralExpensesItemDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Deleting...");
                pDialog.setCancelable(false);
                pDialog.show();
               
                AndroidNetworking.post(URLs.GENERAL_EXPENSES_ITEM_DELETE)
                        .addBodyParameter("id", id)
                        .setTag("reset_code")
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

                                        Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Success, item deleted", Toast.LENGTH_LONG).show();
                                        finish();

                                    }else{
                                        Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, item not deleted", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Error, something went wrong", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        buttonReconcile = (Button) findViewById(R.id.button_reconcile);
        buttonReconcile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(GeneralExpensesItemDetailsActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.reconciliation_dialog_layout);

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

                editTextChooseImage = (EditText) dialog.findViewById(R.id.edit_text_choose_image);
                editTextChooseImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN) {
                            openIntent();
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
                Button buttonEdit = (Button) dialog.findViewById(R.id.dialog_info_edit);
                buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        progressBar.setVisibility(View.VISIBLE);

                        String item_name = editTextItemName.getText().toString();
                        if (TextUtils.isEmpty(item_name)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item name", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_description = editTextItemDescription.getText().toString();
                        if (TextUtils.isEmpty(item_description)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_supplier = editTextItemSupplier.getText().toString();
                        if (TextUtils.isEmpty(item_supplier)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter item supplier", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_unit_cost = editTextItemUnitCost.getText().toString();
                        if (TextUtils.isEmpty(item_unit_cost)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter unit cost", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_quantity = editTextItemQuantity.getText().toString();
                        if (TextUtils.isEmpty(item_quantity)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter quantity", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String item_days = editTextQuantityDays.getText().toString();
                        if (TextUtils.isEmpty(item_days)) {
                            Toast.makeText(GeneralExpensesItemDetailsActivity.this, "Please enter days", Toast.LENGTH_LONG).show();
                            return;
                        }

                        int item_amount = Integer.valueOf(item_unit_cost)*Integer.valueOf(item_quantity)*Integer.valueOf(item_days);
                        editTextAmount.setText(String.valueOf(item_amount));

                        AndroidNetworking.upload(URLs.ACTIVATION_RECONILE_ITEM)
                                .addMultipartParameter("id", id)
                                .addMultipartParameter("expenses_id", expenses_id)
                                .addMultipartParameter("item_supplier", item_supplier)
                                .addMultipartParameter("item_name", item_name)
                                .addMultipartParameter("item_description", item_description)
                                .addMultipartParameter("item_unit_cost", item_unit_cost)
                                .addMultipartParameter("item_quantity", item_quantity)
                                .addMultipartParameter("item_days", item_days)
                                .addMultipartParameter("item_amount", String.valueOf(item_amount))
                                .addMultipartFile("image", imageFile)
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        // do anything with progress
                                        Log.d("Response", "bytesUploaded : " + bytesUploaded + " totalBytes : " + totalBytes);
                                        Log.d("Response", "setUploadProgressListener isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                                    }
                                })
                                .getAsJSONObject(new JSONObjectRequestListener() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("Item add response", response.toString());

                                        int success=0;
                                        try {
                                            success = response.getInt("success");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if(success==1){

                                            new SweetAlertDialog(GeneralExpensesItemDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Successful!!")
                                                    .setContentText("Reconciliation was successful")
                                                    .setConfirmText("Dismiss")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dialog.dismiss();
                                                            sDialog.dismissWithAnimation();
                                                            GeneralExpensesItemDetailsActivity.this.finish();
                                                        }
                                                    })
                                                    .show();

                                        }else{

                                            new SweetAlertDialog(GeneralExpensesItemDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Error!")
                                                    .setContentText("Reconciliation was not successful")
                                                    .setConfirmText("Dismiss")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dialog.dismiss();
                                                            sDialog.dismissWithAnimation();
                                                            GeneralExpensesItemDetailsActivity.this.finish();
                                                        }
                                                    })
                                                    .show();

                                        }


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("Error response", "Result " + anError);

                                        new SweetAlertDialog(GeneralExpensesItemDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Error!")
                                                .setContentText("Reconciliation was not successful")
                                                .setConfirmText("Dismiss")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        GeneralExpensesItemDetailsActivity.this.finish();
                                                    }
                                                })
                                                .show();

                                    }
                                });


                    }
                });

                dialog.show();
            }
        });

        linearLayoutRequisitionEdit = (LinearLayout)findViewById(R.id.linear_layout_requisition_edit);
        if(submitted_status.equalsIgnoreCase("1")){
            buttonEdit.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
            linearLayoutRequisitionEdit.setVisibility(View.GONE);
        }

        linearLayoutReconcile = (LinearLayout)findViewById(R.id.linear_layout_reconcile);
        linearLayoutReconcile.setVisibility(View.GONE);
        if(requisition_status.equalsIgnoreCase("1")){
            linearLayoutReconcile.setVisibility(View.VISIBLE);
        }

        if(item_reconciliation_status.equalsIgnoreCase("1")){
            linearLayoutReconcile.setVisibility(View.GONE);
        }

    }

    public void openIntent() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            String file_name = new SimpleDateFormat("yyyyMMddHHmm'.jpg'").format(new Date());
            editTextChooseImage.setText(file_name);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            imageFile = new File(Environment.getExternalStorageDirectory(), file_name);
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(imageFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(GeneralExpensesItemDetailsActivity.this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void persistImage(Bitmap bitmap, String filename) {
        imageFile = new File(GeneralExpensesItemDetailsActivity.this.getCacheDir(), filename + ".jpg");
        try {
            this.imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            OutputStream os = new FileOutputStream(this.imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e2) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e2);
        }
    }

    public String getFileName(Uri uri) {
        String result = "";
        if (uri.getScheme().equals("content")) {
            Cursor cursor = GeneralExpensesItemDetailsActivity.this.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
