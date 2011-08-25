package ws.android;

/**
 * Created by IntelliJ IDEA.
 * User: Carl-Gustaf Harroch (carl@novoda.com)
 * Date: 25/08/11
 * Time: 15:28
 */
public interface ConnectionStatusCallback {
    void onConnected(boolean isFailover);
    void onDisconnected();
    void onNoConnection();
}
