package rw.benax.offguard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallsHandler extends BroadcastReceiver{
	private SQLiteDatabase dataBase;
	private boolean val = false; //Shouldn't be final because it will be assigned values locally (== withing a method)

	@SuppressLint("Range")
	private boolean isWhiteListed(String phoneNumber, Context context){
		String[] whitelistArray = {};
		List<String> whitelist = new ArrayList<>();

		this.dataBase = new DBHandler(context).getWritableDatabase();
		Cursor cursor = this.dataBase.rawQuery(String.format("SELECT * FROM %s", DBHandler.TBL_WHITELIST), null);
		if (cursor.moveToFirst()){
			do {
				whitelist.add(cursor.getString(cursor.getColumnIndex(DBHandler.col_phone_number)));
			}
			while(cursor.moveToNext());
		}
		cursor.close();
		//this.dataBase.close();

		// Convert the Arraylist back to array
		whitelistArray = whitelist.toArray(whitelistArray);

		Log.d("MyTAG", whitelistArray[0]);

		if (Arrays.asList(whitelistArray).contains(phoneNumber)) {
			val=true;
		}
		return val;
	}

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

		if (isWhiteListed(phoneNumber, context)){
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
