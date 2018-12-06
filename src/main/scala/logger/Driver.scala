package logger

import better.files.File
import com.typesafe.scalalogging.Logger
import logger.file.FileBinaryLogger
import monix.eval.Task

import scala.language.implicitConversions

object Driver {
  private val logger = Logger.apply(Driver.getClass)

  def main(args: Array[String]): Unit = {

    /**
      * Brings a string-based instance of the BinaryLoggable type class into implicit scope.
      */
    import string.StringBinaryLoggable._

    /**
      * Create a new logger that can read/write Strings to the specified file path while keep files no larger than 100
      * bytes. It will make use of the implicit `BinaryLoggable[String]` defined above in addition to an implicit
      * instance of `MonadError[Task, Throwable]` which is provided by the Monix library (and is exposed through the
      * `monix.eval.Task` import above).
      *
      * This example creates a file named "test" in the current working directory. Feel free to change this to something
      * else but just remember to use absolute paths.
      */
    val logger = FileBinaryLogger[Task, String](s"${File.currentWorkingDirectory.pathAsString}/test", 100)

    /**
      * Composes a series of sequential write operations.
      */
    val write: Task[Unit] = for {
      _ <- logger.write("Hello World!")
      _ <- logger.write("Use this logger")
      _ <- logger.write("to write to write these messages sequentially")
      _ <- logger.write("to a file.")
    } yield ()

    val read: Task[Unit] = logger.read.map(itr =>
      while (itr.hasNext) {
        println(itr.next())
      }
    )

    /**
      * Creates a "program" that writes messages to a file and subsequently reads and prints them out. All operations
      * declared so far (including this program) are never executed. Rather, we use `Task` to define a program out of
      * other instances of `Task` by composing them together in sequential order.
      */
    val program: Task[Unit] = for {
      _ <- write
      _ <- read
    } yield ()

    /**
      * Here, at the "edge" of our application, is where we actually execute the task. In this case we are executing it
      * using the global scheduler provided by the Monix library. Normally, however, we would create our own dedicated
      * schedulers (ie. threadpools) based on how we want to execute things.
      */
    import monix.execution.Scheduler.Implicits.global

    program
      .onErrorHandle(errorHandler)
      .runSyncUnsafe()
  }

  /**
    * Provides a way to handle any errors that might occur while executing the program. Here we simply log the error but
    * we could also do anything we like in response including things like provide a default value or run some other
    * computation (eg. send a notification of an error).
    */
  private val errorHandler: Throwable => Unit = { throwable =>
    logger.error("There was an error while executing the program", throwable)
  }
}
