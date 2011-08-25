package ws.android

import com.github.jbrechtel.robospecs.RoboSpecs
import org.specs2.mock.Mockito
import android.content.Context
import org.specs2.specification.Scope
import android.net.{NetworkInfo, ConnectivityManager}


class ConnectionStatusTest extends RoboSpecs with Mockito {

  "A connection status" should {

    "should callback if no connection available" in new ConnectivityManagerMock {
      connectivityManager.getActiveNetworkInfo returns (null)
      val cs = new ConnectionStatus(context, callback)
      there was one(callback).onNoConnection
    }

    "should callback if one connection is available but not connected or connecting" in new ConnectivityManagerMock {
      connectivityManager.getActiveNetworkInfo returns (null)
      val cs = new ConnectionStatus(context, callback)
      there was one(callback).onNoConnection
    }
  }

  trait ConnectivityManagerMock extends Scope {
    val connectivityManager = mock[ConnectivityManager]
    val context = mock[Context]
    val callback = mock[ConnectionStatusCallback]
    val networkInfo = mock[NetworkInfo]

    context.getSystemService(anyString) returns connectivityManager

    def noConnectivity {
    }
  }

}