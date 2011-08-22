package ws

import ws.WebSocket.Listener
import ws.WebSocket.Event
import java.util.concurrent.{TimeUnit, CountDownLatch}

/**
 * Created by IntelliJ IDEA.
 * User: acsia
 * Date: 18/08/11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */

class WebSocketTest extends org.specs2.mutable.Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      val w = WebSocket.create("wss://echo.websocket.org:443", "sample")
      val latch = new CountDownLatch(1)
      var m = new MessageEvent    ("")
      w.setListener(new Listener {
        def onClose(socket: WebSocket, event: WebSocket#CloseEvent) {}

        def onMessage(socket: WebSocket, event: MessageEvent) {
          m = event;
          latch.countDown();
        }

        def onOpen(socket: WebSocket, event: WebSocket#OpenEvent) {
          socket.send("hello world")
        }
      })
      latch.await(30, TimeUnit.SECONDS)
      m.getMessage must_== "hello world"
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

}