package $package$.cli

import cats.effect.Effect
import cats.implicits._
import org.rogach.scallop.{ScallopConf, ScallopConfBase}
import $package$.BuildInfo
import $package$.cli.commands.DoSomethingCommand

import scala.language.higherKinds

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
  addSubcommand(new DoSomethingCommand[M]("do-something"))

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
        case x: ScallopConfBase with Command[M @unchecked] => x.compile
        case _ => new Throwable("Not implemented. What a pitty!").asLeft[Int].pure[M]
      }
    }
  }
}
