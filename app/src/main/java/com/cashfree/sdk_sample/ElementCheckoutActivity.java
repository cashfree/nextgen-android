package com.cashfree.sdk_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFCorePaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback;
import com.cashfree.pg.core.api.card.CFCard;
import com.cashfree.pg.core.api.card.CFCardPayment;
import com.cashfree.pg.core.api.emicard.CFEMICard;
import com.cashfree.pg.core.api.emicard.CFEMICardPayment;
import com.cashfree.pg.core.api.exception.CFException;
import com.cashfree.pg.core.api.netbanking.CFNetBanking;
import com.cashfree.pg.core.api.netbanking.CFNetBankingPayment;
import com.cashfree.pg.core.api.paylater.CFPayLater;
import com.cashfree.pg.core.api.paylater.CFPayLaterPayment;
import com.cashfree.pg.core.api.upi.CFUPI;
import com.cashfree.pg.core.api.upi.CFUPIPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.wallet.CFWallet;
import com.cashfree.pg.core.api.wallet.CFWalletPayment;

public class ElementCheckoutActivity extends AppCompatActivity  implements CFCheckoutResponseCallback {
    // Go to https://docs.cashfree.com/docs/31-initiate-payment-native-checkout for the documentation
    String orderID = "ORDER_ID";
    String token = "TOKEN";
    CFSession.Environment cfEnvironment = CFSession.Environment.PRODUCTION;

    //Card Payment Inputs
    public String cardNumber = "4585340002077590";
    public String cardMM = "09";
    public String cardYY = "26";
    public String cardHolderName = "John Doe";
    public String cardCVV = "850";

    // Card EMI Inputs
    public String bankName = "Axis Bank";
    public int emiTenure = 3;

    //UPI Collect mode
    public CFUPI.Mode collectMode = CFUPI.Mode.COLLECT;
    public String upiVpa = "testsuccess@gocash";

    // UPI Intent mode
    public CFUPI.Mode intentMode = CFUPI.Mode.INTENT;
    public String upiAppPackage = "com.google.android.apps.nbu.paisa.user";

    //Wallet mode
    public String channel = "phonepe";
    public String phone = "9999999999";

    //Pay later mode
    public String payLaterChannel = "lazypay";

