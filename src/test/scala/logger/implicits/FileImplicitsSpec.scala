package logger.implicits

import better.files.File
import logger.string.StringUtf8Encodings
import org.scalatest.{BeforeAndAfterEach, WordSpec}

class FileImplicitsSpec extends WordSpec with BeforeAndAfterEach {

  import FileImplicits._

  private val testDir = "./tmp"

  override def afterEach(): Unit = {
    File(testDir).delete(true) // cleanup
  }

  "FileImplicits" should {
    "append a byte array to a file" in {
      val file = () => File(s"$testDir/test").createFileIfNotExists(createParents = true)
      val testString = "hello world"
      val bytes = StringUtf8Encodings.encodeString.f(testString)

      file.appendByteArray(bytes)
      val fileBytes = file().byteArray

      assertResult(fileBytes)(bytes)
      assertResult(testString)(StringUtf8Encodings.decodeString.f(fileBytes))
    }

    "get the next file name in the sequence" in {
      val file = () => File(s"$testDir/test_1").createFileIfNotExists(createParents = true)

      val actualFileName = file.nextFileName

      assertResult("test_2")(actualFileName)
    }

    "get the size of the file" in {
      val file = () => File(s"$testDir/test").createFileIfNotExists(createParents = true)
      val bytes = StringUtf8Encodings.encodeString.f("hello world")

      file.appendByteArray(bytes)
      val size = file.size

      assertResult(bytes.length)(size)
    }
  }
}
