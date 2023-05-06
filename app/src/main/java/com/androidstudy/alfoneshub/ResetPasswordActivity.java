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

import com.androidstudy.alfoneshub.utils.URLs;

import static com.androidnetworking.common.ANConstants.SUCCESS;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnChangePassword;
    TextView btnLogin;
    EditText editTextPassword;

    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnChangePassword = (Button)findViewById(R.id.btn_change_password);
        btnLogin = (TextView)findViewById(R.id.btn_discard_login);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        Intent intent = this.getIntent();
        if (null != intent) {
            email = intent.getStringExtra("email");
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_password = editTextPassword.getText().toString();
                // Check if user provided expense
                if (TextUtils.isEmpty(input_password)) {
                    editTextPassword.setError("Enter Password");
                    return;
                }

                final SweetAlertDialog pDialog = new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Updating password");
                pDialog.setCancelable(false);
                pDialog.show();
                AndroidNetworking.post(URLs.RESET_PASSWORD_URL)
                        .addBodyParameter("password", input_password)
                        .addBodyParameter("email", email)
                        .setTag("forgot_credentials")
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

                                        new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Successful")
                                                .setContentText("Your have successfully changed your password")
                                                .setConfirmText("Okay! Thanks")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        //Restart intent
                                                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                        ResetPasswordActivity.this.startActivity(intent);
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
                                        new SweetAlertDialog(ResetPasswordActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("ERROR!")
                                                .setContentText("Failed to change password")
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    pDialog.dismiss();
                                    Toast.makeText(ResetPasswordActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(ResetPasswordActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Restart intent
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
