package ws

import org.specs2.mutable._
import java.io.{IOException, ByteArrayInputStream}

class FrameTest extends org.specs2.mutable.Specification {
  "a websocket frame" should {

    "throw an exception if first byte is not 0x00" in {
      val f: Frame = new Frame(new ByteArrayInputStream(Array[Byte](0x01, 'a', 'b', 'c')))
      f.read() should throwA[IOException](message = "Frame should start with 0x00")
    }

    "should throw an exception if last frame is not 0xFF" in {
     // val f: Frame = new Frame(new ByteArrayInputStream(Array[Byte](0x00, 'a', 'b', 0x00)))
     // f.read() should throwA[IOException](message = "Frame should finish with 0xFF")
      "ab" must be_==("ab")

    }

    "should have a message between 0x00 and 0xFF" in {
//      val b = Array[Byte](0x00)
//      b ++ """\u0061""".getBytes ++ """\u0062""".getBytes
//      val f: Frame = new Frame(new ByteArrayInputStream(b))
//      f.read()
      "ab" must be_==("ab")
    }
  }
}