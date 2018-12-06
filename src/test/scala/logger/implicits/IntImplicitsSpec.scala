package logger.implicits

import org.scalatest.WordSpec

class IntImplicitsSpec extends WordSpec {

  import IntImplicits._

  "IntImplicits" should {
    "convert an integer to a byte array" in {
      val bytes = 3.bytes

      assertResult(Array[Byte](0, 0, 0, 3))(bytes)
    }

    "convert the maximum value of an integer to a byte array" in {
      val bytes = Int.MaxValue.bytes

      assertResult(Array[Byte](127, -1, -1, -1))(bytes)
    }

    "convert the minimum value of an integer to a byte array" in {
      val bytes = Int.MinValue.bytes

      assertResult(Array[Byte](128.toByte, 0, 0, 0))(bytes)
    }
  }
}
