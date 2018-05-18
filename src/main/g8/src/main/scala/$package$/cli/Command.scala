package $package$.cli

import org.rogach.scallop.Subcommand

import scala.language.higherKinds

/**
  * Provides a trait for standardising the preparation of CLI program commands.
  *
  * This is quite useful if we have relatively tedious steps to prepare CLI program command instances, such as
  * interpreting command line arguments and options, picking the right program instance for a given algebra, etc...
  *
  * @tparam M Type parameter for the effect.
  */
trait Command[M[_]] { this: Subcommand =>
  /**
    * Returns the purpose of the program.
    *
    * @return The purpose of the program.
    */
  def purpose: String

  /**
    * Compiles and returns the program to be executed eventually.
    *
    * @return The program to be executed.
    */
  def compile: M[Either[Throwable, Int]]

  // Set the banner:
  banner(purpose)
}
