package rw.benax.offguard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;

public class MainActivity extends Activity {
	public static final String SHARED_PREFERENCES = "call_blocker";
	public static final String IS_ENABLED = "is_enabled";
	public static final boolean IS_ENABLED_DEFAULT = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initIsEnabled();
		requestRequestedPermissions();

		Button btnLinkToAdd = findViewById(R.id.btnLinkToNewWhitelist);
		btnLinkToAdd.setOnClickListener(view -> {
			Intent i = new Intent(getApplicationContext(), AddToWhitelist.class);
			startActivity(i);
			//finish();
		});

		Button btnLinkToView = findViewById(R.id.btnLinkToViewWhitelist);
		btnLinkToView.setOnClickListener(view -> {
			Intent i = new Intent(getApplicationContext(), ViewWhitelist.class);
			startActivity(i);
			//finish();
		});

	}

	private void initIsEnabled(){
		CompoundButton isEnabledButton = findViewById(R.id.is_enabled);
		if (isEnabledButton == null) {
			return;
		}
		var isEnabled = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
				.getBoolean(IS_ENABLED, IS_ENABLED_DEFAULT);
		isEnabledButton.setChecked(isEnabled);
		isEnabledButton.setOnCheckedChangeListener((buttonView, isChecked) ->
				buttonView.getContext()
						.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
						.edit()
						.putBoolean(IS_ENABLED, isChecked)
						.apply());
	}

	private void requestRequestedPermissions(){
		var packageName = getPackageName();
		try {
			var requestedPermissions = getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
					.requestedPermissions;
			if (requestedPermissions != null && requestedPermissions.length > 0) {
				requestPermissions(requestedPermissions, 0);
			}
		} catch (PackageManager.NameNotFoundException ignore) {
		}
	}
}
