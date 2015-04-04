package com.soyblue.bluesound;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BlueService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String STATUS_UPDATE = "com.soyblue.bluesound.action.STATUS";
    private static final String STATUS_ON = "com.soyblue.bluesound.extra.ON";
    private static final String TOGGLE = "com.soyblue.bluesound.action.TOGGLE";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BlueService.class);
        intent.setAction(STATUS_UPDATE);
        intent.putExtra(STATUS_ON, 1);
        context.startService(intent);
    }

    public static Intent getToggleIntent( Context context ){
        Intent intent = new Intent(context, BlueService.class);
        intent.setAction(TOGGLE);
        return intent;
    }

    public BlueService() {
        super("BlueService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (STATUS_UPDATE.equals(action)) {
                final boolean status = intent.getBooleanExtra(STATUS_ON, false);
                handleStatus(status);
            } else if (TOGGLE.equals(action)) {
                handleStatus(!BluetoothManager.getInstance().getStatus());
            }
        }
    }

    /**
     * Request On/Off for bluetooth streamming
     */
    private void handleStatus(boolean status) {
        if( status ){
            BluetoothManager.getInstance().streamAudioStart(getApplicationContext());
        }else{
            BluetoothManager.getInstance().streamAudioStop(getApplicationContext());
        }
    }

}
