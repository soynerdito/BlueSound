package com.soyblue.bluesound;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	private boolean mAudioON = false;
	private final String AUDIO_STATUS = "AUDIO_STATUS";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if( savedInstanceState != null ){
			mAudioON = savedInstanceState.getBoolean( AUDIO_STATUS );
		}
		refreshButtonImage(findViewById(R.id.btnAudioON));
	}
	
	private void refreshButtonImage(View view){
		view.setBackground(getResources().getDrawable(
				(mAudioON?R.drawable.ic_audio_off:R.drawable.ic_audio_on)
				));	
	}
	
	public void onStreamClick(View view){				
		if( mAudioON ){
			//Turn Audio Off
			BluetoothManager.streamAudioStop(getApplicationContext());
		}else{
			//Turn Audio ON
			BluetoothManager.streamAudioStart(getApplicationContext());
		}
		//Toggle audio flag
		mAudioON = !mAudioON;
		refreshButtonImage(view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(AUDIO_STATUS, mAudioON );
		super.onSaveInstanceState(outState);
	}
	
}
