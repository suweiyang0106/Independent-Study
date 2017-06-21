package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import info.androidhive.loginandregistration.R;

public class Foodmateria extends AppCompatActivity {

    String url="https://fatraceschool.moe.gov.tw/frontend/index.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmateria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       /* WebView webView=(WebView)findViewById(R.id.foodmateria);
      //  webView.setWebViewClient(new WebViewClient());
       WebSettings websettings=webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://fatraceschool.moe.gov.tw/frontend/");*/
        Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fatraceschool.moe.gov.tw/frontend/"));
        startActivity(ie);
    }

}
