package ws.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        checkConnectivity();
    }

    private void checkConnectivity() {
        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();
        if (networkInfo != null) {

        } else {
            callback.onNoConnection();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }

    public ConnectionStatusCallback getCallback() {
        return callback;
    }

    public void setCallback(ConnectionStatusCallback callback) {
        this.callback = callback;
    }
}
