package com.cashfree.sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.ui.api.CFDropCheckoutPayment;
import com.cashfree.pg.ui.api.CFPaymentComponent;

public class DropCheckoutActivity extends AppCompatActivity  implements  CFCheckoutResponseCallback {

    String orderID = "ORDER_ID";
    String token = "TOKEN";
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
            Toast.makeText(this,"Please set the orderId (DropCheckoutActivity.class,  line: 22)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (token.equals("TOKEN") || TextUtils.isEmpty(token)) {
            Toast.makeText(this,"Please set the token (DropCheckoutActivity.class,  line: 23)", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setOrderToken(token)
                    .setOrderId(orderID)
                    .build();
            CFPaymentComponent cfPaymentComponent = new CFPaymentComponent.CFPaymentComponentBuilder()
                    .add(CFPaymentComponent.CFPaymentModes.CARD)
                    .add(CFPaymentComponent.CFPaymentModes.UPI)
                    .build();
            CFTheme cfTheme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#fc2678")
                    .setNavigationBarTextColor("#ffffff")
                    .setButtonBackgroundColor("#fc2678")
                    .setButtonTextColor("#ffffff")
                    .setPrimaryTextColor("#000000")
                    .setSecondaryTextColor("#000000")
                    .build();
            CFDropCheckoutPayment cfDropCheckoutPayment = new CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                    .setSession(cfSession)
                    //By default all modes are enabled. If you want to restrict the payment modes uncomment the next line
//                        .setCFUIPaymentModes(cfPaymentComponent)
                    .setCFNativeCheckoutUITheme(cfTheme)
                    .build();
            CFPaymentGatewayService gatewayService = CFPaymentGatewayService.getInstance();
            gatewayService.doPayment(DropCheckoutActivity.this, cfDropCheckoutPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }
}