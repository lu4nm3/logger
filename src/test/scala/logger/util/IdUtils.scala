package logger.util

import cats.{Id, Monad, MonadError}

object IdUtils {
  implicit def idMonad(implicit I: Monad[Id]): MonadError[Id, Throwable] =
    new MonadError[Id, Throwable] {

      override def pure[A](x: A): Id[A] = I.pure(x)

      override def ap[A, B](ff: Id[A ⇒ B])(fa: Id[A]): Id[B] = I.ap(ff)(fa)

      override def map[A, B](fa: Id[A])(f: Id[A ⇒ B]): Id[B] = I.map(fa)(f)

      override def flatMap[A, B](fa: Id[A])(f: A ⇒ Id[B]): Id[B] = I.flatMap(fa)(f)

      override def tailRecM[A, B](a: A)(f: A ⇒ Id[Either[A, B]]): Id[B] = I.tailRecM(a)(f)

      override def raiseError[A](e: Throwable): Id[A] = throw e

      override def handleErrorWith[A](fa: Id[A])(f: Throwable ⇒ Id[A]): Id[A] = {
        try {
          fa
        } catch {
          case e: Exception ⇒ f(e)
        }
      }
    }

}
