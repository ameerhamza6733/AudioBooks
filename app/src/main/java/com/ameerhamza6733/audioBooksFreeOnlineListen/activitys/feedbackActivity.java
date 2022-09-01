package com.ameerhamza6733.audioBooksFreeOnlineListen.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ameerhamza6733.audioBooksFreeOnlineListen.R;



public class feedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
