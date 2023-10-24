package com.dinesh.resolib;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewFlipper;

import java.net.URLEncoder;

public class viewpdf extends AppCompatActivity {

    WebView pdfview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpdf);
//         calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        assert getSupportActionBar() != null;
        actionBar.setDisplayHomeAsUpEnabled(true);


        pdfview=findViewById(R.id.viewpdf);
        pdfview.getSettings().setJavaScriptEnabled(true);
        pdfview.getSettings().setLoadWithOverviewMode(true);
        pdfview.getSettings().setUseWideViewPort(true);
        pdfview.getSettings().setBuiltInZoomControls(true);
        pdfview.getSettings().setSupportZoom(true);
        pdfview.getSettings().setDisplayZoomControls(false);


        String filename=getIntent().getStringExtra("filename");
        String fileurl=getIntent().getStringExtra("fileurl");

        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle(filename);
        pd.setMessage("Opening....!!!");


        pdfview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pdfview.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                pd.dismiss();
            }
        });

        String url="";
        try {
            url= URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception ignored)
        {}

        pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);

    }
//    @Override
//    public boolean onSupportNavigateUp() {
////        finish();
//        pdfview.destroy();
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
////                super.onBackPressed();
////                this.finish();
//                Intent in = new Intent(this, DashboardCardView.class);
//                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(in);
////                finish();
////                Intent i=new Intent(this, MainActivity.class);
////                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(i);
////            default:
////                Intent intent = new Intent(this, MainActivity.class);
////                startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }



//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
////            super.onBackPressed();
////            this.finish();
//            onBackPressed();
////            finish();
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
//            this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);

        if (pdfview.canGoBack()) {
            pdfview.goBack();
            Intent intent = new Intent(this, DashboardCardView.class);
            startActivity(intent);
            return true;
        } else {
            Intent intent = new Intent(this, DashboardCardView.class);
            startActivity(intent);
//            finish();
        }
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        if (pdfview.canGoBack()) {
//            pdfview.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

}
