// Define versions for libraries:
val VersionCats       = "$versionCats$"
val VersionCatsEffect = "$versionCatsEffect$"


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

    // BuildInfo Settings:
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "$package$",

    // Libraries:
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core"   % VersionCats,
      "org.typelevel" %% "cats-effect" % VersionCatsEffect,
    )
  )
