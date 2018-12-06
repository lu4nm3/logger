package logger.binary

import logger.encoding.Encoding

/**
  * Type class that defines behaviors for converting a type `T` to and from a byte array.
  */
trait BinaryLoggable[T] {
  def toBytes(value: T): Array[Byte]

  def fromBytes(bytes: Array[Byte]): T
}

object BinaryLoggable {
  /**
    * Creates an instance of `BinaryLoggable` that is able to convert back and forth between some type `T` and
    * `Array[Byte]`. In order to do this, 2 encodings must be provided which define how to convert between the 2 types.
    */
  def apply[T](implicit to: Encoding[T, Array[Byte]], from: Encoding[Array[Byte], T]): BinaryLoggable[T] = {
    new BinaryLoggable[T] {
      def toBytes(value: T): Array[Byte] = to.f(value)

      def fromBytes(bytes: Array[Byte]): T = from.f(bytes)
    }
  }
}
