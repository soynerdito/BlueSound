package com.soyblue.bluesound;

import android.content.Context;
import android.media.AudioManager;

public final class BluetoothManager {
	private static BluetoothManager mInstance = null;
	//Remember the status of audio
	private boolean mAudioStatus = false;

	public static boolean power(boolean on) {
		return false;
	}

	public boolean getStatus(){
		return mAudioStatus;
	}

	private BluetoothManager(){

	}

	public static BluetoothManager getInstance(){
		if( mInstance == null ){
			mInstance = new BluetoothManager();
		}
		return mInstance;
	}
	
	public boolean streamAudioStart(Context context) {
		try{
			AudioManager localAudioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if( localAudioManager == null ){ return false; }
			localAudioManager.setMode(0);
			localAudioManager.setBluetoothScoOn(true);
			localAudioManager.startBluetoothSco();
			localAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
			mAudioStatus = true;
		}catch( Exception ex){
			System.out.println("Exception on Start " + ex.getMessage() );
			mAudioStatus = false;
			return false;
		}

		return mAudioStatus;
	}

	public boolean streamAudioStop(Context context) {
		mAudioStatus = false;
		AudioManager localAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if( localAudioManager == null ){ return false; }
		localAudioManager.setBluetoothScoOn(false);
		localAudioManager.stopBluetoothSco();
		localAudioManager.setMode(AudioManager.MODE_NORMAL);
		return true;
	}

	protected void toggleAudioStatus(Context context) {
		if ( getStatus()) {
			// Turn Audio Off
			streamAudioStop(context);
		} else {
			// Turn Audio ON
			streamAudioStart(context);
		}

		ControlWidget.requestWidgetUpdate( context );
	}

}