    // Net Banking mode
    public int bankCode = 3003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_checkout);
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentVerify(String orderID) {
        Log.e("onPaymentVerify", "verifyPayment triggered");
    }

    @Override
    public void onPaymentFailure(CFErrorResponse cfErrorResponse, String orderID) {
        Log.e("onPaymentFailure " + orderID, cfErrorResponse.getMessage());
    }

    public void doCardPayment(View view) {
        if (orderID.equals("ORDER_ID") || TextUtils.isEmpty(orderID)) {
            Toast.makeText(this,"Please set the orderId (DropCheckoutActivity.class,  line: 32)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (token.equals("TOKEN") || TextUtils.isEmpty(token)) {
            Toast.makeText(this,"Please set the token (DropCheckoutActivity.class,  line: 33)", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setOrderToken(token)
                    .setOrderId(orderID)
                    .build();
            CFCard cfCard = new CFCard.CFCardBuilder()
                    .setCardHolderName(cardHolderName)
                    .setCardNumber(cardNumber)
                    .setCardExpiryMonth(cardMM)
                    .setCardExpiryYear(cardYY)
                    .setCVV(cardCVV)
                    .build();
            CFCardPayment cfCardPayment = new CFCardPayment.CFCardPaymentBuilder()
                    .setSession(cfSession)
                    .setCard(cfCard)
                    .build();
            CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfCardPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    public boolean hasValidCardInputs() {
        if (cardNumber.length() != 16) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Number");
            return false;
        }
        if (cardHolderName.length() < 3) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Holder Name");
            return false;
        }
        if (cardMM.length() != 2) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Expiry Month in MM format");
            return false;
        }
        if (cardYY.length() != 2) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card Expiry Year in YY format");
            return false;
        }
        if (cardCVV.length() < 3 || cardCVV.length() > 4) {
            Log.e("CARD INPUT VALIDATION", "Enter a Valid Card CVV");
            return false;
        }
        return true;
    }



    public void doCardEMIPayment(View view) {
        if (hasValidCardInputs())
            try {
                CFSession cfSession = new CFSession.CFSessionBuilder()
                        .setEnvironment(cfEnvironment)
                        .setOrderToken(token)
                        .setOrderId(orderID)
                        .build();
                CFEMICard cfCard = new CFEMICard.CFEMICardBuilder()
                        .setCardHolderName(cardHolderName)
                        .setCardNumber(cardNumber)
                        .setCardExpiryMonth(cardMM)
                        .setCardExpiryYear(cardYY)
                        .setCVV(cardCVV)
                        .setBankName(bankName)
                        .setEMITenure(emiTenure)
                        .build();
                CFEMICardPayment cfCardPayment = new CFEMICardPayment.CFEMICardPaymentBuilder()
                        .setSession(cfSession)
                        .setCard(cfCard)
                        .build();
                CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfCardPayment);
            } catch (CFException exception) {
                exception.printStackTrace();
            }
    }



    public void doNetBankingPayment(View view) {
        if (hasValidNetBankingInputs())
            try {
                CFSession cfSession = new CFSession.CFSessionBuilder()
                        .setEnvironment(cfEnvironment)
                        .setOrderToken(token)
                        .setOrderId(orderID)
                        .build();
                CFNetBanking cfNetBanking = new CFNetBanking.CFNetBankingBuilder()
                        .setBankCode(bankCode)
                        .build();
                CFNetBankingPayment cfNetBankingPayment = new CFNetBankingPayment.CFNetBankingPaymentBuilder()
                        .setSession(cfSession)
                        .setCfNetBanking(cfNetBanking)
                        .build();
                CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfNetBankingPayment);
            } catch (CFException exception) {
                exception.printStackTrace();
            }
    }

    public boolean hasValidNetBankingInputs() throws IllegalArgumentException {
        if (String.valueOf(bankCode).length() != 4) {
            Log.e("NET_BANKING VALIDATION", "Enter a Valid 4 digit Bank Code");
            return false;
        }
        return true;
    }

    public void doWalletPayment(View view) {
        if (hasValidWalletPaymentInputs())
            try {
                CFSession cfSession = new CFSession.CFSessionBuilder()
                        .setEnvironment(cfEnvironment)
                        .setOrderToken(token)
                        .setOrderId(orderID)
                        .build();
                CFWallet cfWallet = new CFWallet.CFWalletBuilder()
                        .setProvider(channel)
                        .setPhone(phone)
                        .build();
                CFWalletPayment cfWalletPayment = new CFWalletPayment.CFWalletPaymentBuilder()
                        .setSession(cfSession)
                        .setCfWallet(cfWallet)
                        .build();
                CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfWalletPayment);
            } catch (CFException exception) {
                exception.printStackTrace();
            }
    }

    public void doPayLaterPayment(View view) {
        if (hasValidWalletPaymentInputs())
            try {
                CFSession cfSession = new CFSession.CFSessionBuilder()
                        .setEnvironment(cfEnvironment)
                        .setOrderToken(token)
                        .setOrderId(orderID)
                        .build();
                CFPayLater cfPayLater = new CFPayLater.CFPayLaterBuilder()
                        .setProvider(payLaterChannel)
                        .setPhone(phone)
                        .build();
                CFPayLaterPayment cfPayLaterPayment = new CFPayLaterPayment.CFPayLaterPaymentBuilder()
                        .setSession(cfSession)
                        .setCfPayLater(cfPayLater)
                        .build();
                CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfPayLaterPayment);
            } catch (CFException exception) {
                exception.printStackTrace();
            }
    }

    public boolean hasValidWalletPaymentInputs() throws IllegalArgumentException {
        if (channel.length() < 4) {
            Log.e("WALLET INPUT VALIDATION", "Enter a Valid channel");
            return false;
        }

        if (phone.length() < 10) {
            Log.e("WALLET INPUT VALIDATION", "Enter a Valid phone number");
            return false;
        }
        return true;
    }



    public void doUPICollectPayment(View view) {
        initiatePayment(collectMode, token);
    }

    public void doUPIIntentPayment(View view) {

        initiatePayment(intentMode, upiAppPackage);
    }

    private void initiatePayment(CFUPI.Mode mode, String id) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setOrderToken(token)
                    .setOrderId(orderID)
                    .build();
            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                    .setMode(mode)
                    .setUPIID(id)
                    .build();
            CFUPIPayment cfupiPayment = new CFUPIPayment.CFUPIPaymentBuilder()
                    .setSession(cfSession)
                    .setCfUPI(cfupi)
                    .build();
            CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfupiPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

}