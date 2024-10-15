package com.cashfree.sdk_sample

import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.upi.CFUPI

data class Config(
    // CFSession Inputs
    val orderID: String = "order_101024392nTK4aL5VTPYQXpp7Rlmni0ZQp7",
    val paymentSessionID: String = "session_E7TvF9s3StIOdu6cyLb2oDACvjUZjMfCxoomZ4XtUVaZVT8P3_XAVxIlB-T7Iba3vsCVSBDxeX6a6DAXGRcVS_cX_BMSTmxQaJsWjp59txVT",
    val environment: CFSession.Environment = CFSession.Environment.SANDBOX,

    //Card Payment Inputs
    val cardNumber: String = "4706130002077527",
    val cardMM: String = "09",
    val cardYY: String = "26",
    val cardHolderName: String = "John Doe",
    val cardCVV: String = "850",

    // Card EMI Inputs
    val bankName: String = "Axis Bank",
    val emiTenure: Int = 3,

    //UPI Collect mode
    val collectMode: CFUPI.Mode = CFUPI.Mode.COLLECT,
    val upiVpa: String = "testfailure@gocash",

    // UPI Intent mode
    val intentMode: CFUPI.Mode = CFUPI.Mode.INTENT,
    val upiAppPackage: String = "com.cashfree.cashfreetestupi",

    //Wallet mode
    val channel: String = "phonepe",
    val phone: String = "9999999999",

    //Pay later mode
    val payLaterChannel: String = "lazypay",

    // Net Banking mode
    val bankCode: Int = 3006
) 