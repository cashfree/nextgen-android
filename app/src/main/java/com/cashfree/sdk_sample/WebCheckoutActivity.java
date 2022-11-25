package com.cashfree.sdk_sample;

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
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment;
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme;

public class WebCheckoutActivity extends AppCompatActivity implements CFCheckoutResponseCallback {

    String orderID = "ORDER_ID";
    String paymentSessionID = "PAYMENT_SESSION_ID";
    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

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
            Toast.makeText(this,"Please set the orderId (DropCheckoutActivity.class,  line: 21)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (paymentSessionID.equals("PAYMENT_SESSION_ID") || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(this,"Please set the payment_session_id (webCheckoutActivity.class,  line: 22)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
            CFWebCheckoutTheme cfTheme = new CFWebCheckoutTheme.CFWebCheckoutThemeBuilder()
                    .setNavigationBarBackgroundColor("#000000")
                    .setNavigationBarTextColor("#FFFFFF")
                    .build();
            CFWebCheckoutPayment cfWebCheckoutPayment = new CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    .setCFWebCheckoutUITheme(new CFWebCheckoutTheme.CFWebCheckoutThemeBuilder().build())
                    .build();
            CFPaymentGatewayService.getInstance().doPayment(this, cfWebCheckoutPayment);
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(WebCheckoutActivity.this, cfWebCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }
}