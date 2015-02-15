package com.soyblue.bluesound;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	private void enableView( int resId, boolean enabled ){
		this.findViewById(resId).setEnabled(enabled);	
		
	}
	
	public void onStreamClick(View view){		
		
		boolean isOFF = (view.getTag() !=null)?(boolean)view.getTag():true;		
		if( !isOFF ){
			//It is ON
			view.setTag( true );
			BluetoothManager.streamAudioStop(getApplicationContext());
			view.setBackground(getResources().getDrawable(R.drawable.ic_audio_on));			
		}else{
			//It is OFF
			view.setTag( false );
			BluetoothManager.streamAudioStart(getApplicationContext());
			view.setBackground(getResources().getDrawable(R.drawable.ic_audio_off));
		}
		
		//		
		//enableView(R.id.btnAudioON, true);
		//enableView(R.id.btnAudioOFF, false);
	}
	
	public void onStreamOn(View view){
		BluetoothManager.streamAudioStart(getApplicationContext());
		enableView(R.id.btnAudioON, false);
	}
}
