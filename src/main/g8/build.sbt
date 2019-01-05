// Define versions for libraries:
val VersionCats              = "$versionCats$"
val VersionCatsEffect        = "$versionCatsEffect$"
val VersionCatsTaglessMacros = "$versionCatsTaglessMacros$"
val VersionHabitatConsole    = "$versionHabitatConsole$"
val VersionHabitatEmailer    = "$versionHabitatEmailer$"
val VersionHabitatLogger     = "$versionHabitatLogger$"
val VersionHabitatNotifier   = "$versionHabitatNotifier$"


// Configure the root project:
lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    // Top-level Settings:
    name := "$name$",
    organization := "$organization$",
    scalaVersion := "$scala_version$",
    version := "0.0.1-SNAPSHOT",

    // Scalac Options:
    scalacOptions += "-deprecation",
    scalacOptions in (Compile, console) := Seq(),

    // BuildInfo Settings:
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "$package$",

    // Libraries:
    libraryDependencies ++= Seq(
      "org.typelevel"   %% "cats-core"           % VersionCats,
      "org.typelevel"   %% "cats-effect"         % VersionCatsEffect,
      "org.typelevel"   %% "cats-tagless-macros" % VersionCatsTaglessMacros,
      "com.vsthost.rnd" %% "habitat-console"     % VersionHabitatConsole,
      "com.vsthost.rnd" %% "habitat-emailer"     % VersionHabitatEmailer,
      "com.vsthost.rnd" %% "habitat-logger"      % VersionHabitatLogger,
      "com.vsthost.rnd" %% "habitat-notifier"    % VersionHabitatNotifier,
    )
  )
