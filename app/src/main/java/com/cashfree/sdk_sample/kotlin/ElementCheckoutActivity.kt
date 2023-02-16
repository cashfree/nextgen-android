package com.cashfree.sdk_sample.kotlin

import androidx.appcompat.app.AppCompatActivity
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.upi.CFUPI
import android.os.Bundle
import com.cashfree.sdk_sample.R
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.utils.CFErrorResponse
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.cashfree.pg.core.api.CFSession.CFSessionBuilder
import com.cashfree.pg.core.api.card.CFCard.CFCardBuilder
import com.cashfree.pg.core.api.card.CFCardPayment.CFCardPaymentBuilder
import com.cashfree.pg.core.api.CFCorePaymentGatewayService
import com.cashfree.pg.core.api.emicard.CFEMICard.CFEMICardBuilder
import com.cashfree.pg.core.api.emicard.CFEMICardPayment.CFEMICardPaymentBuilder
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.netbanking.CFNetBanking.CFNetBankingBuilder
import com.cashfree.pg.core.api.netbanking.CFNetBankingPayment.CFNetBankingPaymentBuilder
import kotlin.Throws
import com.cashfree.pg.core.api.wallet.CFWallet.CFWalletBuilder
import com.cashfree.pg.core.api.wallet.CFWalletPayment.CFWalletPaymentBuilder
import com.cashfree.pg.core.api.paylater.CFPayLater.CFPayLaterBuilder
import com.cashfree.pg.core.api.paylater.CFPayLaterPayment.CFPayLaterPaymentBuilder
import com.cashfree.pg.core.api.upi.CFUPI.CFUPIBuilder
import com.cashfree.pg.core.api.upi.CFUPIPayment.CFUPIPaymentBuilder
import com.cashfree.sdk_sample.Config
import java.lang.IllegalArgumentException

class ElementCheckoutActivity : AppCompatActivity(), CFCheckoutResponseCallback {
    // Go to https://docs.cashfree.com/docs/31-initiate-payment-native-checkout for the documentation
    private val config = Config()
    private val orderID = config.orderID
    private val paymentSessionID = config.paymentSessionID
    private val cfEnvironment = config.environment

    //Card Payment Inputs
    private val cardNumber = config.cardNumber
    private val cardMM = config.cardMM
    private val cardYY = config.cardYY
    private val cardHolderName = config.cardHolderName
    private val cardCVV = config.cardCVV

    // Card EMI Inputs
    private val bankName = config.bankName
    private val emiTenure = config.emiTenure

    //UPI Collect mode
    private val collectMode = config.collectMode
    private val upiVpa = config.upiVpa

    // UPI Intent mode
    private val intentMode = config.intentMode
    private val upiAppPackage = config.upiAppPackage

    //Wallet mode
    private val channel = config.channel
    private val phone = config.phone

    //Pay later mode
    private val payLaterChannel = config.payLaterChannel

    // Net Banking mode
    private val bankCode = config.bankCode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_checkout)
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
        } catch (e: CFException) {
            e.printStackTrace()
        }
    }

    override fun onPaymentVerify(orderID: String) {
        Log.e("onPaymentVerify", "verifyPayment triggered")
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e("onPaymentFailure $orderID", cfErrorResponse.message)
    }

    fun doCardPayment(view: View?) {
        if (orderID == "ORDER_ID" || TextUtils.isEmpty(orderID)) {
            Toast.makeText(
                this,
                "Please set the orderId (DropCheckoutActivity.class,  line: 32)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (paymentSessionID == "TOKEN" || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(
                this,
                "Please set the token (DropCheckoutActivity.class,  line: 33)",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfCard = CFCardBuilder()
                .setCardHolderName(cardHolderName)
                .setCardNumber(cardNumber)
                .setCardExpiryMonth(cardMM)
                .setCardExpiryYear(cardYY)
                .setCVV(cardCVV)
                .build()
            val cfCardPayment = CFCardPaymentBuilder()
                .setSession(cfSession)
                .setCard(cfCard)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfCardPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    fun hasValidCardInputs(): Boolean {
        if (cardNumber.length != 16) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Number")
            return false
        }
        if (cardHolderName.length < 3) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Holder Name")
            return false
        }
        if (cardMM.length != 2) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Expiry Month in MM format")
            return false
        }
        if (cardYY.length != 2) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Expiry Year in YY format")
            return false
        }
        if (cardCVV.length < 3 || cardCVV.length > 4) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card CVV")
            return false
        }
        return true
    }

    fun doCardEMIPayment(view: View?) {
        if (hasValidCardInputs()) try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfCard = CFEMICardBuilder()
                .setCardHolderName(cardHolderName)
                .setCardNumber(cardNumber)
                .setCardExpiryMonth(cardMM)
                .setCardExpiryYear(cardYY)
                .setCVV(cardCVV)
                .setBankName(bankName)
                .setEMITenure(emiTenure)
                .build()
            val cfCardPayment = CFEMICardPaymentBuilder()
                .setSession(cfSession)
                .setCard(cfCard)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfCardPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    fun doNetBankingPayment(view: View?) {
        if (hasValidNetBankingInputs()) try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfNetBanking = CFNetBankingBuilder()
                .setBankCode(bankCode)
                .build()
            val cfNetBankingPayment = CFNetBankingPaymentBuilder()
                .setSession(cfSession)
                .setCfNetBanking(cfNetBanking)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfNetBankingPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    @Throws(IllegalArgumentException::class)
    fun hasValidNetBankingInputs(): Boolean {
        if (bankCode.toString().length != 4) {
            Log.e("NET_BANKING VALIDATION", "Enter a Valid 4 digit Bank Code")
            return false
        }
        return true
    }

    fun doWalletPayment(view: View?) {
        if (hasValidWalletPaymentInputs()) try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfWallet = CFWalletBuilder()
                .setProvider(channel)
                .setPhone(phone)
                .build()
            val cfWalletPayment = CFWalletPaymentBuilder()
                .setSession(cfSession)
                .setCfWallet(cfWallet)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfWalletPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    fun doPayLaterPayment(view: View?) {
        if (hasValidWalletPaymentInputs()) try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfPayLater = CFPayLaterBuilder()
                .setProvider(payLaterChannel)
                .setPhone(phone)
                .build()
            val cfPayLaterPayment = CFPayLaterPaymentBuilder()
                .setSession(cfSession)
                .setCfPayLater(cfPayLater)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfPayLaterPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    @Throws(IllegalArgumentException::class)
    fun hasValidWalletPaymentInputs(): Boolean {
        if (channel.length < 4) {
            Log.e("WALLET INPUT VALIDATION", "Enter a Valid channel")
            return false
        }
        if (phone.length < 10) {
            Log.e("WALLET INPUT VALIDATION", "Enter a Valid phone number")
            return false
        }
        return true
    }

    fun doUPICollectPayment(view: View?) {
        initiatePayment(collectMode, upiVpa)
    }

    fun doUPIIntentPayment(view: View?) {
        initiatePayment(intentMode, upiAppPackage)
    }

    private fun initiatePayment(mode: CFUPI.Mode, id: String) {
        try {
            val cfSession: CFSession = CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()
            val cfupi = CFUPIBuilder()
                .setMode(mode)
                .setUPIID(id)
                .build()
            val cfupiPayment = CFUPIPaymentBuilder()
                .setSession(cfSession)
                .setCfUPI(cfupi)
                .build()
            CFCorePaymentGatewayService.getInstance()
                .doPayment(this@ElementCheckoutActivity, cfupiPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }
}