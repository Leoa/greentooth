package ws;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;

public class ChatWebSocketServer {


    public static void main(String[] args) {

        try {
            Server server = new Server(8081);
            ChatWebSocketHandler chatWebSocketHandler = new ChatWebSocketHandler();

            chatWebSocketHandler.setHandler(new DefaultHandler());

            server.setHandler(chatWebSocketHandler);

            // 2) Start the Jetty server.

            server.start();


            server.join();

        } catch (Throwable e) {

            e.printStackTrace();

        }

    }

}
