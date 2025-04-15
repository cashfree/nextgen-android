package com.cashfree.sdk_sample.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.CFSession
import android.os.Bundle
import com.cashfree.sdk_sample.R
import com.cashfree.pg.api.CFPaymentGatewayService
import android.content.Intent
import com.cashfree.pg.core.api.utils.CFErrorResponse
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.cashfree.pg.core.api.CFSession.CFSessionBuilder
import com.cashfree.pg.core.api.CFTheme.CFThemeBuilder
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.ui.api.CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder
import com.cashfree.sdk_sample.Config

class DropCheckoutActivity : AppCompatActivity(), CFCheckoutResponseCallback {
    private val config = Config()
    private val orderID = config.orderID
    private val paymentSessionID = config.paymentSessionID
    private var cfEnvironment = config.environment
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

    private fun doDropCheckoutPayment() {
        Log.d(this@DropCheckoutActivity.localClassName, config.toString())
        if (orderID == "ORDER_ID" || TextUtils.isEmpty(orderID)) {
            Toast.makeText(
                this,
                "Please set the orderId (DropCheckoutActivity.class,  line: 22)",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        if (paymentSessionID == "TOKEN" || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(
                this,
                "Please set the token (DropCheckoutActivity.class,  line: 23)",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }
        try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
//            val cfPaymentComponent = CFPaymentComponentBuilder()
//                .add(CFPaymentComponent.CFPaymentModes.CARD)
//                .add(CFPaymentComponent.CFPaymentModes.UPI)
//                .build()
            val cfTheme = CFThemeBuilder()
                .setNavigationBarBackgroundColor("#006EE1")
                .setNavigationBarTextColor("#ffffff")
                .setButtonBackgroundColor("#006EE1")
                .setButtonTextColor("#ffffff")
                .setPrimaryTextColor("#000000")
                .setSecondaryTextColor("#000000")
                .build()
            val cfDropCheckoutPayment = CFDropCheckoutPaymentBuilder()
                .setSession(cfSession) //By default all modes are enabled. If you want to restrict the payment modes uncomment the next line
                //                        .setCFUIPaymentModes(cfPaymentComponent)
                .setCFNativeCheckoutUITheme(cfTheme)
                .build()
            CFPaymentGatewayService.getInstance()
                .doPayment(this@DropCheckoutActivity, cfDropCheckoutPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}