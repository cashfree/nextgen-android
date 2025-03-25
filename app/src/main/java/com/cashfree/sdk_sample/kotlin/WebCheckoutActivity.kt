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
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme.CFWebCheckoutThemeBuilder
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder
import com.cashfree.sdk_sample.Config

class WebCheckoutActivity : AppCompatActivity(), CFCheckoutResponseCallback {
    val config = Config()
    var orderID = config.orderID
    var paymentSessionID = config.paymentSessionID
    var cfEnvironment = config.environment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drop_checkout)
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            doDropCheckoutPayment()
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

    fun doDropCheckoutPayment() {
        if (orderID == "ORDER_ID" || TextUtils.isEmpty(orderID)) {
            Toast.makeText(
                this,
                "Please set the orderId",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        if (paymentSessionID == "PAYMENT_SESSION_ID" || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(
                this,
                "Please set the payment_session_id ",
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
            val cfTheme = CFWebCheckoutThemeBuilder()
                .setNavigationBarBackgroundColor("#000000")
                .setNavigationBarTextColor("#FFFFFF")
                .build()
            val cfWebCheckoutPayment = CFWebCheckoutPaymentBuilder()
                .setSession(cfSession)
                .setCFWebCheckoutUITheme(cfTheme)
                .build()
            CFPaymentGatewayService.getInstance()
                .doPayment(this@WebCheckoutActivity, cfWebCheckoutPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}