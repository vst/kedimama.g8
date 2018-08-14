package $package$

import cats.effect._
import cats.syntax.all._

import scala.language.higherKinds


/**
  * Provides the application entry point.
  */
object MyApplication extends IOApp {
  /**
    * Returns an [[IO]] program to be run.
    *
    * @param args List of command line arguments.
    * @return An [[IO]] program returning the [[ExitCode]] after run.
    */
  def run(args: List[String]): IO[ExitCode] = {
    (IO(println("Hello.")) *> IO(args).map(_.foreach(println))).as(ExitCode.Success)
  }
}
