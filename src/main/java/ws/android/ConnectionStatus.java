package ws.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionStatus extends BroadcastReceiver {

    private boolean isConnected = false;

    private final ConnectivityManager connectivityService;

    private final Context context;

    private ConnectionStatusCallback callback;

    public ConnectionStatus(Context context, ConnectionStatusCallback callback) {
        this.context = context;
        this.callback = callback;
        connectivityService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        registerForConnectivityChange();
        checkConnectivity(false);
    }

    private void registerForConnectivityChange() {
        context.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void checkConnectivity(boolean isFailover) {
        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnectedOrConnecting()) {
                callback.onConnected(isFailover);
            } else {
                callback.onNoConnection();
            }
        } else {
            callback.onNoConnection();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
            callback.onDisconnected();
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false)) {
            checkConnectivity(true);
        }
    }

    public ConnectionStatusCallback getCallback() {
        return callback;
    }

    public void setCallback(ConnectionStatusCallback callback) {
        this.callback = callback;
    }
}
