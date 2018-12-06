package logger.string

import java.nio.charset.StandardCharsets

import logger.encoding.Encoding

/**
  * Defines 2 encodings for converting [[String]]s to and from byte arrays using UTF-8 encoding.
  */
object StringUtf8Encodings {
  implicit val encodeString: Encoding[String, Array[Byte]] = {
    Encoding(string => string.getBytes(StandardCharsets.UTF_8))
  }

  implicit val decodeString: Encoding[Array[Byte], String] = {
    Encoding(bytes => new String(bytes, StandardCharsets.UTF_8))
  }
}
