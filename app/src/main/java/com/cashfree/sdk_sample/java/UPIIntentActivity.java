package com.cashfree.sdk_sample.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.upi.intent.CFIntentTheme;
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckout;
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckoutPayment;
import com.cashfree.sdk_sample.Config;
import com.cashfree.sdk_sample.R;

public class UPIIntentActivity extends AppCompatActivity implements CFCheckoutResponseCallback {
    Config config = new Config();
    String orderID = config.getOrderID();
    String paymentSessionID = config.getPaymentSessionID();
    CFSession.Environment cfEnvironment = config.getEnvironment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_checkout);
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
            doDropCheckoutPayment();
        } catch (CFException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentVerify(String orderID) {
        Log.e("onPaymentVerify", "verifyPayment triggered");
        Intent intent = new Intent();
        intent.putExtra("orderID", orderID);
        intent.putExtra("result", "VerifyPayment");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        Log.e("onPaymentFailure " + orderID, cfErrorResponse.getMessage());
        Intent intent = new Intent();
        intent.putExtra("orderID", orderID);
        intent.putExtra("result", "PaymentFailure");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void doDropCheckoutPayment() {
        if (orderID.equals("ORDER_ID") || TextUtils.isEmpty(orderID)) {
            Toast.makeText(this,"Please set the orderId (DropCheckoutActivity.class,  line: 26)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (paymentSessionID.equals("TOKEN") || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(this,"Please set the token (DropCheckoutActivity.class,  line: 27)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
            CFIntentTheme cfTheme = new CFIntentTheme.CFIntentThemeBuilder()
                    .setPrimaryTextColor("#000000")
                    .setBackgroundColor("#FFFFFF")
                    .build();
            CFUPIIntentCheckout cfupiIntentCheckout = new CFUPIIntentCheckout.CFUPIIntentBuilder()
                    // Use either the enum or the application package names to order the UPI apps as per your needed
                    // Remove both if you want to use the default order which cashfree provides based on the popularity
                    // NOTE - only one is needed setOrder or setOrderUsingPackageName
//                                        .setOrderUsingPackageName(Arrays.asList("com.dreamplug.androidapp", "in.org.npci.upiapp"))
//                                        .setOrder(Arrays.asList(CFUPIIntentCheckout.CFUPIApps.BHIM, CFUPIIntentCheckout.CFUPIApps.PHONEPE))
                    .build();
            CFUPIIntentCheckoutPayment cfupiIntentCheckoutPayment = new CFUPIIntentCheckoutPayment.CFUPIIntentPaymentBuilder()
                    .setSession(cfSession)
                    .setCfUPIIntentCheckout(cfupiIntentCheckout)
                    .setCfIntentTheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(UPIIntentActivity.this, cfupiIntentCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }
}