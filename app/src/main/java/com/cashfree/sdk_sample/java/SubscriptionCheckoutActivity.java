package com.cashfree.sdk_sample.java;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFSubscriptionSession;
import com.cashfree.pg.core.api.callback.CFSubscriptionResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.subscription.CFSubscriptionPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.utils.CFSubscriptionResponse;
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme;
import com.cashfree.sdk_sample.R;

public class SubscriptionCheckoutActivity extends AppCompatActivity {
    String subsID = "135285068";
    String subsSessionID = "sub_session_5USUO7xcEIShHekkHLKI9AsJ72an_CVBYR3jCRCEfi3beqvE0BeCzzXxRWbuBirVBKmDAWXMOUc6XC4HtwcA6_GU46Yo4k0payment";
    CFSubscriptionSession.Environment cfEnvironment = CFSubscriptionSession.Environment.PRODUCTION;

    private final CFSubscriptionResponseCallback callback = new CFSubscriptionResponseCallback() {
        @Override
        public void onSubscriptionVerify(CFSubscriptionResponse cfSubscriptionResponse) {
            Log.d("cashfree_subscription", "JAVA verifyPayment triggered"+ cfSubscriptionResponse.getSubscriptionId());
            finish();
        }

        @Override
        public void onSubscriptionFailure(CFErrorResponse cfErrorResponse) {
            Log.d("cashfree_subscription " , "JAVA onSubscriptionFailure  "+ cfErrorResponse.getMessage());
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_checkout);
        doSubscriptionCheckoutPayment();
    }


    public void doSubscriptionCheckoutPayment() {
        if (subsID.equals("ORDER_ID") || TextUtils.isEmpty(subsID)) {
            Toast.makeText(this, "Please set the subsId", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (subsSessionID.equals("SUBS_SESSION_ID") || TextUtils.isEmpty(subsSessionID)) {
            Toast.makeText(this, "Please set the subs_session_id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            CFSubscriptionSession cfSession = new CFSubscriptionSession.CFSubscriptionSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setSubscriptionSessionID(subsSessionID)
                    .setSubscriptionId(subsID)
                    .build();

            CFSubscriptionPayment cfSubscriptionPayment = new CFSubscriptionPayment.CFSubscriptionCheckoutBuilder()
                    .setSubscriptionSession(cfSession)
                    .setSubscriptionUITheme(new CFWebCheckoutTheme.CFWebCheckoutThemeBuilder()
                            .setNavigationBarBackgroundColor("#d11b1b")
                            .build())
                    .build();

            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(callback);

            CFPaymentGatewayService.getInstance().doSubscriptionPayment(SubscriptionCheckoutActivity.this, cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }
}