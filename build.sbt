
import scoverage.ScoverageKeys

name := "apibuilder-generator"

organization := "io.apibuilder.generator"

val scalaVer = "2.12.6"

scalaVersion in ThisBuild := scalaVer

lazy val generated = project
  .in(file("generated"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      ws
    )
  )

// TODO: lib will eventually be published as a jar if it turns out
// that we need it. For now it is here mostly for reference - hoping
// we end up not needing it.
lazy val lib = project
  .in(file("lib"))
  .dependsOn(generated)
  .settings(commonSettings: _*)

lazy val generator = project
  .in(file("generator"))
  .dependsOn(scalaGenerator, rubyGenerator, javaGenerator, goGenerator, androidGenerator, kotlinGenerator, javaAwsLambdaPojos)
  .aggregate(scalaGenerator, rubyGenerator, javaGenerator, goGenerator, androidGenerator, kotlinGenerator, javaAwsLambdaPojos)
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    routesImport += "io.apibuilder.generator.v0.Bindables.Core._",
    routesImport += "io.apibuilder.generator.v0.Bindables.Models._",
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      ws,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
    )
  )

lazy val javaAwsLambdaPojos = project
  .in(file("java-aws-lambda-pojos"))
  .dependsOn(lib, lib % "test->test")
  .settings(commonSettings: _*)
  .settings(Seq(ScoverageKeys.coverageMinimum := 0.0))
lazy val scalaGenerator = project
  .in(file("scala-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(commonSettings: _*)
  .settings(Seq(ScoverageKeys.coverageMinimum := 80.7))

lazy val rubyGenerator = project
  .in(file("ruby-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(commonSettings: _*)
  .settings(Seq(ScoverageKeys.coverageMinimum := 86.5))

lazy val javaGenerator = project
  .in(file("java-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(commonSettings: _*)
  .settings(Seq(ScoverageKeys.coverageMinimum := 66.98))

lazy val goGenerator = project
  .in(file("go-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(commonSettings: _*)

lazy val androidGenerator = project
  .in(file("android-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(
    commonSettings: _*
  )
  .settings(Seq(ScoverageKeys.coverageMinimum := 77.8))

lazy val kotlinGenerator = project
  .in(file("kotlin-generator"))
  .dependsOn(lib, lib % "test->test")
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" % "jackson-module-kotlin" % "2.9.3",
      //"com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.9.3",
      "org.threeten" % "threetenbp" % "1.3.6",
      "com.squareup" % "kotlinpoet" % "0.7.0",
      "com.squareup.retrofit2" % "retrofit" % "2.3.0",
      "com.jakewharton.retrofit" % "retrofit2-rxjava2-adapter" % "1.0.0",
      "org.jetbrains.kotlin" % "kotlin-compiler" % "1.2.41" % "test",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.mockito" % "mockito-core" % "2.18.3" % "test"
    )
  )
  .settings(Seq(ScoverageKeys.coverageMinimum := 95.3))

lazy val commonSettings: Seq[Setting[_]] = Seq(
  name ~= ("apibuilder-generator-" + _),
  organization := "io.apibuilder",
  ScoverageKeys.coverageFailOnMinimum := true,
  libraryDependencies ++= Seq(
    "org.atteo" % "evo-inflector" % "1.2.2",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.mockito" % "mockito-core" % "2.18.3" % "test",
    "com.github.javaparser" % "javaparser-core" % "3.5.10" % "test",
    "org.scala-lang" % "scala-compiler" % scalaVer % "test",
    "com.squareup" % "javapoet" % "1.11.1",
    "com.squareup.retrofit2" % "retrofit" % "2.3.0",
    "io.reactivex.rxjava2" % "rxjava" % "2.1.3"
  ),
  libraryDependencies += guice,
  scalacOptions ++= Seq("-feature", "-Ycache-plugin-class-loader:last-modified", "-Ycache-macro-class-loader:last-modified"),
  sources in (Compile,doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false
)
version := "0.5.77"
