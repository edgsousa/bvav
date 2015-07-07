package pt.bvav.smsalert;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class Notifier extends WakefulBroadcastReceiver {

	public static Lock rl = new ReentrantLock();
	public static WakeLock currentLock;
	
	public final static String NOTIFY_ACTION = "pt.bvav.smsalert.NOTIFYUSER";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i("Notifer", "Notify");
		PowerManager pm = (PowerManager) arg0.getSystemService(Context.POWER_SERVICE);
		

		rl.lock(); try {
			if(currentLock == null) {
				currentLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
											 PowerManager.ACQUIRE_CAUSES_WAKEUP |
											 PowerManager.ON_AFTER_RELEASE, "WAKEUP");
				currentLock.acquire();
			}
			
		} finally {
			rl.unlock();
		}
		String msg = arg1.getStringExtra("data");
        Intent service = new Intent(arg0, WakefullService.class);
        service.putExtra("data", msg);
		startWakefulService(arg0, service);
		

		
		
	}
}
