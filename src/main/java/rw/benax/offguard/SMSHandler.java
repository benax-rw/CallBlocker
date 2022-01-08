package rw.benax.offguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            StringBuilder smsSender = new StringBuilder();
            StringBuilder smsBody = new StringBuilder();
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody.append(smsMessage.getMessageBody());
                smsSender.append(smsMessage.getOriginatingAddress());
            }
            Model.reportEvent(smsSender.toString(), smsBody.toString(), context);
            Toast.makeText(context, "SMS from " + smsSender.toString() + " uploaded.", Toast.LENGTH_LONG).show();

            var TAG = "incomingSMS";
            Log.d(TAG, "SMS from " + smsSender.toString() + " is uploaded.");
        }
    }
}
