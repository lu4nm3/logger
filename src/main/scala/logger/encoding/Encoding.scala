package logger.encoding

/**
  * An encoding is simply a function that converts an input type to an output type.
  */
case class Encoding[I, O](f: I => O)
