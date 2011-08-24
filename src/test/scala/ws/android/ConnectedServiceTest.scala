package ws.android

import com.github.jbrechtel.robospecs.RoboSpecs
import org.specs2.mock.Mockito

class ConnectedServiceTest extends RoboSpecs with Mockito {

  "A connected service" should {
    "connect as soon as a connection is available" in {
      val cs = new ConnectedService();

      "1" must be_==( "1")
    }
  }


}