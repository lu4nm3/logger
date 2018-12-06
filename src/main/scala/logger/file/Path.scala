package logger.file

import scala.annotation.tailrec
import scala.language.implicitConversions

/**
  * Helper class to make it easier to work with file paths.
  */
case class Path private(value: String) extends AnyVal {
  override def toString: String = value

  def /(relativePath: Path): Path = Path(value + "/" + relativePath.value)

  def parent: Path = value.lastIndexOf("/") match {
    case 0 => Path()
    case last => Path(value.substring(0, last))
  }

  def resourceName: String = {
    val lastDiv = value.lastIndexOf("/")
    value.substring(lastDiv + 1, value.length)
  }
}

object Path {
  def apply(): Path = new Path("/")

  def apply(path: String): Path = {
    val newPath = s"/${path.stripAllPrefixSuffix("/")}"
    new Path(newPath)
  }

  implicit def fromAny[A](id: A)(implicit f: A => String): Path = apply(id)

  implicit class StringOps(value: String) {
    /**
      * Strips all characters in the prefix and suffix that match any of the characters specified in the input string.
      */
    def stripAllPrefixSuffix(strip: String): String = {
      @tailrec
      def start(n: Int): String =
        if (n == value.length) ""
        else if (strip.indexOf(value.charAt(n).toInt) < 0) end(n, value.length)
        else start(1 + n)

      @tailrec
      def end(a: Int, n: Int): String =
        if (n <= a) value.substring(a, n)
        else if (strip.indexOf(value.charAt(n - 1).toInt) < 0) value.substring(a, n)
        else end(a, n - 1)

      start(0)
    }
  }

}
