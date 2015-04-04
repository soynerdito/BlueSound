package com.soyblue.bluesound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BlueToggleReceiver extends BroadcastReceiver {
    public BlueToggleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       BluetoothManager.getInstance().toggleAudioStatus(context);
    }
}
