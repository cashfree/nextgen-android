package com.cashfree.sdk_sample.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashfree.sdk_sample.compose.ui.theme.CashfreeAndroidSampleTheme
import com.cashfree.sdk_sample.kotlin.ElementCheckoutActivity
import com.cashfree.sdk_sample.kotlin.UPIIntentActivity
import com.cashfree.sdk_sample.kotlin.WebCheckoutActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            CashfreeAndroidSampleTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(
                    {
                        context.startActivity(Intent(context, DropCheckoutActivity::class.java))
                    },
                    {
                        context.startActivity(Intent(context, ElementCheckoutActivity::class.java))
                    },
                    {
                        context.startActivity(Intent(context, UPIIntentActivity::class.java))
                    },
                    {
                        context.startActivity(Intent(context, WebCheckoutActivity::class.java))
                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen(
        onDropClick: () -> Unit = {},
        onElementClick: () -> Unit = {},
        onUPIClick: () -> Unit = {},
        onWebClick: () -> Unit = {}
    ) {
        // A surface container using the 'background' color from the theme
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Button(
                    onClick = onDropClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Drop Checkout")
                }
                Button(
                    onClick = onElementClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Element Checkout")
                }
                Button(
                    onClick = onUPIClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "UPI Intent Checkout")
                }
                Button(
                    onClick = onWebClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Web Checkout")
                }
            }
        }
    }

    @Preview
    @Composable
    fun SampleMainView() {
        CashfreeAndroidSampleTheme {
            MainScreen()
        }
    }
}