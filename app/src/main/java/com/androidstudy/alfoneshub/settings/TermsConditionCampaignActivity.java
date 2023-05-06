package com.androidstudy.alfoneshub.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



import cn.pedant.SweetAlert.SweetAlertDialog;
import com.androidstudy.alfoneshub.R;

public class TermsConditionCampaignActivity extends AppCompatActivity {
    String campaign_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Terms & Conditions");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        WebView termsWebView = (WebView) findViewById(R.id.web_terms_condition);

        Intent intent = this.getIntent();
        if (null != intent) {
            campaign_name = intent.getStringExtra("campaign_name");
        }

        if(!campaign_name.isEmpty()){

            campaign_name = campaign_name.toLowerCase();
            campaign_name = campaign_name.replace(' ', '_');

            Log.d("campaign_name", campaign_name);
            termsWebView.loadUrl("http://alfonesltd.com/alfonesApp/v2/"+campaign_name+".php");
            // Enable Javascript
            WebSettings webSettings = termsWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            // Force links and redirects to open in the WebView instead of in a browser
            termsWebView.setWebViewClient(new WebViewClient());
        }else{
            new SweetAlertDialog(TermsConditionCampaignActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("ERROR!")
                    .setContentText("Please select an activation first")
                    .show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
