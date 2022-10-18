package edu.monash.fit3164.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class TitlePageActivity extends AppCompatActivity {

    public final int s = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TitlePageActivity.this, MapsActivity.class);
                TitlePageActivity.this.startActivity(intent);
                TitlePageActivity.this.finish();
            }
        }, s);
    }
}