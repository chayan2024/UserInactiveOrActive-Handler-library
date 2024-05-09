package com.example.userinactiveoractive;

import android.app.Activity;
import android.app.Dialog;
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

    private static final long INACTIVITY_DELAY = 10000; // 2 seconds for testing purposes
    private Dialog mDialog;

    private PopupWindow mPopupWindow;
    private Handler mHandler;
    private Runnable mRunnable;
    private long mTime = 2000;

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
    public boolean onTouchEvent(MotionEvent event) {
        stopHandler();
        startHandler();
        return super.onTouchEvent(event);
    }

    private void startHandler() {
        mHandler.postDelayed(mRunnable, INACTIVITY_DELAY);
    }

    private void stopHandler() {
        mHandler.removeCallbacks(mRunnable);
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
                startHandler();
            }
        });

        mDialog.setCancelable(false);
        mDialog.show();
    }


    private void dismissCustomDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }}
