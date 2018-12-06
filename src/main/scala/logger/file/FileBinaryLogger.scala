package logger.file

import better.files._
import cats.MonadError
import cats.implicits._
import logger.binary.{BinaryLoggable, BinaryLogger}
import logger.implicits.ByteArrayImplicits._
import logger.implicits.FileImplicits._
import logger.implicits.IntImplicits._

import scala.language.{higherKinds, implicitConversions}

object FileBinaryLogger {
  /**
    * An `apply` constructor that creates a [[BinaryLogger]] that will read and write values of type `T` (as bytes) to
    * and from a file with the given filename. The filename must be an absolute path pointing to a file. If the file
    * and/or the directory(ies) the file is located within do not exist, they will be created automatically.
    *
    * In addition to a file name, a maximum file size must be provided. This will be used to ensure that file size
    * doesn't go beyond the maximum. In the event that this maximum is reached, the file will be rolled over and a new
    * empty file will be created. The way this is handled is by appending an increasing sequential number to the
    * filename for each new files that gets created. From the user's point of view, however, this process will be
    * transparent for simplicity in order to make it seem as if though they are only dealing with a single file.
    *
    * Furthermore, [[FileBinaryLogger]] abstracts out the effects of its operations through the `F[_]` higher-kinded
    * type which allows for different versions of a logger to be defined. The effect `F[_]` that is chosen must also
    * provide an instance of `MonadError[F, Throwable]` which can be used to handle errors at the call site.
    */
  def apply[F[_], T](filename: String, maxFileSizeInBytes: Long)
                    (implicit loggable: BinaryLoggable[T], M: MonadError[F, Throwable]): BinaryLogger[F, T] = {
    new BinaryLogger[F, T] {
      /**
        * The filename is implicitly converted to a `Path` in order to provide some normalization and error checking.
        */
      private val filePath: Path = filename

      /**
        * This creates a "lazy" var. Normally, the `lazy` keyword is reserved for `val`s (ie. immutable data). However,
        * we can mimic laziness with mutable `var`s by creating this as a function from `Unit` () to the data type.
        *
        * The reason for keeping track of the most "current" file is in order avoid the overhead of reading all existing
        * files in order to calculate the current file every time we write a value to it.
        */
      private var currentFile: () => File = () => {
        File(filePath.parent.value).createDirectoryIfNotExists(createParents = true)
          .glob(s"${filePath.resourceName}_*")
          .toList match {
          case Nil => File(s"${filePath.value}_1").createFileIfNotExists(createParents = true)
          case list => list.maxBy(file => file.name.split("_")(1).toInt)
        }
      }

      def write(value: T): F[Unit] = M.unit.map { _ =>
        val bytes: Array[Byte] = loggable.toBytes(value)
        val size: Int = bytes.length

        if (currentFile.size + (size + 4) > maxFileSizeInBytes) {
          val newFileName: Path = filePath.parent / currentFile.nextFileName
          currentFile = () => File(newFileName.value).createFileIfNotExists(createParents = true)
        }

        currentFile.appendByteArray(size.bytes ++ loggable.toBytes(value))
      }

      /**
        * Creates an `Iterator[T]` to read values of type `T` that were written to the file starting from the beginning.
        * The iterator will account for the case where the file referred to by the filename has been rolled over and
        * split into multiple files.
        */
      def read: F[Iterator[T]] = M.unit.map { _ =>
        new Iterator[T] {
          /**
            * Internally, an iterator for the bytes of the entire file (or files if it has been rolled over) is used
            * in order to read out separate values of type `T`.
            */
          private val bytes: Iterator[Byte] = {
            File(filePath.parent.value) match {
              case file if file.notExists => Iterator.empty
              case file =>
                file.glob(s"${filePath.resourceName}_*")
                  .toList
                  .sortBy(file => file.name.split("_")(1).toInt) // use ascending order based on the number appended to the file name
                  .toIterator
                  .flatMap(_.bytes)
            }
          }

          def hasNext: Boolean = bytes.hasNext

          def next(): T = {
            val size: Int = bytes.take(4).toArray.int
            val content: Array[Byte] = bytes.take(size).toArray
            loggable.fromBytes(content)
          }
        }
      }
    }
  }
}
