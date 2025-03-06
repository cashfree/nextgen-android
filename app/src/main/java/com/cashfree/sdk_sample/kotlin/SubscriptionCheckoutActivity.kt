package com.cashfree.sdk_sample.kotlin

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSubscriptionSession
import com.cashfree.pg.core.api.CFSubscriptionSession.CFSubscriptionSessionBuilder
import com.cashfree.pg.core.api.callback.CFSubscriptionResponseCallback
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.subscription.CFSubscriptionPayment.CFSubscriptionCheckoutBuilder
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.cashfree.pg.core.api.utils.CFSubscriptionResponse
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme.CFWebCheckoutThemeBuilder
import com.cashfree.sdk_sample.R

class SubscriptionCheckoutActivity  : AppCompatActivity(), CFSubscriptionResponseCallback {

    var subsID: String = "sub_1936672690"
    var subsSessionID: String =
        "sub_session_Axr2QtKpKwh4_dQypuNwtkpy0uZ8wWbwejEv7gYqrsej2jfXDpQPfDfJr999H-ay9ipYx4-2ZIV6MXX-d7vodyuEHVpDDH43yavnmPiK9kTtNosm"
    var cfEnvironment: CFSubscriptionSession.Environment = CFSubscriptionSession.Environment.SANDBOX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_checkout)
        try {
            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(this)
            doSubscriptionCheckoutPayment()
        } catch (e: CFException) {
            e.printStackTrace()
        }
    }

    override fun onSubscriptionVerify(cfSubscriptionResponse: CFSubscriptionResponse?) {
        Log.d("onSubscriptionVerify", "verifyPayment triggered")
        finish()
    }

    override fun onSubscriptionFailure(cfErrorResponse: CFErrorResponse) {
        Log.d("onPaymentFailure $subsID", cfErrorResponse.message)
        finish()
    }

    private fun doSubscriptionCheckoutPayment() {
        if (subsID == "ORDER_ID" || TextUtils.isEmpty(subsID)) {
            Toast.makeText(this, "Please set the subsId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        if (subsSessionID == "SUBS_SESSION_ID" || TextUtils.isEmpty(subsSessionID)) {
            Toast.makeText(this, "Please set the subs_session_id", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        try {
            val cfSession = CFSubscriptionSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setSubscriptionSessionID(subsSessionID)
                .setSubscriptionId(subsID)
                .build()

            val cfSubscriptionPayment = CFSubscriptionCheckoutBuilder()
                .setSubscriptionSession(cfSession)
                .setSubscriptionUITheme(
                    CFWebCheckoutThemeBuilder()
                        .setNavigationBarBackgroundColor("#d11b1b")
                        .build()
                )
                .build()
            val gatewayService = CFPaymentGatewayService.getInstance()
            gatewayService.doSubscriptionPayment(
                this@SubscriptionCheckoutActivity,
                cfSubscriptionPayment
            )
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}