package com.androidstudy.alfoneshub.payslips;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidstudy.alfoneshub.R;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PayslipDetailsActivity extends AppCompatActivity {

    String id, payslip_id, admin_id, name, employee_no, gross_salary, nssf_contibution, taxable_pay, personal_relief, insurance_relief, paye, nhif_contribution, net_pay, tax_rate, month, year, created_at, updated_at;
    TextView textViewPayslipNo,textViewName, textViewEmployeeNo, textViewGrossSalary,
            textViewNssfContribution, textViewTaxablePay, textViewPersonalRelief,
            textViewInsuranceRelief, textViewPaye, textViewNhifContibution, textViewNetPay;
    Button buttonDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_details);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();
        if (null != intent) {
            id = intent.getStringExtra("id");
            payslip_id = intent.getStringExtra("payslip_id");
            admin_id = intent.getStringExtra("admin_id");
            name = intent.getStringExtra("name");
            employee_no = intent.getStringExtra("employee_no");
            gross_salary = intent.getStringExtra("gross_salary");
            nssf_contibution = intent.getStringExtra("nssf_contibution");
            taxable_pay = intent.getStringExtra("taxable_pay");
            personal_relief = intent.getStringExtra("personal_relief");
            insurance_relief = intent.getStringExtra("insurance_relief");
            paye = intent.getStringExtra("paye");
            nhif_contribution = intent.getStringExtra("nhif_contribution");
            net_pay = intent.getStringExtra("net_pay");
            tax_rate = intent.getStringExtra("tax_rate");
            month = intent.getStringExtra("month");
            year = intent.getStringExtra("year");
            created_at = intent.getStringExtra("created_at");
            updated_at = intent.getStringExtra("updated_at");

            getSupportActionBar().setTitle("Payslip #" + payslip_id);

        }

        textViewPayslipNo = (TextView)findViewById(R.id.text_view_payslip_no);
        textViewPayslipNo.setText("Payslip #" +payslip_id);

        textViewName = (TextView)findViewById(R.id.text_view_name);
        textViewName.setText(name);

        textViewEmployeeNo = (TextView)findViewById(R.id.text_view_employee_no);
        textViewEmployeeNo.setText(employee_no);

        textViewGrossSalary = (TextView)findViewById(R.id.text_view_gross_salary);
        textViewGrossSalary.setText(gross_salary);

        textViewNssfContribution = (TextView)findViewById(R.id.text_view_nssf_contribution);
        textViewNssfContribution.setText(nssf_contibution);

        textViewTaxablePay = (TextView)findViewById(R.id.text_view_taxable_pay);
        textViewTaxablePay.setText(taxable_pay);

        textViewPersonalRelief = (TextView)findViewById(R.id.text_view_personal_relief);
        textViewPersonalRelief.setText(personal_relief);

        textViewInsuranceRelief = (TextView)findViewById(R.id.text_view_insurance_relief);
        textViewInsuranceRelief.setText(insurance_relief);

        textViewPaye = (TextView)findViewById(R.id.text_view_paye);
        textViewPaye.setText(paye);

        textViewNhifContibution = (TextView)findViewById(R.id.text_view_nhif_contribution);
        textViewNhifContibution.setText(nhif_contribution);

        textViewNetPay = (TextView)findViewById(R.id.text_view_net_pay);
        textViewNetPay.setText(net_pay);

        buttonDownload = (Button)findViewById(R.id.button_payslip);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(PayslipDetailsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#1a365d"));
                pDialog.setTitleText("Loading details...");
                pDialog.setCancelable(false);
                pDialog.show();

                File mydir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "AlfonesHub");
                if (!mydir.exists())
                {
                    mydir.mkdirs();
                }

                String file_name = name+"-"+month+"-"+year+".pdf";
                file_name = file_name.replaceAll("\\s+", "");
                String url = "http://159.89.97.75/api/file-get-payslip/"+file_name;
                final String path_name = mydir.getAbsolutePath();

                final String finalFile_name = file_name;
                AndroidNetworking.download(url, path_name,file_name)
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {

                                Log.d("Response", "bytesUploaded : " + bytesDownloaded + " totalBytes : " + totalBytes);
                                Log.d("Response", "setUploadProgressListener isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));

                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                pDialog.dismiss();

                                new SweetAlertDialog(PayslipDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("SUCCESS!")
                                        .setContentText("Payslip downloaded")
                                        .show();

                                File file = new File(path_name+"/"+ finalFile_name);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);



                            }
                            @Override
                            public void onError(ANError error) {
                                Log.d("ERROR", error.toString());
                                new SweetAlertDialog(PayslipDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("ERROR!")
                                        .setContentText("Payslip not downloaded")
                                        .show();
                            }
                        });
            }
        });
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
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
