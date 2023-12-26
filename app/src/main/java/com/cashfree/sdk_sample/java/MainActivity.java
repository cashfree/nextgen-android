package com.cashfree.sdk_sample.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cashfree.sdk_sample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDropCheckout(View view) {
        startActivity(new Intent(MainActivity.this, DropCheckoutActivity.class));
    }

    public void startElementCheckout(View view) {
        startActivity(new Intent(MainActivity.this, ElementCheckoutActivity.class));
    }

    public void startUPIIntentCheckout(View view) {
        startActivity(new Intent(MainActivity.this, UPIIntentActivity.class));
    }

    public void startWebCheckout(View view) {
        startActivity(new Intent(MainActivity.this, WebCheckoutActivity.class));
    }
}
