package pt.bvav.smsalert.notif;

import pt.bvav.smsalert.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotifBuilderCompat implements NotificationBuilder {

	@Override
	public Notification getNotification(Context context, Uri soundUri) {
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
			      0, 
			      new Intent(),  //Dummy Intent do nothing 
			      Intent.FLAG_ACTIVITY_NEW_TASK);
		
		 Notification n= new NotificationCompat.Builder(context)
			.setContentTitle("Alerta SMS")
			.setSmallIcon(R.drawable.ic_launcher)
			.setDefaults(Notification.DEFAULT_LIGHTS)
			.setSound(soundUri, AudioManager.STREAM_ALARM)
			.setVibrate(new long[]{20,10000})
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)
			.build();
		 
		 return n;
	}

}
