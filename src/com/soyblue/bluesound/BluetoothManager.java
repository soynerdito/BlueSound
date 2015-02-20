package com.soyblue.bluesound;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

public final class BluetoothManager {

	public static boolean power(boolean on) {
		return false;
	}

	
	
	public static boolean streamAudioStart(Context context) {
		try{
			AudioManager localAudioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if( localAudioManager == null ){ return false; }
			localAudioManager.setMode(0);
			localAudioManager.setBluetoothScoOn(true);
			localAudioManager.startBluetoothSco();
//			localAudioManager.setMode(AudioManager.MODE_IN_CALL);	
			
			/*int scoAudioMode =
		            (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) ?
		            		AudioManager.MODE_IN_CALL : AudioManager.SCO_AUDIO_STATE_CONNECTED;
			localAudioManager.setMode(scoAudioMode);*/
		}catch( Exception ex){
			System.out.println("Exception on Start " + ex.getMessage() );
			return false;
		}
		
		
	    
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
