package com.example.winme.mylauncher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextClock;
import android.widget.TextView;

public class TimeActivity extends AppCompatActivity {
    private TextClock textClock;
    private TextView timezone_text;

    private String timezone = null;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            finish();
            handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        textClock = (TextClock)findViewById(R.id.textClock);
        timezone_text = (TextView)findViewById(R.id.Timezone_text);
        Intent intent = getIntent();
        timezone = intent.getStringExtra("timezone");
        textClock.setTimeZone(timezone);
        timezone_text.setText(timezone);
        handler.postDelayed(runnable,4000);
    }

}
