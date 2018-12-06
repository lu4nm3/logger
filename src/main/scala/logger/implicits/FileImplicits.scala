package logger.implicits

import better.files.File

/**
  * Contains an implicit class to extend the functionality of values of type `() => File` by exposing several methods
  * found in [[File]]s in addition to providing other custom methods.
  */
object FileImplicits {

  implicit class FileOperations(file: () => File) {
    def appendByteArray(bytes: Array[Byte]): File = file().appendByteArray(bytes)

    def nextFileName: String = {
      val nameArr = file().name.split("_")
      s"${nameArr(0)}_${nameArr(1).toLong + 1}"
    }

    def size: Long = file().size
  }

}
