package logger.string

import org.scalatest.WordSpec

class StringBinaryLoggableSpec extends WordSpec {
  "StringBinaryLoggable" should {
    "convert a string to and from a byte array using UTF-8 encoding" in {
      val expectedString = "hello world!"

      val bytes = StringBinaryLoggable.utf8StringLoggable.toBytes(expectedString)
      val actualString = StringBinaryLoggable.utf8StringLoggable.fromBytes(bytes)

      assertResult(expectedString)(actualString)
    }
  }
}
