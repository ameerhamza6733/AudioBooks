package com.ameerhamza6733.businessaudiobook.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.businessaudiobook.R;

import com.webianks.easy_feedback.EasyFeedback;

public class feedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        new EasyFeedback.Builder(this)
                .withEmail("develpore2017@gmail.com")
                .withSystemInfo()
                .build()
                .start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
