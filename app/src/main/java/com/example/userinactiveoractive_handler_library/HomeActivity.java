package com.example.userinactiveoractive_handler_library;

import android.os.Bundle;

import com.example.userinactiveoractive.BaseActivity;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable inactivity detection for this activity
        setInactivityDetectionEnabled(true);
    }
}