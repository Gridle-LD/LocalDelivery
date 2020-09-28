package com.example.localdelivery.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

import com.example.localdelivery.fragment.OtpFragment;

public class OtpReceiver extends BroadcastReceiver {

    @SuppressLint("StaticFieldLeak")
    private static EditText editText1;
    @SuppressLint("StaticFieldLeak")
    private static EditText editText2;
    @SuppressLint("StaticFieldLeak")
    private static EditText editText3;
    @SuppressLint("StaticFieldLeak")
    private static EditText editText4;

    public String otp = "";

    public void setEditText(EditText editText1, EditText editText2, EditText editText3, EditText editText4) {
        OtpReceiver.editText1 = editText1;
        OtpReceiver.editText2 = editText2;
        OtpReceiver.editText3 = editText3;
        OtpReceiver.editText4 = editText4;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(SmsMessage smsMessage : messages) {
            String message = smsMessage.getMessageBody();
            otp = message.split("- ")[1];
            editText1.setText(String.valueOf(otp.charAt(0)));
            editText2.setText(String.valueOf(otp.charAt(1)));
            editText3.setText(String.valueOf(otp.charAt(2)));
            editText4.setText(String.valueOf(otp.charAt(3)));
        }

        //called when otp is detected
        if(otp.length() == 4) {
            OtpFragment.getInstance().verifyOtp(otp);
        }
    }
}
