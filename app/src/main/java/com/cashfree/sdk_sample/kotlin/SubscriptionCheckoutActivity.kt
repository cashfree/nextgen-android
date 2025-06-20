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

class SubscriptionCheckoutActivity : AppCompatActivity() {

    var subsID: String = "135285068"
    var subsSessionID: String =
        "sub_session_5USUO7xcEIShHekkHLKI9AsJ72an_CVBYR3jCRCEfi3beqvE0BeCzzXxRWbuBirVBKmDAWXMOUc6XC4HtwcA6_GU46Yo4k0payment"
    var cfEnvironment: CFSubscriptionSession.Environment =
        CFSubscriptionSession.Environment.PRODUCTION

    private val callback : CFSubscriptionResponseCallback = object  : CFSubscriptionResponseCallback{
        override fun onSubscriptionVerify(cfSubscriptionResponse: CFSubscriptionResponse?) {
            Log.d("cashfree_subscription", "Kotlin onSubscriptionVerify")
            finish()
        }

        override fun onSubscriptionFailure(cfErrorResponse: CFErrorResponse?) {
            Log.d("cashfree_subscription", "Kotlin onPaymentFailure")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_checkout)
        try {
            doSubscriptionCheckoutPayment()
        } catch (e: CFException) {
            e.printStackTrace()
        }
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

            CFPaymentGatewayService.getInstance().setSubscriptionCheckoutCallback(callback)
            CFPaymentGatewayService.getInstance().doSubscriptionPayment(
                this@SubscriptionCheckoutActivity,
                cfSubscriptionPayment
            )
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}