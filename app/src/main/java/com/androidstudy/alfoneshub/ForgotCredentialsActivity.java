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

public class ForgotCredentialsActivity extends AppCompatActivity {
    Button btnRetrieve;
    TextView btnLogin;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_credentials);

        btnRetrieve = (Button)findViewById(R.id.btn_retrieve);
        btnLogin = (TextView)findViewById(R.id.btn_discard_login);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);

        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input_email = editTextEmail.getText().toString();
                // Check if user provided expense
                if (TextUtils.isEmpty(input_email)) {
                    editTextEmail.setError("Enter Email");
                    return;
                }

                final SweetAlertDialog pDialog = new SweetAlertDialog(ForgotCredentialsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Checking account");
                pDialog.setCancelable(false);
                pDialog.show();
                AndroidNetworking.post(URLs.FORGOT_CREDENTIALS_URL)
                        .addBodyParameter("email", input_email)
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

                                        Intent intent = new Intent(ForgotCredentialsActivity.this, CheckResetCodeActivity.class);
                                        intent.putExtra("email",input_email);
                                        ForgotCredentialsActivity.this.startActivity(intent);
                                        finish();

                                    }else{
                                        new SweetAlertDialog(ForgotCredentialsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("ERROR!")
                                                .setContentText("Account does not exist")
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    pDialog.dismiss();
                                    Toast.makeText(ForgotCredentialsActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                pDialog.dismiss();
                                Log.d("ERROR",error.toString());
                                Toast.makeText(ForgotCredentialsActivity.this, "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Restart intent
                Intent intent = new Intent(ForgotCredentialsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



}
