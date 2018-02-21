package $package$

import cats.effect.{Effect, IO}
import cats.implicits._
import org.rogach.scallop.{ScallopConf, ScallopConfBase, Subcommand}

import scala.language.higherKinds


/**
  * Provides an abstract class extending the [[App]] to facilitate convenience when writing `tagree` applications.
  */
abstract class Application extends App {
  /**
    * Returns the program to be run.
    *
    * @return An [[cats.effect.Effect]] of [[Either]] a [[Throwable]] in failure, or [[Int]] as exit code otherwise.
    */
  def program: IO[Either[Throwable, Int]]

  /**
    * Runs the program and exits the application.
    */
  def run(): Unit = {
    // Run the program, handle the return value and exit accordingly:
    program.attempt.unsafeRunSync match {
      case Left(error) => exitOnError(new Throwable("Unexpected error occurred. Reach out the programmer!", error), 2)
      case Right(rets) => rets match {
        case Left(error) => exitOnError(error, 1)
        case Right(code) => exitWithCode(code)
      }
    }
  }

  /**
    * Provides a function to exit the application with the given code.
    *
    * @param code Application exit code.
    */
  protected def exitWithCode(code: Int): Unit = System.exit(code)

  /**
    * Provides a function to print the error message and exit the application with code `1`.
    *
    * @param error Error message caught.
    */
  protected def exitOnError(error: Throwable, code: Int): Unit = {
    // Print the stack trace:
    error.printStackTrace()

    // Print the error message:
    Console.err.println(s"\${error.getMessage}\nExiting...")

    // Exit the application:
    exitWithCode(code)
  }

  // Call run:
  run()
}


/**
  * Provides a trait for standardising the preparation of programs.
  *
  * This is quite useful if we have relatively tedious steps to
  * prepare program instances, such as interpreting command line
  * arguments and options, picking the right program instance for a
  * given algebra, etc...
  *
  * @tparam M Type parameter for the effect.
  */
trait Program[M[_]] { this: ScallopConf =>
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


/**
  * Provides the dummy program.
  *
  * @param M Parameter for the [[Effect]] type, evidence for [[M]].
  * @tparam M [[Effect]] type.
  */
class DoSomethingProgram[M[_]](command: String)(implicit M : Effect[M]) extends Subcommand(command) with Program[M] {
    /**
    * Returns the purpose of the program.
    *
    * @return The purpose of the program.
    */
  override def purpose: String = "List remote folders"

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


/**
  * Provides the command line arguments and options parser.
  *
  * @param arguments List of strings, possibly arguments consumed directly from the command line.
  */
class CLIConf[M[_]](arguments: Seq[String])(implicit M : Effect[M]) extends ScallopConf(arguments) {
  // Define the version:
  version(s"\${BuildInfo.name} / v\${BuildInfo.version} / Copyright (c) $copyright_year$ $author_name$ <$author_mail$>")

  // Define the banner:
  banner(s"\n\${BuildInfo.name} command line interface\n")

  // Define the footer for additional notes for questions and feedback:
  footer("\nPlease contact the author in case you have any questions of feedback.")

  // Add sub-commands:
  addSubcommand(new DoSomethingProgram[M]("do-something"))

  // Done, let's verify:
  verify()
}


/**
  * Provides the companion object to [[CLIConf]] for convenience.
  */
object CLIConf {
  /**
    * Returns the program instructions to be executed.
    *
    * @param args Command-line arguments.
    * @tparam M Parameter for the [[Effect]] type.
    * @return Program instructions.
    */
  def getProgram[M[_] : Effect](args: Seq[String]): M[Either[Throwable, Int]] = {
    // Parse the configuration:
    val conf = new CLIConf(args)

    // Build the program and return (or return the error message if something is wrong):
    conf.subcommand match {
      case None => new Throwable("No command provided. Consider running with --help flag.").asLeft[Int].pure[M]
      case Some(cmd) => cmd match {
        case x: ScallopConfBase with Program[M @unchecked] => x.compile
        case _ => new Throwable("Not implemented. What a pitty!").asLeft[Int].pure[M]
      }
    }
  }
}


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
