package com.example.userinactiveoractive;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private static final long INACTIVITY_DELAY = 10000; // 10 seconds for testing purposes
    private Dialog mDialog;

    private PopupWindow mPopupWindow;
    private Handler mHandler;
    private Runnable mRunnable;

    private BroadcastReceiver mUserActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resetHandler();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                onUserInactive();
            }
        };
        startHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mUserActivityReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mUserActivityReceiver);
    }

    private void startHandler() {
        mHandler.postDelayed(mRunnable, INACTIVITY_DELAY);
    }

    private void resetHandler() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, INACTIVITY_DELAY);
    }

    protected void onUserInactive() {
        showCustomDialog(this);
    }

    private void showCustomDialog(Activity activity) {
        mDialog = new Dialog(activity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.popup_layout, null);
        mDialog.setContentView(dialogView);

        // Find the dismiss button in the dialog layout
        Button btnDismiss = dialogView.findViewById(R.id.btnDismiss);

        // Set click listener for the dismiss button
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissCustomDialog();
            }
        });

        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void dismissCustomDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        resetHandler(); // Reset the handler after dismissing the dialog
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        resetHandler(); // Reset the handler on any touch event
        return super.dispatchTouchEvent(event);
    }
}
