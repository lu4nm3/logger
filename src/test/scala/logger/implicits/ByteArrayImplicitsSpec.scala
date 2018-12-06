package logger.implicits

import org.scalatest.WordSpec

class ByteArrayImplicitsSpec extends WordSpec {

  import IntImplicits._
  import ByteArrayImplicits._

  "ByteArrayImplicits" should {
    "convert a byte array representing an Int back to an Int" in {
      val expectedInt = 3
      val bytes = expectedInt.bytes

      val actualInt = bytes.int

      assertResult(expectedInt)(actualInt)
    }

    "convert a byte array representing the maximum value of an integer back to an integer" in {
      val expectedInt = Int.MaxValue
      val bytes = expectedInt.bytes

      val actualInt = bytes.int

      assertResult(expectedInt)(actualInt)
    }

    "convert a byte array representing the minimum value of an integer back to an integer" in {
      val expectedInt = Int.MinValue
      val bytes = expectedInt.bytes

      val actualInt = bytes.int

      assertResult(expectedInt)(actualInt)
    }
  }

}
