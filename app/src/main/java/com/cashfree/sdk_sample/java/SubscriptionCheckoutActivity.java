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

public class SubscriptionCheckoutActivity extends AppCompatActivity implements CFSubscriptionResponseCallback {
    String subsID = "sub_1936672690";
    String subsSessionID = "sub_session_Axr2QtKpKwh4_dQypuNwtkpy0uZ8wWbwejEv7gYqrsej2jfXDpQPfDfJr999H-ay9ipYx4-2ZIV6MXX-d7vodyuEHVpDDH43yavnmPiK9kTtNosm";
    CFSubscriptionSession.Environment cfEnvironment = CFSubscriptionSession.Environment.SANDBOX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_checkout);
        try {
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(this);
            doSubscriptionCheckoutPayment();
        } catch (CFException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubscriptionVerify(CFSubscriptionResponse cfSubscriptionResponse) {
        Log.d("onSubscriptionVerify", "verifyPayment triggered");
        finish();
    }

    @Override
    public void onSubscriptionFailure(CFErrorResponse cfErrorResponse) {
        Log.d("onPaymentFailure " + subsID, cfErrorResponse.getMessage());
        finish();
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
            CFPaymentGatewayService.getInstance().doSubscriptionPayment(SubscriptionCheckoutActivity.this, cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }
}