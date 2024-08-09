package com.cashfree.sdk_sample

import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.upi.CFUPI

data class Config(
    // CFSession Inputs
    val orderID: String = "devstudio_95987450",
    val paymentSessionID: String = "session_Diki726QGmNZ3GRLMjM0G5HdSQ3CoaQFsU6qLVY4NRrOr98KtY0MWbMRo0kD_jkDHilmYpXX2Iurk1bHlgUXKiyPOeKXKTc_yJsWb4qm4pXY",
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