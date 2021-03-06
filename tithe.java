package com.example.tumainichurch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.ar.core.Config;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class tithe extends AppCompatActivity {

  private static final  String TAG="paypal";

  public static final String Paypal_key="AcAx2SJKxPyNLz-JaABdwgyhzpNQrjIjwoFzfieqOArc8CcfOdeEpQeLktIKq7k0mRnnjJ9iesJDSTQB";

  private static  final int REQUEST_CODE_PAYMENT =1;
  private static  final int REQUEST_CODE_FUTURE_PAYMENT =2;
  private static final String CONFIG_ENVIRONMENT= PayPalConfiguration.ENVIRONMENT_SANDBOX;
  private static PayPalConfiguration config;

  PayPalPayment offering;

  private Button payout;
  private EditText tithe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tithe);
        payout=(Button) findViewById(R.id.payout);


        payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              Pay();
            }
        });

        configPaypal();
    }

    private void configPaypal()
    {
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(Paypal_key)
                .merchantName("PayPal")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    }

    private void Pay()
    {

        Intent intent= new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);

        offering= new PayPalPayment(new BigDecimal(String.valueOf("10.45")), "KSH", "Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent payment = new Intent(this, PaymentActivity.class);
        payment.putExtra(PaymentActivity.EXTRA_PAYMENT,offering);
        payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startActivityForResult(payment, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (confirm !=null){
                try {
                    System.out.println(confirm.toJSONObject().toString(4));
                    System.out.println(confirm.getPayment().toJSONObject()
                    .toString(4));

                }
                catch (JSONException e){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (resultCode ==Activity.RESULT_CANCELED){
            Toast.makeText(this, "Payment Has Been Cancelled", Toast.LENGTH_SHORT).show();
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == Activity.RESULT_OK){
            PayPalAuthorization authorization = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
            if (authorization !=null){
                try {
                    Log.i("FuturePayment",authorization.toJSONObject().toString(4));
                    String autherization_code = authorization.getAuthorizationCode();
                    Log.d("FuturePayment", autherization_code);
                }
                catch (Exception e){

                }
            }
        }
    }
}