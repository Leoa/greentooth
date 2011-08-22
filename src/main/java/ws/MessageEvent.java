package ws;

/**
 * Created by IntelliJ IDEA.
 * User: acsia
 * Date: 18/08/11
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String s) {
        message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}