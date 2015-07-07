package pt.bvav.smsalert;

import pt.bvav.smsalert.notif.NotificationBuilder;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.Keyboard.Key;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class Alert extends Activity {

	private int alarmVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON 
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON 
				);
		String msg = getIntent().getStringExtra("data");
		setContentView(R.layout.activity_alert);

		((TextView) findViewById(R.id.textView1)).setText(msg);

		setTitle("Alerta");

		Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/raw/aa01");// + R.raw.aa01); 		
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		//multiple sms?
		nm.cancel(0);


		NotificationBuilder nb = null;
		try {
			if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {


				nb = (NotificationBuilder) Class.forName("pt.bvav.smsalert.notif.NotifBuilderCompat").newInstance();


			} else {
				nb = (NotificationBuilder) Class.forName("pt.bvav.smsalert.notif.NotifBuilder").newInstance();

			}	
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		} catch (ClassNotFoundException e) {

		}


		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		this.alarmVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
		am.setStreamVolume(AudioManager.STREAM_ALARM, 
							am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 
							0);
		nm.notify(0, nb.getNotification(this, soundUri));




	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	public void dismiss(View v){
		finish();
	}

	@Override
	protected void onDestroy() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(0);
		Notifier.rl.lock(); try {
			if(Notifier.currentLock != null) {
				Notifier.currentLock.release();
				Notifier.currentLock = null;
				//shouldn't happen, but hell...
			}
		} finally {
			Notifier.rl.unlock();
		}
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_ALARM, 
				this.alarmVolume, 
				0);
		
		super.onDestroy();
	}
	
	
}
