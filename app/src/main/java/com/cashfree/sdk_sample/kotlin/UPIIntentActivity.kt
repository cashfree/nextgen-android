package com.cashfree.sdk_sample.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import android.os.Bundle
import com.cashfree.sdk_sample.R
import com.cashfree.pg.api.CFPaymentGatewayService
import android.content.Intent
import com.cashfree.pg.core.api.utils.CFErrorResponse
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.cashfree.pg.core.api.CFSession.CFSessionBuilder
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.ui.api.upi.intent.CFIntentTheme.CFIntentThemeBuilder
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckout.CFUPIIntentBuilder
import com.cashfree.pg.ui.api.upi.intent.CFUPIIntentCheckoutPayment.CFUPIIntentPaymentBuilder
import com.cashfree.sdk_sample.Config

class UPIIntentActivity : AppCompatActivity(), CFCheckoutResponseCallback {
    val config = Config()
    var orderID = config.orderID
    var paymentSessionID = config.paymentSessionID
    var cfEnvironment = config.environment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_checkout)
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            doUPIIntentCheckoutPayment()
        } catch (e: CFException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentVerify(orderID: String) {
        Log.e("onPaymentVerify", "verifyPayment triggered")
        val intent = Intent()
        intent.putExtra("orderID", orderID)
        intent.putExtra("result", "VerifyPayment")
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e("onPaymentFailure $orderID", cfErrorResponse.message)
        val intent = Intent()
        intent.putExtra("orderID", orderID)
        intent.putExtra("result", "PaymentFailure")
        setResult(RESULT_OK, intent)
        finish()
    }

    fun doUPIIntentCheckoutPayment() {
        if (orderID == "ORDER_ID" || TextUtils.isEmpty(orderID)) {
            Toast.makeText(
                this,
                "Please set the orderId",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        if (paymentSessionID == "TOKEN" || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(
                this,
                "Please set the paymentSessionId",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        try {
            val cfSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfTheme = CFIntentThemeBuilder()
                .setPrimaryTextColor("#000000")
                .setBackgroundColor("#FFFFFF")
                .build()
            val cfupiIntentCheckout =
                CFUPIIntentBuilder() // Use either the enum or the application package names to order the UPI apps as per your needed
                    // Remove both if you want to use the default order which cashfree provides based on the popularity
                    // NOTE - only one is needed setOrder or setOrderUsingPackageName
                    //                                        .setOrderUsingPackageName(Arrays.asList("com.dreamplug.androidapp", "in.org.npci.upiapp"))
                    //                                        .setOrder(Arrays.asList(CFUPIIntentCheckout.CFUPIApps.BHIM, CFUPIIntentCheckout.CFUPIApps.PHONEPE))
                    .build()
            val cfupiIntentCheckoutPayment = CFUPIIntentPaymentBuilder()
                .setSession(cfSession)
                .setCfUPIIntentCheckout(cfupiIntentCheckout)
                .setCfIntentTheme(cfTheme)
                .build()
            CFPaymentGatewayService.getInstance()
                .doPayment(this@UPIIntentActivity, cfupiIntentCheckoutPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}