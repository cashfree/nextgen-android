package com.cashfree.sdk_sample.java;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.callback.CFSubscriptionResponseCallback;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.subscription.CFSubscriptionPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.utils.CFSubscriptionResponse;

public class SubscriptionHelper implements CFSubscriptionResponseCallback{

    @Override
    public void onSubscriptionVerify(CFSubscriptionResponse cfSubscriptionResponse) {
        Log.d("cashfree_subscription", "JAVA SubscriptionHelper verifyPayment triggered"+ cfSubscriptionResponse.getSubscriptionId());
    }

    @Override
    public void onSubscriptionFailure(CFErrorResponse cfErrorResponse) {
        Log.d("cashfree_subscription " , "JAVA SubscriptionHelper onSubscriptionFailure  "+ cfErrorResponse.getMessage());
    }

    public void openSubscriptionCheckout(Context context, CFSubscriptionPayment cfSubscriptionPayment){
        Log.d("cashfree_subscription " , "JAVA SubscriptionHelper");
        try {
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(this);
            CFPaymentGatewayService.getInstance().doSubscriptionPayment(context, cfSubscriptionPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }

    }
}
