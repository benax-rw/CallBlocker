package rw.benax.callblocker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Arrays;

public class CallBlocker extends BroadcastReceiver {
	private static boolean isWhiteListed(String phoneNumber) {
		boolean val = false;
		String[] whitelist = {
				"0788301945", //Jean Claude, NGA
				"0788548000", //Dr. Papias, RCA
				"0791255485", //Gabriel-Super-Private
				"0783070801" //DalYoung-Public
		};
		if (Arrays.asList(whitelist).contains(phoneNumber)) {
			val=true;
		}
		return val;
	}
	@SuppressWarnings({"deprecation", "RedundantSuppression"})
	@Override
	public void onReceive(Context context, Intent intent) {
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
		var phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if (isWhiteListed(phoneNumber)) {
			return; //Return means don't block it!
		}
		if (!context.getSharedPreferences(MainActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
				.getBoolean(MainActivity.IS_ENABLED, MainActivity.IS_ENABLED_DEFAULT)) {
			return;
		}
		if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(context, Manifest.permission.ANSWER_PHONE_CALLS, Toast.LENGTH_LONG)
					.show();
		}
		var telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
		if (telecomManager != null && telecomManager.endCall()) {
			Toast.makeText(context, "Blocked a call from "+phoneNumber, Toast.LENGTH_LONG)
					.show();
		}
	}
}
