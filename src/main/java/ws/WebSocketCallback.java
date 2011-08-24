package ws;

public interface WebSocketCallback {
    public void onConnect();
    public void onDisconnect();
    public void onMessage(ws.Frame frame);
    public void onError(Throwable t);
}
