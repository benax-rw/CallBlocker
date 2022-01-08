package rw.benax.offguard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class CallsHandler extends BroadcastReceiver{
	private final String[] whitelist = {
			"0788301945", //Jean Claude, NGA
			"0788548000", //Dr. Papias, RCA
			"0791255485", //Gabriel-Super-Private
			"0783070801" //DalYoung-Public
	};
	private boolean val = false; //Shouldn't be final because it will be assigned values locally (== withing a method)

	private boolean isWhiteListed(String phoneNumber) {
		if (Arrays.asList(this.whitelist).contains(phoneNumber)) {
			val=true;
		}
		return val;
	}

	@SuppressLint({"ResourceAsColor", "SetTextI18n"})
	public void sendSMS(String phone, String SMS, Context context){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendMultipartTextMessage(phone,null,smsManager.divideMessage(SMS),null,null);
		Toast.makeText(context, "Auto-replying", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onReceive(Context context, Intent intent){
		if (context == null || intent == null) {
			return;
		}

		var action = intent.getAction();
		if (!TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
			return;
		}

		var state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (!TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
			return;
		}

		if (!intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
			return;
		}

		if (!context.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
				.getBoolean(MainActivity.IS_ENABLED, MainActivity.IS_ENABLED_DEFAULT)) {
			return;
		}

		if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(context, Manifest.permission.ANSWER_PHONE_CALLS, Toast.LENGTH_LONG)
					.show();
		}

		var phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		var TAG = "Event Handling";
		var owner = "BAZIRAMWABO Gabriel";

		if (isWhiteListed(phoneNumber)){
			Model.reportEvent(owner, "Received a call from "+phoneNumber+".", context);
			Log.d(TAG, "Received a call from "+phoneNumber+".");
			return; //Return means don't block it!
		}

		var telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
		if (telecomManager != null && telecomManager.endCall()) {
			Toast.makeText(context, "Blocked a call from " + phoneNumber, Toast.LENGTH_LONG).show();
			sendSMS(phoneNumber, "Hello.\nI am currently unable to answer your call.\nCan you rather send me an SMS?", context);
			Model.reportEvent(owner, "Blocked a call from " + phoneNumber + ".", context);
			Log.d(TAG, "Blocked a call from "+phoneNumber+".");
		}
	}
}
