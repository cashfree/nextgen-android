package com.cashfree.sdk_sample.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cashfree.sdk_sample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startDropCheckout(view: View?) {
        startActivity(Intent(this@MainActivity, DropCheckoutActivity::class.java))
    }

    fun startElementCheckout(view: View?) {
        startActivity(Intent(this@MainActivity, ElementCheckoutActivity::class.java))
    }

    fun startUPIIntentCheckout(view: View?) {
        startActivity(Intent(this@MainActivity, UPIIntentActivity::class.java))
    }

    fun startWebCheckout(view: View?) {
        startActivity(Intent(this@MainActivity, WebCheckoutActivity::class.java))
    }

    fun startSubscriptionCheckout(view: View?) {
        startActivity(Intent(this@MainActivity, SubscriptionCheckoutActivity::class.java))
    }
}