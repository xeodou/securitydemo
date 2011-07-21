package com.android.securityDemo;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class SecurityDemoReceiver extends BroadcastReceiver {

	private static final String KEY_FIRST_RUN = "FIRST_RUN";
	private static final String KEY_SIM_SERIAL_NUMBER = "ICCID";
	private static final String KEY_LOCATION_LATITUDE = "LATITUDE";
	private static final String KEY_LOCATION_LONGITUDE = "LONGITUDE";
	private static final String KEY_HOST_NUMBER = "HOST_NUMBER";
	private double lat, lng;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			int Sim_State = mTelephonyManager.getSimState();
			if (Sim_State == TelephonyManager.SIM_STATE_READY) {
				String SimSerialNumber = mTelephonyManager.getSimSerialNumber();
				SharedPreferences preferences = context.getSharedPreferences("device", Context.MODE_PRIVATE);
				boolean isFirstRun = preferences.getBoolean(KEY_FIRST_RUN, true);
				String SIM_SERIAL_NUMBER = preferences.getString("SIM_SERIAL_NUMBER", "UnkownNumber");
				if (isFirstRun) {
					
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean(KEY_FIRST_RUN, true);
					editor.putString(KEY_SIM_SERIAL_NUMBER, SimSerialNumber);
					editor.commit();
				} else {
					String hostSimSerialNumber = preferences.getString(KEY_SIM_SERIAL_NUMBER, "Unknown");
					if (!SimSerialNumber.equals(hostSimSerialNumber)) {
						
						String Host_Number = mTelephonyManager.getLine1Number();

						//获取经纬度
						LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
						Criteria criteria = new Criteria();
						criteria.setAccuracy(Criteria.ACCURACY_FINE);
						criteria.setAltitudeRequired(false);
						criteria.setBearingRequired(false);
						criteria.setCostAllowed(true);
						criteria.setPowerRequirement(Criteria.POWER_LOW);
						String provider = locationManager.getBestProvider(criteria, true);
						Location location = locationManager.getLastKnownLocation(provider);
						if (location != null) {
							lat = location.getLatitude();
							lng = location.getLongitude();
						} else {
							lat = 0;
							lng = 0;
						}
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString(KEY_LOCATION_LATITUDE, String.valueOf(lat));
						editor.putString(KEY_LOCATION_LONGITUDE, String.valueOf(lng));
						editor.putString(KEY_HOST_NUMBER, Host_Number);
						editor.commit();
						
						//发送消息
						PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
						SmsManager smsManager = SmsManager.getDefault();
						String smsManage = "SIM Serial Number" + SimSerialNumber + "主机号码：" + Host_Number + "纬度：" + String.valueOf(lat) + "经度：" + String.valueOf(lng); 
						smsManager.sendTextMessage(SIM_SERIAL_NUMBER, null, smsManage, mPendingIntent, null);
					}
				}
			}
		}
	}
		
	
}
