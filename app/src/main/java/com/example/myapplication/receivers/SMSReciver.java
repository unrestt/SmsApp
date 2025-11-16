package com.example.myapplication.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.myapplication.utills.FileManager;

public class SMSReciver extends BroadcastReceiver {

    private static final String TAG = "Bartosz Kutnik";
    private static String SMS = "android.provider.Telephony.SMS_RECEIVED";
    public static final String pdu_type = "pdus";
    public SMSReciver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive, start");
        if (intent.getAction().equalsIgnoreCase(SMS)){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsMessages = null;

            String format = bundle.getString("format");
            Log.v(TAG, "format=" + format);

            Object[] pdus =(Object[]) bundle.get(pdu_type);
            if (pdus != null) {
                smsMessages = new SmsMessage[pdus.length];

                Log.v(TAG, String.valueOf(smsMessages.length));
                for (int i = 0; i < smsMessages.length; i++){
                    smsMessages[i] = SmsMessage.createFromPdu(
                            (byte[]) pdus[i],
                            format
                    );

//                    message += "Sms from: " + smsMessages[i].getOriginatingAddress() + " ";
//                    message += smsMessages[i].getMessageBody() + "\n";
//                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                    FileManager.appendMessageToFile(context, "messages", smsMessages[i].getOriginatingAddress(), smsMessages[i].getMessageBody());
                }
            }
        }
    }
}
