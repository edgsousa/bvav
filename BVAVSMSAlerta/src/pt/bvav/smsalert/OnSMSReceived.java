package pt.bvav.smsalert;

import pt.bvav.smsalert.migration.DefaultPhoneNumber;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class OnSMSReceived extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		DefaultPhoneNumber.update(arg0);
		
		SharedPreferences sp = arg0.getSharedPreferences("default", Context.MODE_PRIVATE);
		String numOrig = sp.getString("num", arg0.getString(R.string.defaultphone));
		String prefix = sp.getString("tag", "BVAV");
		Bundle bundle = arg1.getExtras();

		try {
		     
		    if (bundle != null) {
		         
		        final Object[] pdusObj = (Object[]) bundle.get("pdus");
		         
		        for (int i = 0; i < pdusObj.length; i++) {
		             
		            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		            String phoneNumber = currentMessage.getDisplayOriginatingAddress();
		             
		            if(phoneNumber.endsWith(numOrig)){
		            	String message = currentMessage.getDisplayMessageBody();
		            	if(message.startsWith(prefix)) {
		            		notifySMS(arg0, message);
		            	}
		            }

		        } // end for loop
		      } // bundle is null
		 
		} catch (Exception e) {
		    Log.e("SmsReceiver", "Exception smsReceiver" +e);
		     
		}
		
	}

	private void notifySMS(Context arg0, String msg) {
		Intent i = new Intent();
		i.setAction(Notifier.NOTIFY_ACTION);
		i.putExtra("data", msg);
		arg0.sendBroadcast(i);
		Log.i(OnSMSReceived.class.getName(), "Sent broadcast");
	}

}
