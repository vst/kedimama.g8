package $package$.cli.commands

import cats.effect.Effect
import cats.implicits._
import org.rogach.scallop.Subcommand
import $package$.cli.Command

import scala.language.higherKinds

/**
  * Provides the dummy program.
  *
  * @param M Parameter for the [[Effect]] type, evidence for [[M]].
  * @tparam M [[Effect]] type.
  */
class DoSomethingCommand[M[_]](command: String)(implicit M : Effect[M]) extends Subcommand(command) with Command[M] {
    /**
    * Returns the purpose of the program.
    *
    * @return The purpose of the program.
    */
  override def purpose: String = "Does something"

  /**
    * Compiles and returns the program to be executed eventually.
    *
    * @return The program to be executed.
    */
  override def compile: M[Either[Throwable, Int]] = M.delay({
    // Do your stuff:
    println("What am I doing here?")

    // And return the exit code:
    0.asRight
  })
}
