package com.androidstudy.alfoneshub;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;
import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class CheckResetCodeActivity extends AppCompatActivity {

    Button btnVerifyCode;
    TextView btnLogin;
    EditText editTextResetCode;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_reset_code);

        btnVerifyCode = (Button)findViewById(R.id.btn_verify);
        btnLogin = (TextView)findViewById(R.id.btn_discard_login);
        editTextResetCode = (EditText) findViewById(R.id.edit_text_reset_code);

        Intent intent = this.getIntent();
        if (null != intent) {
            email = intent.getStringExtra("email");
        }

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_reset_code = editTextResetCode.getText().toString();
                // Check if user provided expense
                if (TextUtils.isEmpty(input_reset_code)) {
                    editTextResetCode.setError("Enter Reset Code");
                    return;
                }

                final SweetAlertDialog pDialog = new SweetAlertDialog(CheckResetCodeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Verifying code");
                pDialog.setCancelable(false);
                pDialog.show();
                AndroidNetworking.post(URLs.VERIFY_RESET_CODE)
                        .addBodyParameter("reset_code", input_reset_code)
                        .addBodyParameter("email", email)
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

                                        new SweetAlertDialog(CheckResetCodeActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Successful")
                                                .setContentText("Your code has been verified")
                                                .setConfirmText("Okay! Proceed to change password")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        //Restart intent
                                                        Intent intent = new Intent(CheckResetCodeActivity.this, ResetPasswordActivity.class);
                                                        intent.putExtra("email",email);
                                                        CheckResetCodeActivity.this.startActivity(intent);
                                                        finish();

                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismiss();
                                                    }
                                                })
                                                .show();

                                    }else{
                                        pDialog.dismiss();
                                        new SweetAlertDialog(CheckResetCodeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("ERROR!")
                                                .setContentText("Failed to verify code")
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    pDialog.dismiss();
                                    Toast.makeText(CheckResetCodeActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(CheckResetCodeActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Restart intent
                Intent intent = new Intent(CheckResetCodeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
