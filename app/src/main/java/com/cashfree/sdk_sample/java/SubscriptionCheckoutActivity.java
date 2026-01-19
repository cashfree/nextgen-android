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
import com.cashfree.pg.core.api.CFCorePaymentGatewayService;
import com.cashfree.pg.core.api.CFSubscriptionSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.callback.CFSubscriptionResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.subscription.CFSubscriptionPayment;
import com.cashfree.pg.core.api.subscription.card.CFSubsCard;
import com.cashfree.pg.core.api.subscription.card.CFSubsCardPayment;
import com.cashfree.pg.core.api.subscription.enach.CFSubsNetBanking;
import com.cashfree.pg.core.api.subscription.enach.CFSubsNetBankingPayment;
import com.cashfree.pg.core.api.subscription.upi.CFSubsUpi;
import com.cashfree.pg.core.api.subscription.upi.CFSubsUpiPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.utils.CFSubscriptionResponse;
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme;
import com.cashfree.sdk_sample.R;

public class SubscriptionCheckoutActivity extends AppCompatActivity {
    String subsID = "devstudio_subs_7418928130455926161";
    String subsSessionID = "sub_session_OESkkngNmnRUUPRKDMb-vEyK99yQBf03VuwSR79rIT5MGebGVw38JWgHShxXGkLD9E-bRsUXTspYuCXI-eHI5LKPjXc2U2Cq7BesykfOphjP2r0FxLjLeNvrde3C8XUpayment";
    CFSubscriptionSession.Environment cfEnvironment = CFSubscriptionSession.Environment.SANDBOX;

    private AppCompatButton activityFlow, activtyHelperFlow, fragmentFlow;
    private AppCompatButton cardElement, upiElement, enachElement;
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
        cardElement = findViewById(R.id.subs_element_card);
        upiElement = findViewById(R.id.subs_element_upi);
        enachElement = findViewById(R.id.subs_element_enach);
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

        cardElement.setOnClickListener(v -> openCardElementFlow());
        upiElement.setOnClickListener(v -> openUpiElementFlow());
        enachElement.setOnClickListener(v -> openEnachElementFlow());
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
            Log.d("cashfree_subscription ", "Activity SubscriptionHelper");
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

    private void openCardElementFlow() {
        try {
            CFSubscriptionSession cfSession = new CFSubscriptionSession.CFSubscriptionSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setSubscriptionSessionID(subsSessionID)
                    .setSubscriptionId(subsID)
                    .build();
            CFSubsCard cfCard = new CFSubsCard.CFSubsCardBuilder()
                    .setCardHolderName("Kishan")
                    .setCardNumber("4011381307299555")
                    .setCardExpiryMonth("08")
                    .setCardExpiryYear("29")
                    .setCVV("950")
                    .build();
            CFSubsCardPayment cfCardPayment = new CFSubsCardPayment.CFSubsCardPaymentBuilder()
                    .setSubscriptionSession(cfSession)
                    .setSubsCard(cfCard)
                    .build();
            CFTheme theme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#6A2222")
                    .setNavigationBarTextColor("#FFFFFF")
                    .build();

            cfCardPayment.setTheme(theme);
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(callback);
            CFCorePaymentGatewayService.getInstance().doSubscriptionPayment(this, cfCardPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }

    }

    private void openUpiElementFlow() {
        try {
            CFSubscriptionSession cfSession = new CFSubscriptionSession.CFSubscriptionSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setSubscriptionSessionID(subsSessionID)
                    .setSubscriptionId(subsID)
                    .build();

            CFSubsUpi cfSubsUpi = new CFSubsUpi.CFSubsUpiBuilder()
                    .setMode(CFSubsUpi.Mode.INTENT)
                    .setUPIID("com.phonepe.app")
                    .build();
            CFSubsUpiPayment cfSubsUpiPayment = new CFSubsUpiPayment.CFSubsUpiPaymentBuilder()
                    .setSubscriptionSession(cfSession)
                    .setSubsUpi(cfSubsUpi)
                    .build();
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(callback);
            CFCorePaymentGatewayService.getInstance().doSubscriptionPayment(this, cfSubsUpiPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }

    }

    private void openEnachElementFlow() {
        try {
            CFSubscriptionSession cfSession = new CFSubscriptionSession.CFSubscriptionSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setSubscriptionSessionID(subsSessionID)
                    .setSubscriptionId(subsID)
                    .build();
            CFSubsNetBanking subsNetBanking = new CFSubsNetBanking.CFSubsNetBankingBuilder()
                    .setAccountHolderName("Harshad Mallya")
                    .setAccountBankCode("SBIN")
                    .setAccountNumber("9876543210")
                    .setAccountType("SAVINGS")
                    .build();
            CFSubsNetBankingPayment cfSubsNetBankingPayment = new CFSubsNetBankingPayment.CFNetBankingPaymentBuilder()
                    .setSubscriptionSession(cfSession)
                    .setCfSubsNetBanking(subsNetBanking)
                    .build();
            CFTheme theme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#6A2222")
                    .setNavigationBarTextColor("#FFFFFF")
                    .build();

            cfSubsNetBankingPayment.setTheme(theme);
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(callback);
            CFCorePaymentGatewayService.getInstance().doSubscriptionPayment(this, cfSubsNetBankingPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

}