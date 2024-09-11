package com.cashfree.sdk_sample.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cashfree.pg.api.CFPaymentGatewayService;
import com.cashfree.pg.core.api.CFCorePaymentGatewayService;
import com.cashfree.pg.core.api.CFSession;
import com.cashfree.pg.core.api.CFTheme;
import com.cashfree.pg.core.api.base.CFPayment;
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
import com.cashfree.pg.core.api.ui.CFCardNumberView;
import com.cashfree.pg.core.api.ui.ICardInfo;
import com.cashfree.pg.core.api.upi.CFUPI;
import com.cashfree.pg.core.api.upi.CFUPIPayment;
import com.cashfree.pg.core.api.utils.CFErrorResponse;
import com.cashfree.pg.core.api.wallet.CFWallet;
import com.cashfree.pg.core.api.wallet.CFWalletPayment;
import com.cashfree.sdk_sample.Config;
import com.cashfree.sdk_sample.R;

import org.json.JSONObject;

public class ElementCheckoutActivity extends AppCompatActivity implements CFCheckoutResponseCallback {
    // Go to https://docs.cashfree.com/docs/31-initiate-payment-native-checkout for the documentation
    Config config = new Config();
    String orderID = config.getOrderID();
    String paymentSessionID = config.getPaymentSessionID();
    CFSession.Environment cfEnvironment = config.getEnvironment();

    //Card Payment Inputs
    private final String cardNumber = config.getCardNumber();
    private final String cardMM = config.getCardMM();
    private final String cardYY = config.getCardYY();
    private final String cardHolderName = config.getCardHolderName();
    private final String cardCVV = config.getCardCVV();

    // Card EMI Inputs
    private final String bankName = config.getBankName();
    private final int emiTenure = config.getEmiTenure();

    //UPI Collect mode
    private final CFUPI.Mode collectMode = config.getCollectMode();
    private final String upiVpa = config.getUpiVpa();

    // UPI Intent mode
    private final CFUPI.Mode intentMode = config.getIntentMode();
    private final String upiAppPackage = config.getUpiAppPackage();

    //Wallet mode
    private final String channel = config.getChannel();
    private final String phone = config.getPhone();

    //Pay later mode
    private final String payLaterChannel = config.getPayLaterChannel();

    // Net Banking mode
    private final int bankCode = config.getBankCode();

