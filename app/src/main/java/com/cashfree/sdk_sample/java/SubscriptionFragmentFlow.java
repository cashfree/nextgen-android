package com.cashfree.sdk_sample.java;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
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

public class SubscriptionFragmentFlow extends Fragment {

    private AppCompatButton fragmentFlow, fragmentHelperFlow;

    String subsID = "135285068";
    String subsSessionID = "sub_session_5USUO7xcEIShHekkHLKI9AsJ72an_CVBYR3jCRCEfi3beqvE0BeCzzXxRWbuBirVBKmDAWXMOUc6XC4HtwcA6_GU46Yo4k0payment";
    CFSubscriptionSession.Environment cfEnvironment = CFSubscriptionSession.Environment.PRODUCTION;

    private SubscriptionHelper helper;

    private final CFSubscriptionResponseCallback callback = new CFSubscriptionResponseCallback() {
        @Override
        public void onSubscriptionVerify(CFSubscriptionResponse cfSubscriptionResponse) {
            Log.d("cashfree_subscription", "JAVA Fragment verifyPayment triggered" + cfSubscriptionResponse.getSubscriptionId());
        }

        @Override
        public void onSubscriptionFailure(CFErrorResponse cfErrorResponse) {
            Log.d("cashfree_subscription ", "JAVA Fragment onSubscriptionFailure  " + cfErrorResponse.getMessage());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subs_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentFlow = view.findViewById(R.id.subs_fragment_flow);
        fragmentHelperFlow = view.findViewById(R.id.subs_fragment_helper_flow);
        handleClick();
    }

    private void handleClick() {
        fragmentFlow.setOnClickListener(v -> openSubscriptionInFragment());
        fragmentHelperFlow.setOnClickListener(v -> openSubscriptionWithFragmentHelper());
    }

    private void openSubscriptionInFragment() {
        Log.d("cashfree_subscription", "Subscription Fragment Flow trigger");
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
            CFPaymentGatewayService.getInstance().doSubscriptionPayment(getContext(), cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    private void openSubscriptionWithFragmentHelper() {
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
            Log.d("cashfree_subscription " , "Fragment SubscriptionHelper");
            helper.openSubscriptionCheckout(getContext(), cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

}
