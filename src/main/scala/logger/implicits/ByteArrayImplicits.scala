package logger.implicits

import java.nio.ByteBuffer

/**
  * Contains an implicit class to extend the functionality of values of type `Int` by providing an additional method to
  * convert them to a byte array.
  */
object ByteArrayImplicits {

  implicit class ByteArrayOperations(arr: Array[Byte]) {
    def int: Int = ByteBuffer.wrap(arr).getInt
  }

}
