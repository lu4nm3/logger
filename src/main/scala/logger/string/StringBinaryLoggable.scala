package logger.string

import logger.binary.BinaryLoggable

/**
  * Contains instances of the type class [[BinaryLoggable]] for [[String]]s.
  */
object StringBinaryLoggable {
  /**
    * Type class instance for UTF-8 encoding.
    */
  implicit val utf8StringLoggable: BinaryLoggable[String] = {
    // Bring the encodings for converting Strings to/from byte arrays into implicit scope.
    import StringUtf8Encodings._
    BinaryLoggable[String]
  }
}
