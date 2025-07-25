package com.cashfree.sdk_sample.java;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

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

    private AppCompatButton activityFlow, activtyHelperFlow, fragmentFlow;
    private LinearLayoutCompat llContainer;

    private SubscriptionHelper helper;
    private Fragment subscriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs_checkout);

        activityFlow = findViewById(R.id.subs_activity_flow);
        activtyHelperFlow = findViewById(R.id.subs_activity_helper_flow);
        fragmentFlow = findViewById(R.id.subs_fragment);
        llContainer = findViewById(R.id.ll_container);
        handleClick();
    }

    private void handleClick() {
        activityFlow.setOnClickListener(v -> openActivityFlow());
        activtyHelperFlow.setOnClickListener(v -> openActivityFlowHelper());
        fragmentFlow.setOnClickListener(v -> {
            llContainer.setVisibility(View.INVISIBLE);
            openFragmentFlow();
        });
    }

    private final CFSubscriptionResponseCallback callback = new CFSubscriptionResponseCallback() {
        @Override
        public void onSubscriptionVerify(CFSubscriptionResponse cfSubscriptionResponse) {
            Log.d("cashfree_subscription", "JAVA verifyPayment triggered" + cfSubscriptionResponse.getSubscriptionId());
            finish();
        }

        @Override
        public void onSubscriptionFailure(CFErrorResponse cfErrorResponse) {
            Log.d("cashfree_subscription ", "JAVA onSubscriptionFailure  " + cfErrorResponse.getMessage());
            finish();
        }
    };

    private void openActivityFlow() {
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
        Log.d("cashfree_subscription", "Subscription Activity Flow trigger");
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

    private void openActivityFlowHelper() {
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

            helper = new SubscriptionHelper();
            Log.d("cashfree_subscription " , "Activity SubscriptionHelper");
            helper.openSubscriptionCheckout(this, cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    private void openFragmentFlow() {
        subscriptionFragment = new SubscriptionFragmentFlow();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, subscriptionFragment)
                .commit();
    }

}