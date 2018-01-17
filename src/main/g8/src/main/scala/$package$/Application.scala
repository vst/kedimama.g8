package $package$

/**
  * Provides the application entry point for $name$.
  */
object Application extends App{
  println(s"$name$ (\${BuildInfo.version})")
  println(s"Copyright (c) $copyright_year$ $author_name$ <$author_mail$>")
}
