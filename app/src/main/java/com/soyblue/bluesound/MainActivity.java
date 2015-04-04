package com.soyblue.bluesound;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private final String AUDIO_STATUS = "AUDIO_STATUS";
	final Handler mguiHandler = new Handler();

	// The BroadcastReceiver that listens for Bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// BluetoothDevice device =
			// intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Device found
			} else if (BluetoothManager.getInstance().getStatus()
					&& BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				// Device is now connected				
				refreshButtonImage();
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				// Done searching
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED
					.equals(action)) {
				// Device is about to disconnect
			} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				// Device has disconnected
				stopAudio();
				refreshButtonImage();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		refreshButtonImage();

		// Register Intent Filter to identify when bluetooth device is
		// disconnected
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		registerReceiver(mReceiver, filter);

	}

	private void refreshButtonImage() {
		refreshButtonImage(findViewById(R.id.btnAudioON));
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void refreshButtonImage(View view) {

		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			Drawable background = getResources().getDrawable(
					(BluetoothManager.getInstance().getStatus() ? R.drawable.ic_audio_off
							: R.drawable.ic_audio_on));
			view.setBackgroundDrawable(background);
		} else {
			view.setBackground(getResources().getDrawable(
					(BluetoothManager.getInstance().getStatus() ? R.drawable.ic_audio_off
							: R.drawable.ic_audio_on)));
		}
		//Update App Widget
		ControlWidget.requestWidgetUpdate(getApplicationContext());
	}

	protected void stopAudio() {
		BluetoothManager.getInstance().streamAudioStop(getApplicationContext());
	}

	protected void startAudio() {
		BluetoothManager.getInstance().streamAudioStart(getApplicationContext());
		// mAudioON = true;
		if (!BluetoothManager.getInstance().getStatus()) {
			Toast.makeText(this, R.string.cannot_connect, Toast.LENGTH_SHORT)
					.show();
		}
	}



	private static final ScheduledExecutorService worker = Executors
			.newSingleThreadScheduledExecutor();
	

	private void delayAudioStart() {
		Runnable task = new Runnable() {
			public void run() {
				startAudio();

				// Refresh UI
				mguiHandler.post(new Runnable() {
					public void run() {
						refreshButtonImage();
					}
				});
			}
		};
		worker.schedule(task, 250, TimeUnit.MILLISECONDS);
	}

	private void broadcastKey(int key, boolean restartAudio) {
		if (restartAudio) {
			stopAudio();
		}
		Intent mbIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		// KeyEvent
		KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, key);
		// mbIntent
		mbIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		sendBroadcast(mbIntent);

		if (restartAudio) {
			delayAudioStart();
		}
	}

	public void onSoundStop(View view) {
		broadcastKey(KeyEvent.KEYCODE_MEDIA_PAUSE, false);
	}

	public void onSoundPlay(View view) {
		broadcastKey(KeyEvent.KEYCODE_MEDIA_PLAY, true);
	}

	public void onSoundBack(View view) {
		broadcastKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS, true);
	}

	public void onSoundNext(View view) {
		broadcastKey(KeyEvent.KEYCODE_MEDIA_NEXT, true);
	}

	public void onStreamClick(View view) {
		BluetoothManager.getInstance().toggleAudioStatus( getApplicationContext() );
		refreshButtonImage(view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(AUDIO_STATUS, BluetoothManager.getInstance().getStatus());
		super.onSaveInstanceState(outState);
	}

}
