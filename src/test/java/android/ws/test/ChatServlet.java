package android.ws.test;


import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketFactory;

public class ChatServlet extends HttpServlet 
{
    private WebSocketFactory _wsFactory;
    private final Set<ChatWebSocket> _members = new CopyOnWriteArraySet<ChatWebSocket>();
    
    /* ------------------------------------------------------------ */
    /** Initialise the servlet by creating the WebSocketFactory.
     */
    @Override
    public void init() throws ServletException
    {
        // Create and configure WS factory
        _wsFactory=new WebSocketFactory(new WebSocketFactory.Acceptor()
        {
            public boolean checkOrigin(HttpServletRequest request, String origin)
            {
                // Allow all origins
                return true;
            }
            
            public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol)
            {
                System.out.println(" protocol " + protocol);

                // Return new ChatWebSocket for chat protocol connections
                if ("chat".equals(protocol))
                    return new ChatWebSocket();
                return null;
            }

            public String checkOrigin(HttpServletRequest request, String host, String origin) {

                System.out.println(origin + " " + host);
                return origin;
            }
        });
        _wsFactory.setBufferSize(4096);
        _wsFactory.setMaxIdleTime(60000);
    }
    
    /* ------------------------------------------------------------ */
    /** Handle the handshake GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {        
        // If the WebSocket factory accepts the connection, then return
        if (_wsFactory.acceptWebSocket(request,response))
            return;
        // Otherwise send an HTTP error.
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"Websocket only");
    }

    
    /* ------------------------------------------------------------ */
    /** Chat WebSocket Example.
     * <p>This class implements the {@link OnTextMessage} interface so that
     * it can handle the call backs when websocket messages are received on
     * a connection.
     * </p>
     */
    private class ChatWebSocket implements WebSocket.OnTextMessage
    {
        volatile Connection _connection;

        /* ------------------------------------------------------------ */
        /** Callback for when a WebSocket connection is opened.
         * <p>Remember the passed {@link Connection} object for later sending and 
         * add this WebSocket to the members set.
         */
        public void onOpen(Connection connection)
        {
            _connection=connection;
            _members.add(this);
        }

        /* ------------------------------------------------------------ */
        /** Callback for when a WebSocket connection is closed.
         * <p>Remove this WebSocket from the members set.
         */
        public void onClose(int closeCode, String message)
        {
            _members.remove(this);
        }

        /* ------------------------------------------------------------ */
        /** Callback for when a WebSocket message is received.
         * <p>Send the message to all connections in the members set.
         */
        public void onMessage(String data)
        {
            for (ChatWebSocket member : _members)
            {
                try
                {
                    member._connection.sendMessage(data);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
