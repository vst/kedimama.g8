package $package$

import cats.effect.{Effect, IO}
import $package$.cli.{Application, CLIConf}

import scala.language.higherKinds


/**
  * Provides the application entry point.
  */
object MyApplication extends Application {
  /**
    * Returns the program to be run.
    *
    * @return An [[Effect]] of [[Either]] a [[Throwable]] in failure case, or [[Int]] as exit code otherwise.
    */
  override def program: IO[Either[Throwable, Int]] = CLIConf.getProgram[IO](args)
}
