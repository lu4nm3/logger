# Logger

### Layout

This projects uses standard SBT layout.

- `src/main/scala`: Application source code.
- `src/main/scala/logging/Driver`: The application driver (ie. it contains the `main` method)
- `src/test/scala`: Tests
- `buid.sbt`: The main SBT configuration file.
- `/project`: Contains SBT configurations.

### 3rd-Party Dependencies

- [Cats](https://typelevel.org/cats/)
- [Monix](https://monix.io/)
- [better-files](https://github.com/pathikrit/better-files)
- [ScalaTest](http://www.scalatest.org/) (for testing)

### How to run

This is simply a Scala (2.12) application that uses SBT as its build tool. As such, in order to build and run it, simply 
`cd` into the top level root directory and run `sbt run` from the command line (this assumes you have SBT installed on 
your system). Alternative, you should be able to import it into an IDE like IntelliJ which should be able to recognize 
SBT. From here you can also run the application using the UI controls.

### Notable mentions

- `Driver.scala` showcases the usage of the logger classes.
- Makes use of functional programming concepts such as higher-kinded types and type classes for modularity and 
extensibility.
- The code has been heavily commented to explain the decisions made. Normally, this many comments would not be 
necessary.
- Supports rolling over files. However this is made transparent to the user. This means that if you want to read/write 
to a file called "test.txt", for example, you can read/write from/to it using the logger class as if though it was 
always a single file but behind the scenes, the library will manage rolled over files for you.

### Possible future enhancements

#### Extend it to make use of a serialization framework such as Protocol Buffers or Thrift.

##### Pros:

- More efficient and compact serialization.
- Provides RPC capabilities.

##### Cons:

- Less flexibility since you are now bound to that framework.
- Less flexibility when defining a schema. Must adhere to a set of rules when modifying it.

#### Provide the ability to compress files

##### Pros:

- More efficient use of disk.

##### Cons:

- You incur a performance a hit when reading/writing from/to files due to the constant compression and decompression.