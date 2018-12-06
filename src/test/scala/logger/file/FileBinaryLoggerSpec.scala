package logger.file

import better.files.File
import cats.Id
import logger.binary.BinaryLogger
import logger.util.IdUtils._
import org.scalatest.{BeforeAndAfterEach, WordSpec}

/**
  * One thing of note here is that we are using the `Id` monad for our effect `F[_]` in `FileBinaryLogger`. This enables
  * us to keep our business logic the same regardless of whether we run things asynchronously (in, say, production) or
  * synchronously like we do here for tests.
  */
class FileBinaryLoggerSpec extends WordSpec with BeforeAndAfterEach {

  import logger.string.StringBinaryLoggable._

  private val testDir = s"${File.currentWorkingDirectory.pathAsString}/tmp"

  override def afterEach(): Unit = {
    File(testDir).delete(true) // cleanup
  }

  "A FileBinaryLogger for strings" should {
    "write a single string to a file and read it back again" in {
      val logger: BinaryLogger[Id, String] = FileBinaryLogger[Id, String](s"$testDir/test", 100)
      val testString = "hello world"

      logger.write(testString)
      val itr = logger.read

      assertResult(testString)(itr.next())
      assert(!itr.hasNext)
    }

    "write multiple strings to a file and read them back again in the same order" in {
      val logger: BinaryLogger[Id, String] = FileBinaryLogger[Id, String](s"$testDir/test", 100)
      val testStrings = List("a", "b", "c")

      testStrings.foreach(logger.write)
      val itr = logger.read

      assertResult(itr.toList)(testStrings)
      assert(!itr.hasNext)
    }

    "read an empty file" in {
      val logger: BinaryLogger[Id, String] = FileBinaryLogger[Id, String](s"$testDir/test", 100)

      val itr = logger.read

      assert(!itr.hasNext)

      println()
    }
  }
}
