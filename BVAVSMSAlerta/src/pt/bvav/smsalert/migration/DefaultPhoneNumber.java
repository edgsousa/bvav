package pt.bvav.smsalert.migration;

import pt.bvav.smsalert.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;

public class DefaultPhoneNumber {
	
	public static boolean update(Context arg0) {
		SharedPreferences sp = arg0.getSharedPreferences("default", Context.MODE_PRIVATE);
		Resources res = arg0.getResources();
		
		String numOrig = sp.getString("num", res.getString(R.string.defaultphone));
		String[] olds = res.getStringArray(R.array.oldphones);
		
		boolean upgrade = false;
		for(String old: olds) {
			if(numOrig.endsWith(old)) {
				upgrade = true;
				break;
			}
		}
		
		if(upgrade) {
			Editor ed = sp.edit();
			ed.putString("num", res.getString(R.string.defaultphone));
			ed.commit();
		}
			
		
		return upgrade;
		
	}

}
