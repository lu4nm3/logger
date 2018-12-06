package logger.binary

import scala.language.higherKinds

/**
  * Interface to read and write values of type `T`.
  */
trait BinaryLogger[F[_], T] {
  def write(value: T): F[Unit]

  def read: F[Iterator[T]]
}
