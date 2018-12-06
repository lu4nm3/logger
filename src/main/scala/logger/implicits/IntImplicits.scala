package logger.implicits

import java.nio.ByteBuffer

/**
  * Contains an implicit class to extend the functionality of values of type `Int` by providing an additional method to
  * convert them to a byte array.
  */
object IntImplicits {

  implicit class IntOperations(i: Int) {
    def bytes: Array[Byte] = ByteBuffer.allocate(4).putInt(i).array
  }

}
