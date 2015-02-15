package com.soyblue.bluesound;

import android.content.Context;
import android.media.AudioManager;

public final class BluetoothManager {

	public static boolean power(boolean on) {
		return false;
	}

	
	
	public static boolean streamAudioStart(Context context) {
		AudioManager localAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if( localAudioManager == null ){ return false; }
		localAudioManager.setMode(0);
		localAudioManager.setBluetoothScoOn(true);
		localAudioManager.startBluetoothSco();
		localAudioManager.setMode(AudioManager.MODE_IN_CALL);
		
		return true;
	}

	public static boolean streamAudioStop(Context context) {
		AudioManager localAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if( localAudioManager == null ){ return false; }
		localAudioManager.setBluetoothScoOn(false);
		localAudioManager.stopBluetoothSco();
		localAudioManager.setMode(AudioManager.MODE_NORMAL);
		
		return true;
	}
}
