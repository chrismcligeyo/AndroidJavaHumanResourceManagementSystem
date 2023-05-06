package com.androidstudy.alfoneshub.leaves;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.androidnetworking.interfaces.UploadProgressListener;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LeaveDetailsActivity extends AppCompatActivity {

    TextView textViewReason, textViewLeaveDescription, textViewStartDate, textViewEndDate, textViewStatus;
    String id ="", reasons ="", description ="", date_from ="", date_to ="", status="";
    Button button_attachment;
    EditText editTextChooseImage;
    LinearLayout linearLayoutAttachment;

    File imageFile=null;
    private int REQUEST_CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Leave details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        textViewReason = (TextView)findViewById(R.id.text_view_reason);
        textViewLeaveDescription = (TextView)findViewById(R.id.text_view_reason_description);
        textViewStartDate = (TextView)findViewById(R.id.text_view_start_date);
        textViewEndDate = (TextView)findViewById(R.id.text_view_end_date);
        textViewStatus = (TextView)findViewById(R.id.text_view_status);

        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            reasons = intent.getStringExtra("reasons");
            textViewReason.setText(reasons);

            description = intent.getStringExtra("description");
            textViewLeaveDescription.setText(description);

            date_from = intent.getStringExtra("date_from");
            textViewStartDate.setText(date_from);

            date_to = intent.getStringExtra("date_to");
            textViewEndDate.setText(date_to);

            status = intent.getStringExtra("status");
            if(status.equalsIgnoreCase("1")){
                textViewStatus.setText("APPROVED");
            }else if(status.equalsIgnoreCase("2")){
                textViewStatus.setText("DECLINED");
            }else {
                textViewStatus.setText("PENDING");
            }
        }

        button_attachment = (Button) findViewById(R.id.button_attachment);
        button_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LeaveDetailsActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.leave_attachment_dialog_layout);

                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_dialog);
                progressBar.setVisibility(View.GONE);

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

                        AndroidNetworking.upload(URLs.LEAVE_ATTACHMNENT)
                                .addMultipartParameter("id", id)
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

                                            new SweetAlertDialog(LeaveDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Successful!!")
                                                    .setContentText("Attachment was uploaded")
                                                    .setConfirmText("Dismiss")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dialog.dismiss();
                                                            sDialog.dismissWithAnimation();
                                                            LeaveDetailsActivity.this.finish();
                                                        }
                                                    })
                                                    .show();

                                        }else{

                                            new SweetAlertDialog(LeaveDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Error!")
                                                    .setContentText("Attachment was not uploaded")
                                                    .setConfirmText("Dismiss")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dialog.dismiss();
                                                            sDialog.dismissWithAnimation();
                                                            LeaveDetailsActivity.this.finish();
                                                        }
                                                    })
                                                    .show();

                                        }


                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("Error response", "Result " + anError);

                                        new SweetAlertDialog(LeaveDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Error!")
                                                .setContentText("Attachment was not uploaded")
                                                .setConfirmText("Dismiss")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        LeaveDetailsActivity.this.finish();
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

        linearLayoutAttachment = (LinearLayout)findViewById(R.id.linear_layout_attachment);
        linearLayoutAttachment.setVisibility(View.GONE);
        if(status.equalsIgnoreCase("1")){
            linearLayoutAttachment.setVisibility(View.VISIBLE);
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
