package pt.bvav.smsalert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import pt.bvav.smsalert.migration.DefaultPhoneNumber;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private static String SENDER_ID = "883549896767";
	private String GCMTAG = "GCM";

	private String regID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		boolean upgraded = DefaultPhoneNumber.update(this);
		
		SharedPreferences sp = getSharedPreferences("default", Context.MODE_PRIVATE);
		((TextView) findViewById(R.id.editText1)).setText(sp.getString("num", getString(R.string.defaultphone)));
		((TextView) findViewById(R.id.editText2)).setText(sp.getString("tag", "BVAV"));
		regID = sp.getString(PROPERTY_REG_ID, "");
		
		if(upgraded) {
			Toast.makeText(this, "Num. atualizado", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	public void saveData(View v) {
		SharedPreferences sp = getSharedPreferences("default", Context.MODE_PRIVATE);

		Editor ed = sp.edit();

		ed.putString("num", ((EditText) findViewById(R.id.editText1)).getText().toString());
		ed.putString("tag", ((EditText) findViewById(R.id.editText2)).getText().toString());

		ed.commit();
//		if(regID.equals("")) {
//			IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//			OnNetworkChangeReceiver receiver = new OnNetworkChangeReceiver();
//			this.registerReceiver(receiver, filter);
//			registerInBackground(this, 1);
//		}

		Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
		finish();
	}

	public void addAlarm(View v) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent();
		String tag = ((EditText) findViewById(R.id.editText2)).getText().toString();
		i.setAction(Notifier.NOTIFY_ACTION);
		i.putExtra("data", tag +  " Teste");
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);

		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pi);

		Toast.makeText(this, "Modo de teste", Toast.LENGTH_LONG).show();
		finish();
	}

//	public static void registerInBackground(final Context ctx, final int delay) {
//
//		new AsyncTask<Object, Integer, String>() {
//			@Override
//			protected String doInBackground(Object... params) {
//
//				try {
//					Thread.sleep(1000*delay);
//				} catch (InterruptedException e) {}
//
//				String msg = "";
//				ConnectivityManager connMgr = (ConnectivityManager) ctx
//						.getSystemService(Context.CONNECTIVITY_SERVICE);
//				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//				if (networkInfo != null && networkInfo.isConnected()) {
//
//					try {
//
//						GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(ctx);
//
//						String regID = gcm.register(SENDER_ID);
//						Log.i("GCM", "REGID:" + regID);
//						SharedPreferences sp = ctx.getSharedPreferences("default", Context.MODE_PRIVATE);
//						
//						postData("http://localhost:8000/rest/register", "regid", regID);
//
//						Editor ed = sp.edit();
//						ed.putString(PROPERTY_REG_ID, regID);
//						ed.commit();
//
//					} catch (IOException ex) {
//						registerInBackground(ctx, delay<<1);
//					}
//				}
//				return msg;
//			}
//
//		}.execute();
//	}

	private static String postData(String URL, String tag, String value) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		try {
			StringEntity se = new StringEntity("{'"+tag+"': '" + value +"'}");
			post.setEntity(se);
			HttpResponse response = httpClient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.d("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}        
		return stringBuilder.toString();
	}

}
