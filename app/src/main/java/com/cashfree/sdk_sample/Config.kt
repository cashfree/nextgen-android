package com.cashfree.sdk_sample

import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.upi.CFUPI

data class Config(
    // CFSession Inputs
    val orderID: String = "order_4303292iMYeI9c7fQnDCXZvt5azb6ezYd",
    val paymentSessionID: String = "session_WrhBTuY14P5r1mbQ7IFUhfQbdxHw6cxsNWGivf_XeOXHHvYsNo7Ci6uJQzQVMUk2AbcM8NKDjeTkREnhXA-YoxC3Zb6gXPLBvk6iZf_XB6H9",
    val environment: CFSession.Environment = CFSession.Environment.SANDBOX,

    //Card Payment Inputs
    val cardNumber: String = "4585340002077590",
    val cardMM: String = "09",
    val cardYY: String = "26",
    val cardHolderName: String = "John Doe",
    val cardCVV: String = "850",

    // Card EMI Inputs
    val bankName: String = "Axis Bank",
    val emiTenure: Int = 3,

    //UPI Collect mode
    val collectMode: CFUPI.Mode = CFUPI.Mode.COLLECT,
    val upiVpa: String = "testsuccess@gocash",

    // UPI Intent mode
    val intentMode: CFUPI.Mode = CFUPI.Mode.INTENT,
    val upiAppPackage: String = "com.google.android.apps.nbu.paisa.user",

    //Wallet mode
    val channel: String = "phonepe",
    val phone: String = "9999999999",

    //Pay later mode
    val payLaterChannel: String = "lazypay",

    // Net Banking mode
    val bankCode: Int = 3003
) 