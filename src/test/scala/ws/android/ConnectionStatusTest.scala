package ws.android

import com.github.jbrechtel.robospecs.RoboSpecs
import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import android.net.{NetworkInfo, ConnectivityManager}
import android.content.{Intent, Context}

class ConnectionStatusTest extends RoboSpecs with Mockito {

  "An initial connection status" should {

    "should callback if no connection available" in new NoConnectivity {
      val status = new ConnectionStatus(context, callback)
      there was one(callback).onNoConnection
      there was no(callback).onConnected(any)
    }

    "should callback if one connection is available but not connected or connecting" in new NotConnecting {
      val status = new ConnectionStatus(context, callback)
      there was one(callback).onNoConnection
      there was no(callback).onConnected(any)
    }

    "should callback as connected if active network is connected or connecting" in new Connecting {
      val status = new ConnectionStatus(context, callback)
      there was one(callback).onConnected(false)
      there was no(callback).onNoConnection
    }
  }

  "A connection status class" should {
    "react upon a change of connection being broadcasted" in {
      "a loss of connection" in new Connecting {
        val status = new ConnectionStatus(context, callback)
        val intent = new Intent();
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
        status.onReceive(context, intent)
        there was one(callback).onConnected(false) then one(callback).onDisconnected()
      }

      "a change in network (fallback)" in new Connecting {
        val status = new ConnectionStatus(context, callback)
        val intent = new Intent();
        intent.putExtra(ConnectivityManager.EXTRA_IS_FAILOVER, true)
        status.onReceive(context, intent)
        there was one(callback).onConnected(false) then one(callback).onConnected(true)
      }
    }
  }

  trait ConnectivityManagerMock extends Scope {
    val connectivityManager = mock[ConnectivityManager]
    val context = mock[Context]
    val callback = mock[ConnectionStatusCallback]
    val networkInfo = mock[NetworkInfo]
    context.getSystemService(anyString) returns connectivityManager
  }

  trait NoConnectivity extends ConnectivityManagerMock {
    connectivityManager.getActiveNetworkInfo returns (null)
  }

  trait NotConnecting extends ConnectivityManagerMock {
    networkInfo.isConnectedOrConnecting returns false
    connectivityManager.getActiveNetworkInfo returns (networkInfo)
  }

  trait Connecting extends ConnectivityManagerMock {
    networkInfo.isConnectedOrConnecting returns true
    connectivityManager.getActiveNetworkInfo returns (networkInfo)
  }

}