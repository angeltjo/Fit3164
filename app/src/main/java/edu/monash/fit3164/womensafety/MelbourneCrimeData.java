package edu.monash.fit3164.womensafety;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

//import edu.monash.fit2081.countryinfo.R;


public class MelbourneCrimeData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_wiki);

        getSupportActionBar().setTitle("Melbourne Crime Data Ranking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        final String selectedCountry = getIntent().getStringExtra("country");

        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient()); // to tell android to show within the app
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://angeltjo.github.io/group12/");
//        webView.loadUrl("https://en.wikipedia.org/wiki/" + selectedCountry);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
