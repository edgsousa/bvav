package pt.bvav.smsalert.notif;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;

public interface NotificationBuilder {

	
	Notification getNotification(Context context, Uri soundUri);
}