    private CFCardNumberView cfElementCard;
    private CFSession cfSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_checkout);
        cfElementCard = findViewById(R.id.cf_element_card);
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this);
        } catch (CFException e) {
            e.printStackTrace();
        }
        try {
            cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
        } catch (CFException e) {
            e.printStackTrace();
        }
        try {
            cfElementCard.initialize(cfSession, new ICardInfo() {
                @Override
                public void onInfo(JSONObject jsonObject) {
                    Log.d("CFCARDVIEW", jsonObject.toString());
                }
            });
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
            Toast.makeText(this, "Please set the orderId (DropCheckoutActivity.class,  line: 32)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paymentSessionID.equals("TOKEN") || TextUtils.isEmpty(paymentSessionID)) {
            Toast.makeText(this, "Please set the paymentSessionID (DropCheckoutActivity.class,  line: 33)", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
            CFCard cfCard = new CFCard.CFCardBuilder()
                    .setCardHolderName(cardHolderName)
                    .setCardNumber(cardNumber)
                    .setCardExpiryMonth(cardMM)
                    .setCardExpiryYear(cardYY)
                    .setCVV(cardCVV)
                    .setChannel("post")   //This will be required for merchant if they want native opt flow
                    .build();

            //To Set your theme on Cashfree UI
            CFTheme theme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#6A2222")
                    .setNavigationBarTextColor("#FFFFFF")
                    .setButtonBackgroundColor("#6Aaaaa")
                    .setButtonTextColor("#FFFFFF")
                    .setPrimaryTextColor("#11385b")
                    .setSecondaryTextColor("#808080")
                    .build();
            CFCardPayment cfCardPayment = new CFCardPayment.CFCardPaymentBuilder()
                    .setSession(cfSession)
                    .setCard(cfCard)
                    .build();
            cfCardPayment.setTheme(theme);

            /**
             * To set Full Screen Loader UI before  order pay network call.
             * This is optional for merchants. If they specially want to show UI loader then only enable it.
             * Note : If Merchant wants to use Native OTP flow in card payment then this is required.
             */
            cfCardPayment.setCfSDKFlow(CFPayment.CFSDKFlow.WITH_CASHFREE_FULLSCREEN_LOADER);

            CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfCardPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

    public void onElementPayClick(View view) {
        try {
            CFCard cfCard = new CFCard.CFCardBuilder()
                    .setCardHolderName(cardHolderName)
                    .setCardExpiryMonth(cardMM)
                    .setCardExpiryYear(cardYY)
                    .setCVV(cardCVV)
                    .setCfCard(true)
                    .build();
            CFTheme theme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#6A2222")
                    .setNavigationBarTextColor("#FFFFFF")
                    .setButtonBackgroundColor("#6Aaaaa")
                    .setButtonTextColor("#FFFFFF")
                    .setPrimaryTextColor("#11385b")
                    .setSecondaryTextColor("#808080")
                    .build();
            CFCardPayment cfCardPayment = new CFCardPayment.CFCardPaymentBuilder()
                    .setSession(cfSession)
                    .setCard(cfCard)
                    .build();
            cfCardPayment.setTheme(theme);
            /**
             * To set Loader UI before  order pay network call.
             * This is optional for merchants. If they specially want to show UI loader then only enable it.
             */
            cfCardPayment.setLoaderEnable(true);
            cfElementCard.doPayment(ElementCheckoutActivity.this, cfCardPayment);
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
                        .setPaymentSessionID(paymentSessionID)
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
                CFTheme theme = new CFTheme.CFThemeBuilder()
                        .setNavigationBarBackgroundColor("#6A2222")
                        .setNavigationBarTextColor("#FFFFFF")
                        .setButtonBackgroundColor("#6Aaaaa")
                        .setButtonTextColor("#FFFFFF")
                        .setPrimaryTextColor("#11385b")
                        .setSecondaryTextColor("#808080")
                        .build();
                CFEMICardPayment cfCardPayment = new CFEMICardPayment.CFEMICardPaymentBuilder()
                        .setSession(cfSession)
                        .setCard(cfCard)
                        .build();
                cfCardPayment.setTheme(theme);
                /**
                 * To set Loader UI before  order pay network call.
                 * This is optional for merchants. If they specially want to show UI loader then only enable it.
                 */
                cfCardPayment.setLoaderEnable(true);
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
                        .setPaymentSessionID(paymentSessionID)
                        .setOrderId(orderID)
                        .build();
                CFNetBanking cfNetBanking = new CFNetBanking.CFNetBankingBuilder()
                        .setBankCode(bankCode)
                        .build();
                //To Set your theme on Cashfree UI
                CFTheme theme = new CFTheme.CFThemeBuilder()
                        .setNavigationBarBackgroundColor("#6A2222")
                        .setNavigationBarTextColor("#FFFFFF")
                        .setButtonBackgroundColor("#6Aaaaa")
                        .setButtonTextColor("#FFFFFF")
                        .setPrimaryTextColor("#11385b")
                        .setSecondaryTextColor("#808080")
                        .build();
                CFNetBankingPayment cfNetBankingPayment = new CFNetBankingPayment.CFNetBankingPaymentBuilder()
                        .setSession(cfSession)
                        .setCfNetBanking(cfNetBanking)
                        .build();
                cfNetBankingPayment.setTheme(theme);

                /**
                 * To set Loader UI before  order pay network call.
                 * This is optional for merchants. If they specially want to show UI loader then only enable it.
                 */
                cfNetBankingPayment.setCfSDKFlow(CFPayment.CFSDKFlow.WITH_CASHFREE_FULLSCREEN_LOADER);

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
                        .setPaymentSessionID(paymentSessionID)
                        .setOrderId(orderID)
                        .build();
                CFWallet cfWallet = new CFWallet.CFWalletBuilder()
                        .setProvider(channel)
                        .setPhone(phone)
                        .build();

                //To Set your theme on Cashfree UI
                CFTheme theme = new CFTheme.CFThemeBuilder()
                        .setNavigationBarBackgroundColor("#6A2222")
                        .setNavigationBarTextColor("#FFFFFF")
                        .setButtonBackgroundColor("#6Aaaaa")
                        .setButtonTextColor("#FFFFFF")
                        .setPrimaryTextColor("#11385b")
                        .setSecondaryTextColor("#808080")
                        .build();
                CFWalletPayment cfWalletPayment = new CFWalletPayment.CFWalletPaymentBuilder()
                        .setSession(cfSession)
                        .setCfWallet(cfWallet)
                        .build();
                cfWalletPayment.setTheme(theme);
                /**
                 * To set Loader UI before  order pay network call.
                 * This is optional for merchants. If they specially want to show UI loader then only enable it.
                 */
                cfWalletPayment.setCfSDKFlow(CFPayment.CFSDKFlow.WITH_CASHFREE_FULLSCREEN_LOADER);
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
                        .setPaymentSessionID(paymentSessionID)
                        .setOrderId(orderID)
                        .build();
                CFPayLater cfPayLater = new CFPayLater.CFPayLaterBuilder()
                        .setProvider(payLaterChannel)
                        .setPhone(phone)
                        .build();
                CFTheme theme = new CFTheme.CFThemeBuilder()
                        .setNavigationBarBackgroundColor("#6A2222")
                        .setNavigationBarTextColor("#FFFFFF")
                        .setButtonBackgroundColor("#6Aaaaa")
                        .setButtonTextColor("#FFFFFF")
                        .setPrimaryTextColor("#11385b")
                        .setSecondaryTextColor("#808080")
                        .build();
                CFPayLaterPayment cfPayLaterPayment = new CFPayLaterPayment.CFPayLaterPaymentBuilder()
                        .setSession(cfSession)
                        .setCfPayLater(cfPayLater)
                        .build();
                cfPayLaterPayment.setTheme(theme);
                /**
                 * To set Loader UI before  order pay network call.
                 * This is optional for merchants. If they specially want to show UI loader then only enable it.
                 */
                cfPayLaterPayment.setLoaderEnable(true);
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
        initiatePayment(collectMode, upiVpa);
    }

    public void doUPIIntentPayment(View view) {
        initiatePayment(intentMode, upiAppPackage);
    }

    private void initiatePayment(CFUPI.Mode mode, String id) {
        try {
            CFSession cfSession = new CFSession.CFSessionBuilder()
                    .setEnvironment(cfEnvironment)
                    .setPaymentSessionID(paymentSessionID)
                    .setOrderId(orderID)
                    .build();
            CFUPI cfupi = new CFUPI.CFUPIBuilder()
                    .setMode(mode)
                    .setUPIID(id)
                    .build();
            CFTheme theme = new CFTheme.CFThemeBuilder()
                    .setNavigationBarBackgroundColor("#6A2222")
                    .setNavigationBarTextColor("#FFFFFF")
                    .setButtonBackgroundColor("#6Aaaaa")
                    .setButtonTextColor("#FFFFFF")
                    .setPrimaryTextColor("#11385b")
                    .setSecondaryTextColor("#808080")
                    .build();
            CFUPIPayment cfupiPayment = new CFUPIPayment.CFUPIPaymentBuilder()
                    .setSession(cfSession)
                    .setCfUPI(cfupi)
                    .build();
            cfupiPayment.setTheme(theme);
            /**
             * To set Loader UI before  order pay network call.
             * This is optional for merchants. If they specially want to show UI loader then only enable it.
             */
            cfupiPayment.setLoaderEnable(true);
            CFCorePaymentGatewayService.getInstance().doPayment(ElementCheckoutActivity.this, cfupiPayment);
        } catch (CFException exception) {
            exception.printStackTrace();
        }
    }

}