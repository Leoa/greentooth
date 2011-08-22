package ws

import org.eclipse.jetty.websocket.WebSocketServlet
import java.util.concurrent.CopyOnWriteArraySet
import org.eclipse.jetty.websocket.WebSocket.Connection
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

class WebSocketChatServlet extends WebSocketServlet {
  val _members = new CopyOnWriteArraySet()

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    getServletContext().getNamedDispatcher("default").forward(request, response)
  }

  override def doWebSocketConnect(request: HttpServletRequest, protocol: String) = {
    new ChatWebSocket
  }

  class ChatWebSocket extends org.eclipse.jetty.websocket.WebSocket {
    def onOpen(p1: Connection) {}

    def onClose(p1: Int, p2: String) {}
  }
}