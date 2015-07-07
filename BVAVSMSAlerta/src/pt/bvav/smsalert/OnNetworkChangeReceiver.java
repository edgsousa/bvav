package pt.bvav.smsalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OnNetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
//		ConnectivityManager connMgr = (ConnectivityManager) arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//		if (networkInfo != null && networkInfo.isConnected()) {
//			SharedPreferences sp = arg0.getSharedPreferences("default", Context.MODE_PRIVATE);
//			String regID = sp.getString(Settings.PROPERTY_REG_ID, "");
//			
//			if(regID.equals("")) {
//				Settings.registerInBackground(arg0, 1);
//			} else {
//				arg0.unregisterReceiver(this);
//			}
//		}
	}

}
