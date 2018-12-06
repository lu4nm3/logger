package logger.string

import org.scalatest.WordSpec

class StringUtf8EncodingsSpec extends WordSpec {
  "StringUtf8Encodings" should {
    "encode and decode a string successfully" in {
      val expectedString = "hello world!"
      val bytes = StringUtf8Encodings.encodeString.f(expectedString)

      val actualString = StringUtf8Encodings.decodeString.f(bytes)

      assertResult(expectedString)(actualString)
    }

    "encode and decode an empty string successfully" in {
      val expectedString = ""
      val bytes = StringUtf8Encodings.encodeString.f(expectedString)

      val actualString = StringUtf8Encodings.decodeString.f(bytes)

      assertResult(expectedString)(actualString)
    }
  }
}
