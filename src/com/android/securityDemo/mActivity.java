package com.android.securityDemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class mActivity extends Activity {
	private EditText SIM_SERIAL_NUMBER_TEXT;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mActivity.this);
        LayoutInflater factory = LayoutInflater.from(mActivity.this);
        final View view = factory.inflate(R.layout.dialog, null);
        mAlertDialog.setTitle(R.string.info);
        mAlertDialog.setView(view);
        mAlertDialog.setPositiveButton(R.string.ok_button, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				SIM_SERIAL_NUMBER_TEXT = (EditText) view.findViewById(R.id.SIM_SERIAL_NUMBER_TEXT);
				Toast.makeText(mActivity.this, "ÄãÉèÖÃµÄºÅÂëÎª£º" + SIM_SERIAL_NUMBER_TEXT.getText().toString(), Toast.LENGTH_LONG).show();
				SharedPreferences mPreferences = getSharedPreferences("device", Context.MODE_PRIVATE);
				SharedPreferences.Editor mEditor = mPreferences.edit();
				mEditor.putString("SIM_SERIAL_NUMBER", SIM_SERIAL_NUMBER_TEXT.getText().toString());
				mEditor.commit();
				finish();
			}
		});
        mAlertDialog.setNegativeButton(R.string.cancel_button, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        mAlertDialog.create();
        mAlertDialog.show();
    }
}