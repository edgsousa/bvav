package pt.bvav.smsalert;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WakefullService extends IntentService {

	public WakefullService() {
		super("StartActivity");
	}

	protected void onHandleIntent(Intent intent) {

		startActivity(this.getBaseContext(), intent);
		Notifier.completeWakefulIntent(intent);
	}



	public void startActivity(Context arg0, Intent arg1) {
		Intent showAlert = new Intent();
		String msg = arg1.getStringExtra("data");
		showAlert.putExtra("data", msg);


		showAlert.setClass(arg0.getApplicationContext(), Alert.class);
		showAlert.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION
				| Intent.FLAG_FROM_BACKGROUND);
		arg0.startActivity(showAlert);
		Log.i("Notifier", "Started activity");
	}
}