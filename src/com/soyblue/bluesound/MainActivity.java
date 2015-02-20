package com.soyblue.bluesound;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private boolean mAudioON = false;
	private final String AUDIO_STATUS = "AUDIO_STATUS";
	
	//The BroadcastReceiver that listens for Bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        //BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            //Device found
	        }else if ( mAudioON && BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {	        	
	           //Device is now connected
	        	AudioManager localAudioManager = (AudioManager) context
						.getSystemService(Context.AUDIO_SERVICE);
				if( localAudioManager != null ){ 
					localAudioManager.setMode(AudioManager.MODE_IN_CALL);
				}
	        }
	        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	           //Done searching
	        }
	        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
	           //Device is about to disconnect
	        }
	        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
	           //Device has disconnected
	        	stopAudio();
	        	refreshButtonImage();
	        }           
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if( savedInstanceState != null ){
			mAudioON = savedInstanceState.getBoolean( AUDIO_STATUS );
		}
		refreshButtonImage();
		
		//Register Intent Filter to identify when bluetooth device is disconnected
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
	    filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
	    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
	    filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED );
	    filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED );	    
	    		
	    registerReceiver(mReceiver, filter);
		
	}
	
	private void refreshButtonImage(){
		refreshButtonImage(findViewById(R.id.btnAudioON));
	}
	
	@SuppressWarnings("deprecation")
	private void refreshButtonImage(View view){		
		
		
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    Drawable background = getResources().getDrawable(
					(mAudioON?R.drawable.ic_audio_off:R.drawable.ic_audio_on) );
		    view.setBackgroundDrawable( background );
		} else {
			view.setBackground(getResources().getDrawable(
					(mAudioON?R.drawable.ic_audio_off:R.drawable.ic_audio_on) ));
		}
		
	}
	
	protected void stopAudio(){
		BluetoothManager.streamAudioStop(getApplicationContext());
		mAudioON = false;
	}
	
	protected void startAudio(){
		mAudioON = BluetoothManager.streamAudioStart(getApplicationContext());
		//mAudioON = true;
		if( !mAudioON ){
			Toast.makeText(this, R.string.cannot_connect, Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void toggleAudioStatus(){
		if( mAudioON ){
			//Turn Audio Off
			stopAudio();
		}else{
			//Turn Audio ON
			startAudio();
		}
	}
	
	private void broadcastKey( int key ){
		Intent mbIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);  
        //KeyEvent  
        KeyEvent keyEvent = new KeyEvent (KeyEvent.ACTION_DOWN, key ) ;  
        //mbIntent  
        mbIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);  
        sendBroadcast(mbIntent);
	}
	
	public void onSoundStop(View view ) {		 
		broadcastKey( KeyEvent.KEYCODE_MEDIA_PAUSE );
	}
	
	public void onSoundPlay(View view ) {
		broadcastKey( KeyEvent.KEYCODE_MEDIA_PLAY );
	}

	public void onSoundBack(View view ) {
		broadcastKey( KeyEvent.KEYCODE_MEDIA_PREVIOUS );
	}

	public void onSoundNext(View view ) {
		broadcastKey( KeyEvent.KEYCODE_MEDIA_NEXT );
	}
    
	
	public void onStreamClick(View view){				
		toggleAudioStatus();
		refreshButtonImage(view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(AUDIO_STATUS, mAudioON );
		super.onSaveInstanceState(outState);
	}
	
	
}
